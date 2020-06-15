package koha13.spasic.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import koha13.spasic.R;
import koha13.spasic.api.ResponseCallback;
import koha13.spasic.data.AllPlaylistsViewModel;
import koha13.spasic.data.AllSongsViewModel;
import koha13.spasic.data.UserData;
import koha13.spasic.entity.Playlist;
import koha13.spasic.entity.Song;
import koha13.spasic.entity.User;
import koha13.spasic.model.LoginRequest;

public class LoginActivity extends AppCompatActivity implements ResponseCallback<User> {

    private static final float ROTATE_FROM = 0.0f;
    private static final float ROTATE_TO = -10.0f * 360.0f;// 3.141592654f * 32.0f;
    private EditText username;
    private EditText password;
    private Button lgBtn;
    private LinearLayout loadingScreen;
    private ImageView loadingIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.lg_username);
        password = findViewById(R.id.lg_password);
        lgBtn = findViewById(R.id.lg_btn);
        loadingScreen = findViewById(R.id.lg_loading_screen);
        loadingIcon = findViewById(R.id.lg_loading_ic);

        lgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        password.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    login();
                    return true;
                }
                return false;
            }
        });
    }

    private void login() {
        hideKeyboard(this);
        String us = username.getText().toString().toLowerCase().trim();
        String pw = password.getText().toString().toLowerCase().trim();
        if (us.length() < 6 || pw.length() < 6) {
            onDataFail("Failed");
        }
        UserData.login(new LoginRequest(us, pw), this);
        loadingScreen.setVisibility(View.VISIBLE);
        RotateAnimation r = new RotateAnimation(ROTATE_FROM, ROTATE_TO,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        r.setDuration(5000);
        r.setRepeatCount(0);
        loadingIcon.setAnimation(r);
    }

    @Override
    public void onDataSuccess(User data) {
        SharedPreferences.Editor editor = getSharedPreferences("PRIVATE_DATA", Context.MODE_PRIVATE).edit();
        editor.putString("token", UserData.user.getToken());
        editor.putString("role", UserData.user.getRole());
        editor.putString("username", UserData.user.getUsername());
        editor.putInt("id", UserData.user.getId());
        editor.apply();
        fetchData();
    }

    @Override
    public void onDataFail(String message) {
        loadingScreen.setVisibility(View.INVISIBLE);
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Đăng nhập không thành công")
                .setMessage("Tài khoản hoặc mật khẩu không chính xác")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        username.requestFocus();
                    }

                })
                .show();
    }

    @Override
    public void onFailed(Throwable error) {
        loadingScreen.setVisibility(View.INVISIBLE);
        Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
    }

    private void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void fetchData() {
        AllSongsViewModel.fetchAllSongs(new ResponseCallback<List<Song>>() {
            @Override
            public void onDataSuccess(List<Song> data) {
                AllPlaylistsViewModel.fetchAllPlaylists(new ResponseCallback<List<Playlist>>() {
                    @Override
                    public void onDataSuccess(List<Playlist> data) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }

                    @Override
                    public void onDataFail(String message) {

                    }

                    @Override
                    public void onFailed(Throwable error) {

                    }
                });
            }

            @Override
            public void onDataFail(String message) {

            }

            @Override
            public void onFailed(Throwable error) {

            }
        });
    }
}

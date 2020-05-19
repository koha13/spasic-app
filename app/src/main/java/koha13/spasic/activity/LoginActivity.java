package koha13.spasic.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import koha13.spasic.R;
import koha13.spasic.api.ResponseCallback;
import koha13.spasic.data.UserData;
import koha13.spasic.entity.User;
import koha13.spasic.model.LoginRequest;

public class LoginActivity extends AppCompatActivity implements ResponseCallback<User> {

    private EditText username;
    private EditText password;
    private Button lgBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.lg_username);
        password = findViewById(R.id.lg_password);
        lgBtn = findViewById(R.id.lg_btn);

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

    private void login(){
        String us = username.getText().toString().toLowerCase().trim();
        String pw = password.getText().toString().toLowerCase().trim();
        UserData.login(new LoginRequest(us,pw),this);
    }

    @Override
    public void onDataSuccess(User data) {
        Log.d("Login", UserData.user.getToken());
        SharedPreferences.Editor editor = (SharedPreferences.Editor) getSharedPreferences("PRIVATE_DATA",Context.MODE_PRIVATE).edit();
        editor.putString("token", UserData.user.getToken());
        editor.putString("role", UserData.user.getRole());
        editor.putString("username", UserData.user.getUsername());
        editor.putInt("id", UserData.user.getId());
        editor.apply();
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onDataFail(String message) {
        Log.d("Login", "Failed");
    }

    @Override
    public void onFailed(Throwable error) {
        Log.d("Login", "Failed");
    }
}

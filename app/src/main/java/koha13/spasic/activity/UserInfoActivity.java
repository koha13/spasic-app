package koha13.spasic.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import koha13.spasic.R;
import koha13.spasic.api.ResponseCallback;
import koha13.spasic.data.FetchApiImpl;
import koha13.spasic.data.UserData;
import koha13.spasic.entity.User;

public class UserInfoActivity extends AppCompatActivity {
    TextView username;
    EditText oldPass;
    EditText newPass;
    Button changPassBtn;
    ImageButton backBtn;
    Button logoutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        initView();
        setEvent();
    }

    private void setEvent() {
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        changPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String op = oldPass.getText().toString().trim();
                String np = newPass.getText().toString().trim();
                FetchApiImpl.changePassword(op, np, new ResponseCallback<User>() {
                    @Override
                    public void onDataSuccess(User data) {
                        SharedPreferences.Editor editor = getSharedPreferences("PRIVATE_DATA", Context.MODE_PRIVATE).edit();
                        editor.putString("token", data.getToken());
                        UserData.user.setToken(data.getToken());
                        editor.apply();
                        Toast.makeText(UserInfoActivity.this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                        oldPass.setText("");
                        newPass.setText("");
                    }

                    @Override
                    public void onDataFail(String message) {
                        Toast.makeText(UserInfoActivity.this, message, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailed(Throwable error) {
                        Toast.makeText(UserInfoActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getSharedPreferences("PRIVATE_DATA", Context.MODE_PRIVATE).edit();
                editor.clear();
                editor.apply();
                Intent intent = new Intent(UserInfoActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        username = findViewById(R.id.username);
        oldPass = findViewById(R.id.old_pass);
        newPass = findViewById(R.id.new_pass);
        changPassBtn = findViewById(R.id.change_pass_btn);
        backBtn = findViewById(R.id.backBtn);
        logoutBtn = findViewById(R.id.logout_btn);

        username.setText(UserData.user.getUsername());
    }
}

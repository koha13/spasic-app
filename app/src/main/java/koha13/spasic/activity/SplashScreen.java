package koha13.spasic.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import koha13.spasic.R;
import koha13.spasic.api.ResponseCallback;
import koha13.spasic.data.AllPlaylistsViewModel;
import koha13.spasic.data.AllSongsViewModel;
import koha13.spasic.data.UserData;
import koha13.spasic.entity.Playlist;
import koha13.spasic.entity.Song;

public class SplashScreen extends AppCompatActivity {

    private static final float ROTATE_FROM = 0.0f;
    private static final float ROTATE_TO = -10.0f * 360.0f;// 3.141592654f * 32.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ImageView loadingImage = findViewById(R.id.splash_ic);
        RotateAnimation r = new RotateAnimation(ROTATE_FROM, ROTATE_TO,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        r.setDuration(30000);
        r.setRepeatCount(0);
        loadingImage.setAnimation(r);

        SharedPreferences prefs = getSharedPreferences("PRIVATE_DATA", MODE_PRIVATE);
        String token = prefs.getString("token", null);
        if (token == null) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }, 1500);
        } else {
            UserData.user.setId(prefs.getInt("id", 0));
            UserData.user.setRole(prefs.getString("role", null));
            UserData.user.setUsername(prefs.getString("username", null));
            UserData.user.setToken(prefs.getString("token", null));
            fetchData();
        }
    }

    private void fetchData() {
        AllSongsViewModel.fetchAllSongs(new ResponseCallback<List<Song>>() {
            @Override
            public void onDataSuccess(List<Song> data) {
                AllPlaylistsViewModel.fetchAllPlaylists(new ResponseCallback<List<Playlist>>() {
                    @Override
                    public void onDataSuccess(List<Playlist> data) {
                        Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }

                    @Override
                    public void onDataFail(String message) {
                        Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailed(Throwable error) {
                        Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onDataFail(String message) {
                Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }

            @Override
            public void onFailed(Throwable error) {
                Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }
}

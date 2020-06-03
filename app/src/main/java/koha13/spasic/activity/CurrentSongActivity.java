package koha13.spasic.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import koha13.spasic.FragmentCurrentSong.QueueFragment;
import koha13.spasic.R;

public class CurrentSongActivity extends AppCompatActivity {

    private ImageButton backBtn;
    private TextView songName;
    private TextView songArtist;
    private ImageButton queueBtn;
    private boolean isQueue = false;
    Fragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_song);

        initView();
    }

    private void initView() {
        songName = findViewById(R.id.song_name);
        songName.setSelected(true);
        songArtist = findViewById(R.id.song_artist);
        songArtist.setSelected(true);
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mFragment = new QueueFragment();
        queueBtn = findViewById(R.id.queue_btn);
        queueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                if (!isQueue){
                    fragmentManager.beginTransaction()
                            .replace(R.id.framelayout, mFragment).commit();
                    isQueue = true;
                }else{
                    fragmentManager.beginTransaction().remove(mFragment).commit();
                    isQueue = false;
                }

            }
        });
    }

}

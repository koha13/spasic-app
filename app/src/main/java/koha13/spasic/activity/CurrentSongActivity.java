package koha13.spasic.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import koha13.spasic.FragmentCurrentSong.QueueFragment;
import koha13.spasic.R;
import koha13.spasic.data.SongControlViewModel;
import koha13.spasic.entity.Song;

public class CurrentSongActivity extends AppCompatActivity {

    private ImageButton backBtn;
    private ImageButton btnPrevious;
    private ImageButton btnNext;
    private ImageButton btnPlay;
    private ImageButton btnShuffle;
    private ImageButton btnLoop;
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

        btnPlay = findViewById(R.id.btnPlay);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "btnPlay", Toast.LENGTH_SHORT).show();
                // TODO: change button image
                if (SongControlViewModel.currentSong.getValue()!=null){
                    if (SongControlViewModel.isPlaying.getValue()) {
                        MainActivity.musicService.stopSong();
                    } else {
                        MainActivity.musicService.playSong();
                    }
                }
            }
        });

        btnLoop = findViewById(R.id.btnLoop);
        btnLoop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: change button image
                SongControlViewModel.loopState ++;
                if (SongControlViewModel.loopState > 2) {
                    SongControlViewModel.loopState = 0;
                }
                Toast.makeText(getApplicationContext(), "btnLoop" + SongControlViewModel.loopState, Toast.LENGTH_SHORT).show();
            }
        });

        btnPrevious = findViewById(R.id.btnPrevious);
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "btnPrevious", Toast.LENGTH_SHORT).show();
                // TODO: change button image
                if (SongControlViewModel.getPrevious()){
                    MainActivity.musicService.playSong();
                }
            }
        });

        btnNext = findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "btnNext", Toast.LENGTH_SHORT).show();
                // TODO: change button image
                if (SongControlViewModel.getNext()){
                    MainActivity.musicService.playSong();
                }
            }
        });

        btnShuffle = findViewById(R.id.btnShuffle);
        btnShuffle.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "btnShuffle", Toast.LENGTH_SHORT).show();
                // TODO: change button image
                List queueSongs = SongControlViewModel.queueSongs;
                if (queueSongs == null || queueSongs.size() == 0) {
                    // TODO:
                } else {
                    SongControlViewModel.shuffleQueue();
                    int randomIndex = new Random().nextInt(queueSongs.size());
                    Song pickedSong = SongControlViewModel.queueSongs.get(randomIndex);
                    while (pickedSong.equals(SongControlViewModel.currentSong.getValue())){
                        randomIndex = new Random().nextInt(queueSongs.size());
                        pickedSong = SongControlViewModel.queueSongs.get(randomIndex);
                    }
                }
            }
        });


        mFragment = new QueueFragment();
        queueBtn = findViewById(R.id.queue_btn);
        queueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
                if (!isQueue) {
                    ft.replace(R.id.framelayout, mFragment).commit();
                    isQueue = true;
                    queueBtn.setColorFilter(Color.parseColor("#FF5722"));
                } else {
                    ft.remove(mFragment).commit();
                    isQueue = false;
                    queueBtn.setColorFilter(Color.parseColor("#FFFFFF"));
                }

            }
        });
    }

}

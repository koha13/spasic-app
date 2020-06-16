package koha13.spasic.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Random;

import koha13.spasic.AddToPlDialog;
import koha13.spasic.FragmentCurrentSong.QueueFragment;
import koha13.spasic.R;
import koha13.spasic.data.AllPlaylistsViewModel;
import koha13.spasic.data.SongControlViewModel;
import koha13.spasic.entity.Playlist;
import koha13.spasic.entity.Song;

public class CurrentSongActivity extends AppCompatActivity {

    SongControlViewModel songControlViewModel;
    AllPlaylistsViewModel allPlaylistsViewModel;
    Fragment mFragment;
    private ImageButton backBtn;
    private ImageButton btnPrevious;
    private ImageButton btnNext;
    private ImageButton playBtn;
    private ImageButton btnShuffle;
    private ImageButton btnLoop;
    private ImageButton addPlBtn;
    private TextView songName;
    private TextView songArtist;
    private ImageButton queueBtn;
    private ImageButton loveBtn;
    ImageView imageSong;
    private boolean isQueue = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_song);

        initView();
    }

    private void initView() {
        imageSong = findViewById(R.id.image_song);
        addPlBtn = findViewById(R.id.add_to_pl_btn);
        songName = findViewById(R.id.song_name);
        loveBtn = findViewById(R.id.love_btn);
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

        playBtn = findViewById(R.id.btnPlay);
        playBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "btnPlay", Toast.LENGTH_SHORT).show();
                if (SongControlViewModel.currentSong.getValue() != null) {
                    if (SongControlViewModel.isPlaying.getValue()) {
                        playBtn.setImageResource(R.drawable.ic_play_circle_filled_orange_40dp);
                        MainActivity.musicService.pauseSong();
                    } else {
                        playBtn.setImageResource(R.drawable.ic_pause_circle_filled_orange_40dp);
                        MainActivity.musicService.playSong();
                    }
                }
            }
        });

        btnLoop = findViewById(R.id.btnLoop);
        btnLoop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SongControlViewModel.loopState++;
                if (SongControlViewModel.loopState > 2) {
                    SongControlViewModel.loopState = 0;
                }
                updateBtnLoop(SongControlViewModel.loopState);
            }
        });

        btnPrevious = findViewById(R.id.btnPrevious);
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                MainActivity.musicService.playPreviousSong();
            }
        });

        btnNext = findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                MainActivity.musicService.playNextSong();
            }
        });

        btnShuffle = findViewById(R.id.btnShuffle);
        if(SongControlViewModel.randomState.getValue()){
            btnShuffle.setImageResource(R.drawable.ic_shuffle_orange_24dp);
        }else{
            btnShuffle.setImageResource(R.drawable.ic_shuffle_white_24dp);
        }
        btnShuffle.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                if(SongControlViewModel.randomState.getValue()){
                    SongControlViewModel.unShuffle();
                    btnShuffle.setImageResource(R.drawable.ic_shuffle_white_24dp);
                }
                else{
                    List queueSongs = SongControlViewModel.queueSongs;
                    if (queueSongs == null || queueSongs.size() == 0) {
                    } else {
                        SongControlViewModel.shuffleQueue();
                        btnShuffle.setImageResource(R.drawable.ic_shuffle_orange_24dp);
                    }
                }
            }
        });

        songControlViewModel = ViewModelProviders.of(CurrentSongActivity.this).get(SongControlViewModel.class);
        SongControlViewModel.isPlaying.observe(CurrentSongActivity.this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean == true) {
                    playBtn.setImageResource(R.drawable.ic_pause_circle_filled_orange_40dp);
                } else {
                    playBtn.setImageResource(R.drawable.ic_play_circle_filled_orange_40dp);
                }
            }
        });

        SongControlViewModel.currentSong.observe(CurrentSongActivity.this, new Observer<Song>() {
            @Override
            public void onChanged(Song song) {
                Glide.with(CurrentSongActivity.this).load(song.getSongImage()).into(imageSong);
                songName.setText(song.getName());
                songArtist.setText(song.getArtists());
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

        addPlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AddToPlDialog(SongControlViewModel.currentSong.getValue(), CurrentSongActivity.this).getDialog().show();
            }
        });

        allPlaylistsViewModel = ViewModelProviders.of(CurrentSongActivity.this).get(AllPlaylistsViewModel.class);
        allPlaylistsViewModel.getAllPlaylists().observe(this, new Observer<List<Playlist>>() {
            @Override
            public void onChanged(List<Playlist> playlists) {
                for(Playlist pl : playlists){
                    if(pl.getId() == -1){
                        for(Song s:pl.getSongs()){
                            if(s.getId() == songControlViewModel.currentSong.getValue().getId()){
                                loveBtn.setImageResource(R.drawable.ic_favorite_orange_24dp);
                                return;
                            }
                        }
                    }
                }
                loveBtn.setImageResource(R.drawable.ic_favorite_border_orange_24dp);
            }
        });

        loveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Playlist> playlists = allPlaylistsViewModel.getAllPlaylists().getValue();
                for(Playlist pl : playlists){
                    if(pl.getId() == -1){
                        for(Song s:pl.getSongs()){
                            if(s.getId() == songControlViewModel.currentSong.getValue().getId()){
                                allPlaylistsViewModel.deleteSongFromPl(-1, songControlViewModel.currentSong.getValue());
                                return;
                            }
                        }
                    }
                }
                allPlaylistsViewModel.addSongToPl(-1, songControlViewModel.currentSong.getValue());
            }
        });
    }

    private void updateBtnLoop(Integer loopState) {
        if (loopState == 0) {
            btnLoop.setImageResource(R.drawable.ic_repeat_white_24dp);
        } else if (loopState == 1) {
            btnLoop.setImageResource(R.drawable.ic_repeat_one_orange_24dp);
        } else if (loopState == 2) {
            btnLoop.setImageResource(R.drawable.ic_repeat_all_orange_24dp);
        }
    }

}

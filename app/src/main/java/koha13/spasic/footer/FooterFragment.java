package koha13.spasic.footer;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import koha13.spasic.R;
import koha13.spasic.activity.CurrentSongActivity;
import koha13.spasic.activity.MainActivity;
import koha13.spasic.data.SongControlViewModel;
import koha13.spasic.entity.Song;

public class FooterFragment extends Fragment {

    ImageButton playBtn;
    ImageButton nextBtn;
    SongControlViewModel songControlViewModel;
    TextView songName;
    TextView songArtist;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.footer_fragment, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LinearLayout songInfo = getActivity().findViewById(R.id.song_info_ft);
        songInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CurrentSongActivity.class);
                startActivity(intent);
            }
        });

        initView();

        playBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
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

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.musicService.playNextSong();
            }
        });

    }

    private void initView() {
        playBtn = getActivity().findViewById(R.id.playBtn);
        nextBtn = getActivity().findViewById(R.id.nextBtn);
        songName = getActivity().findViewById(R.id.txtSongName);
        songArtist = getActivity().findViewById(R.id.txtSongArtist);
        songControlViewModel = ViewModelProviders.of(getActivity()).get(SongControlViewModel.class);
        SongControlViewModel.isPlaying.observe(getActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean == true) {
                    playBtn.setImageResource(R.drawable.ic_pause_circle_filled_orange_40dp);
                } else {
                    playBtn.setImageResource(R.drawable.ic_play_circle_filled_orange_40dp);
                }
            }
        });

        SongControlViewModel.currentSong.observe(getActivity(), new Observer<Song>() {
            @Override
            public void onChanged(Song song) {
                songName.setText(song.getName());
                songArtist.setText(song.getArtists());
            }
        });

    }
}

package koha13.spasic.activity.csactivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import koha13.spasic.R;
import koha13.spasic.data.SongControlViewModel;
import koha13.spasic.entity.Song;

public class LyricFragment extends Fragment {

    private TextView tvLyric;
    private SongControlViewModel songControlViewModel;

    public LyricFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_lyric_cs, container, false);
        initView(rootView);
        setUpObs();
        return rootView;
    }

    private void setUpObs() {
        songControlViewModel = ViewModelProviders.of(getActivity()).get(SongControlViewModel.class);
        songControlViewModel.currentSong.observe(getActivity(), new Observer<Song>() {
            @Override
            public void onChanged(Song song) {
                if(song.getLyric() != null)
                    tvLyric.setText(song.getLyric());
            }
        });
    }

    private void initView(View rootView){
        tvLyric = rootView.findViewById(R.id.lyric_tv);
    }
}

package koha13.spasic.FragmentCurrentSong;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import koha13.spasic.R;
import koha13.spasic.adapter.SongCardAdapter;
import koha13.spasic.model.Song;

public class QueueFragment extends Fragment {

    private RecyclerView.LayoutManager layoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_queue, container, false);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerQueue);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        List<Song> songs = new ArrayList<>();
        songs.add(new Song("Test1", "Artist", 123));
        songs.add(new Song("Test2", "Artist", 123));
        songs.add(new Song("Test3", "Artist", 123));
        SongCardAdapter songCardAdapter = new SongCardAdapter(songs, getActivity());
        recyclerView.setAdapter(songCardAdapter);
        return rootView;
    }
}

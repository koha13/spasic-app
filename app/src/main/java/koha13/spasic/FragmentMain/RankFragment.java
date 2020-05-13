package koha13.spasic.FragmentMain;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import koha13.spasic.R;
import koha13.spasic.adapter.SongCardAdapter;
import koha13.spasic.model.Song;

public class RankFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    public RankFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private RecyclerView.LayoutManager layoutManager;
    private Spinner spinner;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_rank, container, false);

        //Add spinner
        spinner = (Spinner) rootView.findViewById(R.id.regionSpinner);
        List<String> list = new ArrayList<>();
        list.add("Nhạc US/UK");
        list.add("Nhạc Việt");
        list.add("Nhạc Hàn");
        list.add("Nhạc Trung");
        ArrayAdapter<String> adapter = new ArrayAdapter(this.getActivity(), android.R.layout.simple_spinner_item,list);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);

        //Add recycler viewcard
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerViewSong);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        List<Song> songs = new ArrayList<>();
        songs.add(new Song("Test1","Artist",123));
        songs.add(new Song("Test2","Artist",123));
        songs.add(new Song("Test3","Artist",123));
        SongCardAdapter songCardAdapter = new SongCardAdapter(songs, getActivity());
        recyclerView.setAdapter(songCardAdapter);

        //Swipe refresh
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.rank_swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        //First load when view created
//        mSwipeRefreshLayout.post(new Runnable() {
//            @Override
//            public void run() {
//                mSwipeRefreshLayout.setRefreshing(true);
//                loadRecyclerViewData();
//            }
//        });
        return rootView;
    }

    @Override
    public void onRefresh() {
        loadRecyclerViewData();
    }

    private void loadRecyclerViewData(){
        // Showing refresh animation before making http call
        mSwipeRefreshLayout.setRefreshing(true);
        Toast.makeText(getActivity(), "Ahlo", Toast.LENGTH_SHORT).show();

        // Stop refresh animation
        mSwipeRefreshLayout.setRefreshing(false);
    }
}

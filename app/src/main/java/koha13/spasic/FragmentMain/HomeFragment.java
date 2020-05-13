package koha13.spasic.FragmentMain;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import koha13.spasic.R;
import koha13.spasic.adapter.BigCVAdapter;
import koha13.spasic.model.Song;

public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    public HomeFragment() {
        // Required empty public constructor
    }

    private RecyclerView.LayoutManager layoutManager;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        //Recycler view big Cardview
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerViewSongMain);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        List<Song> songs = new ArrayList<>();
        songs.add(new Song("Test1","Artist",123));
        songs.add(new Song("Test2","Artist",123));
        songs.add(new Song("Test3","Artist",123));
        BigCVAdapter songCardAdapter = new BigCVAdapter(songs, getActivity());
        recyclerView.setAdapter(songCardAdapter);

        //Swipe refresh
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.main_swipe_container);
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

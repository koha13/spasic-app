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
import koha13.spasic.api.AllSongsFeed;
import koha13.spasic.adapter.BigCVAdapter;
import koha13.spasic.api.ResponseCallBack;
import koha13.spasic.data.StaticData;
import koha13.spasic.model.Song;

public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, ResponseCallBack<List<Song>> {
    private RecyclerView.LayoutManager layoutManager;
    private List<Song> songs;
    public static SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView recyclerView;
    BigCVAdapter songCardAdapter;
    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        //Recycler view big Cardview
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerViewSongMain);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        songCardAdapter = new BigCVAdapter(StaticData.songs, getActivity());
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

    private void loadRecyclerViewData() {
        // Showing refresh animation before making http call
        mSwipeRefreshLayout.setRefreshing(true);
        AllSongsFeed.getAllSongs(this);

    }

    @Override
    public void onDataSuccess(List<Song> data) {
        StaticData.updateAllSongs(data);
        mSwipeRefreshLayout.setRefreshing(false);
        songCardAdapter = new BigCVAdapter(StaticData.songs, getActivity());
        recyclerView.setAdapter(songCardAdapter);
    }

    @Override
    public void onDataFail(String message) {
        mSwipeRefreshLayout.setRefreshing(false);
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailed(Throwable error) {
        mSwipeRefreshLayout.setRefreshing(false);
        Toast.makeText(getActivity(), "Can't load", Toast.LENGTH_SHORT).show();
    }
}

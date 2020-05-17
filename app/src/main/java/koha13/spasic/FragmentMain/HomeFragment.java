package koha13.spasic.FragmentMain;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.List;

import koha13.spasic.R;
import koha13.spasic.api.ApiFeed;
import koha13.spasic.adapter.BigCVAdapter;
import koha13.spasic.api.ResponseCallBack;
import koha13.spasic.data.DataViewModel;
import koha13.spasic.data.StaticData;
import koha13.spasic.model.Song;

public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, ResponseCallBack<List<Song>> {
    private RecyclerView.LayoutManager layoutManager;
    private List<Song> songs;
    public static SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView recyclerView;
    private BigCVAdapter songCardAdapter;
    private DataViewModel dataViewModel;

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

        final Observer<List<Song>> allSongsChangeObserver = new Observer<List<Song>>() {
            @Override
            public void onChanged(List<Song> songs) {
                songCardAdapter = new BigCVAdapter(songs, getActivity());
                recyclerView.setAdapter(songCardAdapter);
            }
        };

        dataViewModel = ViewModelProviders.of(getActivity()).get(DataViewModel.class);
        dataViewModel.getAllSongs().observe(getActivity(), allSongsChangeObserver);

        //Swipe refresh
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.main_swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        return rootView;
    }

    @Override
    public void onRefresh() {
        loadRecyclerViewData();
    }

    private void loadRecyclerViewData() {
        // Showing refresh animation before making http call
        mSwipeRefreshLayout.setRefreshing(true);
        ApiFeed.getAllSongs(this);

    }

    @Override
    public void onDataSuccess(List<Song> data) {
        dataViewModel.updateAllSongs(data);
        mSwipeRefreshLayout.setRefreshing(false);
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

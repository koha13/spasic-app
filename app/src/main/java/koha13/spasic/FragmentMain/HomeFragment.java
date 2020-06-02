package koha13.spasic.FragmentMain;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.List;

import koha13.spasic.R;
import koha13.spasic.adapter.BigCVAdapter;
import koha13.spasic.adapter.EndlessRecyclerViewScrollListener;
import koha13.spasic.api.ResponseCallback;
import koha13.spasic.data.AllSongsViewModel;
import koha13.spasic.entity.Song;

public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private LinearLayoutManager layoutManager;
    private List<Song> songs;
    public static SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView recyclerView;
    private BigCVAdapter songCardAdapter;
    private EndlessRecyclerViewScrollListener scrollListener;

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
        recyclerView = rootView.findViewById(R.id.recyclerViewSongMain);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        songCardAdapter = new BigCVAdapter(getActivity());
        recyclerView.setAdapter(songCardAdapter);
        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Log.d("Here", "Load");
                AllSongsViewModel.moreSongs(page, null);
            }
        };
        // Adds the scroll listener to RecyclerView
        recyclerView.addOnScrollListener(scrollListener);

        //Swipe refresh
        mSwipeRefreshLayout = rootView.findViewById(R.id.main_swipe_container);
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
        AllSongsViewModel.fetchAllSongs(new ResponseCallback<List<Song>>() {
            @Override
            public void onDataSuccess(List<Song> data) {
                mSwipeRefreshLayout.setRefreshing(false);
                songCardAdapter.notifyDataSetChanged();
            }

            @Override
            public void onDataFail(String message) {
                mSwipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailed(Throwable error) {
                mSwipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}

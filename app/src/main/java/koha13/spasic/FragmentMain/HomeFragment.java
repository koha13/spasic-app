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
import koha13.spasic.adapter.BigCVAdapter;
import koha13.spasic.adapter.EndlessRecyclerViewScrollListener;
import koha13.spasic.api.ResponseCallback;
import koha13.spasic.data.AllSongsViewModel;
import koha13.spasic.data.SongControlViewModel;
import koha13.spasic.entity.Song;

public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private LinearLayoutManager layoutManager;
    private List<Song> songs;
    public static SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView recyclerView;
    private BigCVAdapter songCardAdapter;
    private EndlessRecyclerViewScrollListener scrollListener;
    private SongControlViewModel songControlViewModel;

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

                AllSongsViewModel.moreSongs(page, null);
                songCardAdapter.notifyDataSetChanged();
            }
        };
        // Adds the scroll listener to RecyclerView
        recyclerView.addOnScrollListener(scrollListener);

        //Swipe refresh
        mSwipeRefreshLayout = rootView.findViewById(R.id.main_swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        //Add observe to current song and playing state change to notify adapter
        songControlViewModel = ViewModelProviders.of(getActivity()).get(SongControlViewModel.class);
        songControlViewModel.currentSong.observe(getActivity(), new Observer<Song>() {
            @Override
            public void onChanged(Song song) {
                songCardAdapter.notifyDataSetChanged();
            }
        });
        songControlViewModel.isPlaying.observe(getActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                songCardAdapter.notifyDataSetChanged();
            }
        });

        return rootView;
    }

    @Override
    public void onRefresh() {
        loadRecyclerViewData();
    }

    private void loadRecyclerViewData() {
        // Showing refresh animation before making http call
        AllSongsViewModel.reset();
        songCardAdapter.notifyDataSetChanged();
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

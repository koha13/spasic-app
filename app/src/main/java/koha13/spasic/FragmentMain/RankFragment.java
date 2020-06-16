package koha13.spasic.FragmentMain;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import koha13.spasic.R;
import koha13.spasic.adapter.SongCardAdapter;
import koha13.spasic.api.ResponseCallback;
import koha13.spasic.data.FetchApiImpl;
import koha13.spasic.data.SongControlViewModel;
import koha13.spasic.entity.Song;

public class RankFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView.LayoutManager layoutManager;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    SongCardAdapter songCardAdapter;
    Button playAllRank;
    List<Song> songs;

    public RankFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_rank, container, false);

        //Add recycler viewcard
        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerViewSong);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        songs = new ArrayList<>();
        songCardAdapter = new SongCardAdapter(songs, getActivity());
        recyclerView.setAdapter(songCardAdapter);

        //Swipe refresh
        mSwipeRefreshLayout = rootView.findViewById(R.id.rank_swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        //First load when view created
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                loadRecyclerViewData();
            }
        });
        playAllRank = rootView.findViewById(R.id.la_rank);
        playAllRank.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                SongControlViewModel.playList(songs);
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
        mSwipeRefreshLayout.setRefreshing(true);
        FetchApiImpl.getRank(new ResponseCallback<List<Song>>() {
            @Override
            public void onDataSuccess(List<Song> data) {
                mSwipeRefreshLayout.setRefreshing(false);
                songs = data;
                songCardAdapter.reset();
                songCardAdapter.addSong(songs);
            }

            @Override
            public void onDataFail(String message) {
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailed(Throwable error) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

    }
}

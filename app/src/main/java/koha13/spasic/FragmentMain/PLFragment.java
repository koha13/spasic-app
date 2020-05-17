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

import java.util.ArrayList;
import java.util.List;

import koha13.spasic.R;
import koha13.spasic.adapter.PLCardAdapter;
import koha13.spasic.api.ResponseCallBack;
import koha13.spasic.data.AllPlaylistsViewModel;
import koha13.spasic.model.Playlist;
import koha13.spasic.model.Song;

public class PLFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, ResponseCallBack<List<Playlist>> {
    private RecyclerView.LayoutManager layoutManager;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private AllPlaylistsViewModel allPlaylistsViewModel;
    private RecyclerView recyclerView;

    public PLFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pl, container, false);

        //Recycler view big Cardview
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerViewSongPL);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        allPlaylistsViewModel = ViewModelProviders.of(getActivity()).get(AllPlaylistsViewModel.class);
        allPlaylistsViewModel.getAllPlaylists().observe(getActivity(), new Observer<List<Playlist>>() {
            @Override
            public void onChanged(List<Playlist> playlists) {
                PLCardAdapter songCardAdapter = new PLCardAdapter(playlists, getActivity());
                recyclerView.setAdapter(songCardAdapter);
            }
        });

        //Swipe refresh
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.pl_swipe_container);
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
        allPlaylistsViewModel.fetchAllPlaylists(this);
    }

    @Override
    public void onDataSuccess(List<Playlist> data) {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onDataFail(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailed(Throwable error) {
        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
    }
}

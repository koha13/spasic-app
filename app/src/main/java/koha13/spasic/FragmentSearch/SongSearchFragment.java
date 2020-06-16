package koha13.spasic.FragmentSearch;

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
import koha13.spasic.adapter.EndlessRecyclerViewScrollListener;
import koha13.spasic.adapter.SongCardAdapter;
import koha13.spasic.api.ResponseCallback;
import koha13.spasic.data.FetchApiImpl;
import koha13.spasic.entity.Song;

public class SongSearchFragment extends Fragment {

    private String searchKey;
    private RecyclerView songRe;
    private SongCardAdapter songAdapter;
    private LinearLayoutManager linearLayoutManager;
    private EndlessRecyclerViewScrollListener scrollListener;

    public SongSearchFragment() {
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
        updateSearch();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_song_search, container, false);
        initView(rootView);
        return rootView;
    }

    private void initView(View rootView) {
        songRe = rootView.findViewById(R.id.re_song_search);
        List<Song> songs = new ArrayList<>();
        songAdapter = new SongCardAdapter(songs, getActivity());
        linearLayoutManager = new LinearLayoutManager(getActivity());
        songRe.setLayoutManager(linearLayoutManager);
        songRe.setAdapter(songAdapter);
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager, 20) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                FetchApiImpl.searchSong(searchKey, page, new ResponseCallback<List<Song>>() {
                    @Override
                    public void onDataSuccess(List<Song> data) {
                        songAdapter.addSong(data);
                    }

                    @Override
                    public void onDataFail(String message) {

                    }

                    @Override
                    public void onFailed(Throwable error) {

                    }
                });
            }
        };
        songRe.addOnScrollListener(scrollListener);
    }

    private void updateSearch() {
        songAdapter.reset();
        FetchApiImpl.searchSong(searchKey, 0, new ResponseCallback<List<Song>>() {
            @Override
            public void onDataSuccess(List<Song> data) {
                songAdapter.addSong(data);
            }

            @Override
            public void onDataFail(String message) {

            }

            @Override
            public void onFailed(Throwable error) {

            }
        });
    }
}

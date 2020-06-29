package koha13.spasic.activity.searchactivity.fragmentsearch;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import koha13.spasic.R;
import koha13.spasic.adapter.ArtistGridViewAdapter;
import koha13.spasic.adapter.EndlessScrollListener;
import koha13.spasic.api.ResponseCallback;
import koha13.spasic.data.FetchApiImpl;
import koha13.spasic.entity.Artist;

public class ArtistSearchFragment extends Fragment {
    private String searchKey;
    private GridView artistGv;
    private ArtistGridViewAdapter artistGridViewAdapter;

    public ArtistSearchFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_artist_search, container, false);
        initView(rootView);
        Log.d("Here", "Load again");
        return rootView;
    }

    private void initView(View rootView) {
        artistGv = rootView.findViewById(R.id.gv_artist_search);
        List<Artist> artists = new ArrayList<>();
        artistGridViewAdapter = new ArtistGridViewAdapter(artists, getActivity());
        artistGv.setAdapter(artistGridViewAdapter);
        artistGv.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                FetchApiImpl.searchArtist(searchKey, page, new ResponseCallback<List<Artist>>() {
                    @Override
                    public void onDataSuccess(List<Artist> data) {
                        artistGridViewAdapter.addArtist(data);
                    }

                    @Override
                    public void onDataFail(String message) {
                    }

                    @Override
                    public void onFailed(Throwable error) {
                    }
                });
                return true; // ONLY if more data is actually being loaded; false otherwise.
            }
        });
    }

    private void updateSearch() {
        artistGridViewAdapter.reset();
        FetchApiImpl.searchArtist(searchKey, 0, new ResponseCallback<List<Artist>>() {
            @Override
            public void onDataSuccess(List<Artist> data) {
                artistGridViewAdapter.addArtist(data);
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

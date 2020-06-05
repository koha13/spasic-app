package koha13.spasic.FragmentSearch;

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
import koha13.spasic.activity.SearchActivity;
import koha13.spasic.adapter.AlbumGridViewAdapter;
import koha13.spasic.adapter.EndlessScrollListener;
import koha13.spasic.api.ResponseCallback;
import koha13.spasic.data.SearchApiImpl;
import koha13.spasic.entity.Album;

public class AlbumSearchFragment extends Fragment {
    private String searchKey;
    private GridView albumGv;
    private AlbumGridViewAdapter albumGridViewAdapter;

    public AlbumSearchFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_album_search, container, false);
        initView(rootView);
        return rootView;
    }

    private void initView(View rootView) {
        albumGv = rootView.findViewById(R.id.gv_album_search);
        List<Album> albums = new ArrayList<>();
        albumGridViewAdapter = new AlbumGridViewAdapter(albums, getActivity());
        albumGv.setAdapter(albumGridViewAdapter);
        albumGv.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                SearchApiImpl.searchAlbum(searchKey, page, new ResponseCallback<List<Album>>() {
                    @Override
                    public void onDataSuccess(List<Album> data) {
                        albumGridViewAdapter.addAlbums(data);
                    }

                    @Override
                    public void onDataFail(String message) { }

                    @Override
                    public void onFailed(Throwable error) { }
                });
                return true; // ONLY if more data is actually being loaded; false otherwise.
            }
        });
    }

    private void updateSearch() {
        albumGridViewAdapter.reset();
        SearchApiImpl.searchAlbum(searchKey, 0, new ResponseCallback<List<Album>>() {
            @Override
            public void onDataSuccess(List<Album> data) {
                albumGridViewAdapter.addAlbums(data);
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

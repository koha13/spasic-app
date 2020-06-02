package koha13.spasic.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import koha13.spasic.FragmentSearch.AlbumSearchFragment;
import koha13.spasic.FragmentSearch.ArtistSearchFragment;
import koha13.spasic.FragmentSearch.SongSearchFragment;
import koha13.spasic.R;
import koha13.spasic.adapter.ViewPagerAdapter;

public class SearchActivity extends AppCompatActivity {

    final int LIMIT_ITEM = 2;

    private Timer timer; //timer to debounce search
    AutoCompleteTextView searchBox;
    ImageButton timesBtn;
    ImageButton backBtn;
//    List<String> suggestions = new ArrayList<>();
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private SongSearchFragment songSearchFragment;
    private AlbumSearchFragment albumSearchFragment;
    private ArtistSearchFragment artistSearchFragment;
    private ViewPagerAdapter adapter;
    private ArrayAdapter<String> adapterSuggestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initView();
    }

    private void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void initView() {
        searchBox = findViewById(R.id.searchbox);
//        adapterSuggestion = new ArrayAdapter<String>(SearchActivity.this,
//                android.R.layout.simple_dropdown_item_1line, suggestions);
//        searchBox.setAdapter(adapterSuggestion);
        timesBtn = findViewById(R.id.btn_delete_search);
        backBtn = findViewById(R.id.back_btn_search);
        searchBox.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                hideKeyboard(SearchActivity.this);
                search();
            }
        });

        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (timer != null) {
                    timer.cancel();
                }
                if (s.length() == 0) {
                    timesBtn.setVisibility(View.INVISIBLE);
                } else {
                    timesBtn.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
//                        updateSuggestion();
                    }
                }, 600);
            }
        });

        searchBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    hideKeyboard(SearchActivity.this);
                    search();
                    return true;
                }
                return false;
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        timesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchBox.setText("");
            }
        });

        viewPager = findViewById(R.id.viewpager_search);
        viewPager.setOffscreenPageLimit(3);
        tabLayout = findViewById(R.id.tabs_search);
        tabLayout.setupWithViewPager(viewPager);
        songSearchFragment = new SongSearchFragment();
        albumSearchFragment = new AlbumSearchFragment();
        artistSearchFragment = new ArtistSearchFragment();
        addTabs(viewPager);
        setupTabIcons();
    }

    private void addTabs(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(songSearchFragment, "Song");
        adapter.addFrag(albumSearchFragment, "Album");
        adapter.addFrag(artistSearchFragment, "Artist");
        viewPager.setAdapter(adapter);
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setText("Songs");
        tabLayout.getTabAt(1).setText("Albums");
        tabLayout.getTabAt(2).setText("Artists");
    }

    private void search() {
        songSearchFragment.setSearchKey(searchBox.getText().toString());
        albumSearchFragment.setSearchKey(searchBox.getText().toString());
        artistSearchFragment.setSearchKey(searchBox.getText().toString());
    }


//    private void updateSuggestion(){
//        suggestions = new ArrayList<>();
//        SearchApiImpl.searchSong(searchBox.getText().toString(), 0, new ResponseCallback<List<Song>>() {
//            @Override
//            public void onDataSuccess(List<Song> data) {
//                for(Song s: data){
//                    if(!suggestions.contains((String) s.getName())){
//                        suggestions.add(s.getName());
//                    }
//                }
//            }
//
//            @Override
//            public void onDataFail(String message) {
//
//            }
//
//            @Override
//            public void onFailed(Throwable error) {
//
//            }
//        });
//        SearchApiImpl.searchArtist(searchBox.getText().toString(), 0, new ResponseCallback<List<Artist>>() {
//            @Override
//            public void onDataSuccess(List<Artist> data) {
//                for(Artist s: data){
//                    if(!suggestions.contains((String) s.getName())){
//                        suggestions.add(s.getName());
//                    }
//                }
//            }
//
//            @Override
//            public void onDataFail(String message) {
//
//            }
//
//            @Override
//            public void onFailed(Throwable error) {
//
//            }
//        });
//        SearchApiImpl.searchAlbum(searchBox.getText().toString(), 0, new ResponseCallback<List<Album>>() {
//            @Override
//            public void onDataSuccess(List<Album> data) {
//                for(Album s: data){
//                    if(!suggestions.contains((String) s.getName())){
//                        suggestions.add(s.getName());
//                    }
//                }
//            }
//
//            @Override
//            public void onDataFail(String message) {
//
//            }
//
//            @Override
//            public void onFailed(Throwable error) {
//
//            }
//        });
//    }
}

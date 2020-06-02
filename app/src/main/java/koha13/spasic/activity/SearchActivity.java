package koha13.spasic.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import koha13.spasic.R;
import koha13.spasic.adapter.AlbumGridViewAdapter;
import koha13.spasic.adapter.ArtistGridViewAdapter;
import koha13.spasic.adapter.SongCardAdapter;
import koha13.spasic.data.AllSongsViewModel;
import koha13.spasic.entity.Album;
import koha13.spasic.entity.Artist;
import koha13.spasic.entity.Song;

public class SearchActivity extends AppCompatActivity {

    final int LIMIT_ITEM = 2;

    AutoCompleteTextView searchBox;
    ImageButton timesBtn;
    ImageButton backBtn;
    RecyclerView songRe;
    GridView artistGv;
    GridView albumGv;
    SongCardAdapter songAdapter;
    ImageButton moreSong;
    ImageButton moreArtist;
    ImageButton moreAlbum;
    List<Song> songsSearch;
    boolean isExpandSongs;
    List<Artist> artists;
    ArtistGridViewAdapter playlistGridViewAdapter;
    boolean isExpandArtists;
    List<Album> albums;
    boolean isExpandAlbum;
    AlbumGridViewAdapter albumGridViewAdapter;
    List<String> suggestions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchBox = findViewById(R.id.searchbox);
        timesBtn = findViewById(R.id.btn_delete_search);
        backBtn = findViewById(R.id.back_btn_search);
        songRe = findViewById(R.id.re_song);
        artistGv = findViewById(R.id.gv_artist);
        albumGv = findViewById(R.id.gv_album);
        moreSong = findViewById(R.id.more_song);
        moreAlbum = findViewById(R.id.more_album);
        moreArtist = findViewById(R.id.more_artist);

        updateSuggestion();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, suggestions);
        searchBox.setAdapter(adapter);
        searchBox.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String temp = (String) parent.getItemAtPosition(position);
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
                if (s.length() == 0) {
                    timesBtn.setVisibility(View.INVISIBLE);
                } else {
                    timesBtn.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

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
    }

    private void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void search() {
        searchSong();
        searchArtist();
        searchAlbum();
    }

    private void searchSong() {
        songsSearch = new ArrayList<>();
        for (Song s : AllSongsViewModel.getAllSongs()) {
            if (s.getName().toLowerCase().contains(searchBox.getText().toString().toLowerCase().trim())) {
                songsSearch.add(s);
            }
        }
        if (songsSearch.size() == 0) {
            LinearLayout songsLinearLayout = findViewById(R.id.ln_songs);
            songsLinearLayout.setVisibility(View.GONE);
            return;
        }
        else{
            LinearLayout songsLinearLayout = findViewById(R.id.ln_songs);
            songsLinearLayout.setVisibility(View.VISIBLE);
        }
        isExpandSongs = false;
        songRe.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(SearchActivity.this);
        songRe.setLayoutManager(layoutManager);
        songAdapter = new SongCardAdapter(songsSearch, SearchActivity.this);
        songAdapter.setNum(songsSearch.size() >= LIMIT_ITEM ? LIMIT_ITEM : songsSearch.size());
        songRe.setAdapter(songAdapter);
        if (songsSearch.size() > LIMIT_ITEM) {
            moreSong.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isExpandSongs) {
                        songAdapter.setNum(songsSearch.size() >= LIMIT_ITEM ? LIMIT_ITEM : songsSearch.size());
                        songAdapter.notifyDataSetChanged();
                        moreSong.setImageResource(R.drawable.ic_keyboard_arrow_down_white_24dp);
                        isExpandSongs = false;
                    } else {
                        songAdapter.setNum(-1);
                        songAdapter.notifyDataSetChanged();
                        moreSong.setImageResource(R.drawable.ic_keyboard_arrow_up_white_24dp);
                        isExpandSongs = true;
                    }
                }
            });
        } else {
            moreSong.setVisibility(View.GONE);
        }
    }

    private void searchArtist() {
        artists = new ArrayList<>();
        for (Song s : AllSongsViewModel.getAllSongs()) {
            if (s.getArtists().toLowerCase().contains(searchBox.getText().toString().toLowerCase().trim())) {
                boolean check = true;
                for (Artist a : artists) {
                    if (a.getName().compareToIgnoreCase(s.getArtists().toLowerCase()) == 0) {
                        check = false;
                        break;
                    }
                }
                if (check) {
                    artists.add(new Artist(s.getArtists(), s.getSongImage()));
                }
            }
        }
        if (artists.size() == 0) {
            LinearLayout artistLinearLayout = findViewById(R.id.ln_artist);
            artistLinearLayout.setVisibility(View.GONE);
            return;
        }
        else{
            LinearLayout artistLinearLayout = findViewById(R.id.ln_artist);
            artistLinearLayout.setVisibility(View.VISIBLE);
        }
        playlistGridViewAdapter = new ArtistGridViewAdapter(artists, SearchActivity.this);
        playlistGridViewAdapter.setNum(artists.size() >= LIMIT_ITEM ? LIMIT_ITEM : artists.size());
        artistGv.setAdapter(playlistGridViewAdapter);
        isExpandArtists = false;
        if (artists.size() > LIMIT_ITEM) {
            moreArtist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isExpandArtists) {
                        playlistGridViewAdapter.setNum(artists.size() >= LIMIT_ITEM ? LIMIT_ITEM : artists.size());
                        playlistGridViewAdapter.notifyDataSetChanged();
                        moreArtist.setImageResource(R.drawable.ic_keyboard_arrow_down_white_24dp);
                        isExpandArtists = false;
                    } else {
                        playlistGridViewAdapter.setNum(-1);
                        playlistGridViewAdapter.notifyDataSetChanged();
                        moreArtist.setImageResource(R.drawable.ic_keyboard_arrow_up_white_24dp);
                        isExpandArtists = true;
                    }
                }
            });
        } else {
            moreArtist.setVisibility(View.GONE);
        }
    }

    private void searchAlbum(){
        albums = new ArrayList<>();
        for (Song s : AllSongsViewModel.getAllSongs()) {
            if (s.getAlbum().toLowerCase().contains(searchBox.getText().toString().toLowerCase().trim())) {
                boolean check = true;
                for (Album a : albums) {
                    if (a.getName().compareToIgnoreCase(s.getAlbum().toLowerCase()) == 0) {
                        check = false;
                        break;
                    }
                }
                if (check) {
                    albums.add(new Album(s.getAlbum(), s.getArtists(), s.getSongImage()));
                }
            }
        }
        Log.d("Here", String.valueOf(albums.size()));
        if (albums.size() == 0) {
            LinearLayout albumLinearLayout = findViewById(R.id.ln_album);
            albumLinearLayout.setVisibility(View.GONE);
            return;
        }
        else{
            LinearLayout albumLinearLayout = findViewById(R.id.ln_album);
            albumLinearLayout.setVisibility(View.VISIBLE);
        }
        albumGridViewAdapter = new AlbumGridViewAdapter(albums, SearchActivity.this);
        albumGridViewAdapter.setNum(albums.size() >= LIMIT_ITEM ? LIMIT_ITEM : albums.size());
        albumGv.setAdapter(albumGridViewAdapter);
        isExpandAlbum = false;
        if (albums.size() > LIMIT_ITEM) {
            moreAlbum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isExpandArtists) {
                        albumGridViewAdapter.setNum(albums.size() >= LIMIT_ITEM ? LIMIT_ITEM : albums.size());
                        albumGridViewAdapter.notifyDataSetChanged();
                        moreAlbum.setImageResource(R.drawable.ic_keyboard_arrow_down_white_24dp);
                        isExpandAlbum = false;
                    } else {
                        albumGridViewAdapter.setNum(-1);
                        albumGridViewAdapter.notifyDataSetChanged();
                        moreAlbum.setImageResource(R.drawable.ic_keyboard_arrow_up_white_24dp);
                        isExpandAlbum = true;
                    }
                }
            });
        } else {
            moreAlbum.setVisibility(View.GONE);
        }
    }

    private void updateSuggestion(){
        suggestions = new ArrayList<>();
        for (Song s : AllSongsViewModel.getAllSongs()) {
            suggestions.add(s.getName());
        }
        for (Song s : AllSongsViewModel.getAllSongs()) {
            boolean checkArtist = true;
            boolean checkAlbum = true;
            for(String stringTemp:suggestions){
                if(s.getAlbum().compareToIgnoreCase(stringTemp) == 0){
                    checkAlbum = false;
                }
                if(s.getArtists().compareToIgnoreCase(stringTemp) == 0){
                    checkArtist = false;
                }
            }
            if(checkArtist) suggestions.add(s.getArtists());
            if (checkAlbum) suggestions.add(s.getAlbum());
        }
    }
}

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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import koha13.spasic.R;
import koha13.spasic.adapter.SongCardAdapter;
import koha13.spasic.data.AllSongsViewModel;
import koha13.spasic.entity.Song;

public class SearchActivity extends AppCompatActivity {

    final int LIMIT_ITEM = 4;

    AutoCompleteTextView searchBox;
    ImageButton timesBtn;
    ImageButton backBtn;
    RecyclerView songRe;
    RecyclerView artistRe;
    RecyclerView albumRe;
    SongCardAdapter songAdapter;
    LinearLayout searchLinear;
    ImageButton moreSong;
    ImageButton moreArtist;
    ImageButton moreAlbum;
    List<Song> songsSearch;
    boolean isExpandSongs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchBox = findViewById(R.id.searchbox);
        timesBtn = findViewById(R.id.btn_delete_search);
        backBtn = findViewById(R.id.back_btn_search);
        songRe = findViewById(R.id.re_song);
        artistRe = findViewById(R.id.re_artist);
        albumRe = findViewById(R.id.re_album);
        searchLinear = findViewById(R.id.linear_search);
        moreSong = findViewById(R.id.more_song);
        moreAlbum = findViewById(R.id.more_album);
        moreArtist = findViewById(R.id.more_artist);

        List<String> l = new ArrayList<>();
        l.add("Here");
        l.add("Here1");
        l.add("Here2");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, l);
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
    }

    private void searchSong(){
        songsSearch = new ArrayList<>();
        for (Song s : AllSongsViewModel.getAllSongs().getValue()) {
            if (s.getName().toLowerCase().contains(searchBox.getText().toString().toLowerCase().trim())) {
                songsSearch.add(s);
            }
        }
        isExpandSongs = false;
        searchLinear.setVisibility(View.VISIBLE);
        songRe.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(SearchActivity.this);
        songRe.setLayoutManager(layoutManager);
        songAdapter = new SongCardAdapter(songsSearch, SearchActivity.this);
        songAdapter.setNum(songsSearch.size() >= LIMIT_ITEM? LIMIT_ITEM:songsSearch.size());
        songRe.setAdapter(songAdapter);
        if (songsSearch.size() > LIMIT_ITEM) {
            moreSong.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isExpandSongs){
                        songAdapter.setNum(songsSearch.size() >= LIMIT_ITEM? LIMIT_ITEM:songsSearch.size());
                        songAdapter.notifyDataSetChanged();
                        moreSong.setImageResource(R.drawable.ic_keyboard_arrow_down_white_24dp);
                        isExpandSongs = false;
                    }else{
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
}

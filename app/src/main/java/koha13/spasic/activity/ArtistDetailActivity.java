package koha13.spasic.activity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import koha13.spasic.R;
import koha13.spasic.adapter.SongCardAdapter;
import koha13.spasic.api.ResponseCallback;
import koha13.spasic.data.FetchApiImpl;
import koha13.spasic.data.SongControlViewModel;
import koha13.spasic.entity.Song;

public class ArtistDetailActivity extends AppCompatActivity {

    private ImageView albumImage;
    private TextView artistName;
    private TextView numSong;
    private ImageButton playAll;
    private RecyclerView recyclerView;
    private SongCardAdapter adapter;
    private String word;
    List<Song> songs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_detail);
        word = getIntent().getStringExtra("artistName");
        initView();
        fetchData();
    }

    private void initView() {
        recyclerView = findViewById(R.id.rv_song_artist);
        albumImage = findViewById(R.id.image_song_artist);
        artistName = findViewById(R.id.artist_name);
        numSong = findViewById(R.id.artist_num_song);
        playAll = findViewById(R.id.play_all_artist);
        playAll.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                SongControlViewModel.playList(songs);
            }
        });
        ImageButton backBtn = findViewById(R.id.back_btn_artist);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    private void fetchData() {
        FetchApiImpl.getSongByArtist(word, new ResponseCallback<List<Song>>() {
            @Override
            public void onDataSuccess(List<Song> data) {
                Glide.with(ArtistDetailActivity.this)
                        .load(data.get(0).getSongImage())
                        .into(albumImage);
                artistName.setText(word);
                numSong.setText(data.size() + " bài hát");
                adapter = new SongCardAdapter(data, ArtistDetailActivity.this);
                recyclerView.setAdapter(adapter);
                songs = data;
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

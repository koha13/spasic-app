package koha13.spasic.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import koha13.spasic.R;
import koha13.spasic.adapter.SongCardAdapter;
import koha13.spasic.adapter.SongPLAdapter;
import koha13.spasic.data.AllPlaylistsViewModel;
import koha13.spasic.model.Song;

public class PLActivity extends AppCompatActivity {

    private RecyclerView.LayoutManager layoutManager;
    private int plID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pl);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerPLItem);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        plID=getIntent().getIntExtra("plID", -1);

        List<Song> songs = AllPlaylistsViewModel.getSongsFromPlByID(plID);

        SongPLAdapter songCardAdapter = new SongPLAdapter(songs, this);
        recyclerView.setAdapter(songCardAdapter);

        ImageButton backBtn = (ImageButton) findViewById(R.id.backBtnPL);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final ImageButton menuBtn = (ImageButton) findViewById(R.id.menuBtnPL);
        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(PLActivity.this, menuBtn);
                popupMenu.inflate(R.menu.option_pl);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.btn_rename_pl:
                                Toast.makeText(PLActivity.this, "Rename pl", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.btn_delete_pl:
                                Toast.makeText(PLActivity.this, "Delete pl", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });

    }
}

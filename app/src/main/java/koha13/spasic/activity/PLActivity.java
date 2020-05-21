package koha13.spasic.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import koha13.spasic.R;
import koha13.spasic.adapter.SongPLAdapter;
import koha13.spasic.data.AllPlaylistsViewModel;
import koha13.spasic.entity.Playlist;

public class PLActivity extends AppCompatActivity {

    private RecyclerView.LayoutManager layoutManager;
    private int plID;
    private AllPlaylistsViewModel viewModel;
    TextView plName;
    ImageButton backBtn;
    SongPLAdapter songCardAdapter;
    RecyclerView recyclerView;
    Playlist pl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pl);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerPLItem);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        plID=getIntent().getIntExtra("plID", -1);

        viewModel = ViewModelProviders.of(this).get(AllPlaylistsViewModel.class);
        viewModel.getAllPlaylists().observe(this, new Observer<List<Playlist>>() {
            @Override
            public void onChanged(List<Playlist> playlists) {
                pl = AllPlaylistsViewModel.getPlByID(plID);
                plName = findViewById(R.id.pl_activity_name);
                plName.setText(pl.getName());

                songCardAdapter = new SongPLAdapter(pl.getSongs(), PLActivity.this);
                recyclerView.setAdapter(songCardAdapter);

                backBtn = (ImageButton) findViewById(R.id.backBtnPL);
                backBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });

                if(pl.getId() != -1) {
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
                                            deletePL();
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
                }else{
                    ImageButton menuBtn = (ImageButton) findViewById(R.id.menuBtnPL);
                    menuBtn.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void deletePL(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xóa playlist: "+pl.getName()).setNegativeButton("Hủy", null)
                .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onBackPressed();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        AllPlaylistsViewModel.deletePlById(plID);
                    }
                },1000);

            }
        }).show();
    }
}

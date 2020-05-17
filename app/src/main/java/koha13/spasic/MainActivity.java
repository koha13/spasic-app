package koha13.spasic;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.List;

import koha13.spasic.FragmentMain.HomeFragment;
import koha13.spasic.FragmentMain.PLFragment;
import koha13.spasic.FragmentMain.RankFragment;
import koha13.spasic.activity.CurrentSongActivity;
import koha13.spasic.adapter.ViewPagerAdapter;
import koha13.spasic.api.ApiFeed;
import koha13.spasic.api.ResponseCallBack;
import koha13.spasic.data.DataViewModel;
import koha13.spasic.model.Song;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private DataViewModel dataViewModel;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        if (toolbar != null) {
//            setSupportActionBar(toolbar);
//            getSupportActionBar().setDisplayShowTitleEnabled(false);
//            getSupportActionBar().setDisplayShowHomeEnabled(false);
//        }

        dataViewModel = ViewModelProviders.of(this).get(DataViewModel.class);
        fetchData();

        LinearLayout songInfo = findViewById(R.id.song_info_ft);
        songInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CurrentSongActivity.class);
                startActivity(intent);
            }
        });

        viewPager = findViewById(R.id.viewpager);
        addTabs(viewPager);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
        tabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);
                int tabIconColor = ContextCompat.getColor(MainActivity.this, R.color.activeMain);
                tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                super.onTabUnselected(tab);
                int tabIconColor = ContextCompat.getColor(MainActivity.this, R.color.tabUnselectedIconColor);
                tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                super.onTabReselected(tab);
            }
        });
    }

    private void fetchData(){
        ApiFeed.getAllSongs(new ResponseCallBack<List<Song>>() {
            @Override
            public void onDataSuccess(List<Song> data) {
                dataViewModel.updateAllSongs(data);
            }

            @Override
            public void onDataFail(String message) {
                Toast.makeText(MainActivity.this, "Can't load", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailed(Throwable error) {
                Toast.makeText(MainActivity.this, "Can't load", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_home_white_24dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_equalizer_white_24dp);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_headset_white_24dp);
//        tabLayout.getTabAt(3).setIcon(R.drawable.ic_person_white_24dp);
    }

    private void addTabs(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new HomeFragment(), "MAIN");
        adapter.addFrag(new RankFragment(), "RANK");
        adapter.addFrag(new PLFragment(), "PL");
//        adapter.addFrag(new RankFragment(), "ORANGE");
        viewPager.setAdapter(adapter);
    }

}

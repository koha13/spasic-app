package koha13.spasic.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import koha13.spasic.FragmentMain.HomeFragment;
import koha13.spasic.FragmentMain.PLFragment;
import koha13.spasic.FragmentMain.RankFragment;
import koha13.spasic.R;
import koha13.spasic.adapter.ViewPagerAdapter;
import koha13.spasic.data.SongControlViewModel;
import koha13.spasic.service.MusicService;

public class MainActivity extends AppCompatActivity {

    public static MusicService musicService;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Intent playIntent;
    private boolean musicBound = false;
    private ServiceConnection musicConnection;
    ImageButton searchBtn;
    ImageButton userBtn;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set event to searchBtn
        searchBtn = findViewById(R.id.tb_search_ic);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });

        //Set event to userBtn
        userBtn = findViewById(R.id.tb_person_ic);
        userBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UserInfoActivity.class);
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

        initPlayerService();
    }

    private void initPlayerService() {
        SongControlViewModel.isPlaying.setValue(false);
        SongControlViewModel.randomState.setValue(false);

        musicConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
                musicService = binder.getService();
                musicBound = true;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                musicBound = false;
            }
        };
        if (playIntent == null) {
            playIntent = new Intent(this, MusicService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
        System.out.println("------------------------------");
        System.out.println(musicConnection);
        System.out.println(musicService);
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_home_white_24dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_equalizer_white_24dp);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_headset_white_24dp);
//        tabLayout.getTabAt(3).setIcon(R.drawable.ic_person_white_48dp);
    }

    private void addTabs(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new HomeFragment(), "MAIN");
        adapter.addFrag(new RankFragment(), "RANK");
        adapter.addFrag(new PLFragment(), "PL");
//        adapter.addFrag(new RankFragment(), "ORANGE");
        viewPager.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        stopService(playIntent);
        musicService = null;
        super.onDestroy();
    }
}

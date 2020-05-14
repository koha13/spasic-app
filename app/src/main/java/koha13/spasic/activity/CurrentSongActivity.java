package koha13.spasic.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.material.tabs.TabLayout;

import koha13.spasic.FragmentCurrentSong.CurrentSongFragment;
import koha13.spasic.FragmentMain.HomeFragment;
import koha13.spasic.FragmentMain.PLFragment;
import koha13.spasic.FragmentMain.RankFragment;
import koha13.spasic.MainActivity;
import koha13.spasic.R;
import koha13.spasic.adapter.ViewPagerAdapter;

public class CurrentSongActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ImageButton backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_song);

        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        viewPager = (ViewPager) findViewById(R.id.viewpager2);
        addTabs(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs2);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
        tabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager){
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);
                int tabIconColor = ContextCompat.getColor(CurrentSongActivity.this, R.color.activeMain);
                tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                super.onTabUnselected(tab);
                int tabIconColor = ContextCompat.getColor(CurrentSongActivity.this, R.color.tabUnselectedIconColor);
                tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                super.onTabReselected(tab);
            }
        });
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_music_note_white_24dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_queue_music_white_24dp);
    }

    private void addTabs(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new CurrentSongFragment(), "CURRENTSONG");
        adapter.addFrag(new RankFragment(), "RANK");
        viewPager.setAdapter(adapter);
    }
}

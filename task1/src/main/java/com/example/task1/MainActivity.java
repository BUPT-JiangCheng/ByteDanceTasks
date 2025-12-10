package com.example.task1;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.task1.fragments.PerformanceFragment;
import com.example.task1.fragments.PersonalFragment;
import com.example.task1.fragments.WeatherFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.view_pager);
        bottomNav = findViewById(R.id.bottom_nav);

        // 初始化Fragment
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new WeatherFragment());
        fragments.add(new PerformanceFragment());
        fragments.add(new PersonalFragment());

        // 设置Adapter
        viewPager.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return fragments.get(position);
            }

            @Override
            public int getItemCount() {
                return fragments.size();
            }
        });

        // 滑动切换ViewPager逻辑
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0: bottomNav.setSelectedItemId(R.id.nav_weather); break;
                    case 1: bottomNav.setSelectedItemId(R.id.nav_performance); break;
                    case 2: bottomNav.setSelectedItemId(R.id.nav_personal); break;
                }
            }
        });

        // 点击切换ViewPager逻辑
        bottomNav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_weather) viewPager.setCurrentItem(0);
            else if (item.getItemId() == R.id.nav_performance) viewPager.setCurrentItem(1);
            else if (item.getItemId() == R.id.nav_personal) viewPager.setCurrentItem(2);
            return true;
        });
    }
}
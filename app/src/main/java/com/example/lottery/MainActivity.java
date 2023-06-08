package com.example.lottery;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.lottery.fragment.CameraFragment;
import com.example.lottery.fragment.PictureListFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "lottery_main_activity";

    public Fragment[] getFragments() {
        return fragments;
    }

    private Fragment[] fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragments = new Fragment[2];
        fragments[0] = CameraFragment.newInstance("", "");
        fragments[1] = PictureListFragment.newInstance("", "");

        BottomNavigationView btn_bottom_nav = findViewById(R.id.btn_bottom_nav);

        btn_bottom_nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switchFragment(item.getItemId());
                return true;
            }
        });

        switchFragment(R.id.btn_picture);
    }

    private void switchFragment(int id) {
        Fragment fragment = null;
        switch (id) {
            case R.id.btn_camera:
                fragment = fragments[0];
                break;
            default:
                fragment = fragments[1];
                break;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fra_con, fragment).commit();
    }
}
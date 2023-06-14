package com.example.lottery.utils;

import com.example.lottery.MainActivity;
import com.example.lottery.R;
import com.example.lottery.fragment.CameraFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class PageUtils {
    public static void performNavigationSelection(MainActivity mainActivity,int index,int fragmentIndex){
        BottomNavigationView btn_bottom_nav = mainActivity.findViewById(R.id.btn_bottom_nav);
        btn_bottom_nav.setSelectedItemId(btn_bottom_nav.getMenu().getItem(index).getItemId());
        mainActivity.getSupportFragmentManager().beginTransaction().replace(R.id.fra_con, mainActivity.getFragments()[fragmentIndex]).commit();
    }
}

package com.project.pointofsaleproject.screen;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.project.pointofsaleproject.R;
import com.project.pointofsaleproject.fragment.HomeDashboardFragment;
import com.project.pointofsaleproject.fragment.HomeSettingFragment;
import com.project.pointofsaleproject.fragment.HomeTransactionFragment;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView btmNav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btmNav = findViewById(R.id.navigation);
        btmNav.getMenu().getItem(0).setIcon(R.drawable.ic_home);

        HomeDashboardFragment homeDashboardFragment = new HomeDashboardFragment();
        HomeSettingFragment homeSettingFragment = new HomeSettingFragment();
        HomeTransactionFragment homeTransactionFragment = new HomeTransactionFragment();

        replaceFragment(homeDashboardFragment);

        btmNav.setOnNavigationItemSelectedListener(
                item -> {
                    switch (item.getItemId()) {
                        case R.id.menu_setting:
                            replaceFragment(homeSettingFragment);
                            break;
                        case R.id.menu_transaction:
                            replaceFragment(homeTransactionFragment);
                            break;
                        default:
                            replaceFragment(homeDashboardFragment);
                            break;
                    }
                    return true;
                });
    }

    private void replaceFragment(Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

}
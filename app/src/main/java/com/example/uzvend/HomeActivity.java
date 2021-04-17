package com.example.uzvend;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.uzvend.Fragments.BasketFragment;
import com.example.uzvend.Fragments.CategoryFragment;
import com.example.uzvend.Fragments.HistoryFragment;
import com.example.uzvend.Fragments.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {
    
    BottomNavigationView bottomNavigation;
    FrameLayout frameLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ///changing the color os status text color///
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        ///status bar color end///


        bottomNavigation = (BottomNavigationView) findViewById(R.id.bottomNavigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigation);

        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,
                new HomeFragment()).commit();

    }


    private  BottomNavigationView.OnNavigationItemSelectedListener navigation =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    Fragment selectedFragment = null;
                    switch (item.getItemId())
                    {
                        case R.id.home:
                             selectedFragment = new HomeFragment();
                        break;

                        case R.id.category:
                            selectedFragment = new CategoryFragment();
                            break;

                        case R.id.basket:
                            selectedFragment = new BasketFragment();
                            break;

                        case R.id.history:
                            selectedFragment = new HistoryFragment();
                            break;

                   }
                    /// replacing by default fragment on home activity ///
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,
                             selectedFragment).commit();
                    return true;
                }
            };





}
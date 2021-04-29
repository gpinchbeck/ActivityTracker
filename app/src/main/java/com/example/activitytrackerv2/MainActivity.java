package com.example.activitytrackerv2;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new ProfileFragment()).commit();
    }

    //chest
    public void browser1(View view){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=MxnzcssW-tk"));
        startActivity(browserIntent);
    }
    //shoulder
    public void browser2(View view){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=2Vprklw8cu8"));
        startActivity(browserIntent);
    }
    //back
    public void browser3(View view){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=8LJ3Q3Fsrzs"));
        startActivity(browserIntent);
    }
    //legs
    public void browser4(View view){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=Xg9B6pqHUQE&t=320s"));
        startActivity(browserIntent);
    }
    //abs
    public void browser5(View view){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=Yz8iZCsG2_U"));
        startActivity(browserIntent);
    }
    //arms
    public void browser6(View view){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=zqDSELw3IA0"));
        startActivity(browserIntent);
    }

    public void diet1(View view){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=0BZxQYXLDbQ"));
        startActivity(browserIntent);
    }

    public void diet2(View view){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=P_wD2zydD_g"));
        startActivity(browserIntent);
    }

    public void diet3(View view){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=ishwT92D8Ec"));
        startActivity(browserIntent);
    }

    public void diet4(View view){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=wJe71nnjLmw"));
        startActivity(browserIntent);
    }

    public void diet5(View view){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=LCyECbA3pUw&t=464s"));
        startActivity(browserIntent);
    }

    public void diet6(View view){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=5s10IzwlXfE"));
        startActivity(browserIntent);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener(){
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menu){
                    Fragment selectedFragment = null;

                    switch (menu.getItemId()){
                        case R.id.nav_profile:
                            selectedFragment = new ProfileFragment();
                            break;
                        case R.id.nav_calorie:
                            selectedFragment = new CalorieFragment();
                            break;
                        case R.id.nav_tracker:
                            selectedFragment = new TrackerFragment();
                            break;
                        case R.id.nav_guide:
                            selectedFragment = new GuideFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();
                    return true;
                }
            };


}
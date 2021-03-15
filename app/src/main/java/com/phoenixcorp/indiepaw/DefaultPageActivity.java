package com.phoenixcorp.indiepaw;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DefaultPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_page);

        BottomNavigationView b = findViewById(R.id.bottomNavigationView);
        b.setBackground(null);
        b.getMenu().getItem(2).setEnabled(false);




    }
}
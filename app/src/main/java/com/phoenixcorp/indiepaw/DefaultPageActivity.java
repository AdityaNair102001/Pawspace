package com.phoenixcorp.indiepaw;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class DefaultPageActivity extends AppCompatActivity {

    private static final String TAG = DefaultPageActivity.class.getSimpleName();
    FragmentManager fragmentManager;
    ChipNavigationBar BottomNavBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_page);

        BottomNavBar = findViewById(R.id.bottom_nav);

        if(savedInstanceState==null)
        {
            BottomNavBar.setItemSelected(R.id.home, true);
            fragmentManager = getSupportFragmentManager();
            HomeFragment homeFragment = new HomeFragment();
            fragmentManager.beginTransaction().replace(R.id.Fragment_Container,homeFragment).commit();
        }

        BottomNavBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int id) {
                Fragment selectedFragment = null;

                switch(id)
                {
                    case R.id.home:
                        selectedFragment = new HomeFragment();
                        break;
                    case R.id.add:
                        selectedFragment = new AddNewPostFragment();
                        break;
                    case R.id.account:
                        selectedFragment = new AccountFragment();
                        break;
                }

                if(selectedFragment!=null)
                {
                        fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.Fragment_Container,selectedFragment).commit();
                }
                else
                {
                    Log.e(TAG, "Error");
                }
            }
        });


        View topAppBar=findViewById(R.id.actionBarDefaultPage);
        ImageView chatButton=topAppBar.findViewById(R.id.chat);

        Intent intent=new Intent(this,ChatActivity.class);


        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("chat", "onClick: Hi");
                startActivity(intent);

            }
        });



    }

    };




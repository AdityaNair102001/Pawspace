package com.phoenixcorp.indiepaw;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;

public class TopAppBarActivity extends AppCompatActivity {

    ImageView chatbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_app_bar);

        chatbutton = findViewById(R.id.chat);

        Gson gson = new Gson();
        String chatButton = gson.toJson(chatbutton);
        Intent intent=new Intent(this,DefaultPageActivity.class);
        intent.putExtra("myjson", chatButton);
        startActivity(intent);

    }
}
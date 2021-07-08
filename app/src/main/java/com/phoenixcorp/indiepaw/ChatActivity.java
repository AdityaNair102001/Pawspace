package com.phoenixcorp.indiepaw;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.mesibo.api.Mesibo;

public class ChatActivity extends AppCompatActivity {


    RecyclerView chatList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatList=findViewById(R.id.chatList);
        ChatListAdapter adapter=new ChatListAdapter();
        chatList.setHasFixedSize(true);
        chatList.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        chatList.setAdapter(adapter);






    }
}
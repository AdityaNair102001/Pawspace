package com.phoenixcorp.indiepaw;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.zip.Inflater;

public class MyPostListAdapter extends RecyclerView.Adapter<MyPostListAdapter.MyPostListViewHolder> {

    ArrayList<String> documentID;

    HashMap<String, String> imageUrls;

    MyPostFragment myPostFragment;



    public MyPostListAdapter(HashMap<String,String>imageUrls,ArrayList<String>documentID, MyPostFragment myPostsFragment) {
        this.documentID = documentID;
        this.imageUrls = imageUrls;
        this.myPostFragment = myPostsFragment;
    }

    @NonNull
    @Override
    public MyPostListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.mypost_post_layout,parent,false);
        return new MyPostListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyPostListViewHolder holder, int position) {

        Picasso.get().load(imageUrls.get(documentID.get(position))).placeholder(R.drawable.loader).into(holder.postImage);

        FirebaseFirestore db=FirebaseFirestore.getInstance();
        final String currentUser = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(myPostFragment.getContext(),MyPostsDescriptionActivity.class);
                myPostFragment.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return imageUrls.size();
    }

    public class MyPostListViewHolder extends RecyclerView.ViewHolder {
        ImageView postImage;

        public MyPostListViewHolder(@NonNull View itemView) {
            super(itemView);

            postImage = itemView.findViewById(R.id.gridImage);
        }
    }
}

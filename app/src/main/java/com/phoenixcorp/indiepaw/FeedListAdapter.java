package com.phoenixcorp.indiepaw;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FeedListAdapter extends RecyclerView.Adapter<FeedListAdapter.FeedListViewHolder> {

    private String[] data;
    public FeedListAdapter(String[] data)
    {
        this.data=data;
    }

    @NonNull
    @Override
    public FeedListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.feedlist_item_layout,parent,false);
        return new FeedListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedListViewHolder holder, int position) {
        String title=data[position];
        holder.texttitle.setText(title);
    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public class FeedListViewHolder extends RecyclerView.ViewHolder{

        ImageView imgicon;
        TextView texttitle;

        public FeedListViewHolder(@NonNull View itemView) {
            super(itemView);
            imgicon=itemView.findViewById(R.id.imgicon);
            texttitle=itemView.findViewById(R.id.texttitle);

        }
    }
}

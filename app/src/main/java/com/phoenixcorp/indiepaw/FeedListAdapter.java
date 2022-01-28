package com.phoenixcorp.indiepaw;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.smarteist.autoimageslider.SliderView;

public class FeedListAdapter extends RecyclerView.Adapter<FeedListAdapter.FeedListViewHolder> {

    private String[] data;
    private String[] location;
    private int[][] dogimages;
//    private SliderView postImageSlider;
    public FeedListAdapter(String[] data, String[] location, int[][] dogimages)
    {
        this.data=data;
        this.location=location;
        this.dogimages = dogimages;
//        this.postImageSlider = postImageSlider;
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
        String loc=location[position];
        holder.username.setText(title);

        int[] images = dogimages[position];
        adapterHandler(images,holder);
//        holder.secondaryText.setText(loc);
    }

    private void adapterHandler(int[] images, FeedListViewHolder holder) {
        HomeFragmentSliderAdapter adapter = new HomeFragmentSliderAdapter(images);
        holder.postImageSlider.setSliderAdapter(adapter);

    }

    @Override
    public int getItemCount() {
        return location.length;
    }

    public class FeedListViewHolder extends RecyclerView.ViewHolder{

        SliderView postImageSlider;
        ImageView circleImageView;
        TextView username;
        TextView secondaryText;
        // Button bookmark;

        public FeedListViewHolder(@NonNull View itemView) {
            super(itemView);
            postImageSlider=itemView.findViewById(R.id.homePageImageSlider);
            circleImageView=itemView.findViewById(R.id.ProfilePicture);
            username=itemView.findViewById(R.id.Username);
          //  secondaryText=itemView.findViewById(R.id.secondaryText);
            //bookmark = itemView.findViewById(R.id.bookmarkButton);

        }
    }

}

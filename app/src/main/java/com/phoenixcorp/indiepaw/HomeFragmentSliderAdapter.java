package com.phoenixcorp.indiepaw;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HomeFragmentSliderAdapter extends SliderViewAdapter<HomeFragmentSliderAdapter.SliderAdapterVH> {
    int[] images;

    public HomeFragmentSliderAdapter(int[] images) {
        this.images=images;

    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_items,parent, false);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, int position) {
        Picasso.get().load(images[position]).into(viewHolder.imageView);
    }

    @Override
    public int getCount() {
        return images.length;
    }

    static class SliderAdapterVH extends SliderViewAdapter.ViewHolder {
        ImageView imageView;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }

}

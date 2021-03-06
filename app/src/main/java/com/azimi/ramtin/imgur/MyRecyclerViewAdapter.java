package com.azimi.ramtin.imgur;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Ramtin on 6/23/2017.
 *
 **/

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.PhotoVH> {

    private List<Photo> photos;
    private LayoutInflater mInflater;
    private Context context;
    private ItemClickListener mClickListener;

    public MyRecyclerViewAdapter(Context context, List<Photo> photos){
        this.mInflater = LayoutInflater.from(context);
        this.photos = photos;
        this.context = context;
    }

    @Override
    public PhotoVH onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = mInflater.inflate(R.layout.item, parent, false);
        PhotoVH vh = new PhotoVH(view);
        vh.photo = (ImageView) vh.itemView.findViewById(R.id.photo);
        vh.title = (TextView) vh.itemView.findViewById(R.id.title);
        return vh;
    }

    @Override
    public void onBindViewHolder(PhotoVH holder, int position) {


        /*Glide for loading the photo from the imgur gallery.
          Glide will cache the image to the memory by default and on top of that looks
          whether an image is not yet in the cache before loading it from the website.
          Latter, will minimize the website requests, thus enhance the application's performance.
        */


        Glide.with(context)
                .load("https://i.imgur.com/" + photos.get(position).getId() + ".jpg")
                .into(holder.photo);

        holder.title.setText(photos.get(position).getTitle());
    }

    @Override
    public int getItemCount() {

        return photos.size();
    }

    public class PhotoVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView photo;
        TextView title;

        public PhotoVH(View itemView) {

            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onImageClick(view, getAdapterPosition());
        }
    }

    public Photo getItem(int id) {
        return photos.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onImageClick(View view, int position);
    }

};



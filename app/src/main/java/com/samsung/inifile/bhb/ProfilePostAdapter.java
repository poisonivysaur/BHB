package com.samsung.inifile.bhb;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ProfilePostAdapter extends RecyclerView.Adapter<ProfilePostAdapter.MyViewHolder> {

    private Context mContext;
    private List<Post> profilePostList;
    private boolean isFeed;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnail;
        public TextView caption, location, profileName;

        public MyViewHolder(View view) {
            super(view);
            if (isFeed){
                thumbnail = (ImageView) view.findViewById(R.id.feed_pic);
                caption = (TextView) view.findViewById(R.id.caption);
                location = (TextView) view.findViewById(R.id.location);
                profileName = (TextView) view.findViewById(R.id.profile_name);
            }
            else {
                thumbnail = (ImageView) view.findViewById(R.id.post_pic);
            }
        }
    }


    public ProfilePostAdapter(Context mContext, List<Post> profilePostList) {
        this.mContext = mContext;
        this.profilePostList = profilePostList;
        isFeed = false;
    }

    public ProfilePostAdapter(Context mContext, List<Post> profilePostList, boolean forFeed) {
        this.mContext = mContext;
        this.profilePostList = profilePostList;
        isFeed = true;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;

        if(isFeed){
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.feed_post_item_layout, parent, false);
        }
        else {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.post_item_layout, parent, false);
        }

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Post post = profilePostList.get(position);

        if(isFeed){
            holder.caption.setText(post.getCaption());
            holder.location.setText(post.getAddress());
            holder.profileName.setText(post.getName());

            /*if(post.getImaage() != null)
                holder.thumbnail.setImageBitmap(post.getImaage());*/
        }

        if(post.getImaage() != null)
            holder.thumbnail.setImageBitmap(post.getImaage());
    }

    @Override
    public int getItemCount() {
        return profilePostList.size();
    }
}

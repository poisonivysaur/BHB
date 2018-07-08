package com.samsung.inifile.bhb;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {

    private Context mContext;
    private List<Post> postList;
    private boolean isFeed;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnail;
        public TextView caption;

        public MyViewHolder(View view) {
            super(view);
            if (isFeed){
                thumbnail = (ImageView) view.findViewById(R.id.feed_pic);
                caption = (TextView) view.findViewById(R.id.caption);
            }
            else {
                thumbnail = (ImageView) view.findViewById(R.id.post_pic);
            }
        }
    }


    public PostAdapter(Context mContext, List<Post> postList) {
        this.mContext = mContext;
        this.postList = postList;
        isFeed = false;
    }

    public PostAdapter(Context mContext, List<Post> postList, boolean forFeed) {
        this.mContext = mContext;
        this.postList = postList;
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
        Post post = postList.get(position);

        if(isFeed){
            holder.caption.setText(post.getCaption());
            if(post.getImaage() != null)
                holder.thumbnail.setImageBitmap(post.getImaage());
        }
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }
}

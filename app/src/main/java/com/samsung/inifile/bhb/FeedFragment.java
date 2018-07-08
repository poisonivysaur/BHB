package com.samsung.inifile.bhb;


import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;


public class FeedFragment extends Fragment {
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    private RecyclerView recyclerView;
    private View mEmptyStateTextView;
    private PostAdapter postAdapter;

    private final boolean FOR_FEED = true;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle("Feed");
        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        recyclerView = view.findViewById(R.id.feed_recycleview);

        // set visibility of the empty view to be GONE initially
        mEmptyStateTextView = (View) view.findViewById(R.id.empty_view);
        mEmptyStateTextView.setVisibility(View.GONE);

        postAdapter = new PostAdapter(getContext(), DummyDB.postList, FOR_FEED);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        ProfileFragment.ItemOffsetDecoration itemDecoration = new ProfileFragment.ItemOffsetDecoration(getContext(), R.dimen.post);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setAdapter(postAdapter);

        //Toast.makeText(getContext(), "in feed fragment " + DummyDB.postList.size(), Toast.LENGTH_SHORT).show();

        //preparePosts();

        if(DummyDB.postList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            mEmptyStateTextView.setVisibility(View.VISIBLE);
        }
        postAdapter.notifyDataSetChanged();

        return view;
    }

    public void preparePosts() {
        for (int ctr = 0; ctr < 2; ctr ++) {
            DummyDB.postList.add(new Post(getString(R.string.sample_caption)));
        }

        postAdapter.notifyDataSetChanged();
    }

    public class ItemOffsetDecoration extends RecyclerView.ItemDecoration {

        private int mItemOffset;

        public ItemOffsetDecoration(int itemOffset) {
            mItemOffset = itemOffset;
        }

        public ItemOffsetDecoration(@NonNull Context context, @DimenRes int itemOffsetId) {
            this(context.getResources().getDimensionPixelSize(itemOffsetId));
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(mItemOffset, mItemOffset, mItemOffset, mItemOffset);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.profile_menu, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile_setting:
                return true;
            case R.id.profile_signout:
                mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();

                // [START config_signin]
                // Configure Google Sign In
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();
                // [END config_signin]

                mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);

                mGoogleSignInClient.signOut();

                Intent i = new Intent(getContext(), LoginActivity.class);
                startActivity(i);
                getActivity().finish();
                return true;
        }
        return false;
    }
}

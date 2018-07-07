package com.samsung.inifile.bhb;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class FeedFragment extends Fragment{
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private List<Post> postList;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle("Profile");
        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        recyclerView = view.findViewById(R.id.profile_recycleview);

        postList = new ArrayList<>();
        postAdapter = new PostAdapter(getContext(), postList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(mLayoutManager);
        ProfileFragment.ItemOffsetDecoration itemDecoration = new ProfileFragment.ItemOffsetDecoration(getContext(), R.dimen.post);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setAdapter(postAdapter);

        preparePosts();

        return view;
    }

    public void preparePosts() {
        for (int ctr = 0; ctr < 20; ctr ++) {
            postList.add(new Post());
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

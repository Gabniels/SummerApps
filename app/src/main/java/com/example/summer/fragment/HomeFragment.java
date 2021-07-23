package com.example.summer.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.summer.R;
import com.example.summer.adapter.PostAdapter;
import com.example.summer.model.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    Button btnAll, btnFollowing;

    RecyclerView rvFollowing;
    PostAdapter postAdapter;
    private List<Post> postList;

    RecyclerView rvAll;
    PostAdapter allpostAdapter;
    List<Post> allpostList;


    private List<String> followingList;

    ProgressBar progressBar;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_home, container, false);

        btnAll = view.findViewById(R.id.btnAll);
        btnFollowing = view.findViewById(R.id.btnFollowing);



        rvAll = view.findViewById(R.id.rvall);
        GridLayoutManager layoutManager1 = new GridLayoutManager(getContext(),2);
        layoutManager1.setReverseLayout(true);
        rvAll.setLayoutManager(layoutManager1);
        allpostList = new ArrayList<>();
        allpostAdapter = new PostAdapter(getContext(), allpostList);
        rvAll.setAdapter(allpostAdapter);

        rvFollowing = view.findViewById(R.id.rvFollowing);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        layoutManager.setReverseLayout(true);
        rvFollowing.setLayoutManager(layoutManager);
        postList = new ArrayList<>();
        postAdapter = new PostAdapter(getContext(), postList);
        rvFollowing.setAdapter(postAdapter);

        rvAll.setVisibility(View.VISIBLE);
        rvFollowing.setVisibility(View.GONE);


        btnAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rvAll.setVisibility(View.VISIBLE);
                rvFollowing.setVisibility(View.GONE);
            }
        });

        btnFollowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rvAll.setVisibility(View.GONE);
                rvFollowing.setVisibility(View.VISIBLE);

            }
        });

        checkFollowing();
        readAllPosts();


        return view;
    }

    private void checkFollowing() {
        followingList = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Follow")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("following");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                followingList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    followingList.add(dataSnapshot.getKey());
                }

                readPosts();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void readAllPosts() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                allpostList.clear();
                for (DataSnapshot dataSnapshot :  snapshot.getChildren()) {
                    Post post = dataSnapshot.getValue(Post.class);
                    allpostList.add(post);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void readPosts() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Post post = dataSnapshot.getValue(Post.class);
                    for (String id : followingList) {
                        if (post.getPublisher().equals(id)) {
                            postList.add(post);
                        }
                    }
                }
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
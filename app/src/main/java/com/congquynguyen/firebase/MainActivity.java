// MainActivity.java
package com.congquynguyen.firebase;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.congquynguyen.firebase.model.Video;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference videosRef;
    private FirebaseRecyclerAdapter<Video, VideoViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        videosRef = FirebaseDatabase.getInstance().getReference("videos");

        // Kiểm tra đăng nhập
        if (mAuth.getCurrentUser() == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        // Thiết lập ViewPager2
        ViewPager2 vpager = findViewById(R.id.vpager);
        vpager.setOrientation(ViewPager2.ORIENTATION_VERTICAL);

        // Thiết lập FirebaseRecyclerOptions
        FirebaseRecyclerOptions<Video> options = new FirebaseRecyclerOptions.Builder<Video>()
                .setQuery(videosRef, Video.class)
                .build();

        // Thiết lập adapter
        adapter = new FirebaseRecyclerAdapter<Video, VideoViewHolder>(options) {
            @Override
            protected void onBindViewHolder(VideoViewHolder holder, int position, Video model) {
                Log.d("FirebaseAdapter", "Binding video: " + model.getTitle());
                holder.bind(model);
            }

            @Override
            public VideoViewHolder onCreateViewHolder(android.view.ViewGroup parent, int viewType) {
                Log.d("FirebaseAdapter", "Creating view holder");
                android.view.View view = android.view.LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.single_video_item, parent, false);
                return new VideoViewHolder(view);
            }

            @Override
            public void onDataChanged() {
                super.onDataChanged();
                Log.d("FirebaseAdapter", "Data changed, item count: " + getItemCount());
            }
        };

        vpager.setAdapter(adapter);

        // Nút đăng xuất
        Button btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> {
            mAuth.signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
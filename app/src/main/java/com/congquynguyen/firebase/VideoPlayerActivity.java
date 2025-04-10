package com.congquynguyen.firebase;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;

public class VideoPlayerActivity extends AppCompatActivity {
    private SimpleExoPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        String videoUrl = getIntent().getStringExtra("VIDEO_URL");

        PlayerView playerView = findViewById(R.id.player_view);
        player = new SimpleExoPlayer.Builder(this).build();
        playerView.setPlayer(player);

        MediaItem mediaItem = MediaItem.fromUri(videoUrl);
        player.setMediaItem(mediaItem);
        player.prepare();
        player.setPlayWhenReady(true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (player != null) {
            player.setPlayWhenReady(false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.release();
            player = null;
        }
    }
}
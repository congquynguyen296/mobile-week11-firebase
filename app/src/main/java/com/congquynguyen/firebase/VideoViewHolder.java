// VideoViewHolder.java
package com.congquynguyen.firebase;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.congquynguyen.firebase.model.Video;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class VideoViewHolder extends RecyclerView.ViewHolder {

    private TextView txtVideoTitle, txtVideoUrl;
    private ImageView imgVideoThumbnail, imgFavorite, imgShare;

    public VideoViewHolder(View itemView) {
        super(itemView);
        txtVideoTitle = itemView.findViewById(R.id.txtVideoTitle);
        txtVideoUrl = itemView.findViewById(R.id.txtVideoUrl);
        imgVideoThumbnail = itemView.findViewById(R.id.imgVideoThumbnail);
        imgFavorite = itemView.findViewById(R.id.imgFavorite);
        imgShare = itemView.findViewById(R.id.imgShare);
    }

    public void bind(Video video) {
        txtVideoTitle.setText(video.getTitle());
        txtVideoUrl.setText(video.getUrl());

        // Load thumbnail bằng Glide
        Glide.with(itemView.getContext())
                .load(video.getThumbnailUrl())
                .placeholder(R.drawable.placeholder_image)
                .into(imgVideoThumbnail);

        // Cập nhật trạng thái yêu thích
        imgFavorite.setImageResource(video.isFavorite() ? R.drawable.ic_favorite_filled : R.drawable.ic_favorite_border);

        // Xử lý nhấn nút yêu thích
        imgFavorite.setOnClickListener(v -> {
            DatabaseReference videoRef = FirebaseDatabase.getInstance().getReference("videos").child(video.getId());
            video.setFavorite(!video.isFavorite());
            videoRef.setValue(video);
        });

        // Xử lý chia sẻ
        imgShare.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, video.getUrl());
            itemView.getContext().startActivity(Intent.createChooser(shareIntent, "Share Video"));
        });
    }
}
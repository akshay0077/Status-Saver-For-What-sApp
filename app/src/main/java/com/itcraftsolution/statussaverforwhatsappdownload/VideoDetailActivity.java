package com.itcraftsolution.statussaverforwhatsappdownload;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.MediaController;

import com.itcraftsolution.statussaverforwhatsappdownload.Models.Statues;
import com.itcraftsolution.statussaverforwhatsappdownload.Utils.Utils;
import com.itcraftsolution.statussaverforwhatsappdownload.databinding.ActivityVideoDetailBinding;

import java.io.File;

public class VideoDetailActivity extends AppCompatActivity {

    private ActivityVideoDetailBinding binding;
    private String VideoUri, filepath;
    private Animation fabOpen, fabClose, rotateForward, rotatebackward;
    private int DURATION = 300;
    private boolean isOpen = false;
    private boolean isSaved;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVideoDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        LoadData();

        fabOpen = AnimationUtils.loadAnimation(VideoDetailActivity.this, R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(VideoDetailActivity.this, R.anim.fab_close);
        rotateForward = AnimationUtils.loadAnimation(VideoDetailActivity.this, R.anim.rotate_forward);
        rotatebackward = AnimationUtils.loadAnimation(VideoDetailActivity.this, R.anim.rotate_backward);

        rotateForward.setDuration(DURATION);
        rotatebackward.setDuration(DURATION);
        fabOpen.setDuration(DURATION);
        fabClose.setDuration(DURATION);

        binding.fabMainDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateFab();
            }
        });

        binding.fabDetailsShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("image/mp4");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Check Out This Whatsapp Status From @StatusSaverForWhatsapp #Statues #StatusSaver");
                shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + filepath));
                startActivity(Intent.createChooser(shareIntent, "Share Video"));
            }
        });
        binding.fabDetailsDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(filepath);
                Statues status = new Statues(file.getName() , filepath , file , uri);
                Utils.copyFile(status , VideoDetailActivity.this);

            }
        });
    }
    private void LoadData()
    {
        SharedPreferences spf = VideoDetailActivity.this.getSharedPreferences("SendDetails", Context.MODE_PRIVATE);
        VideoUri = spf.getString("URI" , null);
        filepath = spf.getString("FILE_PATH" , null);
        isSaved = spf.getBoolean("isSaved" , false);

        if(isSaved)
        {
            binding.fabDetailsDownload.setVisibility(View.GONE);
        }
        uri = Uri.parse(VideoUri);

        binding.VideoView.setVideoURI(uri);
        MediaController mediaController = new MediaController(VideoDetailActivity.this);
        mediaController.setAnchorView(binding.VideoView);
        mediaController.setMediaPlayer(binding.VideoView);
        binding.VideoView.setMediaController(mediaController);
        binding.VideoView.start();
        binding.VideoView.requestFocus();

        binding.VideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
    }


    private void animateFab() {
        if (isOpen) {
            binding.fabMainDetails.startAnimation(rotateForward);
            binding.fabDetailsShare.startAnimation(fabClose);
            binding.fabDetailsDownload.startAnimation(fabClose);

            binding.fabDetailsShare.setClickable(false);
            binding.fabDetailsDownload.setClickable(false);

            isOpen = false;
        } else {
            binding.fabMainDetails.startAnimation(rotatebackward);
            binding.fabDetailsShare.startAnimation(fabOpen);
            binding.fabDetailsDownload.startAnimation(fabOpen);

            binding.fabDetailsShare.setClickable(true);
            binding.fabDetailsDownload.setClickable(true);

            isOpen = true;
        }
    }



}
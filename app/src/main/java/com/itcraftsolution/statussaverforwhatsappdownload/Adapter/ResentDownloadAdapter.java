package com.itcraftsolution.statussaverforwhatsappdownload.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.itcraftsolution.statussaverforwhatsappdownload.Fragments.ImageDetailsFragment;
import com.itcraftsolution.statussaverforwhatsappdownload.Models.Recents;
import com.itcraftsolution.statussaverforwhatsappdownload.R;
import com.itcraftsolution.statussaverforwhatsappdownload.Utils.Utils;
import com.itcraftsolution.statussaverforwhatsappdownload.databinding.SampleResentBinding;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class ResentDownloadAdapter extends RecyclerView.Adapter<ResentDownloadAdapter.viewHolder> {

    Context context;
    ArrayList<Recents> list;
    String saveFilePath = Utils.RootDirectorywhatsapp+"/";

    public ResentDownloadAdapter(Context context, ArrayList<Recents> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_resent , parent , false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        Recents model = list.get(position);

        Glide.with(context).load(model.getUri()).into(holder.binding.igResentSample);
        holder.binding.btnDownloadResent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.createFileFolder();
                final String path = model.getPath();
                final File file = new File(path);
                File destFile = new File(saveFilePath);

                try {
                    FileUtils.copyFileToDirectory(file , destFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Toast.makeText(context, "Saved To :"+saveFilePath, Toast.LENGTH_SHORT).show();
            }
        });

        holder.binding.igResentSample.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences spf = context.getSharedPreferences("SendDetails", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = spf.edit();
                edit.putString("URI", model.getUri().toString());
                edit.apply();

                ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frMainContainer , new ImageDetailsFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {
        SampleResentBinding binding;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            binding = SampleResentBinding.bind(itemView);
        }
    }
}

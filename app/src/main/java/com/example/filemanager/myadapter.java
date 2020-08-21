package com.example.filemanager;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class myadapter extends RecyclerView.Adapter<myadapter.myviewholder> {

    Context mcontext;
    List<String> mfilelist;
    File[] mfiles;
    RotateAnimation animation , animation1;

    myadapter(Context context, List<String> filelist, File[] files) {
        mcontext = context;
        mfilelist = new ArrayList<>(filelist);
        mfiles = files.clone();
        animation = new RotateAnimation(0.0f, 90.0f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);

        animation.setDuration(0);
        animation.setFillAfter(true);
        animation1 = new RotateAnimation(90.0f, 0.0f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);

        animation1.setDuration(0);
        animation1.setFillAfter(true);

    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.flist_item, parent, false);
        return new myviewholder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final myviewholder holder, final int position) {

        holder.tv.setText(mfilelist.get(position));
        final File[] files = mfiles[position].listFiles();

        holder.tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.plist.getVisibility() == View.GONE) {

                    List<String> sublistFile = new ArrayList<>();
                    sublistFile.clear();
                    if(files!=null) {
                        holder.imageView.setAnimation(animation);
                        for (File file : files) {
                            sublistFile.add(file.getName());
                        }
                        holder.plist.setVisibility(View.VISIBLE);
                        holder.adapter = new myadapter(mcontext, sublistFile, files);
                        holder.plist.setAdapter(holder.adapter);
                    }
                    else{
                        File file = mfiles[position];
                        // Get URI and MIME type of file
                        Uri uri = FileProvider.getUriForFile(
                                mcontext,
                                mcontext.getApplicationContext()
                                        .getPackageName() + ".provider", file);
                        String mime = get_mime_type(uri.toString());

                        // Open file with user selected app
                        try{
                            Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setDataAndType(uri, mime);
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        mcontext.startActivity(intent);
                        }
                        catch (Exception e){
                            Toast t = Toast.makeText(mcontext,e.toString(),Toast.LENGTH_LONG);
                            t.setGravity(Gravity.CENTER|Gravity.BOTTOM,0,0);
                            t.show();
                        }
                    }
                } else {
                    holder.imageView.setAnimation(animation1);
                    holder.plist.setVisibility(View.GONE);
                }

            }
        });
    }
    public String get_mime_type(String url) {
        String ext = MimeTypeMap.getFileExtensionFromUrl(url);
        String mime = null;
        if (ext != null) {
            mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext);
        }
        return mime;
    }

    @Override
    public int getItemCount() {
        return mfilelist.size();
    }

    public class myviewholder extends RecyclerView.ViewHolder {

        TextView tv;
        RecyclerView plist;
        myadapter adapter;
        ImageView imageView ;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.fileitemname);
            imageView=itemView.findViewById(R.id.arrow);
            tv.setMovementMethod(new ScrollingMovementMethod());
            plist = itemView.findViewById(R.id.flist);
            plist.setLayoutManager(new LinearLayoutManager(mcontext));
        }
    }
}

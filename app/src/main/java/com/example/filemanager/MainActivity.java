package com.example.filemanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.widget.LinearLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<String> listFile = new ArrayList<>();
    RecyclerView flist;
    myadapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!(checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE) && checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE))){
            ActivityCompat.requestPermissions(this , new String[]{Manifest.permission.READ_EXTERNAL_STORAGE , Manifest.permission.WRITE_EXTERNAL_STORAGE} , 1);
        }

        flist = findViewById(R.id.flist);
        flist.setLayoutManager(new LinearLayoutManager(this));

        File root = new File("/storage/emulated/0");
        ListDir(root);

    }

    void ListDir(File root){
        File[] files = root.listFiles();
        listFile.clear();
        for(File file : files){
            listFile.add(file.getName());
        }
        adapter = new myadapter(MainActivity.this , listFile , files);
        flist.setAdapter(adapter);
    }

    public Boolean checkPermission(String perm)
    {
        int check = ContextCompat.checkSelfPermission(this, perm);
        return (check == PackageManager.PERMISSION_GRANTED);
    }
}

package com.example.hatemragap.faculty_science_3;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

public class Algorizm_Old extends AppCompatActivity {
    ListView listView;

    String[] values;
    String PDFNAME;

    StorageReference storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_algorizm__old);
        listView = findViewById(R.id.list);
        values = new String[]{"lecture 1", "lecture 2", "lecture 3"};


        final String[] urls = new String[]{"https://firebasestorage.googleapis.com/v0/b/facultyscience3-3ab53.appspot.com/o/oldAlog%2F18795.pdf?alt=media&token=cab9f5b7-29fd-46ac-b409-e76827c7a4f5"
                , "https://firebasestorage.googleapis.com/v0/b/facultyscience3-3ab53.appspot.com/o/oldAlog%2F18795.pdf?alt=media&token=cab9f5b7-29fd-46ac-b409-e76827c7a4f5"
                , "https://firebasestorage.googleapis.com/v0/b/facultyscience3-3ab53.appspot.com/o/oldAlog%2Flearn%20html.pdf?alt=media&token=c94977c8-5b2e-42cb-b519-d91f4bd73f08"};

        storage = FirebaseStorage.getInstance().getReference().child("oldAlg");


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, values);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    PDFNAME = values[i];
                    Intent intent = new Intent(Algorizm_Old.this, DownLoadOldAlog.class);
                    intent.putExtra("postion", i);
                    intent.putExtra("pdfName", PDFNAME);
                    intent.putExtra("pdfUrl", urls[i]);
                    startActivity(intent);



            }
        });

    }



}

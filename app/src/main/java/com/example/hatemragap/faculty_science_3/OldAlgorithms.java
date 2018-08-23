package com.example.hatemragap.faculty_science_3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class OldAlgorithms extends AppCompatActivity {
    ListView listView;

    String[] values;
    String PDF_Name;

    StorageReference storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // set old algorithms activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_old_algorithms);

        // TODO auto-get the values & auto-import those url's from the firebase
        // lectures and it's download url's
        values = new String[]{"lecture 1", "lecture 2", "lecture 3"};
        final String[] urls = new String[]{
                "https://firebasestorage.googleapis.com/v0/b/facultyscience3-3ab53.appspot.com/o/oldAlog%2F18795.pdf?alt=media&token=cab9f5b7-29fd-46ac-b409-e76827c7a4f5"
                , "https://firebasestorage.googleapis.com/v0/b/facultyscience3-3ab53.appspot.com/o/oldAlog%2F18795.pdf?alt=media&token=cab9f5b7-29fd-46ac-b409-e76827c7a4f5"
                , "https://firebasestorage.googleapis.com/v0/b/facultyscience3-3ab53.appspot.com/o/oldAlog%2Flearn%20html.pdf?alt=media&token=c94977c8-5b2e-42cb-b519-d91f4bd73f08"
        };

        storage = FirebaseStorage.getInstance().getReference().child("oldAlg");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, values);


        // configure list view
        listView = findViewById(R.id.list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                PDF_Name = values[i];
                Intent intent = new Intent(OldAlgorithms.this, DownLoadOldAlog.class);
                intent.putExtra("postion", i);// TODO change postion to position
                intent.putExtra("pdfName", PDF_Name);
                intent.putExtra("pdfUrl", urls[i]);
                startActivity(intent);

            }
        });

    }


}

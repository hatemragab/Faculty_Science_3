package com.example.hatemragap.faculty_science_3;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadNewAlg extends AppCompatActivity {
    Button btnDownloadNewAlog;
    Dialog pDialog;
    Intent intent;
    ProgressBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_new_alg);
        btnDownloadNewAlog = findViewById(R.id.btnDownloadNewAlog);

        intent = getIntent();
        final String url = intent.getStringExtra("pdfUrl");
        btnDownloadNewAlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tasks tasks = new Tasks();
                tasks.execute(url);
            }
        });

    }

    class Tasks extends AsyncTask<String, Integer, String> {
        Button button;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new Dialog(DownloadNewAlg.this);
            View view = getLayoutInflater().inflate(R.layout.prograss_dailog, null, true);
            button = view.findViewById(R.id.cancel_DownLoad);

            bar = view.findViewById(R.id.progressBar2);
            bar.setMax(100);
            pDialog.setContentView(view);
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected String doInBackground(String... strings) {
            int count;
            try {
                String root = Environment.getExternalStorageDirectory().toString();
                File fDirectory = new File(root + "/lectures/newAlg");
                if (!fDirectory.exists()) {
                    fDirectory.mkdirs();
                }


                URL uri = new URL(strings[0]);

                HttpURLConnection connection = (HttpURLConnection) uri.openConnection();
                connection.connect();
                int lenghtOfFile = connection.getContentLength();

                final InputStream input = new BufferedInputStream(uri.openStream(), 16192);

                final OutputStream output = new FileOutputStream(fDirectory + "/" + intent.getStringExtra("pdfName") + ".pdf");

                byte data[] = new byte[2000];
                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;

                    // writing data to file
                    output.write(data, 0, count);
                    int t = (int) total * 100 / lenghtOfFile;
                    publishProgress(t);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(DownloadNewAlg.this, "done", Toast.LENGTH_SHORT).show();
            pDialog.dismiss();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            bar.setProgress(values[0]);
        }
    }


}

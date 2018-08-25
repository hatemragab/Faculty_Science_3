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
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadOldAlg extends AppCompatActivity {
    Dialog pDialog;
    Button button;
    Intent intent;
    ProgressBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_old_alog);
        button = findViewById(R.id.btnDownloadOldAlog);
        intent = getIntent();
        final String url = intent.getStringExtra("pdfUrl");


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url1 = new URL(url);
                    HttpURLConnection urlConnection = (HttpURLConnection) url1.openConnection();
                    final int lenghth = urlConnection.getContentLength();
                    urlConnection.disconnect();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String s = Algorithms_New.humanReadableByteCount(lenghth);

                            button.setText(s);
                        }
                    });


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tasks tasks = new Tasks();
                tasks.execute(intent.getStringExtra("pdfUrl"));
            }
        });

    }


    class Tasks extends AsyncTask<String, Integer, String> {
        Button button;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new Dialog(DownloadOldAlg.this);
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
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            try {
                String root = Environment.getExternalStorageDirectory().toString();
                File fDirectory = new File(root + "/lectures/oldAlog");
                if (!fDirectory.exists()) {
                    fDirectory.mkdirs();
                }


                URL uri = new URL(strings[0]);

                connection = (HttpURLConnection) uri.openConnection();
                connection.connect();
                int lenghtOfFile = connection.getContentLength();

                input = new BufferedInputStream(uri.openStream(), 16192);

                output = new FileOutputStream(fDirectory + "/" + intent.getStringExtra("pdfName") + ".pdf");

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


            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    output.flush();
                    connection.disconnect();
                    // closing streams
                    output.close();
                    input.close();
                } catch (Exception e) {

                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(DownloadOldAlg.this, "done", Toast.LENGTH_SHORT).show();
            pDialog.dismiss();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            bar.setProgress(values[0]);
        }
    }


}

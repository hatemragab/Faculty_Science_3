package com.example.hatemragap.faculty_science_3;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class ControlPanel extends AppCompatActivity {
    Dialog dialog;
    RecyclerView recyclerView;
    FirebaseAuth mAuth;
    FirebaseUser muser;
    String myId;
    DatabaseReference algReferenceRoot;
    StorageReference algStorge;
    Uri pdfUrl;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_panel);
        Toolbar toolbar = findViewById(R.id.toolControl);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Control page ");
        recyclerView = findViewById(R.id.recControlPanel);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dialog = new Dialog(this);
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        muser = mAuth.getCurrentUser();
        myId = muser.getUid();
        algReferenceRoot = FirebaseDatabase.getInstance().getReference().child("newAlg").child(myId);
        algStorge = FirebaseStorage.getInstance().getReference().child("newAlg");
        FirebaseRecyclerAdapter<LectureModel, ViewHolder> adapter = new FirebaseRecyclerAdapter<LectureModel, ViewHolder>(
                LectureModel.class,
                R.layout.control_panel_rec_item,
                ViewHolder.class,
                algReferenceRoot

        ) {
            @Override
            protected void populateViewHolder(ViewHolder viewHolder, LectureModel model, int position) {
                viewHolder.control_name.setText(model.getName() + "");
            }
        };

        recyclerView.setAdapter(adapter);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView control_name;

        public ViewHolder(View itemView) {
            super(itemView);
            control_name = itemView.findViewById(R.id.control_name);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.control_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.controlUploadBtn:
                View view = getLayoutInflater().inflate(R.layout.control_dialog_upload, null, true);

                TextView Algorithm = view.findViewById(R.id.algoUploadBtn);
                Algorithm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        UploadAlgorithmMethod();
                        dialog.dismiss();
                    }
                });


                dialog.setContentView(view);
                dialog.show();

                break;
        }
        return true;
    }

    private void UploadAlgorithmMethod() {

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        startActivityForResult(Intent.createChooser(intent, "choose pdf files only"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data.getData() != null) {
            pdfUrl = data.getData();
            //get Name of file to store it in firebase Data base reference and Storage
            String path = data.getData().getPath();
            final String strPath = path.substring(path.lastIndexOf("/") + 1, path.length());
            progressDialog.setMessage("please wait while we are uploading .....");
            progressDialog.setCancelable(false);
            progressDialog.show();

            algStorge.child(strPath).putFile(pdfUrl).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {

                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                       /* Map<String, String> hashAlg = new HashMap<>();
                        hashAlg.put("downloadLink", task.getResult().getDownloadUrl().toString());
                        hashAlg.put("name", strPath);
                        hashAlg.put("size", task.getResult().getMetadata().getSizeBytes() / 1024 * 1024 + "");*/
                        LectureModel model = new LectureModel();
                        model.setUrl(task.getResult().getDownloadUrl().toString());
                        model.setName(strPath);
                        model.setSize(task.getResult().getMetadata().getSizeBytes() / 1024 * 1024 + "");

                        algReferenceRoot.push().setValue(model);
                        progressDialog.dismiss();

                        Toast.makeText(ControlPanel.this, "upload done", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ControlPanel.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            });


        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }

    }

}

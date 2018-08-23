package com.example.hatemragap.faculty_science_3;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Algorithms_New extends AppCompatActivity {
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
        setContentView(R.layout.activity_algorizm__new);
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        muser = mAuth.getCurrentUser();
        myId = muser.getUid();
        algReferenceRoot = FirebaseDatabase.getInstance().getReference().child("newAlg").child(myId);
        algStorge = FirebaseStorage.getInstance().getReference().child("newAlg");

    }

    public void upload(View view) {

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
                        Map<String, String> hashAlg = new HashMap<>();
                        hashAlg.put("downloadLink", task.getResult().getDownloadUrl().toString());
                        hashAlg.put("name", strPath);
                        hashAlg.put("size", task.getResult().getMetadata().getSizeBytes() / 1024 * 1024 + "");
                        algReferenceRoot.push().setValue(hashAlg);
                        progressDialog.dismiss();
                        Toast.makeText(Algorithms_New.this, "upload done", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Algorithms_New.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            });


        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }

    }
}

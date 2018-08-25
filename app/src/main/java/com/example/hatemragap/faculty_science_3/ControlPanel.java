package com.example.hatemragap.faculty_science_3;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.Date;

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
    AlertDialog.Builder builder1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_panel);
        intializeView();
        algReferenceRoot = FirebaseDatabase.getInstance().getReference().child("newAlg");
        algStorge = FirebaseStorage.getInstance().getReference().child("newAlg");
        FirebaseRecyclerAdapter<LectureModel, ViewHolder> adapter = new FirebaseRecyclerAdapter<LectureModel, ViewHolder>(
                LectureModel.class,
                R.layout.control_panel_rec_item,
                ViewHolder.class,
                algReferenceRoot

        ) {

            @Override
            protected void populateViewHolder(final ViewHolder viewHolder, final LectureModel model, final int position) {
                viewHolder.lecture_name.setText(model.getLecturename() + "");
                viewHolder.delete_item.setVisibility(View.VISIBLE);
                viewHolder.date.setText(model.getDate());
                viewHolder.uploader_name.setText(model.getUploder_name());
                viewHolder.pdfsize.setText(Algorithms_New.humanReadableByteCount(Long.parseLong(model.getSize())));
                viewHolder.delete_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        builder1.setMessage("are you want to delete the file!");
                        builder1.setNegativeButton("cancel", null);
                        builder1.setCancelable(false);
                        builder1.setPositiveButton("delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialogInterface, int i) {

                                progressDialog.setMessage("please wait ");
                                progressDialog.setCancelable(false);
                                progressDialog.show();

                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();


                                Query applesQueryID = algReferenceRoot.child("newAlg").orderByChild("uploder_id").equalTo(myId);

                                final Query applesQueryName = ref.child("newAlg").orderByChild("lecturename").equalTo(model.getLecturename());
                                //Query applesQueryName = applesQueryID.child("newAlg").orderByChild("uploder_id").equalTo(myId).orderByChild("name").equalTo(model.getLecturename());

                                applesQueryID.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        Log.d("onDataChang","d");
                                        applesQueryName.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                Toast.makeText(ControlPanel.this, "دخلهاdataSnapshot", Toast.LENGTH_SHORT).show();
                                                for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                                                    Toast.makeText(ControlPanel.this, "يارب يخش دي بقا", Toast.LENGTH_SHORT).show();
                                                    appleSnapshot.getRef().removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            algStorge.child(model.getLecturename()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    Toast.makeText(ControlPanel.this, "finally deleted hhhhhh", Toast.LENGTH_SHORT).show();
                                                                    progressDialog.dismiss();
                                                                }
                                                            });

                                                        }
                                                    });

                                                }


                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                                Log.d("errordatabase",databaseError.getMessage());
                                                progressDialog.dismiss();
                                            }
                                        });
                                        progressDialog.dismiss();

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Log.e("error", "onCancelled", databaseError.toException());
                                    }
                                });


                            }


                        });
                        builder1.show();


                    }
                });

            }
        };
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void intializeView() {
        Toolbar toolbar = findViewById(R.id.toolControl);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Control page ");
        recyclerView = findViewById(R.id.recControlPanel);
        dialog = new Dialog(this);
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        builder1 = new AlertDialog.Builder(this);
        muser = mAuth.getCurrentUser();
        myId = muser.getUid();
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

    public String dumpPdfMetaDataName(Uri uri) {

        Cursor cursor = getContentResolver()
                .query(uri, null, null, null, null, null);
        String displayName = "Not specific";
        try {

            if (cursor != null && cursor.moveToFirst()) {

                displayName = cursor.getString(

                        cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                return displayName;

            }
        } finally {
            cursor.close();
        }
        return displayName;
    }


    public String dumpPdfMetaDataSize(Uri uri) {

        Cursor cursor = getContentResolver()
                .query(uri, null, null, null, null, null);
        int sizeIndex = 0;
        String size = "Unknown";
        try {

            if (cursor != null && cursor.moveToFirst()) {


                sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);


                if (!cursor.isNull(sizeIndex)) {

                    size = cursor.getString(sizeIndex);
                    return size;
                } else {
                    size = "Unknown";
                    return size;
                }

            }
        } finally {
            cursor.close();
        }
        return size;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data.getData() != null) {

            pdfUrl = data.getData();
            //get Name of file to store it in firebase Data base reference and Storage


            progressDialog.setMessage("please wait while we are uploading .....");
            progressDialog.setCancelable(false);
            progressDialog.show();

            algStorge.child(dumpPdfMetaDataName(pdfUrl)).putFile(pdfUrl).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {

                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        LectureModel model = new LectureModel();
                        model.setDownloadLink(task.getResult().getDownloadUrl().toString());
                        String Lecturename = dumpPdfMetaDataName(pdfUrl);
                        model.setLecturename(Lecturename);
                        model.setDate(getCurrentDate());
                        model.setUploder_name(muser.getDisplayName() + "");
                        model.setSize(task.getResult().getMetadata().getSizeBytes() / 1024 * 1024 + "");
                        model.setUploder_id(myId);
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

    private String getCurrentDate() {
        Date dateee = new Date(); // your date
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateee);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        String datePone = day + "/" + month + "/" + year;
        return datePone;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView uploader_name, pdfsize, date, lecture_name;
        ImageButton delete_item;

        public ViewHolder(View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            uploader_name = itemView.findViewById(R.id.uploader_name);
            pdfsize = itemView.findViewById(R.id.pdfsize);
            lecture_name = itemView.findViewById(R.id.lecture_name);
            delete_item = itemView.findViewById(R.id.delete_item);

        }

    }

}

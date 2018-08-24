package com.example.hatemragap.faculty_science_3;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    AlertDialog.Builder builder;
    AlertDialog.Builder builder1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_panel);
        Toolbar toolbar = findViewById(R.id.toolControl);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Control page ");
        recyclerView = findViewById(R.id.recControlPanel);

        dialog = new Dialog(this);
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        builder = new AlertDialog.Builder(this);
        builder1 = new AlertDialog.Builder(this);
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
            protected void populateViewHolder(ViewHolder viewHolder, final LectureModel model, int position) {
                viewHolder.control_name.setText(model.getName() + "");

                viewHolder.delete_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        builder1.setMessage("are you want to delete the file!");
                        builder1.setNegativeButton("cancel",null);
                        builder1.setCancelable(false);
                        builder1.setPositiveButton("delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialogInterface, int i) {

                                progressDialog.setMessage("please wait ");
                                progressDialog.setCancelable(false);
                                progressDialog.show();
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                                Query applesQuery = ref.child("newAlg").child(myId).orderByChild("name").equalTo(model.getName());

                                applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                                            appleSnapshot.getRef().removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful())
                                                    {
                                                        algStorge.child(model.getName()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                progressDialog.dismiss();
                                                                dialogInterface.dismiss();
                                                                Toast.makeText(ControlPanel.this, "deleted", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }
                                                }
                                            });

                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Log.e("data base error", "onCancelled", databaseError.toException());
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


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView control_name;
        ImageButton delete_item;
        public ViewHolder(View itemView) {
            super(itemView);
            control_name = itemView.findViewById(R.id.control_name);
            delete_item = itemView.findViewById(R.id.delete_item);

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
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data.getData() != null) {

            builder.setMessage("Enter the name of pdf");
            builder.setCancelable(false);
            final EditText editText = new EditText(this);

            builder.setView(editText);
            builder.setPositiveButton("done", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(final DialogInterface dialogInterface, int i) {
                    final String s = editText.getText().toString().trim();
                    if (!TextUtils.isEmpty(s)) {

                        pdfUrl = data.getData();
                        //get Name of file to store it in firebase Data base reference and Storage


                        progressDialog.setMessage("please wait while we are uploading .....");
                        progressDialog.setCancelable(false);
                        progressDialog.show();

                        algStorge.child(s).putFile(pdfUrl).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {

                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if (task.isSuccessful()) {
                                    LectureModel model = new LectureModel();
                                    model.setUrl(task.getResult().getDownloadUrl().toString());
                                    model.setName(s);
                                    model.setSize(task.getResult().getMetadata().getSizeBytes() / 1024 * 1024 + "");

                                    algReferenceRoot.push().setValue(model);
                                    progressDialog.dismiss();
                                    dialogInterface.dismiss();
                                    Toast.makeText(ControlPanel.this, "upload done", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ControlPanel.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                    dialogInterface.dismiss();
                                }
                            }
                        });

                    } else {
                        Toast.makeText(ControlPanel.this, "name is empty !", Toast.LENGTH_SHORT).show();
                        editText.setError("name is empty !");
                        editText.setFocusable(true);


                    }


                }
            });
            builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.show();


        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();

        }

    }

}

package com.example.hatemragap.faculty_science_3;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Algorithms_New extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseUser muser;
    String myId;
    DatabaseReference algReferenceRoot;
    RecyclerView recyclerView;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_algorizm__new);
        recyclerView = findViewById(R.id.recalgorithm);
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        muser = mAuth.getCurrentUser();
        myId = muser.getUid();
        algReferenceRoot = FirebaseDatabase.getInstance().getReference().child("newAlg").child(myId);


        FirebaseRecyclerAdapter<LectureModel, ViewHolder> adapter = new FirebaseRecyclerAdapter<LectureModel, ViewHolder>(
                LectureModel.class,
                R.layout.control_panel_rec_item,
                ViewHolder.class,
                algReferenceRoot
        ) {
            @Override
            protected void populateViewHolder(ViewHolder viewHolder, final LectureModel model, int position) {

                viewHolder.pdfsize.setText(model.getSize());
                viewHolder.control_name.setText(model.getName());
                final String url = model.getUrl();
                viewHolder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(Algorithms_New.this,DownloadNewAlg.class);
                        intent.putExtra("pdfUrl",url);
                        intent.putExtra("pdfName",model.getName());
                        startActivity(intent);


                    }
                });



            }
        };
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView control_name, pdfsize;

        View view;
        public ViewHolder(View itemView) {
            super(itemView);
            control_name = itemView.findViewById(R.id.control_name);
            pdfsize = itemView.findViewById(R.id.pdfsize);
            view = itemView;
        }
    }


}

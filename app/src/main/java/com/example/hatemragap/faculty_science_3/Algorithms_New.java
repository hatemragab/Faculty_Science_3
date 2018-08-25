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

    public static String humanReadableByteCount(long bytes) {
        int unit = 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = "kMGTPE".charAt(exp - 1) + "";
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_algorizm__new);
        recyclerView = findViewById(R.id.recalgorithm);
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        muser = mAuth.getCurrentUser();
        myId = muser.getUid();
        algReferenceRoot = FirebaseDatabase.getInstance().getReference().child("newAlg");


        FirebaseRecyclerAdapter<LectureModel, ViewHolder> adapter = new FirebaseRecyclerAdapter<LectureModel, ViewHolder>(
                LectureModel.class,
                R.layout.control_panel_rec_item,
                ViewHolder.class,
                algReferenceRoot
        ) {
            @Override
            protected void populateViewHolder(ViewHolder viewHolder, final LectureModel model, int position) {

                viewHolder.lecture_name.setText(model.getLecturename());
                viewHolder.pdfsize.setText(humanReadableByteCount(Long.parseLong(model.getSize())));
                viewHolder.date.setText(model.getDate());
                viewHolder.uploader_name.setText(model.getUploder_name());
                final String url = model.getDownloadLink();
                viewHolder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(Algorithms_New.this, DownloadNewAlg.class);
                        intent.putExtra("pdfUrl", url);
                        intent.putExtra("pdfName", model.getLecturename());
                        startActivity(intent);
                    }
                });
            }
        };
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView uploader_name, pdfsize, date, lecture_name;

        View view;

        public ViewHolder(View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            uploader_name = itemView.findViewById(R.id.uploader_name);
            pdfsize = itemView.findViewById(R.id.pdfsize);
            lecture_name = itemView.findViewById(R.id.lecture_name);
            view = itemView;
        }


    }
}

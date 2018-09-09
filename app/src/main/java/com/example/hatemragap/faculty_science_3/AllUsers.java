package com.example.hatemragap.faculty_science_3;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class AllUsers extends AppCompatActivity {
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // set all users layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);

        // configure and get all users from firebase
        final DatabaseReference mdatabase = FirebaseDatabase.getInstance().getReference("/users");
        FirebaseRecyclerAdapter<User, ViewHolder> recAtapter = new FirebaseRecyclerAdapter<User, ViewHolder>(
                User.class,
                R.layout.user_item,
                ViewHolder.class,
                mdatabase
        ) {
            @Override
            protected void populateViewHolder(ViewHolder viewHolder, User model, int position) {
                viewHolder.textName.setText(model.getName());

            }
        };

        // show the user's in recycle view
        recyclerView = findViewById(R.id.rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recAtapter);

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textName;
        ImageView imageView;
        View view;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            textName = itemView.findViewById(R.id.recName);
            imageView = itemView.findViewById(R.id.recImag);
        }
    }

}

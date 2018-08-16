package com.example.hatemragap.faculty_science_3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);
        recyclerView = findViewById(R.id.rec);

        final DatabaseReference mdatabase = FirebaseDatabase.getInstance().getReference("/users");

        FirebaseRecyclerAdapter<Users, ViewHolderr> recAtapter = new FirebaseRecyclerAdapter<Users, ViewHolderr>(

                Users.class,
                R.layout.user_item,
                ViewHolderr.class,
                mdatabase
        ) {
            @Override
            protected void populateViewHolder(ViewHolderr viewHolder, Users model, int position) {
                viewHolder.textName.setText(model.getName());
                String s = model.getImgUrl();
                Picasso.get().load(s).placeholder(R.drawable.person).into(viewHolder.imageView);


            }
        };
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recAtapter);

    }

    public static class ViewHolderr extends RecyclerView.ViewHolder {
        TextView textName;
        ImageView imageView;
        View view;

        public ViewHolderr(View itemView) {
            super(itemView);
            view = itemView;
            textName = itemView.findViewById(R.id.recName);
            imageView = itemView.findViewById(R.id.recImag);
        }
    }

}

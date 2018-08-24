package com.example.hatemragap.faculty_science_3;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class activity_navigation extends AppCompatActivity {

    private static boolean userExist;
    private static String username;
    private static String userMail;
    private static String userImageURL;

    private TextView usernameTextView;
    private TextView userMailTextView;
    private ImageView userImageView;

    public static void setUserInformation(FirebaseUser user) {
        userExist = true;
        username = user.getDisplayName();
        userMail = user.getEmail();
        userImageURL = user.getPhotoUrl().toString();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set layout
        setContentView(R.layout.activity_navigation);

        // set views
        usernameTextView = findViewById(R.id.username_textView);
        userMailTextView = findViewById(R.id.userMail_textView);
        userImageView = findViewById(R.id.userImage_imageView);

        if (userExist) {
            usernameTextView.setText(username);
            userMailTextView.setText(userMail);
            Picasso.get()
                    .load(userImageURL)
                    .resize(userImageView.getMaxWidth(), userImageView.getMaxHeight())
                    .into(userImageView);
        }
    }

}

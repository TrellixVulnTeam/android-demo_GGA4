package com.karengryg.flickrapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class PhotoDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);
        activateToolbar(true);

        Intent intent = getIntent();

        Photo photo = (Photo) intent.getSerializableExtra(PHOTO_TRANSFER);

        if (photo != null) {
            TextView photoTitle = findViewById(R.id.photo_title);

            photoTitle.setText(getResources().getString(R.string.photo_title_text, photo.getTitle()));
//            photoTitle.setText("Title: " + photo.getTitle());

            TextView phtoTags = findViewById(R.id.photo_tags);
            phtoTags.setText(getResources().getString(R.string.photo_tags_text, photo.getTags()));

            TextView photoAuthor = findViewById(R.id.photo_author);
            photoAuthor.setText(photo.getAuthor());

            ImageView photoImage = findViewById(R.id.photo_image);
            Picasso.with(this).load(photo.getLink())
                    .error(R.drawable.placeholder)
                    .placeholder(R.drawable.placeholder)
                    .into(photoImage);
        }
    }

}

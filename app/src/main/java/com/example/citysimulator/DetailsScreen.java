package com.example.citysimulator;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailsScreen extends AppCompatActivity
{
    // private class fields
    private Button backButton;
    private Button cameraButton;
    private Button applyButton;
    private TextView structureName;
    private TextView coordinates;
    private EditText editStructureName;
    private ImageView iv;
    private String name;
    private int row;
    private int column;
    private int defaultImage;
    private Bitmap image;

    private static final int REQUEST_THUMBNAIL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_screen);

        backButton = (Button) findViewById(R.id.backButtonDetails);
        cameraButton = (Button) findViewById(R.id.cameraButton);
        applyButton = (Button) findViewById(R.id.applyNameButton);
        structureName = (TextView) findViewById(R.id.structureName);
        coordinates = (TextView) findViewById(R.id.coordinates);
        editStructureName = (EditText) findViewById(R.id.editStructureName);
        iv = (ImageView) findViewById(R.id.thumbnail);

        // query the intent that was created from the MapFragment
        // this intent contains data that was passed from MapFragment to be used in DetailsScreen
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        row = intent.getIntExtra("row", 0);
        column = intent.getIntExtra("column", 0);
        defaultImage = intent.getIntExtra("drawableID", 0);
        image = (Bitmap) intent.getParcelableExtra("bitmap");

        // apply button onClick
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                // set the owner name of the structure on xRow and xCol coordinates
                // then set the text to be the ownerName
                GameData.get().get(row, column).setOwnerName(editStructureName.getText().toString());
                structureName.setText(GameData.get().get(row, column).getOwnerName());
                name = structureName.getText().toString();
            }
        });

        // back button onClick
        backButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // creating an intent to send results back to MapFragment
                Intent returnData = new Intent();
                returnData.putExtra("row", row);
                returnData.putExtra("column", column);
                returnData.putExtra("image", image);
                returnData.putExtra("name", name);

                setResult(RESULT_OK, returnData);
                GameData.get().addGameElement(row, column); // add to the gameElement database
                finish();
            }
        });

        // camera button onClick
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                // create an intent to invoke the camera app
                Intent thumbnailPhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(thumbnailPhotoIntent, REQUEST_THUMBNAIL);
            }
        });

        // check for if the image is not null
        // sets what the image is accordingly
        if (image != null)
        {
            iv.setImageBitmap(image);
        }
        else
        {
            iv.setImageResource(defaultImage);
        }
        updateStats(); // call updateStats method to print out the texts
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultIntent)
    {
        super.onActivityResult(requestCode, resultCode, resultIntent);

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_THUMBNAIL)
        {
            image = (Bitmap) resultIntent.getExtras().get("data");

            // if image is not null, sets the image bitmap to be image, which is the newly taken image
            if (image != null)
            {
                iv.setImageBitmap(image);
            }
            else // else sets image to the default
            {
                iv.setImageResource(defaultImage);
            }
        }
    }

    // method to display text
    public void updateStats()
    {
        structureName.setText(name); // empty by default, changes if the user sets a name to the structure
        coordinates.setText("Row: " + row + ", Column: " + column); // display row and column coordinates
    }
}
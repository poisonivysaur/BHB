package com.samsung.inifile.bhb;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class PhotoDetailsActivity extends AppCompatActivity{

    private EditText caption;
    private ImageView image;
    private TextView location;
    Bitmap imageBitmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_details);

        caption = (EditText) findViewById(R.id.caption_input);
        image = (ImageView) findViewById(R.id.image);
        location = (TextView) findViewById(R.id.location);

        //String strBitmap = getIntent().getExtras().getString(BundleKeys.IMAGE_BITMAP_KEY, "asdf");
        Bundle extras = getIntent().getExtras();
        imageBitmap = (Bitmap) extras.get(BundleKeys.IMAGE_BITMAP_KEY);
        image.setImageBitmap(imageBitmap);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.post_photo_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_save) {
            DummyDB.postList.add(new Post(caption.getText().toString(), imageBitmap));
            Toast.makeText(this, "Posted successfully!", Toast.LENGTH_SHORT).show();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // TODO check if caption is empty or not
        if(caption.getText().toString().length() > 0){
            showDeleteConfirmationDialog();
        }
        else
            super.onBackPressed();
    }

    /**
     * Prompt the user to confirm that they want to discard draft.
     */
    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure to discard your changes?");
        builder.setPositiveButton("Discard", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Discard" button, so delete the draft.

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the media.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}

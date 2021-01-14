package com.example.android.glass.glasstest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.glass.glasstest.menu.MenuActivity;
import com.example.glass.ui.GlassGestureDetector;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class CameraActivity extends BaseActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView imageView;
    private StorageReference storageReference, storageReference1;
    private DatabaseReference reference, ref1, ref2;
    private Date now = new Date();
    String currentPhotoPath;
    private Uri uri, photoURI;
    private int step;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        imageView = findViewById(R.id.activity_camera_imageview);

        step = getIntent().getIntExtra("step", 0);

        reference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        dispatchTakePictureIntent();

      //  Uri outputFileUri = Uri.fromFile(newfile);
       // Uri outputFileUri = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".fileprovider", newfile);
     /*   outputFileUri = FileProvider.getUriForFile(Objects.requireNonNull(getApplicationContext()),
                BuildConfig.APPLICATION_ID + ".provider", newfile);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

        startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
        imageView.setImageURI(outputFileUri);


      */
    }

    @Override
    public boolean onGesture(GlassGestureDetector.Gesture gesture) {
        switch (gesture) {
            case TAP:

                if (step == 5){
                    ref1 = reference.child("records");
                    storageReference1 = storageReference.child("records");
                }else if (step == 8){
                    ref1 = reference.child("batch");
                    storageReference1 = storageReference.child("batch");
                }
                String key = ref1.push().getKey();
                ref2 = ref1.child(key);
                DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

                Toast.makeText(this, "Working", Toast.LENGTH_SHORT).show();

                if (photoURI != null && now != null) {
                    UploadTask uploadTask = storageReference1.child(key).putFile(photoURI);
                    Toast.makeText(this, "Uploading", Toast.LENGTH_SHORT).show();

                    Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            return storageReference1.child(key).getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri uri1 = task.getResult();
                                ref2.setValue(new DataRecord(key, now.toString(), uri1.toString()));
                                finish();
                                Toast.makeText(CameraActivity.this, "image Upload Succesfull", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.e(task.getException() + "s", "this");
                                Toast.makeText(CameraActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else if (uri == null){
                Toast.makeText(this, "No image", Toast.LENGTH_SHORT).show();
            }
                return true;
            default:
                return super.onGesture(gesture);
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
         /*   Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);

          */
            imageView.setImageURI(photoURI);
            Toast.makeText(this, "Image Saved to gallery", Toast.LENGTH_SHORT).show();
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.e("Error Creating File" , "");
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }


}
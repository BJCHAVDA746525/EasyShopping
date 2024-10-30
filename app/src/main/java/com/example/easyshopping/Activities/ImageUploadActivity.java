package com.example.easyshopping.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.easyshopping.Data.Room_Helper;
import com.example.easyshopping.R;
import com.example.easyshopping.Utils.Constants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class ImageUploadActivity extends AppCompatActivity implements View.OnClickListener {
    //Views
    AppCompatButton UploadPhoto, SkipPhoto;

    //Variables
    private static final int CAM_REQ_CODE = 100;
    private static final int GALL_REQ_CODE = 1000;

    String userFireKey;

    private FirebaseAuth mAuth;
    Room_Helper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_upload);
        InitView();

        if (getIntent().hasExtra("userFireKey")) {
            // Use the string key "userFireKey" to retrieve the value from the intent
            userFireKey = getIntent().getStringExtra("userFireKey");

        } else {

            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();

        }
    }

    private void InitView() {
        //Find Id's
        UploadPhoto = findViewById(R.id.upload_profile_photo);
        SkipPhoto = findViewById(R.id.skip_profile_photo);

        UploadPhoto.setOnClickListener(this);
        SkipPhoto.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        helper = Room_Helper.GetDB(ImageUploadActivity.this);
    }

    @Override
    public void onClick(View v) {
        int ID = v.getId();
        if (ID==R.id.upload_profile_photo){

            InsertProfileImage();

        } else if (ID==R.id.skip_profile_photo) {

            Bitmap nameLetterImage = createInitialsBitmap(Constants.USER_NAME);
            uploadImageToFirebaseStorage(nameLetterImage, Constants.USER_ID);
        }

    }

    private void InsertProfileImage() {
        final BottomSheetDialog dialog = new BottomSheetDialog(ImageUploadActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.updatedp_layout);

        LinearLayout Camera = dialog.findViewById(R.id.CameraDP_layout);
        LinearLayout Gallery = dialog.findViewById(R.id.GallaryDP_layout);



        Camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent icam = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(icam, CAM_REQ_CODE);

            }
        });


        Gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent igallery = new Intent(Intent.ACTION_PICK);
                igallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(igallery, GALL_REQ_CODE);

            }
        });


        dialog.show();
    }

    public Bitmap createInitialsBitmap(String name) {
        // Extract the first letter
        String firstLetter = name.substring(0, 1).toUpperCase();

        // Define the bitmap size and create the canvas
        int width = 200;
        int height = 200;
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        // Set background color
        canvas.drawColor(Color.LTGRAY);

        // Create a paint object for text
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(100);
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.CENTER);

        // Draw the first letter in the center of the canvas
        int xPos = canvas.getWidth() / 2;
        int yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));

        canvas.drawText(firstLetter, xPos, yPos, paint);

        return bitmap;
    }

    public Bitmap uriToBitmap(Uri uri) {
        try {
            // Use the content resolver to get an input stream from the URI
            InputStream inputStream = getContentResolver().openInputStream(uri);

            // Decode the input stream into a Bitmap
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            // Close the stream
            assert inputStream != null;
            inputStream.close();

            return bitmap;
        } catch (IOException e) {
            Log.e("Uri to Bitmap Converting Error", "ERROR : " + e);
            return null;
        }
    }

    // Function to upload bitmap image to Firebase Storage
    public void uploadImageToFirebaseStorage(Bitmap bitmap, String userId) {

        // Code for showing progressDialog while uploading
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();

        // Convert Bitmap to ByteArray
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] data = byteArrayOutputStream.toByteArray();

        // Get Firebase Storage reference
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        // Create a unique file path for the image in Firebase Storage
        StorageReference imageRef = storageRef.child("profile_images/" + userId + ".jpg");

        // Upload the image
        UploadTask uploadTask = imageRef.putBytes(data);

        // Listen for success or failure of the upload
        uploadTask.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Get the image download URL
                imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    // Save the URL in Firebase Realtime Database
                    progressDialog.dismiss();
                    Toast.makeText(this, "Image Upload Successfully", Toast.LENGTH_SHORT).show();
                    saveImageUrlToDatabase(uri.toString(), userId);
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle any errors
                progressDialog.dismiss();
                Toast.makeText(ImageUploadActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("FirebaseStorage", "Image upload failed: " + e.getMessage());
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                progressDialog.setMessage("Uploaded " + (int)progress + "%");
            }
        });
    }

    // Function to save image URL to Firebase Realtime Database
    public void saveImageUrlToDatabase(String imageUrl, String userId) {
        // Get Firebase Realtime Database reference
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();

        // Save the image URL under the user's profile data
        databaseRef.child("users").child(userId).child(userFireKey).child("profileImageUrl").setValue(imageUrl)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("FirebaseDatabase", "Image URL saved successfully");

                        databaseRef.child("users").child(userId).child("profileImageUrl").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                String profileUrl = snapshot.child("profileImageUrl").getValue(String.class);

                                helper.room_dao().updateUserProfilePhoto(profileUrl,userId);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(ImageUploadActivity.this, "Profile photo not update", Toast.LENGTH_SHORT).show();
                            }
                        });

                        Intent iLogin = new Intent(ImageUploadActivity.this, LoginActivity.class);
                        startActivity(iLogin);
                        finish();

                    } else {

                        Intent iShopping = new Intent(ImageUploadActivity.this,ShoppingActivity.class);
                        startActivity(iShopping);
                        finish();

                        Log.e("FirebaseDatabase", "Failed to save image URL: " + task.getException().getMessage());

                    }
                });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            Uri fireDP;

            if (requestCode == CAM_REQ_CODE) {

                Bitmap imgbit = (Bitmap) (data.getExtras().get("data"));

                uploadImageToFirebaseStorage(imgbit, Objects.requireNonNull(mAuth.getCurrentUser()).getUid());

            } else if (requestCode == GALL_REQ_CODE) {

                fireDP = data.getData();
                Bitmap userimg = uriToBitmap(fireDP);

                uploadImageToFirebaseStorage(userimg, Objects.requireNonNull(mAuth.getCurrentUser()).getUid());

            }
        }
    }


}
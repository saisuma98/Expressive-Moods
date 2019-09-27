package com.expressivemoods;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteReadOnlyDatabaseException;
import android.icu.lang.UProperty;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UploadActivity extends AppCompatActivity {
        TextView t1;
        Uri songuri;
        FirebaseStorage storage;
        FirebaseDatabase database;
        String category,name,userId;




        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_upload);
            storage = FirebaseStorage.getInstance();
            database = FirebaseDatabase.getInstance();
            Toast.makeText(getApplicationContext(),"initial Database Ref "+database,Toast.LENGTH_SHORT).show();
            t1 = findViewById(R.id.loc);


            Bundle bundle = getIntent().getExtras();

            category = bundle.getString("CAT");
            userId = bundle.getString("USR");

            Toast.makeText(getApplicationContext(),"Category "+category,Toast.LENGTH_SHORT).show();



        }

        public void select(View view) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                selectSong();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 9);

            }
        }

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            if (requestCode == 9 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                selectSong();
            } else {
                Toast.makeText(this, "Hey Fucker!Give Permission", Toast.LENGTH_SHORT).show();
            }

        }

        private void selectSong() {
            Intent i = new Intent();
            i.setType("application/mp3");
            i.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(i, 86);
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            if (requestCode == 86 && resultCode == RESULT_OK && data != null) {
                songuri = data.getData();

                name = data.getData().getLastPathSegment();

                t1.setText("Selected File "+name);
            } else {
                Toast.makeText(this, "Fucker!! Select a Song", Toast.LENGTH_SHORT).show();
            }

        }

        public void upload(View view) {
            if (songuri != null) {
                uploadsong(songuri);

            } else {
                Toast.makeText(this, "Select a song", Toast.LENGTH_SHORT).show();
            }
        }

    /*
    photoStorageReference.putFile(selectedImageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
    @Override
    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
        if (!task.isSuccessful()) {
            throw task.getException();
        }
        return photoStorageReference.getDownloadUrl();
    }
}).addOnCompleteListener(new OnCompleteListener<Uri>() {
    @Override
    public void onComplete(@NonNull Task<Uri> task) {
        if (task.isSuccessful()) {
            Uri downloadUri = task.getResult();
            FriendlyMessage friendlyMessage = new FriendlyMessage(null, mUsername, downloadUri.toString());
            mMessagesDatabaseReference.push().setValue(friendlyMessage);
        } else {
            Toast.makeText(MainActivity.this, "upload failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
});
     */


        private void uploadsong(final Uri songuri) {

            final String fileName = System.currentTimeMillis() + "";
            final StorageReference storageReference = storage.getReference();



            storageReference.child(userId).child(category).child(fileName).putFile(songuri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return storageReference.child(userId).child(category).child(fileName).getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        String downloadUri = task.getResult().toString();
                        DatabaseReference myRef = database.getReference();
                        Toast.makeText(UploadActivity.this,"onComplete Database Ref "+myRef,Toast.LENGTH_SHORT).show();

                        SongModel songModel = new SongModel(name , downloadUri);



                        myRef.child(userId).child(category).push().setValue(songModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(UploadActivity.this,"uploaded the url",Toast.LENGTH_SHORT).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(UploadActivity.this,"failed to upload url",Toast.LENGTH_SHORT).show();

                            }
                        });

                    } else {
                        Toast.makeText(UploadActivity.this, "upload failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UploadActivity.this, "my error " + e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });




        }
    }
package com.example.farmcontrolapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


public class FileUsageActivity extends AppCompatActivity {

    private Button buttonUpload , buttonChooseFile;
    private Button buttonDownload;
    private TextView filenametxt , fileDatatxt;
    private Uri filePath;
    private String path;
    FirebaseStorage storage;
    StorageReference storageReference;
    String fileName;

    File root = Environment.getExternalStorageDirectory();// this directory == storage/0/emulated --> after it comes
    // all folders such as Downloads and Documents etc...
    private static final int FILE_SELECT_CODE = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_usage);

//        requestAppPermissions();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        buttonUpload =  findViewById(R.id.uploadbtn);
        buttonDownload =  findViewById(R.id.downloadbtn);
        buttonChooseFile = findViewById(R.id.choosebtn);
        filenametxt = findViewById(R.id.filenametxt);
        fileDatatxt = findViewById(R.id.fileDatatxt);

        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filenametxt.setText("selected file name ...");
                fileDatatxt.setText("selected file data ...");
                showFileChooser();


            }
        });

        buttonDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                downloadFile();
            }
        });

//        buttonChooseFile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showFileChooser();
//
//
//
//            }
//        });

    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    filePath = data.getData();
                    path = filePath.getPath();
                    System.out.println("path ========: " +path );
                    fileName = getFileName(filePath);
                    uploadFile(fileName);
                    String fileData = readText(path,FileUsageActivity.this);
                    fileDatatxt.setText(fileData);
                    Toast.makeText(this, fileName,
                            Toast.LENGTH_SHORT).show();
                    filenametxt.setText(fileName);

                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }


    //this method will upload the file
    private void uploadFile(String filename) {
        //if there is a file to upload
        if (filePath != null) {
            //displaying a progress dialog while upload is going on
            final ProgressDialog progressDialog = new ProgressDialog(FileUsageActivity.this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference riversRef = storageReference.child("files/"+filename);
            riversRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //if the upload is successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();

                            //and displaying a success toast
                            Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //if the upload is not successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();

                            //and displaying error message
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //calculating progress percentage
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                            //displaying percentage in progress dialog
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        }
        //if there is not any file
        else {
            Toast.makeText(FileUsageActivity.this, "no file found ... ", Toast.LENGTH_SHORT).show();
        }
    }


    private void downloadFile() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference fileRef = storage.getReference();

        final ProgressDialog progressDialog = new ProgressDialog(FileUsageActivity.this);
        progressDialog.setTitle("Downloading latest file .... ");
        progressDialog.show();


        StorageReference  islandRef = fileRef.child("files").child("test.txt");

        islandRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String url = uri.toString();


                File directory = new File(Environment.getExternalStorageDirectory()+File.separator+"farm");
                if(!directory.exists()){
                    directory.mkdirs();
                }
                String dir = directory.getAbsolutePath();
                download(FileUsageActivity.this,"test",".txt",dir,url);
                progressDialog.dismiss();
                Toast.makeText(FileUsageActivity.this,"File Downloaded Successfully.." +
                        "at "+ dir + " folder",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(FileUsageActivity.this,"File Download Failed..",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void download(Context context,String fileName , String fileExtention , String destinationDirectory,String url){
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context,destinationDirectory,fileName+fileExtention);
        downloadManager.enqueue(request);
//      destinationDirectory == file:///storage/emulated/0/Android/data/com.example.farmcontrolapplication/files/storage/emulated/0/farm/test.txt

    }


    private String readText(String path, Context context ){

        File file = new File(root , "/Android/data/com.example.farmcontrolapplication/files/storage/emulated/0/farm/test.txt");
        StringBuilder builder = new StringBuilder();

        try{
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line ;
            while ((line = reader.readLine()) != null){
                builder.append(line);
                builder.append("\n");
            }
            reader.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        System.out.println("builder.toString() ===: "+builder.toString());
        return builder.toString();
    }
//
//    private void requestAppPermissions() {
//        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
//            return;
//        }
//
//        if (hasReadPermissions() && hasWritePermissions()) {
//            return;
//        }
//
//        ActivityCompat.requestPermissions(this,
//                new String[] {
//                        Manifest.permission.READ_EXTERNAL_STORAGE,
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE
//                }, 3); // your request code
//    }
//
//    private boolean hasReadPermissions() {
//        return (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
//                == PackageManager.PERMISSION_GRANTED);
//    }
//
//    private boolean hasWritePermissions() {
//        return (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                == PackageManager.PERMISSION_GRANTED);
//    }
}

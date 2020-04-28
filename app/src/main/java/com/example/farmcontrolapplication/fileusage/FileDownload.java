package com.example.farmcontrolapplication.fileusage;

import android.Manifest;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.farmcontrolapplication.FarmControlActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FileDownload {

    private static File root = Environment.getExternalStorageDirectory();// this directory == storage/0/emulated --> after it comes
    private static String sensorsreadings = null;

    public static boolean downloadFile(final Context context) {

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference fileRef = storage.getReference();
        StorageReference  islandRef = fileRef.child("files").child("test.txt");

//        final ProgressDialog progressDialog = new ProgressDialog(context);
//        progressDialog.setTitle("Downloading latest file .... ");
//        progressDialog.show();

        islandRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // to read from
                File file = new File(Environment.getExternalStorageDirectory()+"/farm","test.txt");

                // to write into.
                File folder = new File(Environment.getExternalStorageDirectory()+"/farm");
                // directory folder where file is doenloaded --> mobile internalStorage/farm.

                boolean removed = false ;
                if (file.exists()){
                   removed = removeDirectoryContent(folder);
                }
                else {
                    removed = true;
                }

                boolean downloaded = false;
                if(removed){
                    downloaded = download(uri,context, file);
                }



//                if(downloaded){
//                    progressDialog.dismiss();
//                    readText(file);
//                }




            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
//                progressDialog.dismiss();
                Toast.makeText(context,"File Download Failed..",Toast.LENGTH_SHORT).show();
            }
        });

        return true;
    }

    private static boolean download(Uri uri,Context context,File file) {

        System.out.println("===download===");
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(uri)
                .setTitle("File Download")
                .setDescription("Downloading ...")
                .setDestinationUri(Uri.fromFile(file))
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        downloadManager.enqueue(request);
        return true;
    }

    public static String readText(File file){
        System.out.println("===readText===");
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

    public static boolean removeDirectoryContent(File dir){
        if (dir.isDirectory())
        {
            if(dir.listFiles() != null){
                String[] children = dir.list();
                System.out.println("===removeDirectoryContent===");
                for (int i = 0; i < children.length; i++)
                {
                    new File(dir, children[i]).delete();
                }
            }

        }
        return true;
    }

}

package com.example.farmcontrolapplication;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.farmcontrolapplication.fileusage.FileDownload;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class MainActivity extends AppCompatActivity {

    private int MY_PERMISSIONS_REQUEST_SMS_RECEIVE = 10;
    Boolean smsFolderFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestAppPermissions();

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.RECEIVE_SMS},
                MY_PERMISSIONS_REQUEST_SMS_RECEIVE);

//        Button fileActivityBtn = findViewById(R.id.fileActivityBtn);

//        fileActivityBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this,FileUsageActivity.class));
//            }
//        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_SMS_RECEIVE) {
            // YES!!
            Log.i("TAG", "MY_PERMISSIONS_REQUEST_SMS_RECEIVE --> YES");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void controlFarm(View view) {

//        final String SR = getIntent().getStringExtra("SR");
//        Long time = getIntent().getLongExtra("Time",0);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Long time = prefs.getLong("Time",0);


//        System.out.println(SR + " : " + time);
        boolean timeCalculated = (System.currentTimeMillis() - time) >= 36000;
        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setTitle("Getting sensors readings .... ");
        progressDialog.show();

        if (timeCalculated) {
            // download file from firebase only if there is internet connection.
            boolean internetConnectionFlag = checkConnectivity(MainActivity.this);

            if (internetConnectionFlag){

                FileDownload.downloadFile(MainActivity.this);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        Intent newIntent1 = new Intent(MainActivity.this, FarmControlActivity.class);
                        startActivity(newIntent1);
                        progressDialog.dismiss();

                    }
                }, 4000);
            } else {
                Toast.makeText(MainActivity.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }



        } else {
            Handler handler1 = new Handler();
            handler1.postDelayed(new Runnable() {
                public void run() {

                    Intent newIntent1 = new Intent(MainActivity.this, FarmControlActivity.class);
                    newIntent1.putExtra("sms_read" , "smsFile");
                    startActivity(newIntent1);
                    progressDialog.dismiss();
                }
            }, 4000);

        }

        // 4000 milliseconds delay


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean checkConnectivity(Context context) {
        String status = null;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Network activeNetwork = cm.getActiveNetwork();
        if (activeNetwork != null) {

//            if (activeNetwork.getType() == ConnectivityManager.) {
//                status = "Wifi enabled";
//                return status;
//            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
//                status = "Mobile data enabled";
            return true;
//            }
        } else {
            status = "No internet connection";
            return false;
        }
    }

    private void requestAppPermissions() {
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }

        if (hasReadPermissions() && hasWritePermissions()) {
            return;
        }

        ActivityCompat.requestPermissions(this,
                new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, 3); // your request code
    }

    private boolean hasReadPermissions() {
        return (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED);
    }

    private boolean hasWritePermissions() {
        return (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED);
    }



}

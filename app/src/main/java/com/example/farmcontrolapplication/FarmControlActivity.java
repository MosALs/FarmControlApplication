package com.example.farmcontrolapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.farmcontrolapplication.fileusage.FileDownload;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FarmControlActivity extends AppCompatActivity implements View.OnClickListener {

    Button sectio1btn, sectio2btn, sectio3btn, sectio4btn, sectio5btn;
    boolean downloaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm_control);

        sectio1btn = findViewById(R.id.button1);
        sectio2btn = findViewById(R.id.button2);
        sectio3btn = findViewById(R.id.button3);
        sectio4btn = findViewById(R.id.button4);
        sectio5btn = findViewById(R.id.button5);

        sectio1btn.setOnClickListener(this);
        sectio2btn.setOnClickListener(this);
        sectio3btn.setOnClickListener(this);
        sectio4btn.setOnClickListener(this);
        sectio5btn.setOnClickListener(this);

        System.out.println("FarmControlActivity onCreate()");

    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("onstart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("onresume");
    }

    @Override
    public void onClick(View v) {
//        final String[] sensorsReadings = {""};

//        final ProgressDialog progressDialog = new ProgressDialog(FarmControlActivity.this);
//        new CountDownTimer(4000, 1000) {
//            public void onTick(long millisUntilFinished) {
//                progressDialog.setTitle("Loading sensors data .... ");
//                progressDialog.setProgress(100);
//                progressDialog.show();

        File file = new File(Environment.getExternalStorageDirectory() + "/farm", "test.txt");
        String sensorsReadings = FileDownload.readText(file);
        System.out.println("sensorsReadings: " + sensorsReadings);
//            }
//            public void onFinish() {
//                progressDialog.dismiss();
//            }
//        }.start();

        List<String> sectionOneSensorsReadings = getSectionOneSensorsReadings(sensorsReadings);
        List<String> sectionTwoSensorsReadings = getSectionTwoSensorsReadings(sensorsReadings);
        List<String> sectionThreeSensorsReadings = getSectionThreeSensorsReadings(sensorsReadings);
        List<String> sectionFourSensorsReadings = getSectionFourSensorsReadings(sensorsReadings);
        List<String> sectionFiveSensorsReadings = getSectionFiveSensorsReadings(sensorsReadings);


        Intent intent = new Intent(FarmControlActivity.this, FarmSectionStatus.class);
        int id = v.getId();
        switch (id) {
            case R.id.button1:
                intent.putExtra("FARM_SECTION", "section 1");
                intent.putStringArrayListExtra("sensorsReadings", (ArrayList<String>) sectionOneSensorsReadings);
                break;
            case R.id.button2:
                intent.putExtra("FARM_SECTION", "section 2");
                intent.putStringArrayListExtra("sensorsReadings", (ArrayList<String>) sectionTwoSensorsReadings);
                break;
            case R.id.button3:
                intent.putExtra("FARM_SECTION", "section 3");
                intent.putStringArrayListExtra("sensorsReadings", (ArrayList<String>) sectionThreeSensorsReadings);
                break;
            case R.id.button4:
                intent.putExtra("FARM_SECTION", "section 4");
                intent.putStringArrayListExtra("sensorsReadings", (ArrayList<String>) sectionFourSensorsReadings);
                break;
            case R.id.button5:
                intent.putExtra("FARM_SECTION", "section 5");
                intent.putStringArrayListExtra("sensorsReadings", (ArrayList<String>) sectionFiveSensorsReadings);
                break;
        }

        startActivity(intent);

    }



    private List<String> getSectionOneSensorsReadings(String sensorsReadings) {
        List<String> sectionOneSensorsReadings = new ArrayList<>();
        sectionOneSensorsReadings.add(sensorsReadings.substring(0,2));
        sectionOneSensorsReadings.add(sensorsReadings.substring(3,5));
        sectionOneSensorsReadings.add(sensorsReadings.substring(6,8));
        sectionOneSensorsReadings.add(sensorsReadings.substring(9,11));
        sectionOneSensorsReadings.add(sensorsReadings.substring(12,14));

        return sectionOneSensorsReadings ;
    }
    private List<String> getSectionTwoSensorsReadings(String sensorsReadings) {
        List<String> sectionTwoSensorsReadings = new ArrayList<>();
        sectionTwoSensorsReadings.add(sensorsReadings.substring(15,17));
        sectionTwoSensorsReadings.add(sensorsReadings.substring(18,20));
        sectionTwoSensorsReadings.add(sensorsReadings.substring(21,23));
        sectionTwoSensorsReadings.add(sensorsReadings.substring(24,26));
        sectionTwoSensorsReadings.add(sensorsReadings.substring(27,29));

        return sectionTwoSensorsReadings ;
    }
    private List<String> getSectionThreeSensorsReadings(String sensorsReadings) {
        List<String> sectionThreeSensorsReadings = new ArrayList<>();
        sectionThreeSensorsReadings.add(sensorsReadings.substring(30,32));
        sectionThreeSensorsReadings.add(sensorsReadings.substring(33,35));
        sectionThreeSensorsReadings.add(sensorsReadings.substring(36,38));
        sectionThreeSensorsReadings.add(sensorsReadings.substring(39,41));
        sectionThreeSensorsReadings.add(sensorsReadings.substring(42,44));

        return sectionThreeSensorsReadings;
    }
    private List<String> getSectionFourSensorsReadings(String sensorsReadings) {
        List<String> sectionFourSensorsReadings = new ArrayList<>();
        sectionFourSensorsReadings.add(sensorsReadings.substring(45,47));
        sectionFourSensorsReadings.add(sensorsReadings.substring(48,50));
        sectionFourSensorsReadings.add(sensorsReadings.substring(51,53));
        sectionFourSensorsReadings.add(sensorsReadings.substring(54,56));
        sectionFourSensorsReadings.add(sensorsReadings.substring(57,59));

        return sectionFourSensorsReadings ;
    }
    private List<String> getSectionFiveSensorsReadings(String sensorsReadings) {
        List<String> sectionFiveSensorsReadings = new ArrayList<>();
        sectionFiveSensorsReadings.add(sensorsReadings.substring(60,62));
        sectionFiveSensorsReadings.add(sensorsReadings.substring(63,65));
        sectionFiveSensorsReadings.add(sensorsReadings.substring(66,68));
        sectionFiveSensorsReadings.add(sensorsReadings.substring(69,71));
        sectionFiveSensorsReadings.add(sensorsReadings.substring(72,74));

        return sectionFiveSensorsReadings ;
    }


    private boolean downloadFile() {
        return FileDownload.downloadFile(FarmControlActivity.this);
    }

    private void requestAppPermissions() {
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }

        if (hasReadPermissions() && hasWritePermissions()) {
            return;
        }

        ActivityCompat.requestPermissions(this,
                new String[] {
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

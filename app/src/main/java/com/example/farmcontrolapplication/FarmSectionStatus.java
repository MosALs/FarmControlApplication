package com.example.farmcontrolapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import com.eralp.circleprogressview.CircleProgressView;
import com.example.farmcontrolapplication.fileusage.FileDownload;

import java.util.ArrayList;
import java.util.List;


public class FarmSectionStatus extends AppCompatActivity {

    TextView sectionTextView;
    ImageView temperatureImage;
    CircleProgressView sOneCPView, sTwoCPView, sThreeCPView, sFourCPView, sFiveCPView;

    List<CircleProgressView> sensors = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm_section_status);

        sectionTextView = findViewById(R.id.section_id);
        temperatureImage = findViewById(R.id.temperature_image_id);
        sensors = bindCircleViewsIDs();
        for (int i = 0; i < sensors.size(); i++) {
            sensors.get(i).setInterpolator(new AccelerateDecelerateInterpolator());
        }

        final ProgressDialog progressDialog = new ProgressDialog(FarmSectionStatus.this);
        progressDialog.setTitle("Loading Data .... ");
        progressDialog.setIcon(R.drawable.temperature_sensor);
        progressDialog.show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            String action = getIntent().getStringExtra("FARM_SECTION");
            List<String> list = getIntent().getStringArrayListExtra("sensorsReadings");
            public void run() {
                progressDialog.dismiss();
                setSectionNumberTextAndTemperaturSensorimage(action, temperatureImage,list);
            }
        }, 2000); // 3000 milliseconds delay

    }

    private List<CircleProgressView> bindCircleViewsIDs() {

        List<CircleProgressView> sensors = new ArrayList<>();
        sOneCPView = (CircleProgressView) findViewById(R.id.sensor_one);
        sTwoCPView = (CircleProgressView) findViewById(R.id.sensor_two);
        sThreeCPView = (CircleProgressView) findViewById(R.id.sensor_three);
        sFourCPView = (CircleProgressView) findViewById(R.id.sensor_four);
        sFiveCPView = (CircleProgressView) findViewById(R.id.sensor_five);
        sensors.add(sOneCPView);
        sensors.add(sTwoCPView);
        sensors.add(sThreeCPView);
        sensors.add(sFourCPView);
        sensors.add(sFiveCPView);

        return sensors;
    }


    // this method sets the text in the top of the screen of every section and sets the temprature sensor
    // as it has to be only in the first and last section,
    //also it sets the progress of each sensor.
    private void setSectionNumberTextAndTemperaturSensorimage(String action, ImageView temperatureImage, List<String> list) {
        switch (action) {

            case "section 1":
                setTextDescrption(action);
                temperatureImage.setBackgroundResource(R.drawable.temperature_sensor);
                setSensorsProgress(list,sOneCPView,sTwoCPView,sThreeCPView,sFourCPView,sFiveCPView);
                break;

            case "section 2":
                setTextDescrption(action);
                setSensorsProgress(list,sOneCPView,sTwoCPView,sThreeCPView,sFourCPView,sFiveCPView);
                break;

            case "section 3":
                setTextDescrption(action);
                setSensorsProgress(list,sOneCPView,sTwoCPView,sThreeCPView,sFourCPView,sFiveCPView);
                break;

            case "section 4":
                setTextDescrption(action);
                setSensorsProgress(list,sOneCPView,sTwoCPView,sThreeCPView,sFourCPView,sFiveCPView);
                break;

            case "section 5":
                setTextDescrption(action);
                temperatureImage.setBackgroundResource(R.drawable.temperature_sensor);
                setSensorsProgress(list,sOneCPView,sTwoCPView,sThreeCPView,sFourCPView,sFiveCPView);
                break;
        }
    }

    private void setTextDescrption(String action) {
        sectionTextView.setText(action + " Data");
    }

    private void setSensorsProgress(List<String> list, CircleProgressView sOneCPView, CircleProgressView sTwoCPView,
                                    CircleProgressView sThreeCPView, CircleProgressView sFourCPView,
                                    CircleProgressView sFiveCPView) {

        sOneCPView.setProgressWithAnimation(Float.parseFloat(list.get(0)), 2000);
        sTwoCPView.setProgressWithAnimation(Float.parseFloat(list.get(1)), 2000);
        sThreeCPView.setProgressWithAnimation(Float.parseFloat(list.get(2)), 2000);
        sFourCPView.setProgressWithAnimation(Float.parseFloat(list.get(3)), 2000);
        sFiveCPView.setProgressWithAnimation(Float.parseFloat(list.get(4)), 2000);

    }

}

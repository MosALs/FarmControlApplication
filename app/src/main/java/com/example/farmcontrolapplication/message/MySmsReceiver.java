package com.example.farmcontrolapplication.message;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.example.farmcontrolapplication.HomeActivity;
import com.example.farmcontrolapplication.MainActivity;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;


public class MySmsReceiver extends BroadcastReceiver {

    private static final String TAG =
            MySmsReceiver.class.getSimpleName();
    public static final String pdu_type = "pdus";

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {

//        Toast.makeText(context, "broadcast receiver", Toast.LENGTH_SHORT).show();
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs;
        String strMessage = "";
        String format = bundle.getString("format");
        Object[] pdus = (Object[]) bundle.get(pdu_type);

        msgs = new SmsMessage[pdus.length];

//        if ()
        if (pdus != null) {
            // Check the Android version.
            boolean isVersionM = (Build.VERSION.SDK_INT >=
                    Build.VERSION_CODES.M);

            for (int i = 0; i < msgs.length; i++) {
                // Check Android version and use appropriate createFromPdu.
                if (isVersionM) {
                    // If Android version M or newer:
                    msgs[i] =
                            SmsMessage.createFromPdu((byte[]) pdus[i], format);
                } else {
                    // If Android version L or older:
                    msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                }


                // Build the message to show.
                strMessage += "SMS from " + msgs[i].getOriginatingAddress();
                strMessage += " :" + msgs[i].getMessageBody() + "\n";

                // Log and display the SMS message.
                Log.d(TAG, "onReceive: " + strMessage);
                Toast.makeText(context, strMessage, Toast.LENGTH_LONG).show();

                if (msgs[i].getOriginatingAddress().equals("+201029803456")) {

                    // this method creates file and pmut sensor readings from sms in it
                    createSMSFile(msgs[0].getMessageBody());

                    // save time of message in shared preferences.
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putLong("Time", System.currentTimeMillis());
                    editor.commit();

//                    Intent newIntent = new Intent(context, MainActivity.class);
//                    newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////                    newIntent.putExtra("SR",);
//                    newIntent.putExtra("Time",System.currentTimeMillis());
//                    context.startActivity(newIntent);

                }
            }
        }

    }

    public void createSMSFile(String sensorReadingsFromSMS){
        try {
            String path = Environment.getExternalStorageDirectory()+"/farm/sms";
            File root = new File(path);
            if (!root.exists()) {
                root.mkdirs();       // create folder if not exist
            }
            File file = new File(path + "/test.txt");
            if (!file.exists()) {
                file.createNewFile();   // create file if not exist
            }

            if (file.length() > 0){
                PrintWriter pw = new PrintWriter(file);
                pw.close();
            }
            BufferedWriter buf = new BufferedWriter(new FileWriter(file, true));
            buf.append(sensorReadingsFromSMS);
//            buf.newLine();  // pointer will be nextline
            buf.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}

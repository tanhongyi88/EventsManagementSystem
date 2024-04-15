package com.fit2081.assignment1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.StringTokenizer;

public class AddNewEventCategory extends AppCompatActivity {

    TextView categoryId;
    TextView categoryName;
    TextView eventCount;
    Switch isActive;
    MyBroadCastReceiver myBroadCastReceiver;

    private final int NUM_OPTIONAL_INPUT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_event_category);

        categoryId = findViewById(R.id.categoryIdInput);
        categoryName = findViewById(R.id.categoryNameInput);
        eventCount = findViewById(R.id.eventCountInput);
        isActive = findViewById(R.id.isActiveCategory);

        ActivityCompat.requestPermissions(this, new String[]{
                android.Manifest.permission.SEND_SMS,
                android.Manifest.permission.RECEIVE_SMS,
                android.Manifest.permission.READ_SMS
        }, 0);
    }

    @Override
    protected void onStart() {
        super.onStart();
        myBroadCastReceiver = new MyBroadCastReceiver();
        registerReceiver(myBroadCastReceiver, new IntentFilter(SMSReceiver.SMS_FILTER), RECEIVER_EXPORTED);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(myBroadCastReceiver);
    }

    public void setCategoryName(String categoryName) {
        this.categoryName.setText(categoryName);
    }

    public void setEventCount(String eventCount) {
        this.eventCount.setText(eventCount);
    }

    public void setIsActive(boolean isActive) {
        this.isActive.setChecked(isActive);
    }

    public void onSaveCategoryClick(View view) {
        String localCateName = categoryName.getText().toString();
        String localEventCount = eventCount.getText().toString();
        boolean localIsActive = isActive.isChecked();

        if (localCateName.isEmpty()) {
            Toast.makeText(this, "Please enter category name", Toast.LENGTH_SHORT).show();
            return;
        } else if (!localEventCount.isEmpty() && Integer.parseInt(localEventCount) <= 0) {
            Toast.makeText(this, "Event count cannot be smaller than 0", Toast.LENGTH_SHORT).show();
            return;
        }
        String localCategoryId = generateCategoryId();
        SharedPreferences sharedPref = getSharedPreferences(KeyStore.CATEGORY_FILE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(KeyStore.CATEGORY_ID, localCategoryId);
        editor.putString(KeyStore.CATEGORY_NAME, localCateName);
        if (localEventCount.isEmpty()) {
            editor.putInt(KeyStore.CATEGORY_EVENT_COUNT, -1);
        } else {
            editor.putInt(KeyStore.CATEGORY_EVENT_COUNT, Integer.parseInt(localEventCount));
        }
        editor.putBoolean(KeyStore.IS_CATEGORY_ACTIVE, localIsActive);
        editor.apply();
        Toast.makeText(this, String.format("Category saved: %s", localCategoryId), Toast.LENGTH_SHORT).show();

        categoryId.setText(localCategoryId);
    }

    private String generateCategoryId() {
        String[] alphabets = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L",
                "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
        int num = (int) Math.round(Math.random() * 100) % 26;
        int num2 = (int) Math.round(Math.random() * 100) % 26;
        StringBuilder res = new StringBuilder(String.format("%s%s%s-", "C", alphabets[num], alphabets[num2]));
        for (int i = 0; i < 4; i++) {
            long num3 = Math.round(Math.random() * 100) % 10;
            res.append(num3);
        }

        return String.valueOf(res);
    }

    class MyBroadCastReceiver extends BroadcastReceiver {

        private String removeZero(String str) {
            int i = 0;
            while (i < str.length() && str.charAt(i) == '0') {
                i++;
            }

            StringBuffer sb = new StringBuffer(str);

            sb.replace(0, i, "");

            return sb.toString();
        }

        /*
         * This method 'onReceive' will get executed every time class SMSReceive sends a broadcast
         * */
        @Override
        public void onReceive(Context context, Intent intent) {
            String msg = intent.getStringExtra(SMSReceiver.SMS_MSG_KEY);
            if (msg == null) {
                Toast.makeText(context, "Sorry, SMS receiver does not work", Toast.LENGTH_SHORT).show();
                Log.d(LogTags.MISSING_SMS, "onReceive: SMS is missing");
                return;
            }
            String[] msgParts = msg.split(":");
            if (msgParts.length != 2 || !msgParts[0].equals("category")) {
                Toast.makeText(context, "Unrecognised SMS: Not category type or too many/little arguments", Toast.LENGTH_SHORT).show();
                return;
            }
            String[] msgDataParts = msgParts[1].split(";");
            if (msgDataParts.length < 1 || msgDataParts.length > 3) {
                Toast.makeText(context, "Unrecognised SMS: Too many/little arguments", Toast.LENGTH_SHORT).show();
                return;
            }

            // check first argument
            if (msgDataParts[0].isEmpty()) {
                Toast.makeText(context, "Unrecognised SMS: (1) Empty argument", Toast.LENGTH_SHORT).show();
                return;
            }

            // check number of ";" even if there is only 1 argument
            if (msgDataParts.length == 1) {
                int numOfValidSemicolons = 0;
                for (int i = msgDataParts[0].length(); i < msgParts[1].length(); i++) {
                    if (String.valueOf(msgParts[1].charAt(i)).equals(";")) {
                        numOfValidSemicolons += 1;
                    }
                }
                if (numOfValidSemicolons != NUM_OPTIONAL_INPUT) {
                    Toast.makeText(context, "Unrecognised SMS: Unmatched semicolons", Toast.LENGTH_SHORT).show();
                }
            }
            // prefill category name
            setCategoryName(msgDataParts[0]);

            // check second argument
            if (msgDataParts.length >= 2) {
                String secondArgument = removeZero(msgDataParts[1]);
                if (!secondArgument.equals(msgDataParts[1])) {
                    Toast.makeText(context, "Unrecognised SMS: (2) Extra Zero", Toast.LENGTH_SHORT).show();
                    return;
                }
                for (int i = 0; i < secondArgument.length(); i++) {
                    if (!Character.isDigit(secondArgument.charAt(i))) {
                        Toast.makeText(context, "Unrecognised SMS: (2) Not digit or negative", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                // prefill event count
                setEventCount(msgDataParts[1]);
            }

            // check third argument
            if (msgDataParts.length == 3) {
                if (((msgDataParts[2].length() < 4 || msgDataParts[2].length() > 5)) ||
                        (msgDataParts[2].length() == 4 && !msgDataParts[2].equalsIgnoreCase("true")) ||
                        (msgDataParts[2].length() == 5 && !msgDataParts[2].equalsIgnoreCase("false"))) {
                    Toast.makeText(context, "Unrecognised SMS: (3) Not boolean", Toast.LENGTH_SHORT).show();
                    return;
                }
                // prefill isActive boolean
                setIsActive(msgDataParts[2].length() == 4);
            }
        }
    }
}
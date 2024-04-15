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
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import java.util.Arrays;

public class AddNewEvent extends AppCompatActivity {

    EditText eventId;
    EditText categoryId;
    EditText eventName;
    EditText ticketAvailable;

    Switch isActive;

    MyBroadCastReceiver myBroadCastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_event);

        eventId = findViewById(R.id.eventIdInput);
        categoryId = findViewById(R.id.eventCategoryIdInput);
        eventName = findViewById(R.id.eventNameInput);
        ticketAvailable = findViewById(R.id.ticketsAvailableInput);
        isActive = findViewById(R.id.isActiveEvent);

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

    public void onSaveEventClick(View view) {
        String localCategoryId = categoryId.getText().toString();
        String localEventName = eventName.getText().toString();
        String localTicketAvailable = ticketAvailable.getText().toString();
        boolean localIsActive = isActive.isChecked();

        if (localCategoryId.isEmpty() || localEventName.isEmpty()) {
            Toast.makeText(this, "Please enter category ID and event name", Toast.LENGTH_SHORT).show();
            return;
        } else if (!localTicketAvailable.isEmpty() && Integer.parseInt(localTicketAvailable) <= 0) {
            Toast.makeText(this, "Tickets available cannot be smaller than 0", Toast.LENGTH_SHORT).show();
            return;
        }
        String localEventId = generateEventId();
        SharedPreferences sharedPref = getSharedPreferences(KeyStore.EVENT_FILE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(KeyStore.EVENT_ID, localEventId);
        editor.putString(KeyStore.EVENT_CATEGORY_ID, localCategoryId);
        editor.putString(KeyStore.EVENT_NAME, localEventName);
        if (localTicketAvailable.isEmpty()) {
            editor.putInt(KeyStore.EVENT_TICKETS_AVAILABLE, -1);
        } else {
            editor.putInt(KeyStore.EVENT_TICKETS_AVAILABLE, Integer.parseInt(localTicketAvailable));
        }
        editor.putBoolean(KeyStore.IS_EVENT_ACTIVE, localIsActive);
        editor.apply();

        Toast.makeText(this, String.format("Event saved: %s", localEventId), Toast.LENGTH_SHORT).show();

        eventId.setText(localEventId);
    }

    private String generateEventId() {
        String[] alphabets = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L",
                "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
        int num = (int) Math.round(Math.random() * 100) % 26;
        int num2 = (int) Math.round(Math.random() * 100) % 26;
        StringBuilder res = new StringBuilder(String.format("%s%s%s-", "E", alphabets[num], alphabets[num2]));
        for (int i = 0; i < 5; i++) {
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
            if (msgParts.length != 2 || !msgParts[0].equals("event")) {
                Toast.makeText(context, "Unrecognised SMS: Not event type or too many/little arguments", Toast.LENGTH_SHORT).show();
                return;
            }
            String[] msgDataParts = msgParts[1].split(";");
            if (msgDataParts.length < 2 || msgDataParts.length > 4) {
                Toast.makeText(context, "Unrecognised SMS: Too many/little arguments", Toast.LENGTH_SHORT).show();
                return;
            }

            // check first and second argument
            if (msgDataParts[0].isEmpty() || msgDataParts[1].isEmpty()) {
                Toast.makeText(context, "Unrecognised SMS: (1)/(2) Empty argument", Toast.LENGTH_SHORT).show();
                return;
            }

            // prefill category id and event name
            categoryId.setText(msgDataParts[0]);
            eventName.setText(msgDataParts[1]);

            // check number of ";" even if there is only 2 argument
            if (msgDataParts.length == 2) {
                int numOfValidSemicolons = 0;
                for (int i = msgDataParts[0].length() + msgDataParts[1].length() + 1; i < msgParts[1].length(); i++) {
                    if (String.valueOf(msgParts[1].charAt(i)).equals(";")) {
                        numOfValidSemicolons += 1;
                    }
                }
                if (numOfValidSemicolons != 2) {
                    Toast.makeText(context, "Unrecognised SMS: Unmatched semicolons", Toast.LENGTH_SHORT).show();
                }
            }

            // check third argument
            if (msgDataParts.length >= 3) {
                String thirdArgument = removeZero(msgDataParts[2]);
                if (!thirdArgument.equals(msgDataParts[2])) {
                    Toast.makeText(context, "Unrecognised SMS: (3) Extra Zero", Toast.LENGTH_SHORT).show();
                    return;
                }
                for (int i = 0; i < thirdArgument.length(); i++) {
                    if (!Character.isDigit(thirdArgument.charAt(i))) {
                        Toast.makeText(context, "Unrecognised SMS: (3) Not digit or negative", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                // prefill ticket available
                ticketAvailable.setText(msgDataParts[2]);
            }

            // check fourth argument
            if (msgDataParts.length == 4) {
                if (((msgDataParts[3].length() < 4 || msgDataParts[2].length() > 5)) ||
                        (msgDataParts[3].length() == 4 && !msgDataParts[3].equalsIgnoreCase("true")) ||
                        (msgDataParts[3].length() == 5 && !msgDataParts[3].equalsIgnoreCase("false"))) {
                    Toast.makeText(context, "Unrecognised SMS: (4) Not boolean", Toast.LENGTH_SHORT).show();
                    return;
                }
                // prefill isActive boolean
                isActive.setChecked(msgDataParts[3].length() == 4);
            }
        }
    }
}
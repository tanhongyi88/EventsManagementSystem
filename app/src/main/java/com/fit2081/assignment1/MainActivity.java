package com.fit2081.assignment1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText usernameInput;
    EditText passInput;
    Button goToSignUp;
    Button loginBtn;

    String restoredUsername;
    int restoredPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameInput = findViewById(R.id.usernameInputLogin);
        passInput = findViewById(R.id.passInputLogin);
        loginBtn = findViewById(R.id.loginButton);
        goToSignUp = findViewById(R.id.goToSignUpBtn);

        // PRE-FILL
        SharedPreferences sharedPref = getSharedPreferences(KeyStore.FILE_NAME, MODE_PRIVATE);
        String usernameRestored = sharedPref.getString(KeyStore.KEY_USERNAME, "DEFAULT VALUE");
        int passRestored = sharedPref.getInt(KeyStore.KEY_PASSWORD, 0);
        if (!usernameRestored.equals("DEFAULT VALUE")) {
            restoredUsername = usernameRestored;
            restoredPass = passRestored;
        }
        usernameInput.setText(usernameRestored);
        passInput.setText(String.valueOf(passRestored));
//        if (!usernameRestored.equals("DEFAULT VALUE")) {
//            Toast.makeText(this, "Username and password restored successful", Toast.LENGTH_LONG).show();
//        }
    }

    public void onLoginClick(View view) {
        String usernameInputLogin = usernameInput.getText().toString();
        int passInputLogin = Integer.parseInt(passInput.getText().toString());

        if (!usernameInputLogin.equals(restoredUsername) || passInputLogin != restoredPass) {
            Toast.makeText(this, "Authentication failure: Username or password incorrect", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent dashboardIntent = new Intent(getApplicationContext(), Dashboard.class);
        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
        startActivity(dashboardIntent);
        finish();
    }

    public void onGoToSignUpClick(View view) {
        Intent signUpIntent = new Intent(getApplicationContext(), SignUp.class);
        startActivity(signUpIntent);
        finish();
    }

    public void back() {
        super.onBackPressed();
    }
}
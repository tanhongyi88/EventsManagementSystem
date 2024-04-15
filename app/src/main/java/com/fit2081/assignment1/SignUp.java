package com.fit2081.assignment1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUp extends AppCompatActivity {

    EditText usernameInputSignUp;
    EditText passInputSignUp;
    EditText confirmPassInputSignUp;
    Button signUpBtn;
    Button goToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        usernameInputSignUp = findViewById(R.id.usernameInputSignUp);
        passInputSignUp = findViewById(R.id.passInputSignUp);
        confirmPassInputSignUp = findViewById(R.id.confirmPassInputSignUp);
        signUpBtn = findViewById(R.id.signUpButton);
        goToLogin = findViewById(R.id.goToLoginBtn);
    }

    public void onSignUpClick(View view) {
        if (usernameInputSignUp.getText().toString().isEmpty() || passInputSignUp.getText().toString().isEmpty()) {
            Toast.makeText(this, "Username and password are required", Toast.LENGTH_SHORT).show();
            return;
        }
        if (confirmPassInputSignUp.getText().toString().isEmpty()) {
            Toast.makeText(this, "Confirm password is required", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            if (Integer.parseInt(passInputSignUp.getText().toString()) != Integer.parseInt(confirmPassInputSignUp.getText().toString())) {
                Toast.makeText(this, "Password and Confirm password have to be the same", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (Exception e) {
            System.out.println("Password input is not a number");
        }
        String usernameInput = usernameInputSignUp.getText().toString();
        int passInput = Integer.parseInt(passInputSignUp.getText().toString());

        SharedPreferences sharedPref = getSharedPreferences(KeyStore.FILE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(KeyStore.KEY_USERNAME, usernameInput);
        editor.putInt(KeyStore.KEY_PASSWORD, passInput);
        editor.apply();

        Toast.makeText(this, "Sign up successful", Toast.LENGTH_SHORT).show();
        Intent loginIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(loginIntent);
        finish();
    }

    public void onGoToLoginClick(View view) {
        Intent loginIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(loginIntent);
        finish();
    }
}
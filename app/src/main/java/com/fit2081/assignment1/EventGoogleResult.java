package com.fit2081.assignment1;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class EventGoogleResult extends AppCompatActivity {

    WebView googleResultWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_event_google_result);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        googleResultWebView = findViewById(R.id.googleWebView);

        // Enable loading website content using AJAX (Asynchronous JavaScript And XML) technique
        // AJAX allows web pages to be updated asynchronously by exchanging data with a web server behind the scenes.
        WebSettings settings = googleResultWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        googleResultWebView.setWebViewClient(new WebViewClient());

        // get string from the intent
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String eventName = bundle.getString("eventName");
            String searchURL = "https://www.google.com/search?q=" + eventName;
            googleResultWebView.loadUrl(searchURL);
        } else {
            Toast.makeText(this, "Something is wrong, please try again.", Toast.LENGTH_LONG).show();
        }
    }
}
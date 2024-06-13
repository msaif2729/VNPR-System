package com.example.vnpr;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class E_Challan extends AppCompatActivity {

    WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_echallan);

        webview = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webview.setWebViewClient(new Callback());
        webview.loadUrl("https://echallan.parivahan.gov.in/index/accused-challan");
        String rc ="rc_number_new";
        String ng_model_rcno = "accused.rc_no";
        String ng_model_chassis ="accused.chasis_no";
        String ng_model_engine = "accused.engine_no";

    }

    private class Callback extends WebViewClient {
        @Override
        public boolean shouldOverrideKeyEvent(WebView view,KeyEvent event)
        {
            return false;
        }

    }

}
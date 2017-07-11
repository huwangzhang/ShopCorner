package com.example.huwang.html5demo;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private WebView mWebView;
    private Button mButton;
    private WebAppInterface mInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWebView = (WebView) findViewById(R.id.webview);
        mButton = (Button) findViewById(R.id.btn_on);

        mWebView.loadUrl("file:///android_asset/index.html");
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        mInterface = new WebAppInterface(this);
        mWebView.addJavascriptInterface(mInterface, "app");
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInterface.showName("wind");
            }
        });
    }

    class WebAppInterface {
        private Context mContext;

        public WebAppInterface(Context context) {
            mContext = context;
        }

        @JavascriptInterface
        public void sayHello(String name) {
            Toast.makeText(mContext, "name= " + name, Toast.LENGTH_SHORT).show();
        }

        public void showName(final String name) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mWebView.loadUrl("javascript:showName('" + name + "')");
                }
            });
        }

    }

}

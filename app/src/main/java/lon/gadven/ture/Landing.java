package lon.gadven.ture;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.CookieSyncManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class Landing extends Activity {
    String url;
    public ValueCallback<Uri> mUploadMessage;
    public ValueCallback<Uri[]> uploadMessage;
    WebView web;
    ImageButton close;
    static String TAG = "test_for";


    public boolean isAfterMaxDate() {
        Date currentDate = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        Date maxDate = null;
        try {
            maxDate = dateFormat.parse("17.04.2020");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "current: " + currentDate);

        if (currentDate.after(maxDate)){
            Log.i(TAG, "after");
            return true;
        }


        Log.i(TAG, "before");

      return false;

    }




    public void defineButtonClose(){
        close = findViewById(R.id.close);
        if (isAfterMaxDate()) {

            close.setClickable(false);
            close.setBackgroundResource(0);

        }
        else{

            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Landing.this, TheGameActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }

    private class MyWebViewClient extends WebViewClient {
        private MyWebViewClient() {
        }




        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            view.loadData("<h2>Ошибка! Не удалось загрузить страницу!.</h2>", "text/html", "utf-8");
        }
        public void onPageFinished(WebView view, String url) {
            CookieSyncManager.getInstance().sync();
        }
    }

    @SuppressLint({"SetJavaScriptEnabled"})
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing);
        web = findViewById(R.id.web);
        url = getIntent().getStringExtra("url");

        Log.i(TAG, "url: " + url);
        defineButtonClose();

        if (isNetworkConnected()) {
            WebSettings mWebSettings = web.getSettings();
            mWebSettings.setJavaScriptEnabled(true);
            mWebSettings.setDomStorageEnabled(true);
            mWebSettings.setDatabaseEnabled(true);
            if (Build.VERSION.SDK_INT < 19) {
                mWebSettings.setDatabasePath("/data/data/" + web.getContext().getPackageName() + "/databases/");
            }
            mWebSettings.setSupportZoom(false);
            mWebSettings.setAllowFileAccess(true);
            mWebSettings.setAllowFileAccess(true);
            mWebSettings.setAllowContentAccess(true);
            web.loadUrl(url);
            web.setWebChromeClient(new WebChromeClient() {
                /* access modifiers changed from: protected */
                public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
                    Landing.this.mUploadMessage = uploadMsg;
                    Intent i = new Intent("android.intent.action.GET_CONTENT");
                    i.addCategory("android.intent.category.OPENABLE");
                    i.setType("image/*");
                    Landing.this.startActivityForResult(Intent.createChooser(i, "File Browser"), 1);
                }

                @RequiresApi(api = 21)
                public boolean onShowFileChooser(WebView mWebView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                    if (uploadMessage != null) {
                        uploadMessage.onReceiveValue(null);
                        uploadMessage = null;
                    }
                    uploadMessage = filePathCallback;
                    try {
                        Landing.this.startActivityForResult(fileChooserParams.createIntent(), 100);
                        return true;
                    } catch (ActivityNotFoundException e) {
                        uploadMessage = null;
                        Toast.makeText(Landing.this.getApplicationContext(), "Cannot Open File Chooser", Toast.LENGTH_LONG).show();
                        return false;
                    }
                }

                /* access modifiers changed from: protected */
                public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                    Landing.this.mUploadMessage = uploadMsg;
                    Intent intent = new Intent("android.intent.action.GET_CONTENT");
                    intent.addCategory("android.intent.category.OPENABLE");
                    intent.setType("image/*");
                    Landing.this.startActivityForResult(Intent.createChooser(intent, "File Browser"), 1);
                    Log.i("test_tag", "openFileChooser: ");
                }

                /* access modifiers changed from: protected */
                public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                    mUploadMessage = uploadMsg;
                    Intent i = new Intent("android.intent.action.GET_CONTENT");
                    i.addCategory("android.intent.category.OPENABLE");
                    i.setType("image/*");
                    Landing.this.startActivityForResult(Intent.createChooser(i, "File Chooser"), 1);
                }

            });
            web.setWebViewClient(new MyWebViewClient());
            return;
        }
        web.setWebViewClient(new MyWebViewClient());
        web.loadData("<h2>Ошибка! Отсутствует интернет подключение.</h2>", "text/html", "utf-8");
        web.setWebChromeClient(new WebChromeClient() {
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (Build.VERSION.SDK_INT >= 21) {
            if (requestCode == 100 && this.uploadMessage != null) {
                this.uploadMessage.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, intent));
                this.uploadMessage = null;
            }
        } else if (requestCode != 1) {
            Toast.makeText(getApplicationContext(), "Failed to Upload Image", Toast.LENGTH_LONG).show();
        } else if (this.mUploadMessage != null) {
            Object data = (intent == null || resultCode != -1) ? null : intent.getData();
            this.mUploadMessage.onReceiveValue((Uri)data);
            this.mUploadMessage = null;
        }
    }
    @Override
    public void onBackPressed() {
            if(web.canGoBack()) {
                web.goBack();
            } else {
                web.reload();
            }
    }

    public boolean isNetworkConnected() {
        if (((ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE)).getActiveNetworkInfo() == null) {
            return false;
        }
        return true;
    }


}






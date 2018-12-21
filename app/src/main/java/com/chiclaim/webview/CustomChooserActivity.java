package com.chiclaim.webview;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

/**
 * WebView 选择文件上传，自定义选择框
 */
public class CustomChooserActivity extends Activity {

    private static final int REQUEST_CODE_ALBUM = 1;
    private static final int REQUEST_CODE_CAMERA = 2;

    private WebView mWebView;

    private ValueCallback<Uri[]> mFilePathCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_chooser_activity_layout);
        mWebView = (WebView) findViewById(R.id.webview);
        initListener();
        mWebView.loadUrl("file:///android_asset/www/index2.html");

    }

    private void initListener() {
        mWebView.setWebChromeClient(new WebChromeClient() {

            //For Android5.0+
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                if (mFilePathCallback != null) {
                    mFilePathCallback.onReceiveValue(null);
                }
                mFilePathCallback = filePathCallback;

                showChooserDialog();
                return true;
            }
        });

    }

    private Dialog dialog;
    private boolean resetCallback;

    private void showChooserDialog() {
        if (dialog == null) {
            dialog = new Dialog(this);
            dialog.setTitle("文件选择");
            dialog.setContentView(R.layout.dialog_chooser_layout);
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    if (resetCallback && mFilePathCallback != null) {
                        mFilePathCallback.onReceiveValue(null);
                        mFilePathCallback = null;
                    }
                    resetCallback = true;
                }
            });

            dialog.findViewById(R.id.text_album).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resetCallback = false;
                    dialog.dismiss();
                    Intent albumIntent = new Intent(Intent.ACTION_PICK);
                    albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    startActivityForResult(albumIntent, REQUEST_CODE_ALBUM);
                }
            });
            dialog.findViewById(R.id.text_camera).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resetCallback = false;
                    dialog.dismiss();
                }
            });
        }
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_ALBUM:
                Uri[] results = null;
                if (resultCode == Activity.RESULT_OK && data != null) {
                    String dataString = data.getDataString();
                    if (dataString != null) {
                        results = new Uri[]{Uri.parse(dataString)};
                        Log.d("CustomChooserActivity", dataString);
                    }
                }
                if (mFilePathCallback != null) {
                    mFilePathCallback.onReceiveValue(results);
                    mFilePathCallback = null;
                }
                break;
            case REQUEST_CODE_CAMERA:

                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}

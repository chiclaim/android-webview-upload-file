/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.chiclaim.webview;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.chiclaim.webview.R;

public class MainFragment extends Fragment {

    private static final String TAG = MainFragment.class.getSimpleName();

    public static final int REQUEST_CODE_LOLIPOP = 1;
    private final static int RESULT_CODE_ICE_CREAM = 2;

    private WebView mWebView;
    private ValueCallback<Uri[]> mFilePathCallback;
    private String mCameraPhotoPath;

    private ValueCallback<Uri> mUploadMessage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Get reference of WebView from layout/activity_main.xml
        mWebView = (WebView) rootView.findViewById(R.id.fragment_main_webview);

        setUpWebViewDefaults(mWebView);

        // Check whether we're recreating a previously destroyed instance
        if (savedInstanceState != null) {
            // Restore the previous URL and history stack
            mWebView.restoreState(savedInstanceState);
        }

        mWebView.setWebChromeClient(new WebChromeClient() {


            private void openFileChooser(String type) {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType(type);
                startActivityForResult(Intent.createChooser(i, "File Chooser"),
                        RESULT_CODE_ICE_CREAM);
            }

            private void onShowFileChooser(Intent cameraIntent) {
                //selectionIntent(相册、文件管理)
                Intent selectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
                selectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
                selectionIntent.setType("image/*");

                Intent[] intentArray;
                if (cameraIntent != null) {
                    intentArray = new Intent[]{cameraIntent};
                } else {
                    intentArray = new Intent[0];
                }

                Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
                chooserIntent.putExtra(Intent.EXTRA_TITLE, "选择文件");
                chooserIntent.putExtra(Intent.EXTRA_INTENT, selectionIntent);
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);

                startActivityForResult(chooserIntent, REQUEST_CODE_LOLIPOP);

            }


            //The undocumented magic method override
            //Eclipse will swear at you if you try to put @Override here
            // For Android 3.0+
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                mUploadMessage = uploadMsg;
                openFileChooser("image/*");
            }

            // For Android 3.0+
            public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
                mUploadMessage = uploadMsg;
                openFileChooser("*/*");
            }

            //For Android 4.1
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                mUploadMessage = uploadMsg;
                openFileChooser("image/*");
            }

            //For Android5.0+
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                if (mFilePathCallback != null) {
                    mFilePathCallback.onReceiveValue(null);
                }
                mFilePathCallback = filePathCallback;

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                        takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath);
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                        Log.e(TAG, "Unable to create Image File", ex);
                    }

                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        mCameraPhotoPath = "file:" + photoFile.getAbsolutePath();
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                    } else {
                        takePictureIntent = null;
                    }
                }
                onShowFileChooser(takePictureIntent);
                return true;
            }
        });

        // Load the local index.html file
        if (mWebView.getUrl() == null) {
            mWebView.loadUrl("file:///android_asset/www/index.html");
            //mWebView.loadUrl("file:///android_asset/www/index2.html");
            //mWebView.loadUrl("https://www.script-tutorials.com/demos/199/index.html");
            //mWebView.loadUrl("http://192.168.1.109:8080/AndroidMvvmServer/upload");
        }

        return rootView;
    }

    /**
     * More info this method can be found at
     * http://developer.android.com/training/camera/photobasics.html
     *
     * @return
     * @throws IOException
     */
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        return imageFile;
    }

    /**
     * Convenience method to set some generic defaults for a
     * given WebView
     *
     * @param webView
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setUpWebViewDefaults(WebView webView) {
        WebSettings settings = webView.getSettings();

        // Enable Javascript
        settings.setJavaScriptEnabled(true);

        // Use WideViewport and Zoom out if there is no viewport defined
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);

        // Enable pinch to zoom without the zoom buttons
        settings.setBuiltInZoomControls(true);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            // Hide the zoom controls for HONEYCOMB+
            settings.setDisplayZoomControls(false);
        }

        // Enable remote debugging via chrome://inspect
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        // We set the WebViewClient to ensure links are consumed by the WebView rather
        // than passed to a browser if it can
        mWebView.setWebViewClient(new WebViewClient());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RESULT_CODE_ICE_CREAM:
                Uri uri = null;
                if (data != null) {
                    uri = data.getData();
                }
                mUploadMessage.onReceiveValue(uri);
                mUploadMessage = null;
                break;
            case REQUEST_CODE_LOLIPOP:
                Uri[] results = null;
                // Check that the response is a good one
                if (resultCode == Activity.RESULT_OK) {
                    if (data == null) {
                        // If there is not data, then we may have taken a photo
                        if (mCameraPhotoPath != null) {
                            Log.d("MainFragment", mCameraPhotoPath);

                            results = new Uri[]{Uri.parse(mCameraPhotoPath)};
                        }
                    } else {
                        String dataString = data.getDataString();
                        if (dataString != null) {
                            results = new Uri[]{Uri.parse(dataString)};
                        }
                    }
                }

                mFilePathCallback.onReceiveValue(results);
                mFilePathCallback = null;
                break;
        }
    }
}

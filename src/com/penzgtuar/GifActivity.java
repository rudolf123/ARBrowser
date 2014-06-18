package com.penzgtuar;

import android.app.Activity;
import android.os.Bundle;

public class GifActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GifWebView view = new GifWebView(this, "file:///android_asset/formula.gif");
        view.getSettings().setBuiltInZoomControls(true);
        view.getSettings().setSupportZoom(true);
        view.getSettings().setUseWideViewPort(true);
        setContentView(view);
    }
}
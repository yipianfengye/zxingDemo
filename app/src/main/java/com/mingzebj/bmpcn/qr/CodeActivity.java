package com.mingzebj.bmpcn.qr;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;

import com.google.zxing.Result;

import com.mingzebj.bmpcn.qr.activity.CaptureActivity;

public class CodeActivity extends CaptureActivity {

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
    }


    @Override
    public void myhandleDecode(Result rawResult, Bundle bundle, Rect mCropRect) {
        Intent intent = getIntent();
        intent.putExtra("result", rawResult.getText());
        setResult(RESULT_OK, intent);
        finish();
    }
}

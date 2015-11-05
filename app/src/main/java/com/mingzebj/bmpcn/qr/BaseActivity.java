package com.mingzebj.bmpcn.qr;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.mingzebj.bmpcn.qr.utils.Config;

/**
 * baseActivity activity的基类
 */
public class BaseActivity extends AppCompatActivity {

    public Activity mContext = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        // 侵入式状态栏
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus();
        }
    }


    @TargetApi(19)
    private void setTranslucentStatus() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }


    public void showProgress(boolean canCancled) {
        Config.showProgressDialog(mContext, canCancled, null);
    }

    public void showProgress(boolean canCancled, final Config.ProgressCancelListener listener) {
        Config.showProgressDialog(mContext, canCancled, listener);
    }

    public void dismissProgress() {
        Config.dismissProgress();
    }


    public void showDialog(String result) {
        // Config.showToast(mContext, "扫描的二维码格式不正确");
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("提示");
        builder.setMessage("当前二维码:" + result + " 为无效二维码");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}

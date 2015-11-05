package com.mingzebj.bmpcn.qr;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.w3c.dom.Text;

import com.mingzebj.bmpcn.qr.utils.Config;
import com.mingzebj.bmpcn.qr.utils.ServiceUtils;
import com.mingzebj.bmpcn.qr.utils.UserConfig;

public class UpdatePasswordActivity extends BaseActivity {
    /**
     * 修改密码
     */
    public EditText passwordEdit = null;
    /**
     * 确认修改密码
     */
    public EditText confirmPasswordEdit = null;
    /**
     * 确认按钮
     */
    public Button buttonUpdate = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);

        initView();
    }


    /**
     * 初始化组件
     */
    public void initView() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.mipmap.toolbar_back_icn_transparent);
        setSupportActionBar(mToolbar);

        passwordEdit = (EditText) findViewById(R.id.password_edit);
        confirmPasswordEdit = (EditText) findViewById(R.id.confirm_password_edit);
        buttonUpdate = (Button) findViewById(R.id.button_update);

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String password = passwordEdit.getText().toString();
                if (TextUtils.isEmpty(password)) {
                    Config.showToast(mContext, "密码不能为空");
                    return;
                }
                String confirmPassword = confirmPasswordEdit.getText().toString();
                if (TextUtils.isEmpty(confirmPassword)) {
                    Config.showToast(mContext, "确认密码不能为空");
                    return;
                }
                if (!confirmPassword.equals(password)) {
                    Config.showToast(mContext, "确认密码不正确");
                    return;
                }

                showProgress(false);
                new Thread() {
                    @Override
                    public void run() {
                        requestUpdatePassword(password);
                    }
                }.start();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            dismissProgress();
            if (msg.what == 0) {
                Config.showToast(mContext, "修改密码失败");
            } else {
                UserConfig.password = (String)msg.obj;
                Config.showToast(mContext, "修改密码成功");
                finish();
            }
        }
    };

    /**
     * 请求webservice，更改用户密码
     * @param newPassword
     */
    public void requestUpdatePassword(String newPassword) {
        try {
            // SOAP Action
            String soapAction = ServiceUtils.NAMESPACE + ServiceUtils.UPDATE_METHODNAME;
            // 指定WebService的命名空间和调用的方法名
            SoapObject rpc = new SoapObject(ServiceUtils.NAMESPACE, ServiceUtils.UPDATE_METHODNAME);
            // 设置需调用WebService接口需要传入的两个参数mobileCode、userId
            rpc.addProperty("username", UserConfig.userName);
            rpc.addProperty("oldpwd", UserConfig.password);
            rpc.addProperty("newpwd", newPassword);
            // 生成调用WebService方法的SOAP请求信息,并指定SOAP的版本
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);

            envelope.bodyOut = rpc;
            // 设置是否调用的是dotNet开发的WebService
            envelope.dotNet = true;
            // 等价于envelope.bodyOut = rpc;
            envelope.setOutputSoapObject(rpc);

            HttpTransportSE transport = new HttpTransportSE(ServiceUtils.POINT);
            try {
                // 调用WebService
                transport.call(soapAction, envelope);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // 获取返回的数据
            SoapObject object = (SoapObject) envelope.bodyIn;
            // 获取返回的结果
            String result = object.getProperty(0).toString();
            if (!TextUtils.isEmpty(result) && result.trim().equals("true")) {
                Message msg = new Message();
                msg.obj = newPassword;
                msg.what = 1;
                handler.sendMessage(msg);
            } else {
                handler.sendEmptyMessage(0);
            }

        } catch (Exception e) {
            handler.sendEmptyMessage(0);
        }
    }
}

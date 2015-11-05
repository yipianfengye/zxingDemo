package com.mingzebj.bmpcn.qr;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.mingzebj.bmpcn.qr.utils.Config;
import com.mingzebj.bmpcn.qr.utils.ServiceUtils;
import com.mingzebj.bmpcn.qr.utils.UserConfig;

/**
 * 登录页面
 */
public class LoginActivity extends BaseActivity {

    public Button loginButton = null;
    public EditText userNameEdit = null;
    public EditText passwordEdit = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // getSupportActionBar().hide();
        initView();
    }


    /**
     * 初始化组件
     */
    public void initView() {
        loginButton = (Button) this.findViewById(R.id.login_button);
        userNameEdit = (EditText) this.findViewById(R.id.username_edittext);
        passwordEdit = (EditText) this.findViewById(R.id.password_edittext);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String userName = userNameEdit.getText().toString();
                final String password = passwordEdit.getText().toString();
                if (TextUtils.isEmpty(userName)) {
                    Config.showToast(mContext, "用户名不能为空");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Config.showToast(mContext, "密码不能为空");
                    return;
                }
                showProgress(false);
                new Thread() {
                    @Override
                    public void run() {
                        requestLogin(userName, password);
                    }
                }.start();
            }
        });
    }

    /**
     * 按两次返回键，退出系统
     */
    private static long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                if ((System.currentTimeMillis() - exitTime) > 2000) {
                    Config.showToast(mContext, "在按一次退出");
                    exitTime = System.currentTimeMillis();
                } else {
                    finish();
                }
                return true;
            }
            return true;
        }
        return false;
    }


    /**
     * 登陆异步操作
     */
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            dismissProgress();
            if (msg.what == 0) {
                Config.showToast(mContext, "登陆失败，请重试");
            } else {
                Bundle bundle = msg.getData();
                UserConfig.userName = bundle.getString("username");
                UserConfig.password = bundle.getString("password");
                Intent intent = new Intent(mContext, MainActivity.class);
                startActivity(intent);
            }

        }
    };

    /**
     * 登录请求操作
     * @param userName
     * @param password
     */
    public void requestLogin(String userName, String password) {
        try {
            // SOAP Action
            String soapAction = ServiceUtils.NAMESPACE + ServiceUtils.LOGIN_METHODNAME;
            // 指定WebService的命名空间和调用的方法名
            SoapObject rpc = new SoapObject(ServiceUtils.NAMESPACE, ServiceUtils.LOGIN_METHODNAME);
            // 设置需调用WebService接口需要传入的两个参数mobileCode、userId
            rpc.addProperty("username", userName);
            rpc.addProperty("password", password);
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
                Bundle bundle = new Bundle();
                bundle.putString("username", userName);
                bundle.putString("password", password);
                msg.setData(bundle);
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

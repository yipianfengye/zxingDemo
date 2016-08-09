package com.mingzebj.bmpcn.qr;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.mingzebj.bmpcn.qr.utils.Config;
import com.mingzebj.bmpcn.qr.utils.ServiceUtils;
import com.mingzebj.bmpcn.qr.utils.TextWatcherAdapter;
import com.mingzebj.bmpcn.qr.zxing.activity.CaptureActivity;

/**
 * 主界面
 */
public class MainActivity extends BaseActivity {

    public static final int REQUEST_CODE = 101;
    /**
     * 防伪码输入框
     */
    public EditText inputEdit = null;
    /**
     * 查询按钮
     */
    public ImageView inputImage = null;
    /**
     * 扫码按钮
     */
    public Button buttonCode = null;
    /**
     * 扫描code
     */
    public TextView showCode = null;
    /**
     * 扫描结果
     */
    public TextView showResult = null;
    /**
     * 底部图片
     */
    public ImageView imageButtom = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    /**
     * 初始化组件
     */
    public void initView() {

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("防伪查询");
        mToolbar.setNavigationIcon(R.mipmap.toolbar_back_icn_transparent);
        setSupportActionBar(mToolbar);

        inputEdit = (EditText) findViewById(R.id.input_edit);
        inputImage = (ImageView) findViewById(R.id.input_image);
        buttonCode = (Button) findViewById(R.id.button_code);
        showCode = (TextView) findViewById(R.id.show_code);
        showResult = (TextView) findViewById(R.id.show_result);
        imageButtom = (ImageView) findViewById(R.id.image_buttom);


        inputEdit.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence == null || charSequence.length() == 0) {
                    showResult.setText("");
                }
                showCode.setText(inputEdit.getText());
            }
        });

        inputImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String inputEdits = inputEdit.getText().toString();
                if (TextUtils.isEmpty(inputEdits)) {
                    Config.showToast(mContext, "防伪码不能为空");
                    return;
                }

                showProgress(false);
                new Thread() {
                    @Override
                    public void run() {
                        requestCode(inputEdits);
                    }
                }.start();

            }
        });

        buttonCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showResult.setText("");
                inputEdit.setText("");
                showCode.setText("");
                //打开扫描界面扫描条形码或二维码
                Intent openCameraIntent = new Intent(mContext, CaptureActivity.class);
                startActivityForResult(openCameraIntent, REQUEST_CODE);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item == null)
            return true;
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        // 跳转修改密码
        if (item.getItemId() == R.id.update_password) {
            Intent intent = new Intent(mContext, UpdatePasswordActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                String result = bundle.getString("result");
                if (!TextUtils.isEmpty(result) && result.length() > 16) {
                    final String endResult = result.substring(result.length() - 16, result.length());
                    showCode.setText(endResult);

                    showProgress(false);
                    new Thread() {
                        @Override
                        public void run() {
                            requestCode(endResult);
                        }
                    }.start();
                } else {
                    showDialog(result);
                }
            }
        }
    }


    /**
     * 网络请求异步操作
     */
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            dismissProgress();
            Bundle bundle = msg.getData();
            if (msg.what == 0) {
                Config.showToast(mContext, "查询失败，请重试");
            } else {
                String results = bundle.getString("result");

                Spanned spanned = Html.fromHtml(results, new Html.ImageGetter() {
                    @Override
                    public Drawable getDrawable(String source) {
                        return null;
                    }
                }, null);
                showResult.setText(spanned);
            }
        }
    };

    /**
     * 请求webservice，查询防伪码
     * @param code
     */
    public void requestCode(String code) {
        try {
            // SOAP Action
            String soapAction = ServiceUtils.NAMESPACE + ServiceUtils.CHECK_METHODNAME;
            // 指定WebService的命名空间和调用的方法名
            SoapObject rpc = new SoapObject(ServiceUtils.NAMESPACE, ServiceUtils.CHECK_METHODNAME);
            // 设置需调用WebService接口需要传入的两个参数mobileCode、userId
            rpc.addProperty("code", code);
            // way的参数暂时写死
            rpc.addProperty("way", "APP");
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

            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("result", result);
            msg.setData(bundle);
            msg.what = 1;
            handler.sendMessage(msg);
        } catch (Exception e) {
            Message msg = new Message();
            Bundle bundle = new Bundle();
            msg.what = 0;
            msg.setData(bundle);
            handler.sendMessage(msg);
        }
    }
}

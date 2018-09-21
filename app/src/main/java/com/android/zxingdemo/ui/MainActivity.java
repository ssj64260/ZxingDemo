package com.android.zxingdemo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.android.zxingdemo.R;
import com.android.zxingdemo.app.BaseActivity;
import com.android.zxingdemo.util.PermissionsHelper;
import com.android.zxingdemo.util.ToastMaster;
import com.google.zxing.client.android.CaptureActivity;
import com.google.zxing.client.android.Intents;


public class MainActivity extends BaseActivity {

    private static final int REQUEST_CODE_QRCODE = 100;

    private PermissionsHelper mPermissionsHelper;
    private TextView tvCreateQrCode;
    private TextView tvScan;
    private TextView tvText;

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
        super.initData();

        mPermissionsHelper = new PermissionsHelper
                .Builder()
                .camera()
                .readContacts()
                .writeExternalStorage()
                .bulid();
        mPermissionsHelper.setPermissionsResult(mPermissionsResult);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);


        tvCreateQrCode = findViewById(R.id.tv_create_qr_code);
        tvScan = findViewById(R.id.tv_scan);
        tvText = findViewById(R.id.tv_text);

        mPermissionsHelper.requestPermissions(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mPermissionsHelper.requestPermissionsResult(this, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPermissionsHelper.activityResult(this, requestCode);
        if (RESULT_OK == resultCode) {
            if (REQUEST_CODE_QRCODE == requestCode) {
                if (data != null) {
                    final String text = data.getStringExtra(Intents.Scan.RESULT);
                    tvText.setText(text);
                }
            }
        } else if (RESULT_CANCELED == resultCode) {
            ToastMaster.show("取消扫描");
        }
    }

    private View.OnClickListener mClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_create_qr_code:
                    CreateQrCodeActivity.show(MainActivity.this);
                    break;
                case R.id.tv_scan:
                    CaptureActivity.show(MainActivity.this);
                    break;
            }
        }
    };

    private PermissionsHelper.OnPermissionsResult mPermissionsResult = new PermissionsHelper.OnPermissionsResult() {
        @Override
        public void allPermissionGranted() {
            tvCreateQrCode.setOnClickListener(mClick);
            tvScan.setOnClickListener(mClick);
        }

        @Override
        public void cancelToSettings() {
            finish();
        }
    };
}

package com.etrans.jt.bluetooth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.etrans.jt.bluetooth.base.BaseActivity;
import com.etrans.jt.bluetooth.base.BasePresenter;
import com.etrans.jt.bluetooth.view.MusicActivity;
import com.etrans.jt.bluetooth.view.PhoneActivity;
import com.etrans.jt.btlibrary.module.BluetoothModule;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements View.OnClickListener {


    private static final String TAG = MainActivity.class.getSimpleName();
    @Bind(R.id.btn_start_music)
    Button mBtnStartMusic;
    @Bind(R.id.btn_connect_bt)
    Button mBtnConnectBt;
    @Bind(R.id.btn_get_name)
    Button mBtnGetName;
    @Bind(R.id.btn_start_phone)
    Button mBtnStartPhone;


    @Override
    public void init() {
        mBtnStartMusic.setOnClickListener(this);
        mBtnConnectBt.setOnClickListener(this);
        mBtnGetName.setOnClickListener(this);
        mBtnStartPhone.setOnClickListener(this);
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    public int getLayout() {
        return R.layout.activity_main;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_start_music:
                startActivity(new Intent(this, MusicActivity.class));
                break;
            case R.id.btn_get_name:
                mBtnGetName.setText(BluetoothModule.getInstance().getDevicesName());
                break;
            case R.id.btn_connect_bt:
                BluetoothModule.getInstance().openOrClose();
                break;
            case R.id.btn_start_phone:
                startActivity(new Intent(this, PhoneActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}

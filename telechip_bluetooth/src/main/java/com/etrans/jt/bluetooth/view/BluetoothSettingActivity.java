package com.etrans.jt.bluetooth.view;

import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.etrans.jt.bluetooth.R;
import com.etrans.jt.bluetooth.base.BaseActivity;
import com.etrans.jt.bluetooth.base.BasePresenter;
import com.etrans.jt.bluetooth.utils.ToastFactory;
import com.etrans.jt.btlibrary.manager.XxBaseModule;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 单元名称:BluetoothSettingActivity.java
 * Created by fuxiaolei on 2016/9/8.
 * 说明:
 * Last Change by fuxiaolei on 2016/9/8.
 */
public class BluetoothSettingActivity extends BaseActivity implements View.OnClickListener, XxBaseModule.IUpdateUI {

    @Bind(R.id.btn_commit)
    Button mBtnCommit;
    @Bind(R.id.et_bluetooth_name)
    EditText mEtBluetoothName;
    private BluetoothAdapter adapter;
    private String name;
    private ImageView mBack;

    @Override
    public void init() {

        mBtnCommit.setOnClickListener(this);
        initView();
        initData();
//        initTopView("蓝牙设置");

    }

    private void initView() {
        mBack = (ImageView) findViewById(R.id.iv_back);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initData() {
        adapter = BluetoothAdapter.getDefaultAdapter();
        name = adapter.getName();
        mEtBluetoothName.setText(name);
        mEtBluetoothName.setSelection(name.length());
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    public int getLayout() {
        return R.layout.activity_bluetooth_setting;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_commit:

                String name = mEtBluetoothName.getText().toString();
                if (name == null || name.trim().length() < 4) {
                    Toast.makeText(getApplicationContext(), "蓝牙名称长度必须是4到8位!", Toast.LENGTH_SHORT).show();
                    return;
                }
//				String pwd = mEdtPwd.getText().toString();
//				if(pwd == null || pwd.trim().length() != 4){
//					Toast.makeText(XxBtearPhoneSettting.this, getString(R.string.btearphone_setting_pwd_erro), Toast.LENGTH_SHORT).show();
//
//					return;
//				}
                adapter.setName(name);
                finish();
                Toast.makeText(getApplicationContext(), "设置成功!", Toast.LENGTH_SHORT).show();
                finish();

                break;
            default:

                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    @Override
    public void onUpdateUI(Message msg) {
//        refreshTopUI(msg);
    }
}

package com.etrans.jt.bluetooth.view;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
//import android.vehicle.audio.AudioExManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.broadcom.bt.hfdevice.BluetoothHfDevice;
import com.etrans.jt.bluetooth.BTApplication;
import com.etrans.jt.bluetooth.R;
import com.etrans.jt.bluetooth.base.BaseActivity;
import com.etrans.jt.bluetooth.base.BasePresenter;
import com.etrans.jt.bluetooth.listener.proxy.IPhoneCallProxy;
import com.etrans.jt.bluetooth.listener.view.IPhoneCallView;
import com.etrans.jt.bluetooth.presenter.PhoneCallPresenter;
import com.etrans.jt.bluetooth.proxy.BluetoothStateProxy;
import com.etrans.jt.btlibrary.manager.XxBaseModule;
import com.etrans.jt.btlibrary.module.BluetoothPhoneModule;
import com.txznet.sdk.TXZCallManager;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 单元名称:PhoneCallExActivity.java
 * Created by fuxiaolei on 2016/8/30.
 * 说明:
 * Last Change by fuxiaolei on 2016/8/30.
 */
public class PhoneCallExActivity extends BaseActivity implements View.OnClickListener, IPhoneCallView, XxBaseModule.IUpdateUI {
    private static final String TAG = PhoneCallExActivity.class.getSimpleName();
    ImageView mBtnAnswer;
    ImageView mBtnHangup;
    ImageView mBtnVolType;
    @Bind(R.id.tv_call_state)
    TextView mTvCallState;
    @Bind(R.id.tv_call_phone_num)
    TextView mTvCallPhoneNum;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.iv_back)
    ImageView mIvBack;
    @Bind(R.id.iv_num_1)
    ImageView mIvNum1;
    @Bind(R.id.iv_num_2)
    ImageView mIvNum2;
    @Bind(R.id.iv_num_3)
    ImageView mIvNum3;
    @Bind(R.id.iv_num_4)
    ImageView mIvNum4;
    @Bind(R.id.iv_num_5)
    ImageView mIvNum5;
    @Bind(R.id.iv_num_6)
    ImageView mIvNum6;
    @Bind(R.id.iv_num_7)
    ImageView mIvNum7;
    @Bind(R.id.iv_num_8)
    ImageView mIvNum8;
    @Bind(R.id.iv_num_9)
    ImageView mIvNum9;
    @Bind(R.id.iv_num_x)
    ImageView mIvNumX;
    @Bind(R.id.iv_num_0)
    ImageView mIvNum0;
    @Bind(R.id.iv_num_j)
    ImageView mIvNumJ;
    @Bind(R.id.ll_dial_num)
    LinearLayout mLlDialNum;
    @Bind(R.id.iv_show_dial_num)
    ImageView mIvShowDialNum;
    @Bind(R.id.img_def_icon)
    ImageView mIvDefIcon;
    @Bind(R.id.et_dtmf_code)
    EditText mEtDtmfCode;
    @Bind(R.id.tv_call_time)
    TextView mTvCallTime;
    private PhoneCallPresenter mPresenter;
    private IPhoneCallProxy proxy;
    private boolean misShowDail = false;
    private long click;
    private boolean num = false;
//    private AudioExManager audioExManager;


    @Override
    public void init() {
        mLlDialNum.setVisibility(View.GONE);
        mBtnHangup = (ImageView) findViewById(R.id.iv_hangup);
        mBtnAnswer = (ImageView) findViewById(R.id.iv_answer);
        mBtnVolType = (ImageView) findViewById(R.id.iv_vol_type);
        mIvShowDialNum = (ImageView) findViewById(R.id.iv_show_dial_num);

        mBtnVolType.setEnabled(false);
        mIvBack.setVisibility(View.INVISIBLE);


        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("");
        if (!pm.isScreenOn()) {
            PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, this.getClass().getCanonicalName());
            wakeLock.acquire();

            keyguardLock.disableKeyguard();
        } else {
            keyguardLock.reenableKeyguard();
        }

        initData();
        initOnclickListener();
        initTopView(getString(R.string.bt_title));
    }

    private void initData() {
        //根据传入不同的值 进行展示不同按钮
        int state = getIntent().getIntExtra("callState", -1);
        if (state == -1) {
            finish();
            return;
        } else if (state == BluetoothHfDevice.CALL_SETUP_STATE_DIALING) {
            //打出去 显示语音切换 和 挂断按钮
            mBtnVolType.setVisibility(View.VISIBLE);
            mBtnAnswer.setVisibility(View.GONE);
            mTvCallState.setText(getString(R.string.bt_dialing));
        } else if (state == BluetoothHfDevice.CALL_SETUP_STATE_INCOMING) {
            //打进来 显示挂断 和 接听按钮
            mBtnAnswer.setVisibility(View.VISIBLE);
            mBtnVolType.setVisibility(View.GONE);
//            mTvCallState.setText("Incoming...");
        } else {
            mBtnVolType.setVisibility(View.VISIBLE);
            mBtnAnswer.setVisibility(View.GONE);
        }
    }

    private void initOnclickListener() {
        mBtnAnswer.setOnClickListener(this);
        mBtnHangup.setOnClickListener(this);
        mBtnVolType.setOnClickListener(this);
        mIvBack.setOnClickListener(this);
        mIvNum1.setOnClickListener(this);
        mIvNum2.setOnClickListener(this);
        mIvNum3.setOnClickListener(this);
        mIvNum4.setOnClickListener(this);
        mIvNum5.setOnClickListener(this);
        mIvNum6.setOnClickListener(this);
        mIvNum7.setOnClickListener(this);
        mIvNum8.setOnClickListener(this);
        mIvNum9.setOnClickListener(this);
        mIvNum0.setOnClickListener(this);
        mIvNumX.setOnClickListener(this);
        mIvNumJ.setOnClickListener(this);
        mIvShowDialNum.setOnClickListener(this);
        mEtDtmfCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                proxy.sendDTMFCode(s, start);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    protected BasePresenter initPresenter() {
        mPresenter = new PhoneCallPresenter(this, this);
        proxy = (IPhoneCallProxy) new BluetoothStateProxy(mPresenter).bind();
        return mPresenter;
    }

    @Override
    public int getLayout() {
        return R.layout.activity_phone_call;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        if (BluetoothPhoneModule.getInstance().isLocalCall()) {
            if (((BTApplication) getApplication()).mCallToolStatusListener != null) {
                TXZCallManager.Contact con = new TXZCallManager.Contact();
                con.setName("张三");
                con.setNumber("10086");
                ((BTApplication) getApplication()).mCallToolStatusListener.onMakeCall(con);
                Log.e("audioExManager", "------------onMakeCall--------------");
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (((BTApplication) getApplication()).mCallToolStatusListener != null) {
                        ((BTApplication) getApplication()).mCallToolStatusListener.onIdle();
                        Log.e("audioExManager", "------------onIdle--------------");
                    }
                }
            },500);
            finish();
        }
        ButterKnife.bind(this);
//        audioExManager = (AudioExManager) getSystemService(Context.AUDIOEX_SERVICE);

    }

    /******
     * zwj+
     *****/
    BroadcastReceiver mAnswerBtnReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action != null && action.equals("com.android.vehicle.CALL")) {
                if (true/*is dialing*/) {
                    if (System.currentTimeMillis() - click < 1000) {
                        return;
                    }
                    if (!num) {
                        mIvShowDialNum.setVisibility(View.GONE);
                        num = true;
                    } else {
                        mIvShowDialNum.setVisibility(View.VISIBLE);
                        num = false;
                    }
                    proxy.changeAudio();
                    click = System.currentTimeMillis();
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /******
     * zwj+end
     *******/

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_answer:
                proxy.answer();
                break;
            case R.id.iv_hangup:
                proxy.hangup();
                break;
            case R.id.iv_vol_type:
                if (System.currentTimeMillis() - click < 1000) {
                    return;
                }
                if (!num) {
                    mIvShowDialNum.setVisibility(View.GONE);
                    num = true;
                } else {
                    mIvShowDialNum.setVisibility(View.VISIBLE);
                    num = false;
                }
                proxy.changeAudio();
                click = System.currentTimeMillis();

//                if (mLlDialNum.getVisibility() == View.VISIBLE) {
//                    mLlDialNum.setVisibility(View.GONE);
//                    mEtDtmfCode.setVisibility(View.GONE);
//                }
                break;
            case R.id.iv_show_dial_num:
                misShowDail = !misShowDail;
                showDial(misShowDail);
                break;
            case R.id.iv_num_0:
                appendNum("0");
                break;
            case R.id.iv_num_1:
                appendNum("1");
                break;
            case R.id.iv_num_2:
                appendNum("2");
                break;
            case R.id.iv_num_3:
                appendNum("3");
                break;
            case R.id.iv_num_4:
                appendNum("4");
                break;
            case R.id.iv_num_5:
                appendNum("5");
                break;
            case R.id.iv_num_6:
                appendNum("6");
                break;
            case R.id.iv_num_7:
                appendNum("7");
                break;
            case R.id.iv_num_8:
                appendNum("8");
                break;
            case R.id.iv_num_9:
                appendNum("9");
                break;
            case R.id.iv_num_x:
                appendNum("*");
                break;
            case R.id.iv_num_j:
                appendNum("#");
                break;
            default:
                break;
        }
    }

    @Override
    public void updateCallState(String s) {
    }

    @Override
    public void updateAudioState(int state) {
        if (state == 0) {
            mBtnVolType.setImageResource(R.drawable.btn_mute_recover_nor);
            mIvShowDialNum.setVisibility(View.GONE);
            showDial(false);
        } else if (state == 2) {
            mBtnVolType.setImageResource(R.drawable.btn_mute_nor);
            mIvShowDialNum.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void updateCallEx(String callNumber, String name) {
        if (!TextUtils.isEmpty(name) && name.equals("我的编号")) {
            finish();
            return;
        }
        if (callNumber.length() < 8) {
            mTvCallPhoneNum.setText(callNumber);
        } else {
            mTvCallPhoneNum.setText(name);
        }
    }

    @Override
    public void hangup() {
        this.finish();
    }

    @Override
    public void updateCallTime(String times) {
        mBtnVolType.setEnabled(true);
        mTvCallState.setText(times);
    }

    @Override
    public void updateTalking() {
        mBtnVolType.setVisibility(View.VISIBLE);
        mBtnAnswer.setVisibility(View.GONE);
    }

    @Override
    public void localFinish() {
        finish();
    }

    @Override
    public void updateChangeAudioEnable() {
        mBtnVolType.setEnabled(true);
    }

    @Override
    protected void onResume() {
        proxy.sendBIA(true);
        if (((BTApplication) getApplication()).mCallToolStatusListener != null) {
            TXZCallManager.Contact con = new TXZCallManager.Contact();
            con.setName("张三");
            con.setNumber("10086");
            ((BTApplication) getApplication()).mCallToolStatusListener.onMakeCall(con);
        }
//        audioExManager.setVoiceState(2);
        Log.e("audioExManager", "------------setVoiceState(2)--------------");
        super.onResume();

        /**zwj+*/
        registerReceiver(mAnswerBtnReceiver, new IntentFilter("com.android.vehicle.CALL"));
        /**zwj+end*/

//        Intent a2dpIntent = new Intent(PhoneActivity.A2DP_CONNECT);
//        sendBroadcast(a2dpIntent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        BluetoothPhoneModule.getInstance().getClcc();
    }

    @Override
    protected void onPause() {
        proxy.sendBIA(false);
        if (((BTApplication) getApplication()).mCallToolStatusListener != null) {
            ((BTApplication) getApplication()).mCallToolStatusListener.onIdle();
            Log.e("audioExManager", "------------onIdle--------------");
        }
//        audioExManager.setVoiceState(1);
        Log.e("audioExManager", "------------setVoiceState(1)--------------");
        super.onPause();
        unregisterReceiver(mAnswerBtnReceiver);
//        Intent intent = new Intent(PhoneActivity.A2DP_DISCONNECT);
//        sendBroadcast(intent);
    }

    @Override
    public void onBackPressed() {
        this.moveTaskToBack(true);
    }

    void showDial(boolean show) {
        mIvShowDialNum.setBackgroundResource(show ? R.drawable.ic_call_close_dial : R.drawable.ic_call_show_dial);
        mLlDialNum.setVisibility(show ? View.VISIBLE : View.GONE);
        mEtDtmfCode.setVisibility(show ? View.VISIBLE : View.GONE);
        mIvDefIcon.setVisibility(show ? View.GONE : View.VISIBLE);
        mTvCallPhoneNum.setVisibility(show ? View.GONE : View.VISIBLE);
        mTvCallState.setVisibility(show ? View.GONE : View.VISIBLE);
        mTvCallTime.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    private void appendNum(String num) {
        mEtDtmfCode.append(num);
        Log.d(TAG, "appendNum() called with: " + "num = [" + mEtDtmfCode.getText().toString() + "]");
    }

    @Override
    public void onUpdateUI(Message msg) {
        refreshTopUI(msg);
    }
}

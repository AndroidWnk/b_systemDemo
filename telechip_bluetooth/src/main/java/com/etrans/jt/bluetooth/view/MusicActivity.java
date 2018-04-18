package com.etrans.jt.bluetooth.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.etrans.jt.bluetooth.R;
import com.etrans.jt.bluetooth.base.BaseActivity;
import com.etrans.jt.bluetooth.base.BasePresenter;
import com.etrans.jt.bluetooth.listener.proxy.IMusicProxy;
import com.etrans.jt.bluetooth.listener.view.IMusicView;
import com.etrans.jt.bluetooth.presenter.MusicPresenter;
import com.etrans.jt.bluetooth.proxy.BluetoothStateProxy;
import com.etrans.jt.btlibrary.domin.SongInfo;
import com.etrans.jt.btlibrary.module.XxAudioManager;
import com.etrans.jt.btlibrary.utils.XxRadioUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 单元名称:MusicActivity.java
 * Created by fuxiaolei on 2016/7/4.
 * 说明:
 * Last Change by fuxiaolei on 2016/7/4.
 */
public class MusicActivity extends BaseActivity implements IMusicView, View.OnClickListener, View.OnTouchListener {

    @Bind(R.id.btn_play_pause)
    Button mBtnPlay;
    @Bind(R.id.tv_song_name)
    TextView mTvSongName;
    @Bind(R.id.btn_forward)
    Button mBtnForward;
    @Bind(R.id.btn_backward)
    Button mBtnBackward;
    @Bind(R.id.currTime)
    TextView mCurrTime;
    @Bind(R.id.totalTime)
    TextView mTotalTime;
    @Bind(R.id.seekBar)
    SeekBar mSeekBar;
    private MusicPresenter mMusicPresenter;
    private IMusicProxy mProxy;

    @Override
    public void init() {
        mBtnPlay.setOnClickListener(this);
        mBtnForward.setOnClickListener(this);
        mBtnBackward.setOnClickListener(this);
        mBtnForward.setOnTouchListener(this);
        mBtnBackward.setOnClickListener(this);
    }

    @Override
    protected BasePresenter initPresenter() {
        mMusicPresenter = new MusicPresenter(this, this, mSeekBar);
        mProxy = (IMusicProxy) new BluetoothStateProxy(mMusicPresenter).bind();
        return mMusicPresenter;
    }

    @Override
    public int getLayout() {
        return R.layout.activity_music;
    }

    @Override
    public void onMediaPlay() {
        mBtnPlay.setText("暂停");
    }

    @Override
    public void updateSongInfo(SongInfo songinfo) {
        mTvSongName.setText(songinfo.getMediaTitle());
    }

    @Override
    public void updateSeekBar() {

    }

    @Override
    public void onUpdateDuration(String duration) {
        mTotalTime.setText(duration);
    }

    @Override
    public void onUpdateCurrTime(String currtime) {
        mCurrTime.setText(currtime);
    }

    @Override
    public void onMediaPause() {

    }

    @Override
    public void onClose() {
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_play_pause:
                mProxy.play();
                break;
            case R.id.btn_backward:
                //上一曲
                mProxy.backward();
                break;
            case R.id.btn_forward:
                //下一曲
                mProxy.forward();
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

    @Override
    protected void onResume() {
        super.onResume();
        mProxy.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        // TODO Auto-generated method stub
        if (v.getId() == R.id.btn_backward) {
            if (action == MotionEvent.ACTION_DOWN)
                mProxy.rewind(true);
            else if (action == MotionEvent.ACTION_UP)
                mProxy.rewind(false);
            return true;
        } else if (v.getId() == R.id.btn_forward) {
            if (action == MotionEvent.ACTION_DOWN)
                mProxy.fastForward(true);
            else if (action == MotionEvent.ACTION_UP)
                mProxy.fastForward(false);
            return true;
        }
        return false;
    }

}

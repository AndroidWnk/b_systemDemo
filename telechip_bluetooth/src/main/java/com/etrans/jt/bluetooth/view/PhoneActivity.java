package com.etrans.jt.bluetooth.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ActionMode;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.etrans.jt.bluetooth.BTApplication;
import com.etrans.jt.bluetooth.R;
import com.etrans.jt.bluetooth.adapter.QueryContactAdapter;
import com.etrans.jt.bluetooth.base.BaseActivity;
import com.etrans.jt.bluetooth.base.BasePresenter;
import com.etrans.jt.bluetooth.event.BaseEvent;
import com.etrans.jt.bluetooth.listener.proxy.IPhoneProxy;
import com.etrans.jt.bluetooth.listener.view.IPhoneView;
import com.etrans.jt.bluetooth.presenter.PhonePresenter;
import com.etrans.jt.bluetooth.proxy.BluetoothStateProxy;
import com.etrans.jt.bluetooth.utils.SpectrumSurfaceView;
import com.etrans.jt.bluetooth.utils.ToastFactory;
import com.etrans.jt.bluetooth.utils.XxCircleRotateView;
import com.etrans.jt.btlibrary.domin.ContactBean;
import com.etrans.jt.btlibrary.domin.SongInfo;
import com.etrans.jt.btlibrary.manager.XxBaseModule;
import com.etrans.jt.btlibrary.module.BluetoothMusicModule;
import com.etrans.jt.btlibrary.module.BluetoothPhoneBookModule;
import com.etrans.jt.btlibrary.module.BluetoothPhoneModule;
import com.etrans.jt.btlibrary.module.XxAudioManager;
import com.txznet.sdk.TXZCallManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import butterknife.ButterKnife;

import static com.etrans.jt.bluetooth.R.id.tv_download_count;

/**
 * 单元名称:PhoneActivity.java
 * Created by fuxiaolei on 2016/8/29.
 * 说明:
 * Last Change by fuxiaolei on 2016/8/29.
 */
public class PhoneActivity extends BaseActivity implements IPhoneView, View.OnClickListener, View.OnTouchListener, XxBaseModule.IUpdateUI {
    private static final String TAG = PhoneActivity.class.getSimpleName();
    TextView mTvDownloadCount;
    ImageView mIvnRef;
    EditText mEtPhone;
    Button mIvDial;
    Button mBtnRedial;
    ImageView mIvContact;
    TextView mTvTitle;
    ImageView mIvBack;
    ImageView mIvNum1;
    ImageView mIvNum2;
    ImageView mIvNum3;
    ImageView mIvNum4;
    ImageView mIvNum5;
    ImageView mIvNum6;
    ImageView mIvNum7;
    ImageView mIvNum8;
    ImageView mIvNum9;
    ImageView mIvNumX;
    ImageView mIvNum0;
    ImageView mIvNumJ;
    ImageView mIvClearNum;
    ListView mIvInputNumber;
    RelativeLayout mRlIcon;
    ViewPager mVpBluetooth;
    TextView mTvSongName;
    TextView mTvLblMediaAlbum;
    TextView mTvSongPlayer;
    ImageView mBtnPlayPause;
    Button mBtnStop;
    ImageView mBtnForward;
    ImageView mBtnBackward;
    ImageView mBtnRewind;
    Button mBtnFastForward;
    TextView mCurrTime;
    TextView mTotalTime;
    SeekBar mSeekBar;
    private PhonePresenter mPhonePresenter;
    private IPhoneProxy proxy;
    private QueryContactAdapter mAdapter;
    private View mBtearphoneView;
    private View mBtearMusicView;
    List<View> mListView = new ArrayList<View>();
    private ImageView mSelect1;
    private ImageView mSelect2;
    private int count;
    public static final String EXTRA_START_MUSIC = "startmusic";

    private GestureDetector gestureDetector;
    final int RIGHT = 0;
    final int LEFT = 1;
    private XxCircleRotateView mMusic_album;
    private SpectrumSurfaceView mSpectrumSurfaceView;
    private boolean isStart;
    private CmdReceiver cmdReceiver;
    private IntentFilter cmdIntentFilter;
    public static final String CMD_ACTION = "com.etrans.voicecCmd";
    private boolean isPaly = false;
    private boolean isDialed = false;

    private String BtMusicPlayAction = "ACTION_BTMUSIC_ISSHOWING";
    private String isBtMusicPlaying = "isBtMusicShow";
    private Intent intent;

    private PresskeyReceiver presskeyReceiver;
    private IntentFilter intentFilter;
    private boolean isResume = false;
    private List<ContactBean> mLstContact;
    private int srcW = 0, srcH = 0;

    public static final String A2DP_DISCONNECT = "bluetooth.a2dp.disconnect";
    public static final String A2DP_CONNECT = "bluetooth.a2dp.connect";

    @Override
    public void init() {
        initViewPager();
        initViewPhone();
        initViewMusic();
        initListener();
        initData();
        showQueryList(false);
        initTopView("");
        presskeyReceiver = new PresskeyReceiver();
        intentFilter = new IntentFilter();
        intentFilter.addAction("android.jt.media.control");
        intentFilter.addAction("com.foton.btmusic.stop");
        intentFilter.addAction("com.foton.btmusic.goon");
        intentFilter.addAction("com.foton.btTel");

        registerReceiver(presskeyReceiver, intentFilter);
        mVpBluetooth.setCurrentItem(getIntent().getIntExtra("pageIndex", 0));
    }

    private void initData() {

    }


    void checkMusic() {
        boolean isStartMusic = getIntent().getBooleanExtra(EXTRA_START_MUSIC, false);
        Log.e(TAG, "isStartMusic==" + isStartMusic);
        if (isStartMusic) {
            getIntent().putExtra(EXTRA_START_MUSIC, false);
            mVpBluetooth.setCurrentItem(1);
            proxy.play();
        }
    }


    void checkPhone() {
        String phoneNum = getIntent().getStringExtra("phoneNum");
        Log.e("phoneNum", phoneNum + "");
        if (!TextUtils.isEmpty(phoneNum) && !isDialed) {
            proxy.dial(phoneNum);
            isDialed = true;
        }
    }

    private void initListener() {
        mIvnRef.setOnClickListener(this);
        mIvDial.setOnClickListener(this);
        mBtnRedial.setOnClickListener(this);
        mIvContact.setOnClickListener(this);
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
        mIvClearNum.setOnClickListener(this);
        mBtnPlayPause.setOnClickListener(this);
        mTvDownloadCount.setOnClickListener(this);
//        mBtnStop.setOnClickListener(this);
        mBtnForward.setOnClickListener(this);
        mBtnBackward.setOnClickListener(this);
//        mBtnForward.setOnTouchListener(this);
//        mBtnBackward.setOnTouchListener(this);
        mIvClearNum.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mEtPhone.getText().toString().length() > 0) {
                    mEtPhone.setText("");
                    showQueryList(false);
                }
                return true;
            }
        });

        mEtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    showQueryList(false);
                } else {
                    mPhonePresenter.queryContact(mEtPhone.getText().toString());
                }
            }
        });
        mEtPhone.setLongClickable(false);
        mIvInputNumber.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ContactBean data = mAdapter.get(position);
                if (data == null) {
                    return;
                }
                if (data.getMobilePhone() == null) {
                    return;
                }
                mEtPhone.setText(data.getMobilePhone());
                proxy.dial(data.getMobilePhone());
                showQueryList(false);
            }
        });
    }

    private void initViewMusic() {
        mSeekBar = (SeekBar) mBtearMusicView.findViewById(R.id.seekBar);
        mSeekBar.setEnabled(false);
//        mTvLblMediaAlbum = (TextView) mBtearMusicView.findViewById(R.id.tv_lbl_mediaAlbum);
//        mTvSongPlayer = (TextView) mBtearMusicView.findViewById(R.id.tv_song_player);
        mCurrTime = (TextView) mBtearMusicView.findViewById(R.id.currTime);
        mTotalTime = (TextView) mBtearMusicView.findViewById(R.id.totalTime);
        mTvSongName = (TextView) mBtearMusicView.findViewById(R.id.tv_song_name);

        mBtnPlayPause = (ImageView) mBtearMusicView.findViewById(R.id.btn_play_pause);
//        mBtnStop = (Button) mBtearMusicView.findViewById(R.id.btn_stop);
        mBtnForward = (ImageView) mBtearMusicView.findViewById(R.id.btn_forward);
        mBtnBackward = (ImageView) mBtearMusicView.findViewById(R.id.btn_backward);
//        mBtnRewind = (Button) mBtearMusicView.findViewById(R.id.btn_rewind);
//        mBtnFastForward = (Button) mBtearMusicView.findViewById(R.id.btn_fast_forward);

        mMusic_album = (XxCircleRotateView) mBtearMusicView.findViewById(R.id.music_album);
        mMusic_album.setImageBitmap(
                BitmapFactory.decodeResource(this.getResources(), R.drawable.bg_musicalbum_default));
        mSpectrumSurfaceView = (SpectrumSurfaceView) mBtearMusicView.findViewById(R.id.waveform_view);
        mSpectrumSurfaceView.setRandom(true);
    }

    private void initViewPhone() {
        mIvNum0 = (ImageView) mBtearphoneView.findViewById(R.id.iv_num_0);
        mIvNum1 = (ImageView) mBtearphoneView.findViewById(R.id.iv_num_1);
        mIvNum2 = (ImageView) mBtearphoneView.findViewById(R.id.iv_num_2);
        mIvNum3 = (ImageView) mBtearphoneView.findViewById(R.id.iv_num_3);
        mIvNum4 = (ImageView) mBtearphoneView.findViewById(R.id.iv_num_4);
        mIvNum5 = (ImageView) mBtearphoneView.findViewById(R.id.iv_num_5);
        mIvNum6 = (ImageView) mBtearphoneView.findViewById(R.id.iv_num_6);
        mIvNum7 = (ImageView) mBtearphoneView.findViewById(R.id.iv_num_7);
        mIvNum8 = (ImageView) mBtearphoneView.findViewById(R.id.iv_num_8);
        mIvNum9 = (ImageView) mBtearphoneView.findViewById(R.id.iv_num_9);
        mIvNumX = (ImageView) mBtearphoneView.findViewById(R.id.iv_num_x);
        mIvNumJ = (ImageView) mBtearphoneView.findViewById(R.id.iv_num_j);

        mIvClearNum = (ImageView) mBtearphoneView.findViewById(R.id.iv_clear_num);
        mIvInputNumber = (ListView) mBtearphoneView.findViewById(R.id.iv_input_number);
        mRlIcon = (RelativeLayout) mBtearphoneView.findViewById(R.id.rl_icon);
        mIvnRef = (ImageView) mBtearphoneView.findViewById(R.id.iv_ref_contact);
        mEtPhone = (EditText) mBtearphoneView.findViewById(R.id.et_phone);
        mBtnRedial = (Button) mBtearphoneView.findViewById(R.id.btn_redial);
        mIvContact = (ImageView) mBtearphoneView.findViewById(R.id.iv_contact);
        mIvDial = (Button) mBtearphoneView.findViewById(R.id.iv_dial);

        mIvNum0.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                appendNum("+");
                return false;
            }
        });
        mIvDial.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    mIvDial.setBackgroundResource(R.drawable.dial_press);
                }else  if (event.getAction() == MotionEvent.ACTION_UP){
                    mIvDial.setBackgroundResource(R.drawable.dial);
                }
                return false;
            }
        });

    }

    private void initViewPager() {
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mVpBluetooth = (ViewPager) findViewById(R.id.vp_bluetooth);
        gestureDetector = new GestureDetector(this, onGestureListener);
        //选择页的点
        mSelect1 = (ImageView) findViewById(R.id.iv_select_1);
        mSelect2 = (ImageView) findViewById(R.id.iv_select_2);
        mTvDownloadCount = (TextView) findViewById(tv_download_count);


        LayoutInflater lf = getLayoutInflater().from(this);
        mBtearphoneView = lf.inflate(R.layout.activity_phone, null);
        mBtearMusicView = lf.inflate(R.layout.activity_music, null);
        mListView.add(mBtearphoneView);
        mListView.add(mBtearMusicView);
        mVpBluetooth.setAdapter(mPageAdapter);

        mVpBluetooth.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                BTApplication.pageIndex = i;
                if (i == 1) {
                    /*XxBBApplication app = (XxBBApplication) getApplicationContext();
                    app.bShowBtearMusic = true;
                    if (!TXZMusicManager.getInstance().isPlaying()){
                        XxAudioManager.getInstance().initRecv();
                    }*/
                    XxAudioManager.getInstance().initRecv();
                    mSelect1.setImageResource(R.drawable.around_slip_other);
                    mSelect2.setImageResource(R.drawable.around_slip_cur);
                } else {
                    /*XxBBApplication app = (XxBBApplication) getApplicationContext();
                    app.bShowBtearMusic = false;*/
                    mSelect1.setImageResource(R.drawable.around_slip_cur);
                    mSelect2.setImageResource(R.drawable.around_slip_other);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    @Override
    protected BasePresenter initPresenter() {
        mPhonePresenter = new PhonePresenter(this, this, mSeekBar);
        proxy = (IPhoneProxy) new BluetoothStateProxy(mPhonePresenter).bind();
        mPhonePresenter.getConnectedDeviceName();
        return mPhonePresenter;
    }

    @Override
    public int getLayout() {
        return R.layout.activity_bluetooth;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
//        boolean isStartMusic = getIntent().getBooleanExtra(EXTRA_START_MUSIC, false);
//        Log.e(TAG, "isStartMusic==" + isStartMusic);
        ButterKnife.bind(this);
        cmdReceiver = new CmdReceiver();
        cmdIntentFilter = new IntentFilter(CMD_ACTION);
        registerReceiver(cmdReceiver, cmdIntentFilter);
        EventBus.getDefault().register(this);
        Log.e("PhoneActivity","-------------onCreate-------");
    }

    XxPageAdapter mPageAdapter = new XxPageAdapter();
    private long clickTime;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        Log.d(TAG, "onTouch: action" + action);
        // TODO Auto-generated method stub
        if (v.getId() == R.id.btn_backward) {
            if (action == MotionEvent.ACTION_DOWN) {
                count = 0;
            } else if (action == MotionEvent.ACTION_UP) {
                proxy.rewind(false);
            } else if (action == MotionEvent.ACTION_MOVE) {
                //判断时间长短
                if (count > 10) {
                    proxy.rewind(true);
                } else {
                    proxy.backward();
                }

            }
            return true;
        } else if (v.getId() == R.id.btn_forward) {
            if (action == MotionEvent.ACTION_DOWN)
                proxy.fastForward(true);
            else if (action == MotionEvent.ACTION_UP)
                proxy.fastForward(false);
            return true;
        }
        return false;
    }

    class XxPageAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mListView.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mListView.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mListView.get(position));
            return mListView.get(position);
        }
    }


    //-----------------------------UI回调--------------------
    @Override
    public void onPhoneBookDownloadCount(String count) {
        mTvDownloadCount.setText(count);
    }

    @Override
    public void showQueryList(boolean isShow) {
        mIvInputNumber.setVisibility(isShow ? View.VISIBLE : View.GONE);
        mRlIcon.setVisibility(isShow ? View.GONE : View.VISIBLE);
//        mIvDial.setVisibility(isShow ? View.GONE : View.VISIBLE);
    }

    @Override
    public void notify(List<ContactBean> mLstContact) {
        Log.e("mLstContact",mLstContact.size()+"");
        if (mAdapter != null) {
            mAdapter.setData(mLstContact);
            mAdapter.notifyDataSetChanged();
        } else {
            mAdapter = new QueryContactAdapter(this);
            mAdapter.setData(mLstContact);
            mIvInputNumber.setAdapter(mAdapter);
        }
    }

    @Override
    public void updateSongInfo(SongInfo songinfo) {
        mTvSongName.setText(songinfo.getMediaTitle());
//        mTvSongPlayer.setText(songinfo.getMediaArtist());
//        mTvLblMediaAlbum.setText(songinfo.getMediaAlbum());
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
        isPaly = false;
        mBtnPlayPause.setImageResource(R.drawable.selector_bluetooth_music_play_nor);
//        sendBroadcast(new Intent("com.foton.btmusic.pause"));
    }

    @Override
    public void localFinish() {
        Log.e("====","==localFinish==");
        finish();
    }

    @Override
    public void onUpdateConnectDeviceName(String name) {
        mTvDownloadCount.setText(name);
        Log.e("mLstContact",name);
//        TXZCallManager.getInstance().setCallTool(new TXZCallManager.CallTool() {
//            @Override
//            public CallStatus getStatus() {
//                return null;
//            }
//
//            @Override
//            public boolean makeCall(TXZCallManager.Contact contact) {
//                return false;
//            }
//
//            @Override
//            public boolean acceptIncoming() {
//                return false;
//            }
//
//            @Override
//            public boolean rejectIncoming() {
//                return false;
//            }
//
//            @Override
//            public boolean hangupCall() {
//                return false;
//            }
//
//            @Override
//            public void setStatusListener(TXZCallManager.CallToolStatusListener callToolStatusListener) {
//
//            }
//        });

        searchContact("");
        /*if (!TextUtils.isEmpty(name)) {
            mTvDownloadCount.setText("已连接:" + name);
        } else {
            BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
            name = adapter.getName();
            mTvDownloadCount.setText("蓝牙名称:" + name);
        }*/
    }

    @Override
    public void startAnimation() {
        startAlbumRotate();
        mSpectrumSurfaceView.startDrawRandom();
    }

    @Override
    public void stopAnimation() {
        stopAlbumRotate();
        mSpectrumSurfaceView.stopDrawRandom();
    }

    @Override
    public void onClose() {
        finish();
    }

    @Override
    public void onMediaPlay() {
        isPaly = true;
        mBtnPlayPause.setImageResource(R.drawable.selector_bluetooth_music_pause_nor);
        sendBroadcast(new Intent("com.foton.btmusic.play"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_ref_contact:
                proxy.downloadPhoneBook();
                break;
            case R.id.iv_dial:
                String phoneNum = mEtPhone.getText().toString();
                if (!TextUtils.isEmpty(phoneNum)) {
                    proxy.dial(phoneNum);
                    mEtPhone.setText("");
//                    BluetoothPhoneModule.getInstance().dial(phoneNum);
                } else {
                    if (TextUtils.isEmpty(BluetoothPhoneModule.getInstance().getLastNumber())) {
                        ToastFactory.showToast(getApplicationContext(), getString(R.string.bt_enter_correct_number));
                    } else {
                        mEtPhone.setText(BluetoothPhoneModule.getInstance().getLastNumber());
                    }
                }
                break;
            case R.id.iv_contact:
                if (BluetoothPhoneBookModule.getInstance().queryContactAll() != null
                        && BluetoothPhoneBookModule.getInstance().queryContactAll().size() == 0) {
                    return;
                }
                if (BluetoothPhoneModule.getInstance().isDownload()) {
                    ToastFactory.showToast(getApplicationContext(), getString(R.string.bt_Donloading_wait));
                } else {
                    startActivity(new Intent(this, ContactActivity.class));
                }
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
            case R.id.iv_clear_num:
                delNum();
                break;
            case R.id.btn_play_pause:
                proxy.play();
                break;
            case R.id.btn_backward:
                //上一曲
                proxy.backward();
                break;
            case R.id.btn_forward:
                //下一曲
                proxy.forward();
                break;
            case R.id.tv_download_count:
                //蓝牙设置界面
//                startActivity(new Intent(PhoneActivity.this, BluetoothSettingActivity.class));
                break;
            case R.id.iv_back:
//                this.moveTaskToBack(true);
//                System.exit(0);
                Log.e("====","==backonclick==");
                finish();
                break;
            default:
                break;
        }
    }


    private void searchContact(String num) {
        ContactBean data = null;
        if (mLstContact == null) {
            mLstContact = new ArrayList<ContactBean>();
        }
        List<ContactBean> allContact = null;
        List<ContactBean> lstT9 = null;
        List<ContactBean> lstPy = null;
        List<ContactBean> hz = null;
        List<ContactBean> lstPhoneNumber = null;

        if (TextUtils.isEmpty(num)) {
            allContact = BluetoothPhoneBookModule.getInstance().queryContactAll();
        } else {
            lstT9 = BluetoothPhoneBookModule.getInstance().queryContactByInitial(
                    num);
            lstPy = BluetoothPhoneBookModule.getInstance().queryContactByPinyin(num);
//            allContact = BluetoothPhoneBookModule.getInstance().queryContactAll();
            hz = BluetoothPhoneBookModule.getInstance().queryContactListByName(num);

            lstPhoneNumber = BluetoothPhoneBookModule.getInstance().queryContactListByNum(num);
        }
        mLstContact.clear();
        HashSet<String> set = new HashSet<String>();
        if (num.length() == 0) {
            if (allContact != null) {
                for (int i = 0; i < allContact.size(); i++) {
                    ContactBean bean = allContact.get(i);
                    if (bean == null) {
                        continue;
                    }
                    String str = bean.getName() + bean.getMobilePhone();
                    if (set.contains(str))
                        continue;
                    set.add(str);
                    mLstContact.add(bean);
                }
            }
        } else if (num.length() > 0) {
            if (lstT9 != null) {
                for (int i = 0; i < lstT9.size(); i++) {
                    ContactBean bean = lstT9.get(i);
                    if (bean == null) {
                        continue;
                    }
                    String str = bean.getName() + bean.getMobilePhone();
                    if (set.contains(str))
                        continue;
                    set.add(str);
                    mLstContact.add(bean);
                }
            }

            if (lstPy != null) {
                for (int i = 0; i < lstPy.size(); i++) {
                    ContactBean bean = lstPy.get(i);
                    if (bean == null) {
                        continue;
                    }
                    String str = bean.getName() + bean.getMobilePhone();
                    if (set.contains(str))
                        continue;
                    set.add(str);
                    mLstContact.add(bean);
                }
            }

            if (hz != null) {
                for (int i = 0; i < hz.size(); i++) {
                    ContactBean bean = hz.get(i);
                    if (bean == null) {
                        continue;
                    }
                    String str = bean.getName() + bean.getMobilePhone();
                    if (set.contains(str))
                        continue;
                    set.add(str);
                    mLstContact.add(bean);
                }
            }

            if (lstPhoneNumber != null) {
                for (int i = 0; i < lstPhoneNumber.size(); i++) {
                    ContactBean bean = lstPhoneNumber.get(i);
                    if (bean == null) {
                        continue;
                    }
                    String str = bean.getName() + bean.getMobilePhone();
                    if (set.contains(str))
                        continue;
                    set.add(str);
                    mLstContact.add(bean);
                }
            }

        }
        if (mLstContact.size() > 0) {
            List<TXZCallManager.Contact> contacts = new ArrayList<TXZCallManager.Contact>();
            for (int i = 0; i < mLstContact.size(); i++) {
                ContactBean bean = mLstContact.get(i);
                TXZCallManager.Contact contact = new TXZCallManager.Contact();
                contact.setName(bean.getName());
                contact.setNumber(TextUtils.isEmpty(bean.getHomePhone())?bean.getMobilePhone():bean.getHomePhone());
                contacts.add(contact);
            }
            TXZCallManager.getInstance().syncContacts(contacts);
        } else {
//            ToastFactory.getToast(getApplicationContext(), "未搜索到结果!").show();
        }

    }

    private void delNum() {
        String num = mEtPhone.getText().toString();
        if (!TextUtils.isEmpty(num) && num.length() > 0) {
            String substring = num.substring(0, num.length() - 1);
            mEtPhone.setText(substring);
            mEtPhone.setSelection(substring.length());
        } else {
            mEtPhone.setText("");
            showQueryList(false);
        }

    }

    private void appendNum(String num) {
        mEtPhone.append(num);
        Log.d(TAG, "appendNum() called with: " + "num = [" + mEtPhone.getText().toString() + "]");
    }

    private ActionMode.Callback mCallback = new ActionMode.Callback() {

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            // TODO Auto-generated method stub
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            mode.finish();
            return true;
        }
    };

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();
        isResume = false;
        proxy.pause();
        Intent intent = new Intent(A2DP_DISCONNECT);
        sendBroadcast(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sendBroadcast(new Intent("com.foton.btmusic.play"));
        isResume = true;
        if (isDialed) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            },1000);
        }
        proxy.initPhoneService();
        proxy.resume();

        checkMusic();
        checkPhone();
        XxAudioManager.getInstance().initRecv();
        if (mPhonePresenter != null) {
            mPhonePresenter.getConnectedDeviceName();
        }
        SongInfo songInfo = BluetoothMusicModule.getInstance().getSongInfo();
        if (songInfo != null) {
            mTvSongName.setText(songInfo.getMediaTitle());
        }
        //判断是否在播放
        if (BluetoothMusicModule.getInstance().isBluetoothMusicPlaying()) {
            startAnimation();
        } else {
            stopAnimation();
        }

        intent = new Intent(BtMusicPlayAction);
        intent.putExtra(isBtMusicPlaying, true);
        sendBroadcast(intent);

        Intent a2dpIntent = new Intent(A2DP_CONNECT);
        sendBroadcast(a2dpIntent);
    }



    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
        proxy.destory();
        unregisterReceiver(cmdReceiver);
        unregisterReceiver(presskeyReceiver);
        EventBus.getDefault().unregister(this);

    }

    @Subscribe
    public void onEventMainThread(BaseEvent event) {
        if (event.getEventCode() == 102) {
            Log.e("onEventMainThread", event.getEventCode() + "");
//            if (isResume) {
//                proxy.initPhoneService();
//                if (!isPaly)
//                    proxy.play();
//                else
//                    proxy.resume();
//                checkMusic();
//                XxAudioManager.getInstance().initRecv();
//                if (mPhonePresenter != null) {
//                    mPhonePresenter.getConnectedDeviceName();
//                }
//                SongInfo songInfo = BluetoothMusicModule.getInstance().getSongInfo();
//                if (songInfo != null) {
//                    mTvSongName.setText(songInfo.getMediaTitle());
//                }
//                //判断是否在播放
//                if (BluetoothMusicModule.getInstance().isBluetoothMusicPlaying()) {
//                    startAnimation();
//                } else {
//                    stopAnimation();
//                }
//            }
        }
    }

    @Override
    public void onUpdateUI(Message msg) {
        refreshTopUI(msg);
    }

    @Override
    public void onBackPressed() {
//        this.moveTaskToBack(true);
        Log.e("====","==backphone==");
        finish();
    }

    private GestureDetector.OnGestureListener onGestureListener =
            new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                                       float velocityY) {
                    float x = e2.getX() - e1.getX();
                    float y = e2.getY() - e1.getY();

                    if (x > 200) {
                        doResult(RIGHT);
                    } else if (x < -200) {
                        doResult(LEFT);
                    }
                    return true;
                }
            };

    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    public void doResult(int action) {

        switch (action) {
            case RIGHT:
                System.out.println("go right");
                if (mVpBluetooth.getCurrentItem() == 0) {
                    return;
                }
                mVpBluetooth.setCurrentItem(0);
                break;

            case LEFT:
                if (mVpBluetooth.getCurrentItem() == 1) {
                    return;
                }
                mVpBluetooth.setCurrentItem(1);
                System.out.println("go left");
                break;

        }
    }

    private void startAlbumRotate() {
        if (isStart) {
            return;
        }
        mMusic_album.stop();
        mMusic_album.start();
        isStart = true;
    }


    private void stopAlbumRotate() {
        //mMusic_album.stopAnimation();
        mMusic_album.stop();
        isStart = false;
    }

    class CmdReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(CMD_ACTION)) {
                String cmd = intent.getStringExtra("cmd");
                if (!TextUtils.isEmpty(cmd)) {
                    if (cmd.equals("stopMusic")) {
                       new Handler().postDelayed(new Runnable() {
                           @Override
                           public void run() {
                               proxy.stop();
                               finish();
                           }
                       },1000);
                    } else if (cmd.equals("stopTel")) {
                        finish();
                    }
                }
            }
        }
    }

    class PresskeyReceiver extends BroadcastReceiver {

        private final static String TAG = "btpresskeyLog";
        private int volume_music = 0, volume_bt = 0, volume_radio = 0;
        private boolean isPlaying = false;

        @Override
        public void onReceive(final Context context, Intent intent) {
            String intentAction = intent.getAction();
            AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            Log.e(TAG, "intentAction==" + intentAction);
            if (intent.getAction().equals("android.jt.media.control")) {
                //改到中控
                int state = intent.getIntExtra("mediaAction", -1);
                Log.e(TAG, "state==" + state);
                if (state == 1) {
                    if (BluetoothMusicModule.getInstance().isBluetoothMusicPlaying()){
                        isPlaying = true;
                        proxy.play();
                    }
                    Log.e(TAG,"isPlaying:"+BluetoothMusicModule.getInstance().isBluetoothMusicPlaying());
                } else if (state == 2) {
                    if (isPlaying){
                        proxy.play();
                        isPlaying = false;
                    }
                }
            } else if (intent.getAction().equals("com.foton.btmusic.stop")) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        BluetoothMusicModule.getInstance().musicPause();
                    }
                },1000);
            } else if (intent.getAction().equals("com.foton.btmusic.goon")) {
                BluetoothMusicModule.getInstance().play();
            } else if (intent.getAction().equals("com.foton.btTel")) {
                String phoneNumber = intent.getStringExtra("phoneNum");
                Log.e(TAG, "phoneNumber==" + phoneNumber);
                if (!TextUtils.isEmpty(phoneNumber)) {
                    proxy.dial(phoneNumber);
                }
            }
        }
    }

}

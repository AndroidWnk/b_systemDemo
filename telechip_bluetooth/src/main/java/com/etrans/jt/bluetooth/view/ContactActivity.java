package com.etrans.jt.bluetooth.view;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.text.InputType;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.etrans.jt.bluetooth.R;
import com.etrans.jt.bluetooth.adapter.ContactAdapter;
import com.etrans.jt.bluetooth.base.BaseActivity;
import com.etrans.jt.bluetooth.base.BasePresenter;
import com.etrans.jt.bluetooth.utils.ToastFactory;
import com.etrans.jt.btlibrary.domin.ContactBean;
import com.etrans.jt.btlibrary.manager.XxBaseModule;
import com.etrans.jt.btlibrary.module.BluetoothPhoneBookModule;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 单元名称:ContactActivity.java
 * Created by fuxiaolei on 2016/8/30.
 * 说明:
 * Last Change by fuxiaolei on 2016/8/30.
 */
public class ContactActivity extends BaseActivity implements View.OnClickListener, XxBaseModule.IUpdateUI {
    @Bind(R.id.et_contact_search)
    EditText mEtContactSearch;
    @Bind(R.id.lv_contact_search)
    ListView mLvContactSearch;
    @Bind(R.id.iv_back)
    ImageView mIvBack;
    @Bind(R.id.search_btn)
    Button button;
    private List<ContactBean> mLstContact;
    private ContactAdapter mAdapter;

    @Override
    public void init() {
        searchContact("");
        mIvBack.setOnClickListener(this);
        mEtContactSearch.setOnClickListener(this);
        button.setOnClickListener(this);
        mLvContactSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    mEtContactSearch.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mEtContactSearch.getWindowToken(), 0);
                }
                return false;
            }
        });
       /* mEtContactSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchContact(mEtContactSearch.getText().toString().trim());
            }
        });*/
        /**
         * 解决键盘不能调起的问题
         */
        mEtContactSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                WindowManager.LayoutParams params = getWindow().getAttributes();
                if (event.getAction() == MotionEvent.ACTION_UP &&
                        params.softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE) {
                    mEtContactSearch.setFocusable(true);
                    mEtContactSearch.setFocusableInTouchMode(true);
                    mEtContactSearch.requestFocus();
                    InputMethodManager imm = (InputMethodManager) ContactActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return true;
            }

        });
        mEtContactSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // 先隐藏键盘
                    ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                    searchContact(mEtContactSearch.getText().toString().trim());
                }
                return false;
            }
        });
//        initTopView("联系人");
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
            notifyData();
        } else {
            notifyData();
//            ToastFactory.getToast(getApplicationContext(), "未搜索到结果!").show();
            ToastFactory.getToast(getApplicationContext(), getResources().getString(R.string.no_results_were_found)).show();
        }

    }

    private void notifyData() {
        if (mAdapter != null) {
            mAdapter.setData(mLstContact);
        } else {
            mAdapter = new ContactAdapter(this);
            mAdapter.setData(mLstContact);
            mLvContactSearch.setAdapter(mAdapter);
        }
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    public int getLayout() {
        return R.layout.activity_contact;
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
            case R.id.search_btn:
                String searchStr = mEtContactSearch.getText().toString().trim();
                if (!searchStr.isEmpty()) {
                    // 先隐藏键盘
                    ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                    searchContact(mEtContactSearch.getText().toString().trim());
                }
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.et_contact_search:
//                mEtContactSearch.setText("");
//                mEtContactSearch.requestFocus();
//                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
                break;
            default:
                break;
        }
    }

    @Override
    public void onUpdateUI(Message msg) {
//        refreshTopUI(msg);
    }
}

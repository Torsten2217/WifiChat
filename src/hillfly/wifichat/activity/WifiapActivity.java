package hillfly.wifichat.activity;

import hillfly.wifichat.BaseActivity;
import hillfly.wifichat.BaseDialog;
import hillfly.wifichat.activity.wifiap.WifiApConst;
import hillfly.wifichat.activity.wifiap.WifiapBroadcast;
import hillfly.wifichat.activity.wifiap.WifiapBroadcast.NetWorkChangeListener;
import hillfly.wifichat.adapter.WifiapAdapter;
import hillfly.wifichat.bean.Users;
import hillfly.wifichat.dialog.ConnWifiDialog;
import hillfly.wifichat.sql.SqlDBOperate;
import hillfly.wifichat.sql.UserInfo;
import hillfly.wifichat.util.DateUtils;
import hillfly.wifichat.util.LogUtils;
import hillfly.wifichat.util.SessionUtils;
import hillfly.wifichat.util.SharePreferenceUtils;
import hillfly.wifichat.util.TextUtils;
import hillfly.wifichat.util.WifiUtils;
import hillfly.wifichat.util.WifiUtils.WifiCipherType;

import java.util.ArrayList;
import java.util.List;

import hillfly.wifichat.R;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @fileName WifiapActivity.java
 * @description 
 * @author _Hill3
 */
public class WifiapActivity extends BaseActivity implements OnClickListener, NetWorkChangeListener,
        OnScrollListener, OnItemClickListener {

    private static final String TAG = "SZU_WifiapActivity";

    private String localIPaddress; // local WifiIP
    private String serverIPaddres; // server IP

    private ApHandler mHandler;
    private SearchWifiThread mSearchWifiThread;
    private ArrayList<ScanResult> mWifiList; // 
    private BaseDialog mHintDialog; // 
    private ConnWifiDialog mConnWifiDialog; // 
    private WifiapAdapter mWifiApAdapter;
    private UserInfo mUserInfo; // 
    private SqlDBOperate mSqlDBOperate;// 
    private WifiapBroadcast mWifiapBroadcast;

    private LinearLayout mLlApInfo;
    private TextView mTvStatusInfo;
    private TextView mTvApSSID;
    private ListView mLvWifiList;
    private Button mBtnBack;
    private Button mBtnCreateAp;
    private Button mBtnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifiap);
        initBroadcast(); // 
        initViews();
        initEvents();
        initAction();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mWifiapBroadcast); // 
        mSearchWifiThread.stop();
        mSearchWifiThread = null;
        super.onDestroy();
    }

    /**  */
    public void initBroadcast() {
        mWifiapBroadcast = new WifiapBroadcast(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.setPriority(Integer.MAX_VALUE);
        registerReceiver(mWifiapBroadcast, filter);
    }

    /**  **/
    protected void initViews() {
        mLlApInfo = (LinearLayout) findViewById(R.id.wifiap_lv_create_ok);
        mTvStatusInfo = (TextView) findViewById(R.id.wifiap_tv_wifistatus);
        mTvApSSID = (TextView) findViewById(R.id.wifiap_tv_createap_ssid);
        mLvWifiList = (ListView) findViewById(R.id.wifiap_lv_wifi);
        mBtnBack = (Button) findViewById(R.id.wifiap_btn_back);
        mBtnCreateAp = (Button) findViewById(R.id.wifiap_btn_createap);
        mBtnNext = (Button) findViewById(R.id.wifiap_btn_next);
    }

    /**  **/
    @Override
    protected void initEvents() {
        mWifiList = new ArrayList<ScanResult>();
        mWifiApAdapter = new WifiapAdapter(this, mWifiList);
        mLvWifiList.setAdapter(mWifiApAdapter);

        hintDialogOnClick hintClick = new hintDialogOnClick();

        mHintDialog = BaseDialog.getDialog(WifiapActivity.this, R.string.dialog_tips, "",
                getString(R.string.btn_yes), hintClick, getString(R.string.btn_cancel), hintClick);

        mHandler = new ApHandler();
        mConnWifiDialog = new ConnWifiDialog(this, mHandler);
        mSearchWifiThread = new SearchWifiThread(mHandler);
        mLvWifiList.setOnScrollListener(this);
        mLvWifiList.setOnItemClickListener(this);
        mBtnCreateAp.setOnClickListener(this);
        mBtnBack.setOnClickListener(this);
        mBtnNext.setOnClickListener(this);
    }

    /**  **/
    protected void initAction() {

        if (!WifiUtils.isWifiConnect() && !WifiUtils.isWifiApEnabled()) { // WIFI
            WifiUtils.OpenWifi();
        }

        if (WifiUtils.isWifiConnect()) { // Wifi already connected
            mTvStatusInfo.setText(getString(R.string.wifiap_text_wifi_connected)
                    + WifiUtils.getSSID());

        }

        if (WifiUtils.isWifiApEnabled()) { // already run server
            if (WifiUtils.getApSSID().startsWith(WifiApConst.WIFI_AP_HEADER)) {
                mTvStatusInfo.setText(getString(R.string.wifiap_text_ap_1));
                mLvWifiList.setVisibility(View.GONE);
                mLlApInfo.setVisibility(View.VISIBLE);
                mTvApSSID.setText("SSID: " + WifiUtils.getApSSID());
                mBtnCreateAp.setText(getString(R.string.wifiap_btn_closeap));
            }
            else {
                WifiUtils.closeWifiAp();
                WifiUtils.OpenWifi();
                mTvStatusInfo.setText(getString(R.string.wifiap_text_wifi_1_0));
            }
        }

        if (WifiUtils.isWifiEnabled() && !WifiUtils.isWifiConnect()) { // Wifi already run and not connected
            mTvStatusInfo.setText(getString(R.string.wifiap_text_wifi_1_0));
        }

        mSearchWifiThread.start();
    }

    private void getWifiList() {
        mWifiList.clear();
        WifiUtils.startScan();
        List<ScanResult> scanResults = WifiUtils.getScanResults();
        mWifiList.addAll(scanResults);

        // int size = scanResults.size();
        // for (int i = 0; i < size; ++i) {
        // ScanResult ap = scanResults.get(i);
        // String apSSID = ap.SSID;
        // if (apSSID.startsWith(WifiApConst.WIFI_AP_HEADER) &&
        // !mWifiList.contains(ap)) {
        // mWifiList.add(ap);
        // }

        // }
    }

    /**
     * 
     * 
     * <p>
     * BuildBRAND  ； BuildMODEL version
     * </p>
     * 
     * @return 
     */
    public String getLocalHostName() {
        String str1 = Build.BRAND;
        String str2 = TextUtils.getRandomNumStr(3);
        return str1 + "_" + str2;
    }

    public String getPhoneModel() {
        String str1 = Build.BRAND;
        String str2 = Build.MODEL;
        str2 = str1 + "_" + str2;
        return str2;
    }

    /**
     * UI
     * 
     * @param list
     */
    public void refreshAdapter(List<ScanResult> list) {
        mWifiApAdapter.setData(list);
        mWifiApAdapter.notifyDataSetChanged();
    }

    /**
     * IP
     * 
     * @param isClient
     *            
     */
    public void setIPaddress() {
        if (WifiUtils.isWifiApEnabled()) {
            serverIPaddres = localIPaddress = "192.168.43.1";
        }
        else {
            localIPaddress = WifiUtils.getLocalIPAddress();
            serverIPaddres = WifiUtils.getServerIPAddress();
        }
        LogUtils.i(TAG, "localIPaddress:" + localIPaddress + " serverIPaddres:" + serverIPaddres);
    }

    /**
     * IP
     * 
     * @return boolean 
     */
    private boolean isValidated() {

        setIPaddress();
        String nullIP = "0.0.0.0";

        if (nullIP.equals(localIPaddress) || nullIP.equals(serverIPaddres)
                || localIPaddress == null || serverIPaddres == null) {
            showShortToast(R.string.wifiap_toast_connectap_unavailable);
            return false;
        }

        return true;
    }

    /**  **/
    private void doLogin() {
        if (!isValidated()) {
            return;
        }
        putAsyncTask(new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                showLoadingDialog(getString(R.string.wifiap_dialog_login_saveInfo));
            }

            @Override
            protected Boolean doInBackground(Void... params) {
                try {
                    mSqlDBOperate = new SqlDBOperate(mContext);

                    String IMEI = SessionUtils.getIMEI();
                    String nickname = SessionUtils.getNickname();
                    String gender = SessionUtils.getGender();
                    String constellation = SessionUtils.getConstellation();
                    String device = getPhoneModel();
                    int age = SessionUtils.getAge();
                    int avatar = SessionUtils.getAvatar();
                    int onlineStateInt = SessionUtils.getOnlineStateInt();

                    String logintime = DateUtils.getNowtime();

                    // 
                    // 
                    if ((mUserInfo = mSqlDBOperate.getUserInfoByIMEI(IMEI)) != null) {
                        mUserInfo.setIPAddr(localIPaddress);
                        mUserInfo.setAvater(avatar);
                        mUserInfo.setOnlineState(onlineStateInt);
                        mUserInfo.setName(nickname);
                        mUserInfo.setSex(gender);
                        mUserInfo.setAge(age);
                        mUserInfo.setDevice(device);
                        mUserInfo.setConstellation(constellation);
                        mUserInfo.setLastDate(logintime);
                        mSqlDBOperate.updateUserInfo(mUserInfo);
                    }
                    else {
                        mUserInfo = new UserInfo(nickname, age, gender, IMEI, localIPaddress,
                                onlineStateInt, avatar);
                        mUserInfo.setLastDate(logintime);
                        mUserInfo.setDevice(device);
                        mUserInfo.setConstellation(constellation);
                        mSqlDBOperate.addUserInfo(mUserInfo);
                    }

                    int usserID = mSqlDBOperate.getIDByIMEI(IMEI); // id
                    // Session
                    SessionUtils.setLocalUserID(usserID);
                    SessionUtils.setDevice(device);
                    SessionUtils.setIsClient(!WifiUtils.isWifiApEnabled());
                    SessionUtils.setLocalIPaddress(localIPaddress);
                    SessionUtils.setServerIPaddress(serverIPaddres);
                    SessionUtils.setLoginTime(logintime);

                    // 
                    SharePreferenceUtils mSPutUtils = new SharePreferenceUtils();
                    SharedPreferences.Editor mEditor = mSPutUtils.getEditor();
                    mEditor.putString(Users.IMEI, IMEI).putString(Users.DEVICE, device)
                            .putString(Users.NICKNAME, nickname).putString(Users.GENDER, gender)
                            .putInt(Users.AVATAR, avatar).putInt(Users.AGE, age)
                            .putString(Users.BIRTHDAY, SessionUtils.getBirthday())
                            .putInt(Users.ONLINESTATEINT, onlineStateInt)
                            .putString(Users.CONSTELLATION, constellation)
                            .putString(Users.LOGINTIME, logintime);
                    mEditor.commit();
                    return true;
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    if (null != mSqlDBOperate) {
                        mSqlDBOperate.close();
                        mSqlDBOperate = null;
                    }
                }
                return false;
            }

            @Override
            protected void onPostExecute(Boolean result) {
                super.onPostExecute(result);
                dismissLoadingDialog();
                if (result) {
                    startActivity(MainTabActivity.class);
                    finish();
                }
                else {
                    showShortToast("The operation fails. Check whether the network is normal.");
                }
            }
        });
    }

    /**  **/
    @Override
    public void onClick(View v) {

        switch (v.getId()) {

        // 
            case R.id.wifiap_btn_createap:

                // 
                if (WifiUtils.getWifiApStateInt() == 4) {
                    showShortToast(R.string.wifiap_dialog_createap_nonsupport);
                    return;
                }

                // 
                if (WifiUtils.isWifiEnabled()) {
                    mHintDialog
                            .setMessage(getString(R.string.wifiap_dialog_createap_closewifi_confirm));
                    mHintDialog.show();
                    return;
                }

                // 
                if (((WifiUtils.getWifiApStateInt() == 3) || (WifiUtils.getWifiApStateInt() == 13))
                        && (WifiUtils.getApSSID().startsWith(WifiApConst.WIFI_AP_HEADER))) {
                    mHintDialog.setMessage(getString(R.string.wifiap_dialog_closeap_confirm));
                    mHintDialog.show();
                    return;
                }

                mHintDialog
                        .setMessage(getString(R.string.wifiap_dialog_createap_closewifi_confirm));
                mHintDialog.show();
                return;

                // 
            case R.id.wifiap_btn_back:
                if (mHintDialog.isShowing()) {
                    mHintDialog.dismiss();
                }
                finish();
                break;

            // 
            case R.id.wifiap_btn_next:
                if (mHintDialog.isShowing()) {
                    mHintDialog.dismiss();
                }
                doLogin();
                break;

        }
    }

    private class ApHandler extends Handler {

        private boolean isRespond = true;

        public ApHandler() {
        }

        public void setRespondFlag(boolean flag) {
            isRespond = flag;
        }

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case WifiApConst.ApScanResult: // 
                    if (isRespond) {
                        getWifiList();
                        refreshAdapter(mWifiList);
                    }
                    break;

                case WifiApConst.ApCreateApSuccess: // 
                    mSearchWifiThread.stop();
                    mTvStatusInfo.setText(getString(R.string.wifiap_text_createap_succeed));
                    mLvWifiList.setVisibility(View.GONE);
                    mLlApInfo.setVisibility(View.VISIBLE);
                    mTvApSSID.setText("SSID: " + WifiUtils.getApSSID());
                    mBtnCreateAp.setText(getString(R.string.wifiap_btn_closeap));
                    mBtnBack.setClickable(true);
                    mBtnCreateAp.setClickable(true);
                    mBtnNext.setClickable(true);
                    break;

                case WifiApConst.WiFiConnectSuccess: // 
                    String str = getString(R.string.wifiap_text_wifi_connected)
                            + WifiUtils.getSSID();
                    mTvStatusInfo.setText(str);
                    showShortToast(str);
                    break;

                case WifiApConst.WiFiConnectError: // 
                    showShortToast(R.string.wifiap_toast_connectap_error);
                    break;

                case WifiApConst.NetworkChanged: // Wifi
                    if (WifiUtils.isWifiEnabled()) {
                        mTvStatusInfo.setText(getString(R.string.wifiap_text_wifi_1_0));
                    }
                    else {
                        mTvStatusInfo.setText(getString(R.string.wifiap_text_wifi_0));
                        showShortToast(R.string.wifiap_text_wifi_disconnect);
                    }

                default:
                    break;
            }
        }
    }

    /**
     * Wifi
     */
    class SearchWifiThread implements Runnable {
        private boolean running = false;
        private Thread thread = null;
        private Handler handler = null;

        SearchWifiThread(Handler handler) {
            this.handler = handler;
        }

        public void run() {
            while (!WifiUtils.isWifiApEnabled()) {
                if (!this.running)
                    return;
                try {
                    Thread.sleep(2000); // 
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(WifiApConst.ApScanResult);
            }
        }

        public void start() {
            try {
                this.thread = new Thread(this);
                this.running = true;
                this.thread.start();
            }
            finally {
            }
        }

        public void stop() {
            try {
                this.running = false;
                this.thread = null;
            }
            finally {
            }
        }
    }

    @Override
    public void WifiConnected() {
        mHandler.sendEmptyMessage(WifiApConst.WiFiConnectSuccess);

    }

    @Override
    public void wifiStatusChange() {
        mHandler.sendEmptyMessage(WifiApConst.NetworkChanged);

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch (scrollState) {
            case OnScrollListener.SCROLL_STATE_IDLE:
                mHandler.setRespondFlag(true);
                break;
            case OnScrollListener.SCROLL_STATE_FLING:
                mHandler.setRespondFlag(false); // 
                break;
            case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                mHandler.setRespondFlag(false); // 
                break;
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
            int totalItemCount) {
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        ScanResult ap = mWifiList.get(position);
        if (ap.SSID.startsWith(WifiApConst.WIFI_AP_HEADER)) {
            mTvStatusInfo.setText(getString(R.string.wifiap_btn_connecting) + ap.SSID);
            // 
            boolean connFlag = WifiUtils.connectWifi(ap.SSID, WifiApConst.WIFI_AP_PASSWORD,
                    WifiCipherType.WIFICIPHER_WPA);
            if (!connFlag) {
                mTvStatusInfo.setText(getString(R.string.wifiap_toast_connectap_error_1));
                mHandler.sendEmptyMessage(WifiApConst.WiFiConnectError);
            }
        }
        else if (!WifiUtils.isWifiConnect() || !ap.BSSID.equals(WifiUtils.getBSSID())) {
            mConnWifiDialog.setTitle(ap.SSID);
            mConnWifiDialog.setScanResult(ap);
            mConnWifiDialog.show();
        }
    }

    public class hintDialogOnClick implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface hintDialog, int which) {
            switch (which) {

            // 
                case 0:
                    hintDialog.dismiss();
                    if (WifiUtils.isWifiApEnabled()) {

                        // 
                        WifiUtils.closeWifiAp();
                        WifiUtils.OpenWifi();

                        showShortToast(R.string.wifiap_text_ap_0);
                        mTvStatusInfo.setText(getString(R.string.wifiap_text_wifi_1_0));
                        mBtnCreateAp.setText(getString(R.string.wifiap_btn_createap));
                        mLlApInfo.setVisibility(View.GONE);
                        mLvWifiList.setVisibility(View.VISIBLE);

                        localIPaddress = null;
                        serverIPaddres = null;

                        mSearchWifiThread.start();
                    }
                    else {
                        // 
                        mTvStatusInfo.setText(getString(R.string.wifiap_text_createap_creating));
                        mBtnBack.setClickable(false);
                        mBtnCreateAp.setClickable(false);
                        mBtnNext.setClickable(false);
                        WifiUtils.startWifiAp(WifiApConst.WIFI_AP_HEADER + getLocalHostName(),
                                WifiApConst.WIFI_AP_PASSWORD, mHandler);
                    }
                    break;

                // 
                case 1:
                    hintDialog.cancel();
                    break;
            }
        }

    }
}

﻿package hillfly.wifichat;

import hillfly.wifichat.dialog.FlippingLoadingDialog;
import hillfly.wifichat.socket.udp.UDPMessageListener;
import hillfly.wifichat.view.HandyTextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

public abstract class BaseActivity extends FragmentActivity {

    protected Context mContext;
    protected ActionBar mActionBar;
    protected UDPMessageListener mUDPListener;
    protected FlippingLoadingDialog mLoadingDialog;

    /**
     * Screen width height,and density
     */
    protected int mScreenWidth;
    protected int mScreenHeight;
    protected float mDensity;

    protected List<AsyncTask<Void, Void, Boolean>> mAsyncTasks = new ArrayList<AsyncTask<Void, Void, Boolean>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getApplicationContext();
        mLoadingDialog = new FlippingLoadingDialog(this, getString(R.string.dialog_loading));

        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        mScreenWidth = metric.widthPixels;
        mScreenHeight = metric.heightPixels;
        mDensity = metric.density;

        ActivitiesManager.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearAsyncTask();
        ActivitiesManager.finishActivity(this);

    }

    @Override
    public void finish() {
        super.finish();
    }

    /** Initiate View **/
    protected abstract void initViews();

    /** Initiate the event **/
    protected abstract void initEvents();

    protected void putAsyncTask(AsyncTask<Void, Void, Boolean> asyncTask) {
        mAsyncTasks.add(asyncTask.execute());
    }

    /** Clear the asynchronous event **/
    protected void clearAsyncTask() {
        Iterator<AsyncTask<Void, Void, Boolean>> iterator = mAsyncTasks.iterator();
        while (iterator.hasNext()) {
            AsyncTask<Void, Void, Boolean> asyncTask = iterator.next();
            if (asyncTask != null && !asyncTask.isCancelled()) {
                asyncTask.cancel(true);
            }
        }
        mAsyncTasks.clear();
    }

    protected void showLoadingDialog(String text) {
        if (text != null) {
            mLoadingDialog.setText(text);
        }
        mLoadingDialog.show();
    }

    protected void dismissLoadingDialog() {
        if (mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

    /** Display Toast from resId) **/
    protected void showShortToast(int resId) {
        Toast.makeText(this, getString(resId), Toast.LENGTH_SHORT).show();
    }

    /** Display Toast from String) **/
    protected void showShortToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    /** 长时间显示Toast提示(来自res) **/
    protected void showLongToast(int resId) {
        Toast.makeText(this, getString(resId), Toast.LENGTH_LONG).show();
    }

    /** Display Toast from String) long time **/
    protected void showLongToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    /** Display Custom Toast from resId) **/
    protected void showCustomToast(int resId) {
        View toastRoot = LayoutInflater.from(BaseActivity.this)
                .inflate(R.layout.common_toast, null);
        ((HandyTextView) toastRoot.findViewById(R.id.toast_text)).setText(getString(resId));
        Toast toast = new Toast(BaseActivity.this);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(toastRoot);
        toast.show();
    }

    /** Display Custom Toast from String) **/
    protected void showCustomToast(String text) {
        View toastRoot = LayoutInflater.from(BaseActivity.this)
                .inflate(R.layout.common_toast, null);
        ((HandyTextView) toastRoot.findViewById(R.id.toast_text)).setText(text);
        Toast toast = new Toast(BaseActivity.this);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(toastRoot);
        toast.show();
    }

   
    protected void startActivity(Class<?> cls) {
        startActivity(cls, null);
    }

    
    protected void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    
    protected void startActivity(String action) {
        startActivity(action, null);
    }

   
    protected void startActivity(String action, Bundle bundle) {
        Intent intent = new Intent();
        intent.setAction(action);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    
    protected AlertDialog showAlertDialog(String title, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle(title).setMessage(message)
                .show();
        return alertDialog;
    }

    /** 含有标题、内容、两个按钮的对话框 **/
    protected AlertDialog showAlertDialog(String title, String message, String positiveText,
            DialogInterface.OnClickListener onPositiveClickListener, String negativeText,
            DialogInterface.OnClickListener onNegativeClickListener) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle(title).setMessage(message)
                .setPositiveButton(positiveText, onPositiveClickListener)
                .setNegativeButton(negativeText, onNegativeClickListener).show();
        return alertDialog;
    }

    /** 含有标题、内容、图标、两个按钮的对话框 **/
    protected AlertDialog showAlertDialog(String title, String message, int icon,
            String positiveText, DialogInterface.OnClickListener onPositiveClickListener,
            String negativeText, DialogInterface.OnClickListener onNegativeClickListener) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle(title).setMessage(message)
                .setIcon(icon).setPositiveButton(positiveText, onPositiveClickListener)
                .setNegativeButton(negativeText, onNegativeClickListener).show();
        return alertDialog;
    }
}

package com.zlk.bigdemo.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zlk.bigdemo.R;

import butterknife.ButterKnife;


public class BaseActivity extends AppCompatActivity{

    private ProgressDialog mProgressDialog;

    protected View mBack;

    public TextView mDialogTitle;
    public TextView mDialogContent;
    public EditText mDialogEditTextContent;
    public ListView mDialogListViewContent;
    public Button mDialogYes;
    public Button mDialogNo;
    public TextView mDeleteGroupItem1;
    public TextView mDeleteGroupItem2;
    public LinearLayout mButtonRela;
    public LinearLayout delete_down;
    public LinearLayout mDeleteLinear;
    public ImageView mDialogLineLong;
    public ImageView mDialogLineShu;
    public RelativeLayout rlativeLayout;
    public CheckBox btn_delete_local_down;
    public Button mNote;
    public Button mCal;
    public Button mCopy;
    public Button mCancle;
    public LinearLayout mChatLinear;

    public static interface OnActivityResultListener {

        public void onActivityResult(int requestCode, int resultCode, Intent data);
    }

    protected void invokeOnActivityResult(Fragment fragment, int requestCode,
                                          int resultCode, Intent data) {
        if (fragment instanceof OnActivityResultListener) {
            OnActivityResultListener listener = (OnActivityResultListener) fragment;
            listener.onActivityResult(requestCode, resultCode, data);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    protected void showSoftKeyBoard(Activity avitvity, View view) {
        InputMethodManager imm = (InputMethodManager) avitvity.getSystemService(INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.RESULT_SHOWN);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    protected void hideKeyBoard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    protected void showProgressDialog() {
        showProgressDialog(getString(R.string.fr_loading));
    }

    protected void showProgressDialog(String info) {
        showProgressDialog(info, true);
    }

    protected void showProgressDialog(String info, boolean cancelable) {
        showProgressDialog("", info, cancelable);
    }

    protected void showProgressDialog(String title, String info,
                                      boolean cancelable) {
        if (!isFinishing()) {
            if (mProgressDialog == null) {
                mProgressDialog = ProgressDialog.show(this, title, info, true,
                        cancelable, new OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialogInterface) {
                                mProgressDialog.dismiss();
                            }
                        });
            } else {
                if (!mProgressDialog.isShowing()) {
                    mProgressDialog.show();
                }
            }

        }
    }



    protected void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    public void showToast(String content) {
        Toast toast = Toast.makeText(this, content, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void showToast(String content, Context context) {
        Toast toast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public void showLongToast(String content) {
        Toast toast = Toast.makeText(this, content, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

}

package com.zlk.bigdemo.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.zlk.bigdemo.R;


public abstract class BaseFragment extends Fragment {
	
	private ProgressDialog mProgressDialog;

	protected void hideKeyBoard() {
		View view = getActivity().getCurrentFocus();
		if (view != null) {
			((InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE))
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
		if (!getActivity().isFinishing()) {
			if (mProgressDialog == null) {
				mProgressDialog = ProgressDialog.show(getActivity(), title, info, true,
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
        Toast toast = Toast.makeText(getActivity(), content, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public void showLongToast(String content) {
        Toast toast = Toast.makeText(getActivity(), content, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}

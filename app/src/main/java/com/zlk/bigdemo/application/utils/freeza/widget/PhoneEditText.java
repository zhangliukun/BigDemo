package com.zlk.bigdemo.application.utils.freeza.widget;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

public class PhoneEditText extends EditText{

	private boolean isChinaCode = true;
	
	public PhoneEditText(Context context) {
		super(context);
		init();
	}

	public PhoneEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public PhoneEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	private void init(){
		addTextChangedListener(new MyTextWatch());
	}
	
	public void setChinaCode(boolean chinaCode){
		isChinaCode = chinaCode;
	}
	
	class MyTextWatch implements TextWatcher {

		String oldStr;
		int selecttion;
		private boolean isSetText;

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			if(!isChinaCode){
				return;
			}
			if (!isSetText) {
				oldStr = s.toString();
			}
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			if(!isChinaCode){
				return;
			}
			if (!isSetText) {
				selecttion = start;
			}
		}

		@Override
		public void afterTextChanged(Editable s) {
			if(!isChinaCode){
				return;
			}
			String newStr = s.toString();
			int len = newStr.length();
			int oldLen = oldStr.length();
			if (isSetText) {
				isSetText = false;

				if (len > oldLen) {
					if (selecttion == 3 && len >= 5 || selecttion == 8
							&& len >= 10) {
						setSelection(selecttion + 2);
					} else {
						setSelection(selecttion + 1);
					}
				} else {
					if (selecttion > len) {
						setSelection(len);
					} else {
						setSelection(selecttion);
					}
				}
				return;
			}
			
			String str = getPhoneNumber(newStr);
			len = str.length();
			if (len > 3) {
				str = str.substring(0, 3) + " "
						+ str.substring(3, str.length());
			}
			if (str.length() > 8) {
				str = str.substring(0, 8) + " "
						+ str.substring(8, str.length());
			}
			isSetText = true;
			setText(str);
		}
	}
	
	public String getPhoneNumber(){
		String number = getText().toString();
		if(isChinaCode){
			number = getPhoneNumber(number);
		}
		return number;
	}
	
	private String getPhoneNumber(String number){
		String[] strArray = number.split(" ");
		StringBuilder builder = new StringBuilder();
		for (String str : strArray) {
			builder.append(str);
		}
		String str = builder.toString();
		return str;
	}
	
	public String getWholePhoneNumber(String code){
		String number = getText().toString();
		if(TextUtils.isEmpty(number)){
			return null;
		}
		if(isChinaCode){
			number = getPhoneNumber(number);
		}
		StringBuilder sb = new StringBuilder();
		sb.append(code).append("-").append(number);
		return sb.toString();
	}
}

package com.zlk.bigdemo.freeza.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class NoGridView extends GridView {
	public NoGridView(Context context) {
		super(context);
	}

	public NoGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public NoGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	// 不出现滚动条
	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}

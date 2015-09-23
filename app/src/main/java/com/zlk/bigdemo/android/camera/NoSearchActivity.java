/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zlk.bigdemo.android.camera;

import android.app.Activity;
import android.view.KeyEvent;

public class NoSearchActivity extends Activity {
	
	private boolean isAllowShowKeyboardByPress = false;
    @Override
    public boolean onSearchRequested() {
        return false;
    }
    
    protected boolean isAllowShowKeyboardByPress(){
		return this.isAllowShowKeyboardByPress;
	}
	
	/**
     * 所有相关的界面中，都禁止通过长按的方式显示键盘
     */
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (!isAllowShowKeyboardByPress() && keyCode == KeyEvent.KEYCODE_MENU) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}

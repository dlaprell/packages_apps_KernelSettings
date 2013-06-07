/*
*  Copyright 2013 Firtecy
*
*  Licensed under the Apache License, Version 2.0 (the "License");
*  you may not use this file except in compliance with the License.
*  You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing, software
*  distributed under the License is distributed on an "AS IS" BASIS,
*  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*  See the License for the specific language governing permissions and
*  limitations under the License.
*/
package de.firtecy.kernelsettings.gui;

import android.content.Context;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 
 * @author David Laprell
 *
 */
public class TitleSeperator extends LinearLayout{

	private TextView tv;
	private Seperator sp;
	
	public TitleSeperator(Context context) {
		super(context);
		this.setOrientation(LinearLayout.VERTICAL);
		tv = new TextView(context);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
    	tv.setPadding(30, 0, 30, 0);
    	this.setPadding(10, 10, 10, 10);
    	this.setClickable(true);
    	this.addView(tv);
    	this.addView(sp);
	}
	
	public TitleSeperator(Context context, String title) {
		this(context);
		this.setText(title);
	}
	
	public void setTextSize(float size) {
		tv.setTextSize(size);
	}
	
	public void setSeperatorColor(int color) {
		sp.setBackgroundColor(color);
	}
	
	public void setText(String text) {
		tv.setText(text);
	}
	
	public String getText() {
		return (String)tv.getText();
	}
	
}

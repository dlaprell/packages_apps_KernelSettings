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

import de.firtecy.kernelsettings.util.Values;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

/**
 * @author David Laprell
 *
 */
public class MenuBar extends LinearLayout implements OnClickListener {

	private TextView tv;
	private ImageView iv;
	
	public MenuBar(Context context) {
		this(context, "empty");
	}
	
	/**
	 * @param context
	 */
	public MenuBar(Context context, String title) {
		super(context);
		tv = new TextView(context);
		iv = new ImageView(context);
		this.setOrientation(HORIZONTAL);
		this.setVisibility(VISIBLE);
		this.setBackgroundColor(Color.DKGRAY);
		this.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		this.setPadding(20, 10, 20, 10);
		this.setWeightSum(10);
		
		tv = new TextView(context);
		tv.setTextColor(Color.WHITE);
		tv.setText(title);
		tv.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, Values.BIGLETTERS);
		tv.setOnClickListener(this);
		iv.setVisibility(INVISIBLE);
		
		this.addView(iv, 0);
		this.addView(tv, 1);
	}
	
	public void addIcon(ImageView v) {
		iv = v;
		LayoutParams ps = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		ps.gravity = Gravity.CENTER;
		iv.setLayoutParams(ps);
		iv.setOnClickListener(this);
		iv.setVisibility(VISIBLE);
	}
	
	public void addImage(Drawable d) {
		iv.setImageDrawable(d);
		iv.setVisibility(VISIBLE);
		LayoutParams ps = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		ps.gravity = Gravity.CENTER;
		iv.setLayoutParams(ps);
		iv.setOnClickListener(this);
		this.setPadding(5, 10, 20, 10);
	}
	
	public void showIcon(boolean b) {
		if(b)iv.setVisibility(VISIBLE);
		else iv.setVisibility(INVISIBLE);
	}
	
	public void setText(String t) {
		tv.setText(t);
	}

	@Override
	public void onClick(View v) {
		this.performClick();
	}

}

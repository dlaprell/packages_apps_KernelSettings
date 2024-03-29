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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 
 * @author David Laprell
 *
 */
public class MenuTitle extends LinearLayout implements OnClickListener{

	private TextView tv;
	private Seperator s;
	private ImageView im;
	private LinearLayout l;
	
	public MenuTitle(Context context) {
		super(context);
		tv = new TextView(context);
		im = new ImageView(context);
		s = new Seperator(context, Color.GRAY, 4);
		s.setPadding(15, 3, 15, 3);
		
		this.setOrientation(LinearLayout.VERTICAL);
		
		l = new LinearLayout(context);
		l.setOrientation(HORIZONTAL);
		l.setWeightSum(6.0f);
		l.setPadding(10, 20, 10, 20);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, Values.BIGLETTERS);
    	tv.setPadding(10, 10, 5, 0);
    	tv.setOnClickListener(this);
    	im.setScaleType(ScaleType.FIT_CENTER);
    	im.setVisibility(INVISIBLE);
    	
    	LayoutParams prm1 = new LayoutParams(0, LayoutParams.WRAP_CONTENT);
    	prm1.weight = 5.0f;
    	LayoutParams prm2 = new LayoutParams(0, LayoutParams.MATCH_PARENT);
    	prm2.weight = 1.0f;
    	
    	l.addView(im, prm2);
    	l.addView(tv, prm1);
    	
    	this.addView(l);
    	this.addView(s);
	}
	
	public void setImage(Drawable d) {
		im.setImageDrawable(d);
		im.setVisibility(VISIBLE);
	}
	
	public MenuTitle(Context context, String title) {
		this(context);
		this.setText(title);
	}
	
	public void setTextSize(float size) {
		tv.setTextSize(size);
	}
	
	public void setSeperatorColor(int color){
		s.setBackgroundColor(color);
	}
	
	public void setText(String text) {
		tv.setText(text);
	}
	
	public String getText() {
		return (String)tv.getText();
	}

	@Override
	public void onClick(View v) {
		if((TextView)v == tv) {
			this.performClick();
		}
	}
}

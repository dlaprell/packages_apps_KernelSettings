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
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

/**
 * 
 * @author David Laprell
 *
 */
public class Enabler extends BasicElement implements OnClickListener{

	private Switch sw;
	private TextView tv;
	private RelativeLayout container;
	
	public Enabler(Context context) {
		super(context);
		this.setOrientation(HORIZONTAL);
		this.setClickable(true);
		this.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		
		sw = new Switch(context);
		tv = new TextView(context);
		container = new RelativeLayout(context);
		
		container.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, Values.TEXTSIZE);
		
		RelativeLayout.LayoutParams prm1 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		RelativeLayout.LayoutParams prm2 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		
		//set id so Rules with Id will work
		sw.setId(Values.generateViewId());
		tv.setId(Values.generateViewId());
		
		prm1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		prm2.addRule(RelativeLayout.LEFT_OF, sw.getId());
		prm2.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		
		sw.setOnClickListener(this);
		tv.setOnClickListener(this);
		container.addView(tv, prm2);
		container.addView(sw, prm1);
		this.addView(container);
	}
	
	public void setText(String t) {
		tv.setText(t);
	}
	
	public void setState(boolean b) {
		sw.setChecked(b);
	}
	
	public boolean getState () {
		return sw.isChecked();
	}

	@Override
	public void setStartValue(String t) {
		if(t.equals("0"))setState(false);
		else if(t.equals("1"))setState(true);
	}

	public String booleanToString(boolean b) {
		return b ? "1" : "0";
	}
	
	@Override
	public void onClick(View v) {
		if(v == tv) {
			sw.setChecked(!sw.isChecked());
		}
		this.performClick();
		super.callValueChange(booleanToString(sw.isChecked()));
	}
}

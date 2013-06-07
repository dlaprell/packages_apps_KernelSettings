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
import android.widget.TableLayout;
import android.widget.TextView;

/**
 * 
 * @author David Laprell
 *
 */
public class TextViewer extends BasicElement implements OnClickListener{
	private TextView tv;

	public TextViewer(Context context) {
		super(context);
		this.setOrientation(HORIZONTAL);
		this.setVisibility(VISIBLE);
		this.setClickable(true);
		
		tv = new TextView(context);
		tv.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 9));
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, Values.TEXTSIZE);
		
		this.addView(tv, 0);
	}
	
	public void setTextSize(int unit, float size) {
		tv.setTextSize(unit, size);
	}
	
	public void setText(String t) {
		tv.setText(t);
	}
	
	public String getText() {
		return (String) tv.getText();
	}
	
	public void setTypeface(int i) {
		tv.setTypeface(null, i);
	}
	
	public void setOnClickListenerNew(final OnClickListener lis)  {
		this.setOnClickListener(lis);
		tv.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		this.performClick();
	}
}

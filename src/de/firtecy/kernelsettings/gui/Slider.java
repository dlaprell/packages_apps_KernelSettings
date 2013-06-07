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
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

/**
 * 
 * @author David Laprell
 *
 */
public class Slider extends BasicElement implements OnSeekBarChangeListener {

	private TextView tv;
	private SeekBar sb;
	private String valueText, unit;
	private boolean nb;
	private int start, steps;
	
	public Slider(Context context) {
		super(context);
		valueText = "";
		unit = "";
		this.setOrientation(LinearLayout.VERTICAL);
		
		tv = new TextView(context);
		sb = new SeekBar(context);
		sb.setOnSeekBarChangeListener(this);
		
		steps = 1;
		
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, Values.TEXTSIZE);
		tv.setText(valueText + ": 1 " + unit);
		
		this.addView(tv, 0);
		this.addView(sb, 1);
	}
	
	public void setMax(int m) {
		sb.setMax(m -  start);
		updateText();
	}
	
	public void setRange(int min, int max, int step) {
		steps = step;
		double range = 0.0;
		range = min / steps;
		start = (int)range;
		range = max / steps - start;
		sb.setMax((int)range);
		
	}
	
	public void setMin(int m) {
		sb.setMax(sb.getMax() + (start - m));
		sb.setProgress(sb.getProgress() + (start - m));
		updateText();
		start = m;
	}
	
	public void setText(String v) {
		valueText = v;
		updateText();
	}
	
	public void setUnit(String t) {
		unit = t;
		tv.setText(valueText + ": " + getValue() + " " + unit);
	}
	
	private void updateText() {
		tv.setText(valueText + ": " + ((sb.getProgress() + start)*steps) + " " + unit);
	}
	
	public int getValue () {
		return (sb.getProgress() + start) * steps;
	}
	
	public void setValue(int i) {
		i = (int)i/steps;
		if(i >= start && i <= (sb.getMax() + start)) {
			sb.setProgress(i - start);
			updateText();
		}
	}
	
	public void setNotifyBefore(boolean b) {
		nb = b;
	}
	
	@Override
	public void setStartValue(String t) {
		int i = start;
		try {
			i = Integer.parseInt(t);
		} catch(Exception ex) {	
		}
		this.setValue(i);
	}
	
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		updateText();
		if(nb)super.callValueChange(String.valueOf((sb.getProgress() + start)*steps));
	}
	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		super.callValueChange(String.valueOf((sb.getProgress() + start)*steps));
	}
}

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
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

/**
 * @author David Laprell
 *
 */
public class ValueSlider extends BasicElement implements OnSeekBarChangeListener {

	private TextView tv, value;
	private SeekBar sb;
	private String valueText;
	private int start, steps;
	private LinearLayout container;
	
	public ValueSlider(Context context) {
		super(context);
		container = new LinearLayout(context);
		container.setOrientation(HORIZONTAL);
		valueText = "";
		this.setOrientation(LinearLayout.VERTICAL);
		this.setWeightSum(2.0f);
		this.setPadding(10, 3, 10, 0);
		LayoutParams params = new LayoutParams(0, LayoutParams.WRAP_CONTENT);
		params.weight =1.5f;
		
		LayoutParams params3 = new LayoutParams(0, LayoutParams.WRAP_CONTENT);
		params3.weight =0.5f;
		
		LayoutParams params2 = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		
		tv = new TextView(context);
		value = new TextView(context);
		sb = new SeekBar(context);
		sb.setOnSeekBarChangeListener(this);
		
		value.setTextSize(TypedValue.COMPLEX_UNIT_SP, Values.TEXTSIZE);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, Values.TEXTSIZE);
		updateText();
		container.setLayoutParams(params2);
		steps = 1;
		tv.setLayoutParams(params);
		value.setLayoutParams(params3);
		sb.setLayoutParams(params2);
		
		container.addView(tv);
		container.addView(value);
		this.addView(container);
		this.addView(sb);
	}
	
	public void setRange(int min, int max, int step) {
		steps = step;
		double range = 0.0;
		range = min / steps;
		start = (int)range;
		range = max / steps - start;
		sb.setMax((int)range);
		
	}
	
	public void setMax(int m) {
		sb.setMax(m -  start);
		updateText();
	}
	
	public void setMin(int m) {
		sb.setMax(sb.getMax() - m);
		sb.setProgress(sb.getProgress() + (start - m));
		updateText();
		start = m;
	}
	
	public void setText(String v) {
		valueText = v;
		updateText();
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
	
	private void updateText() {
		tv.setText(valueText + ": ");
		value.setText(String.valueOf((sb.getProgress()+ start)*steps));
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
	}
	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {}
	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {}
}

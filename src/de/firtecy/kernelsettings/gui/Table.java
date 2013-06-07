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

import java.util.ArrayList;

import de.firtecy.kernelsetting.R;
import de.firtecy.kernelsettings.gui.ValueSlider;
import de.firtecy.kernelsettings.util.Values;

import android.content.Context;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ImageView.ScaleType;

/**
 * @author David Laprell
 *
 */
public class Table extends BasicElement implements OnClickListener{

	private LinearLayout container;
	private ArrayList<ValueSlider>views;
	private LinearLayout l1, l2;
	private ImageButton b;
	private int min, max, steps;
	private TextViewer tv;
	
	/**
	 * @param context
	 */
	public Table(Context context) {
		super(context);
        container = new LinearLayout(this.getContext());
        steps = 1;
        views= new ArrayList<ValueSlider>();
        tv = new TextViewer(context);
        
        container.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        
        container.setOrientation(LinearLayout.VERTICAL);
		l1 =new LinearLayout(this.getContext());
		l1.setOrientation(LinearLayout.VERTICAL);
		l1.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		l2 =new LinearLayout(this.getContext());
		l2.setOrientation(LinearLayout.HORIZONTAL);
		l2.setWeightSum(10.0f);
		l2.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		LayoutParams prm = new LayoutParams(0, LayoutParams.WRAP_CONTENT);
		prm.weight = 8.0f;
		LayoutParams prm2 = new LayoutParams(0, LayoutParams.MATCH_PARENT);
		prm2.weight = 2.0f;
		
		b = new ImageButton(context);
		b.setOnClickListener(this);
		b.setEnabled(true);
		b.setScaleType(ScaleType.FIT_CENTER);
		b.setImageDrawable(getResources().getDrawable(R.drawable.tick));
		b.setClickable(true);
		b.setLayoutParams(prm2);
		tv.setLayoutParams(prm);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, Values.MEDIUMLETTERS);
		tv.setTypeface(Typeface.BOLD);
		
		l2.addView(tv);
		l2.addView(b);
		
		container.addView(l2);
		container.addView(l1);
		this.addView(container);
    }
	
	public void setText(String t) {
		tv.setText(t);
	}
	
	private void addView(String ident) {
		ValueSlider slider = new ValueSlider(this.getContext());
		slider.setName(ident);
		slider.setRange(min, max, steps);
		slider.setText(ident);
		slider.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		views.add(slider);
		l1.addView(slider);
	}
	
	public void setRange(int min, int max, int step) {
		this.min = min;
		this.max = max;
		this.steps = step;
	}
	
	public void setRange(int min, int max) {
		this.min = min;
		this.max = max;
	}
	
	public ValueSlider get(String name) {
		ValueSlider slider = null;
		for(ValueSlider s: views) {
			if(s.getName().equals(name)) {
				slider = s;
				break;
			}
		}
		return slider;
	}
	
	@Override
	public void setStartValue(String t) {
		String[]tmp = t.split(";");
		for(int i = 0; i < tmp.length;i++) {
			String[]typ = tmp[i].replaceAll(" ", "").split("=");
			if(typ.length > 1) {
				addView(typ[0]);
				try{this.get(typ[0]).setValue(Integer.parseInt(typ[1]));}catch(Exception ex){}
			} else {
				typ = tmp[i].replaceAll(" ", "").replaceAll("mV", "").split(":");
				if(typ.length > 1) {
					addView(typ[0]);
					try{this.get(typ[0]).setValue(Integer.parseInt(typ[1]));}catch(Exception ex){}
				} 
			}
		}
	}
	
	@Override
	public void onClick(View v) {
		if(v == b) {
			String values1 = "";
			for(ValueSlider slider: views) {
				if(values1.length() > 0) {
					values1 = values1 + " ";
				}
				values1 = values1 + slider.getValue();
			}
			super.callValueChange(values1);
		}
	}
}

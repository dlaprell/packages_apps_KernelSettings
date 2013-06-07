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
import de.firtecy.kernelsettings.util.Values;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * @author David Laprell
 *
 */
public class ComboBox extends BasicElement implements OnItemSelectedListener, OnClickListener {
	
	private Spinner spin;
	private ArrayAdapter<String> adapter;
	private ArrayList<String>items;
	private TextView tv;
	private LinearLayout container;
	private ImageButton b;
	private String value;
	private boolean returnIndex;
	
	/**
	 * @param context
	 */
	public ComboBox(Context context) {
		super(context);
		
		returnIndex = false;
		
		this.setOrientation(HORIZONTAL);
		this.setVisibility(VISIBLE);
		
		items = new ArrayList<String>();
		
		container = new LinearLayout(context);
		container.setOrientation(VERTICAL);
		container.setWeightSum(10.0f);
		
		b = new ImageButton(context);
		b.setOnClickListener(this);
		b.setEnabled(true);
		b.setScaleType(ScaleType.FIT_CENTER);
		b.setImageDrawable(getResources().getDrawable(R.drawable.tick));
		b.setClickable(true);
		
		tv = new TextView(context);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, Values.TEXTSIZE);
		tv.setPadding(0, 0, 10, 0);
		spin = new Spinner(context);
		LayoutParams prm = new LayoutParams(0, LayoutParams.WRAP_CONTENT);
		prm.weight = 8.0f;
		LayoutParams prm2 = new LayoutParams(0, LayoutParams.MATCH_PARENT);
		prm2.weight = 2.0f;
		container.setLayoutParams(prm);
		b.setLayoutParams(prm2);
		spin.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		
		spin.setOnItemSelectedListener(this);
		
		container.addView(tv);
		container.addView(spin);
		
		super.addView(container);
		super.addView(b);
	}
	/** Sets the Text that is displayed above the Box
	 * @param String the new String
	 */
	public void setText(String t) {
		tv.setText(t);
	}
	
	/**
	 * Sets the current selected item. As param, name the String if the ComboBox is in Name mode or parse a int when it is in Index mode
	 * @param t String the name or index of the Element
	 */
	public void setValue(String t) {
		if(returnIndex) {
			try {
				if(items.size() > Integer.parseInt(t))
					spin.setSelection(Integer.parseInt(t));
			}catch(Exception ex){}
		} else {
			int iX = -1;
			for(int i= 0;i<items.size();i++) {
				if(items.get(i).equals(t)) {
					iX = i;
					break;
				}
			}
			if(iX >= 0) {
				spin.setSelection(iX);
			}
		}
	}
	
	@Override
	public void setStartValue(String str) {
		int i = -1;
		if(returnIndex) {
			try {
				i = Integer.parseInt(str);
			}catch(Exception ex){}
		} else {
			if(str.contains("="))
				str = str.substring(str.lastIndexOf("=") + 1);
			i = items.indexOf(str);
		}
		
		if(i >= 0 && i < items.size()) {
			spin.setSelection(i);
		}
	}
	
	@Override
	public void setAcceptedValues(String t) {
		setAcceptedValues(t, " ");
	}
	
	public void setAcceptedValues(String str, String splitter) {
		items.clear();
		if(str.contains("="))
			str = str.substring(str.lastIndexOf("=") + 1);
		str = str.replace("[", "").replace("]", "");
		String[] tmp = str.split(splitter);
		for(String t:tmp) {
			if(t != null && t.length() > 0)
				items.add(t);
		}
		
		adapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_item, items);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spin.setAdapter(adapter);
	}
	
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		int index = arg0.getSelectedItemPosition();
		value = (String) adapter.getItem(index);
		b.setEnabled(true);
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		
	}

	@Override
	public void onClick(View v) {
		if(v == b) {
			if(returnIndex) {
				int iX = -1;
				for(int i= 0;i<items.size();i++) {
					if(items.get(i).equals(value)) {
						iX = i;
						break;
					}
				}
				if(iX >= 0)
					super.callValueChange(String.valueOf(iX));
			} else 
				super.callValueChange(value);
		}
	}

	/**
	 * @return the returnIndex
	 */
	public boolean isReturnIndex() {
		return returnIndex;
	}

	/**
	 * @param returnIndex the returnIndex to set
	 */
	public void setReturnIndex(boolean returnIndex) {
		this.returnIndex = returnIndex;
	}
}

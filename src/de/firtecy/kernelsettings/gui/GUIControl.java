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

import de.firtecy.kernelsettings.gui.BasicElement.OnChangeValueListener;
import de.firtecy.kernelsettings.settings.Entry;
import de.firtecy.kernelsettings.settings.Settings;
import de.firtecy.kernelsettings.util.Translator;

import android.content.Context;
//import android.content.SharedPreferences;
import android.graphics.Color;
//import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

/**
 * @author David Laprell
 *
 */
public class GUIControl implements OnChangeValueListener {
	
	private final Settings SETTINGS;
	private final Context CONTEXT;
	private ArrayList<Item> items;
	private ArrayList<String> groups;
	private boolean build;
	private LinearLayout[] lay;
	private Translator translator;
	private boolean lowMemory;
	
	public GUIControl(Context context, Settings settings) {
		CONTEXT = context;
		SETTINGS = settings;
		items = new ArrayList<Item>();
		groups = new ArrayList<String>();
		build = false;
		translator = new Translator(context);
		//SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(CONTEXT);
		lowMemory = /*shared.getBoolean("LowMemory", false);*/false;
	}
	
	/**
	 * Sets a new Element to the Gui that should be gerneated (will get build at buildLayout())
	 * @param name the ELements name that should later be generated
	 */
	public void addElement(String name) {
		boolean in = false;
		for(int i= 0; i < items.size(); i++) {
			if(items.get(i).name == name) {
				in = true;
			}
		}
		if(!in) {
			Entry e = SETTINGS.get(name);
			if(e!=null) {
				String trans = translator.translate(e.getGroup());
				items.add(new Item(name, trans));
				if(!groups.contains(trans)) {
					groups.add(trans);
				}
			}
		}
	}
	
	private LinearLayout buildLayout(String name) {
		LinearLayout tmp = null;
		
		if(lowMemory) {
			tmp = new LinearLayout(CONTEXT);
			tmp.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			
			if(!name.equals(translator.translate("Performance"))) {
				for(Item item : items) {
					if(groups.contains(item.group) && name.equals(item.group)) {
						BasicElement child = buildElementfromItem(item);
						Entry e = SETTINGS.get(item.name);
						if(child!=null){
							if(tmp.getChildCount() > 0) {
								tmp.addView(new Seperator(CONTEXT, Color.GRAY, 3));
							}
							tmp.addView(child);
							if(e.getDescription() != "") {
								TextViewer tv =new TextViewer(CONTEXT);
								tv.setText(e.getDescription());
								tmp.addView(tv);
							}
						}//End child!=null
					}
				} // End for 
			} else {
				tmp = new PerformanceSettings(CONTEXT);
			}
		}
		
		return tmp;
	}
	
	/**
	 * Build the Layout, can only be called once!
	 */
	public void buildLayout() {
		if(!build && !lowMemory) {
			lay = new LinearLayout[groups.size() + 1];
			for(int i = 0; i < lay.length;i++) {
				lay[i] = new LinearLayout(CONTEXT);
				lay[i].setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
				lay[i].setOrientation(LinearLayout.VERTICAL);
			}
			for(Item item : items) {
				if(groups.contains(item.group)) {
					BasicElement child = buildElementfromItem(item);
					Entry e = SETTINGS.get(item.name);
					int i = groups.indexOf(item.group);
					if(child!=null){
						if(lay[i].getChildCount() > 0) {
							lay[i].addView(new Seperator(CONTEXT, Color.GRAY, 3));
						}
						lay[i].addView(child);
						if(e.getDescription() != "") {
							TextViewer tv =new TextViewer(CONTEXT);
							tv.setText(e.getDescription());
							lay[i].addView(tv);
						}
					}//End child!=null
				}
			} // End for 
			lay[lay.length - 1] = new PerformanceSettings(CONTEXT);
			groups.add(translator.translate("Performance"));
			build = true;
		}
	}
	
	private BasicElement buildElementfromItem (Item item) {
		Entry e = SETTINGS.get(item.name);
		BasicElement child = null;
		if(e.isEnable()) {
			child = new Enabler(CONTEXT);
			child.setName(e.NAME);
			((Enabler)child).setText(e.getFullname());
			if(e.getFrom() != null && e.getFrom().length() > 0) {
				child.callStartValue(e.getFrom(), "t");
			}
		} else if(e.isVaule()) {
			child = new Slider(CONTEXT);
			child.setName(e.NAME);
			((Slider)child).setText(e.getFullname());
			if(e.getSteps() > 0) {
				((Slider)child).setRange(e.getMinRange(), e.getMaxRange(), e.getSteps());
			} else {
				((Slider)child).setMax(e.getMaxRange());
				((Slider)child).setMin(e.getMinRange());
			}
			if(e.getFrom() != null && e.getFrom().length() > 0) {
				child.callStartValue(e.getFrom(), "t");
			}
		} else if(e.isCombo()) {
			child = new ComboBox(CONTEXT);
			child.setName(e.NAME);
			((ComboBox)child).setText(e.getFullname());
			if(e.getValues() != null && e.getValues().length() > 0)
				((ComboBox)child).callAcceptedValues(e.getValues());
			if(e.getFrom() != null && e.getFrom().length() > 0)
				child.callStartValue(e.getFrom(), "w");
		} else if(e.isTable()) {
			child = new Table(CONTEXT);
			child.setName(e.NAME);
			if(e.getSteps() > 0) {
				((Table)child).setRange(e.getMinRange(), e.getMaxRange(), e.getSteps());
			} else {
				((Table)child).setRange(e.getMinRange(), e.getMaxRange());
			}
			child.callStartValue(e.getFrom(), " ");
			((Table)child).setText(e.getFullname());
			
		}
		
		if(child != null) {
			child.setOnChangeListener(this);
		}
		return child;
	}
	
	public boolean StringToBoolean(String t) {
		if(t.equals("1"))return true;
		else if(t.equals("0"))return false;
		
		return false;
	}
	
	class Item {
		private String name, group;
		
		public Item(String n, String g) {
			name = n;
			group = g;
		}
	}
	
	public View getGroup(String name) {
		View v = null;
		if(groups.contains(name)) {
			if(lowMemory) {
				v = buildLayout(name);
			} else {
				v = lay[groups.indexOf(name)];
			}
		}
		return v;
	}
	
	public String[] getGroups() {
		String[] tmp = new String[groups.size()];
		for(int i = 0;i<groups.size();i++) {
			tmp[i] = groups.get(i);
		}
		return tmp;
	}
	
	@Override
	public void onValueChange(String name, String value) {
		Entry e = SETTINGS.get(name);
		if(e != null) {
			SETTINGS.setValue(e, value);
		}
	}
}

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
package de.firtecy.kernelsettings.settings;

import java.util.ArrayList;

import de.firtecy.kernelsettings.settings.CapCommand.OnReturnListener;
import de.firtecy.kernelsettings.util.Feature;
import de.firtecy.kernelsettings.util.Translator;
import de.firtecy.kernelsettings.util.Values;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * @author David Laprell
 */
public class Settings {
	private ArrayList<Entry> entries;
	private Terminal terminal;
	private Context con;
	private int counter;
	private SharedPreferences settings;
	private SharedPreferences.Editor editor;
	private Translator translator;
	
	public Settings(Context context) {
		terminal = new Terminal(true);
		con = context;
		entries = new ArrayList<Entry>();
		translator = new Translator(context);
		init();
	}
	
	private void init() {
		settings = con.getSharedPreferences(Values.PREFS_KERNEL, 4);
		editor = settings.edit();
	}
	
	public boolean addFeature(Feature item) {
		boolean success = false;
		if(item != null && item.isValid()) {
			boolean notNull = false;
			
			//Add Element from type
			if(item.getType().equals("switch")) {
				//Add a Switch to the Control
    			addEnabler(item.getName(), item.getLongname(), item.getCategory(), item.getPath());
    			success = true;
    		} else if(item.getType().equals("value")) {
    			//Add a Slider to the Control
    			try {
    				addValue(item.getName(), item.getLongname(), item.getCategory(), item.getPath(), Integer.parseInt(item.getMin()), Integer.parseInt(item.getMax()));
    				
    				if(item.getSteps() != null && item.getSteps().length() > 0) {
    					get(item.getName()).setSteps(item.getSteps());
    				}
    				success = true;
    			}catch(Exception ex) {
    				
    			}
    			
    		} else if(item.getType().equals("combo")) {
    			//Add a ComboBox
    			addCombo(item.getName(), item.getLongname(), item.getCategory(), item.getPath(), item.getFrom());
    			
    			//ComboBoxStuff
    			if(item.getValues() != null && item.getValues().length() > 0) {
    				get(item.getName()).setValues(item.getValues());
    			}
    			success = true;

    		} else if(item.getType().equals("table")) {
    			addTable(item.getName(), item.getLongname(), item.getCategory(), item.getPath(), item.getFrom());
    			if(item.getMin() != null && item.getMin().length() > 0 && item.getMax() != null && item.getMax().length() > 0) {
    				try{
    					get(item.getName()).setRange(Integer.parseInt(item.getMin()), Integer.parseInt(item.getMax()));
    					
    					if(item.getSteps() != null && item.getSteps().length() > 0) {
        					get(item.getName()).setSteps(item.getSteps());
        				}
    					success = true;
    				}catch (Exception ex){}
    			}
    		} else {
    			notNull = true;
    		}
			
			
			//Settings for all Elements:
    		if(!notNull) {
				//Check if there should also be a description
    			if(item.getDescription() != null && item.getDescription().length() > 0) {
    				get(item.getName()).setDescription(item.getDescription());
    			}
    			
    			
    			//Check if from can get parsed
    			if(item.getFrom() != null && item.getFrom().length() > 0 && !item.getType().equals("table")) {
    				get(item.getName()).setFrom(item.getFrom());
    			}
    		}
		}
		return success;
	}
	
	public int sendCommand(String cmd) {
		terminal.sendCommand(counter, cmd);
		counter++;
		return (counter - 1);
	}
	
	public void addEnabler(String name, String longname, String group, String path) {
		if(get(name) == null) {
			Entry e = new Entry(name, Entry.ENABLE);
			e.setPath(path);
			e.setFullname(longname);
			e.setGroup(group);
			e.setValue(settings.getString(name, "0"));
			entries.add(e);
		}
	}
	
	public void addValue(String name, String longname, String group, String path, int min, int max) {
		if(get(name) == null) {
			Entry e = new Entry(name, Entry.VALUE);
			e.setPath(path);
			e.setFullname(longname);
			e.setGroup(group);
			e.setRange(min, max);
			e.setValue(settings.getString(name, String.valueOf(min)));
			entries.add(e);
		}
	}

	public void addTable(String name, String longname, String group, String path, String from) {
		if(get(name) == null) {
			Entry e = new Entry(name, Entry.TABLE);
			e.setFullname(longname);
			e.setPath(path);
			e.setFrom(from);
			e.setGroup(group);
			entries.add(e);
		}
	}

	public void addCombo(String name, String longname, String group, String path, String from) {
		if(get(name) == null) {
			Entry e = new Entry(name, Entry.COMBO);
			e.setPath(path);
			e.setFullname(longname);
			e.setGroup(group);
			e.setFrom(from);
			entries.add(e);
		}
	}
	
	public Entry get(String name) {
		Entry e = null;
		for(int i = 0; i< entries.size();i++) {
			if(entries.get(i) != null && entries.get(i).NAME == name){
				e =entries.get(i);
				i = entries.size();
			}
		}
		return e;
	}
	
	public int getCount() {
		return entries.size();
	}
	
	public void setValue(Entry e, String val) {
		if(e != null) {
			if(e.setValue(val)) {
				editor.putString(e.NAME, val);
				if(e.getPath() != "") {
					String cmd = e.getPath().replaceAll("!VALUE!", val);
					writeStartSetting(e.NAME, cmd);
					terminal(cmd);
				}
				editor.commit();
			}
		}
	}
	
	private void terminal(String t) {
		if(t.contains(";")) {
			String[] cmd = t.split(";");
			terminal.sendCommand(counter, cmd);
			for(String s: cmd) {
				Log.i(Values.LOG_TAG, s);
			}
		} else {
			terminal.sendCommand(counter, t);
			Log.i(Values.LOG_TAG, t);
		}
		counter++;
	}
	
	public void setValue(String name, String val) {
		Entry e = get(name);
		if(e != null) {
			if(e.setValue(val)) {
				editor.putString(e.NAME, val);
				if(e.getPath() != "") {
					String cmd = e.getPath().replaceAll("!VALUE!", val);
					writeStartSetting(name, cmd);
					terminal(cmd);
				}
				editor.commit();
			}
		}
	}
	
	private void writeStartSetting(String name, String value) {
		SharedPreferences set = con.getSharedPreferences(Values.PREFS_RESTORE, 4);
		Entry e = this.get(name);
		SharedPreferences.Editor edit = set.edit();
		if(e != null && e.getGroup().equals(translator.translate("Wlan"))) {
			edit.putString("WIFI", set.getString("WIFI", "") + ";" + name);
			edit.commit();
		}
		
		if(!set.getString("RESTORE", "").contains(name))
			edit.putString("RESTORE", set.getString("RESTORE", "") + ";" + name);
		edit.putString(name, value);
		edit.commit();
	}
	
	public void setOnReturnListener(OnReturnListener l) {
		terminal.setOnReturnListener(l);
	}
}

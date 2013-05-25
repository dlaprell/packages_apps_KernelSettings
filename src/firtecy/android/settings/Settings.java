package firtecy.android.settings;

import java.util.ArrayList;

import firtecy.android.settings.CapCommand.onReturnListener;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * @author David Laprell
 */
public class Settings {
	private ArrayList<Entry> entries;
	private Terminal terminal;
	public static final String PREFS_NAME = "Kernel";
	private Context con;
	private int counter;
	private SharedPreferences settings;
	private SharedPreferences.Editor editor;
	
	public Settings(Context context) {
		terminal = new Terminal(true);
		con = context;
		entries = new ArrayList<Entry>();
		init();
	}
	
	private void init() {
		settings = con.getSharedPreferences(PREFS_NAME, 4);
		editor = settings.edit();
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
				Log.i("KS", s);
			}
		} else {
			terminal.sendCommand(counter, t);
			Log.i("KS", t);
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
		SharedPreferences set = con.getSharedPreferences("Restore", 4);
		Entry e = this.get(name);
		SharedPreferences.Editor edit = set.edit();
		if(e != null && e.getGroup().equals("Wlan")) {
			edit.putString("WIFI", set.getString("WIFI", "") + ";" + name);
			edit.commit();
		}
		
		if(set.getString(name, "") == "") {
			edit.putString("RESTORE", set.getString("RESTORE", "") + ";" + name);
			edit.putString(name, value);
			edit.commit();
		}
	}
	
	public void setOnReturnListener(onReturnListener l) {
		terminal.setOnReturnListener(l);
	}
}

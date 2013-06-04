package de.firtecy.receiver;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.stericson.RootTools.RootTools;
import com.stericson.RootTools.execution.Command;
import com.stericson.RootTools.execution.CommandCapture;
import com.stericson.RootTools.execution.Shell;

import de.firtecy.kernelsetting.Values;

/**
 * 
 * @author David Laprell
 */
public class BootRestore extends BroadcastReceiver {

	private Context main;
	private ArrayList<String>terms;
	private boolean boot, performance, kernel;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		terms = new ArrayList<String>(); 
		main = context;
		
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(main);
		
		boot = settings.getBoolean("RestoreBoot", true);
		performance = settings.getBoolean("RestorePerformance", false);
		kernel = settings.getBoolean("VertifyKernel", false);
		
		apply();
		
	}
	private void apply () {
		if(RootTools.isAccessGiven() && boot) { //Without root, settings can't be restored
			if(kernel) {
				try {
					Shell.runRootCommand(new BootCommand(1, new String[]{"cat /proc/version"}));
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				restore();
			}
		}
	}
	
	private void restoreValues() { //Run commands 
		for(int i = 0;i < terms.size();i++){
			try {
				Shell.runRootCommand(new CommandCapture(0, terms.get(i)));
				Log.i(Values.LOG_TAG, "Restore with:" + terms.get(i)); //Send logcat message
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
		Toast.makeText(main, "All Settings restored", Toast.LENGTH_LONG).show(); //Show Toast
	}
	
	public String booleanToString(boolean b) {
    	return b ? "1" : "0";
    }

	private void restore() {
		SharedPreferences settings = main.getSharedPreferences(Values.PREFS_RESTORE, 0); 
		String get = settings.getString("RESTORE", "");
		if(get.length() > 0) {
			String[]tmp = get.split(";"); //Store all Values that should be restored
			for(String t: tmp) { //loop through them and add them to terms
				if(t != null && t.length() > 0) { 
					String s = settings.getString(t, "");
					if(s != null && s.length() > 0) {
						terms.add(s);
					}
				}
			}
		}
		if(performance) {
			settings = main.getSharedPreferences("Performance", 4);
			get = settings.getString("Values", "");
			if(get.length() > 0) {
				String[]tmp = get.split(";"); //Store all Values that should be restored
				for(String t: tmp) { //loop through them and add them to terms
					if(t != null && t.length() > 0) { 
						String s = settings.getString(t, "");
						if(s != null && s.length() > 0) {
							terms.add(s);
						}
					}
				}
			}
		}
		settings = null;
		restoreValues();
    }
	
	@Override
	protected void finalize() {
		main = null;
		terms = null;
	}
	
	class BootCommand extends Command {

		public BootCommand(int id, String[] command) {
			super(id, command);
			
		}

		@Override
		public void output(int arg0, String arg1) {
			SharedPreferences settings = main.getSharedPreferences(Values.PREFS_RESTORE, 0); 
			if(arg1.contains(settings.getString("KernelInfo", "No_KERNEL"))) {
				restore();
			}
		}
		
	}
}

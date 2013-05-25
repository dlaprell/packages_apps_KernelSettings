package de.firtecy.kernelsettings;

import java.util.ArrayList;

import com.stericson.RootTools.RootTools;
import com.stericson.RootTools.execution.Shell;
import com.stericson.RootTools.execution.CommandCapture;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

/**
 * 
 * @author David Laprell
 */
public class WifiRestore extends BroadcastReceiver {

	public static final String PREFS_NAME = "Restore";
	private Context main;
	private ArrayList<String>terms;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		terms = new ArrayList<String>(); 
		main = context;
		WifiManager wifi = (WifiManager)main.getSystemService(Context.WIFI_SERVICE);
		if(wifi.isWifiEnabled()) {
			apply();
		}
	}
	private void apply () {
		if(RootTools.isAccessGiven()) {
			restore();
		}
	}
	
	private void restoreValues() {
		for(int i = 0;i < terms.size();i++){
			try {
				Shell.runRootCommand(new CommandCapture(0, terms.get(i)));
				Log.i("Restore Wifi", "Restore with:" + terms.get(i));
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
		Toast.makeText(main, "All Settings restored", Toast.LENGTH_LONG).show();
	}
	
	public String booleanToString(boolean b) {
    	return b ? "1" : "0";
    }

	private void restore() {
		SharedPreferences settings = main.getSharedPreferences(PREFS_NAME, 0); 
		String get = settings.getString("WIFI", "");
		if(get.length() > 0) {
			String[]tmp = get.split(";");
			for(String t: tmp) {
				if(t != null && t.length() > 0) {
					String s = settings.getString(t, "");
					if(s != null && s.length() > 0) {
						terms.add(s);
					}
				}
			}
		}
		restoreValues();
    }
}

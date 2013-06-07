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
package de.firtecy.kernelsettings.receiver;

import java.util.ArrayList;

import com.stericson.RootTools.RootTools;
import com.stericson.RootTools.execution.Shell;
import com.stericson.RootTools.execution.CommandCapture;

import de.firtecy.kernelsettings.util.Values;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

/**
 * 
 * @author David Laprell
 */
public class WifiRestore extends BroadcastReceiver {

	private Context main;
	private ArrayList<String>terms;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		terms = new ArrayList<String>(); 
		main = context;
		
		try {
			Looper.prepare();
		}catch(Exception ex){}
		
		WifiManager wifi = (WifiManager)main.getSystemService(Context.WIFI_SERVICE);
		if(wifi.isWifiEnabled()) {
			apply();
		} //If it is not Enabled it is useless to apply the values
	}
	private void apply () {
		//Without root access we can't proceed
		if(RootTools.isAccessGiven())
			restore();
	}
	
	private void restoreValues() {
		for(int i = 0;i < terms.size();i++){
			
			try {
				Shell.runRootCommand(new CommandCapture(0, terms.get(i)));
				Log.i(Values.LOG_TAG, "Restore with:" + terms.get(i));
			} 
			catch (Exception e) {
				e.printStackTrace();
			} 
		}
		Toast.makeText(main, "All Settings restored", Toast.LENGTH_LONG).show();
	}
	
	public String booleanToString(boolean b) {
    	return b ? "1" : "0";
    }

	private void restore() {
		SharedPreferences settings = main.getSharedPreferences(Values.PREFS_RESTORE, 0); 
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
			restoreValues();
		}
    }
	
	@Override
	protected void finalize() {
		main = null;
		terms = null;
	}
}

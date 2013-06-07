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

import android.util.Log;

import com.stericson.RootTools.execution.Shell;

import de.firtecy.kernelsettings.settings.CapCommand.OnReturnListener;


/**
 * @author David Laprell
 */
public class Terminal{
	private boolean access;
	private OnReturnListener lis;
	
	public Terminal(boolean superuser) {
		access = superuser;
	}
	
	public void sendCommand(int id, String cmd) {
		sendCommand(id, cmd, false);
	}
	
	public void sendCommand(int id, String[] cmd) {
		sendCommand(id, cmd, false);
	}
	
	public void sendCommand(int id, String cmd, boolean returns) {
		String cmd1[] = new String[1];
		cmd1[0] = cmd;
		CapCommand cap = new CapCommand(id, cmd1, lis);
		if(access) {
			try {
				Shell.runRootCommand(cap);
			} catch (Exception ex) {
				Log.d("Shell", ex.toString());
			}
		} else {
			try {
				Shell.runRootCommand(cap);
			} catch (Exception ex) {
				Log.d("Shell", ex.toString());
			}
		}
	}
	
	public void sendCommand(int id, String[] cmd, boolean returns) {
		CapCommand cap = new CapCommand(id, cmd, lis);
		if(access) {
			try {
				Shell.runRootCommand(cap);
			} catch (Exception ex) {
				Log.d("Shell", ex.toString());
			}
		} else {
			try {
				Shell.runRootCommand(cap);
			} catch (Exception ex) {
				Log.d("Shell", ex.toString());
			}
		}
	}
	
	public void setOnReturnListener(OnReturnListener l) {
		lis = l;
	}
}

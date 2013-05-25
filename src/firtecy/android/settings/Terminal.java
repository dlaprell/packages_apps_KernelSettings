package firtecy.android.settings;

import android.util.Log;

import com.stericson.RootTools.execution.Shell;

import firtecy.android.settings.CapCommand.onReturnListener;

/**
 * @author David Laprell
 */
public class Terminal{
	private boolean access;
	private onReturnListener lis;
	
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
	
	public void setOnReturnListener(onReturnListener l) {
		lis = l;
	}
}

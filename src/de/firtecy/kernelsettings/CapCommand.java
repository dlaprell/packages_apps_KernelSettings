package de.firtecy.kernelsettings;

import com.stericson.RootTools.execution.Command;
 
/**
 * 
 * @author David Laprell
 *
 */
public class CapCommand extends Command {

	private onReturnListener lis;
	/**
	 * Make this command an multiline command
	 * @param id
	 * @param command
	 */
	public CapCommand(int id, String[] command) {
		super(id, command);
	}
	
	/**
	 * Make this command an multiline command with returnListener
	 * @param id
	 * @param command
	 * @param l
	 */
	public CapCommand(int id, String[]command, onReturnListener l) {
		super(id, command);
		lis = l;
	}

	@Override
	public void output(int arg0, String arg1) {
		if(lis != null) {
			lis.onReturn(arg1);
		}
	}
    public interface onReturnListener {
    	public void onReturn(String t);
    }
}
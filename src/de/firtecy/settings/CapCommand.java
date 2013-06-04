package de.firtecy.settings;

import com.stericson.RootTools.execution.Command;
 
/**
 * 
 * @author David Laprell
 *
 */
public class CapCommand extends Command {

	private int Id;
	private OnReturnListener lis;
	/**
	 * Make this command an multiline command
	 * @param id
	 * @param command
	 */
	public CapCommand(int id, String[] command) {
		super(id, command);
		Id = id;
	}
	
	/**
	 * Make this command an multiline command with returnListener
	 * @param id
	 * @param command
	 * @param l
	 */
	public CapCommand(int id, String[]command, OnReturnListener l) {
		super(id, command);
		Id = id;
		lis = l;
	}

	@Override
	public void output(int arg0, String arg1) {
		if(lis != null) {
			lis.onReturn(Id, arg1);
		}
	}
    public interface OnReturnListener {
    	public void onReturn(int id, String t);
    }
}
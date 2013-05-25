package firtecy.android.settings;

import com.stericson.RootTools.execution.Command;

/**
 * 
 * @author David Laprell
 */
public class CapCommand extends Command {

	private int Id;
	
	private onReturnListener lis;
	/**
	 * @param id
	 * @param command
	 */
	public CapCommand(int id, String[] command) {
		super(id, command);
		Id = id;
	}
	
	public CapCommand(int id, String[]command, onReturnListener l) {
		this(id, command);
		lis = l;
	}

	@Override
	public void output(int arg0, String arg1) {
		if(lis != null) {
			lis.onReturn(Id, arg1);
		}
	}
    public interface onReturnListener {
    	public void onReturn(int id, String t);
    }
}
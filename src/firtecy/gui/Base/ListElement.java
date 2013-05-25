package firtecy.gui.Base;

import firtecy.android.settings.CapCommand.onReturnListener;
import firtecy.android.settings.Terminal;
import android.content.Context;
import android.widget.LinearLayout;

/**
 * 
 * @author David Laprell
 *
 */
public class ListElement extends LinearLayout implements onReturnListener{
	
	private String name = "";
	private OnChangeListener onlis;
	private Terminal term;
	private String remove;
	private boolean disable;
	
	public ListElement(Context context) {
		super(context);
		this.setVisibility(VISIBLE);
		this.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
		this.setPadding(10, 20, 10, 20);
		term = new Terminal(true);
		term.setOnReturnListener(this);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String Name) {
		name = Name;
	}
	
	public void callChange(String value) {
		if(onlis != null && !disable) {
			onlis.onChange(getName(), value);
		}
	}
	
	public interface OnChangeListener{
		public void onChange(String name, String value);
	}

	public void setStartValue(String t) {
		
	}
	
	public void setAcceptedValues(String t) {
		
	}
	
	public void callAcceptedValues(String from) {
		term.sendCommand(2, from, true);
	}
	
	public void callStartValue(String from, String rm) {
		String[]tmp = null;
		if(from.contains(";")) {
			tmp = from.split(";");
			if(tmp.length == 2) {
				remove = tmp[1];
				from = tmp[0];
			}
		}
		term.sendCommand(1, from, true);
		this.setDisable(true);
	}
	 
	/**
	 * @param OnChangeListener
	 */
	public void setOnChangeListener(OnChangeListener l) {
		onlis = l;
	}

	@Override
	public void onReturn(int id, String t) {
		if(id == 1 && t.length() > 0) {
			if(remove != null && remove.length() > 0) {
				String[]tmp = null;
				try {
					tmp =remove.split("!VALUE!");
				}catch(Exception ex){}
				if(tmp != null && tmp.length == 2 && t.contains(tmp[0]) && t.contains(tmp[1])){
					t = t.substring((t.indexOf(tmp[0]) + tmp[0].length()), t.indexOf(tmp[1]));
					this.setStartValue(t);
					this.setDisable(false);
				}
			} else {
				this.setStartValue(t);
				this.setDisable(false);
			}
		} else if(id == 2 && t.length() >0) {
			this.setAcceptedValues(t);
			this.setDisable(false);
		}
	}

	/**
	 * @return the disable
	 */
	public boolean isDisabled() {
		return disable;
	}

	/**
	 * That should be called before any other if you try to load the current value
	 * @param disable the disable to set
	 */
	public void setDisable(boolean disable) {
		this.disable = disable;
	}
}

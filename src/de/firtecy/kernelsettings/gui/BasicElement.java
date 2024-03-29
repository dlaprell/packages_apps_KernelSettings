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
package de.firtecy.kernelsettings.gui;

import android.content.Context;
import android.widget.LinearLayout;
import de.firtecy.kernelsettings.settings.Terminal;
import de.firtecy.kernelsettings.settings.CapCommand.OnReturnListener;

/**
 * 
 * @author David Laprell
 *
 */
public class BasicElement extends LinearLayout implements OnReturnListener{
	
	private String name = "";
	private OnChangeValueListener onlis;
	private Terminal term;
	private String remove;
	private boolean disable;
	
	public BasicElement(Context context) {
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
	
	public void callValueChange(String value) {
		if(onlis != null && !disable) {
			onlis.onValueChange(getName(), value);
		}
	}
	
	public interface OnChangeValueListener{
		public void onValueChange(String name, String value);
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
	 * @param OnChangeValueListener
	 */
	public void setOnChangeListener(OnChangeValueListener l) {
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

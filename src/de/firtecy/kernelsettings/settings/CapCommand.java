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
	public CapCommand(int id, String[] command, OnReturnListener l) {
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
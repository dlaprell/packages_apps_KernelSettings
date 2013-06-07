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
package de.firtecy.kernelsettings.util;

import android.content.Context;

/**
 * @author David Laprell
 *
 */
public class Translator {
	
	private Context context;
	
	public Translator(Context con){
		context = con;
	}
	
	public String translate(String t) {
		int i = getIntResourceByName(t);
		
		if(i != 0) {
			t = context.getString(i);
		}
		return t;
	}
	
	private int getIntResourceByName(String aString) {
	    String packageName = context.getPackageName();
	    return context.getResources().getIdentifier(aString, "string", packageName);
    }
}

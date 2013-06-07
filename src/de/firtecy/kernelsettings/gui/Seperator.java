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
import android.graphics.Color;
import android.view.View;

/**
 * 
 * @author David Laprell
 *
 */
public class Seperator extends BasicElement{

	private View v;
	
	public Seperator(Context context) {
		super(context);
		this.setPadding(30, 0, 30, 0);
		v = new View(context);  
		v.setBackgroundColor(Color.WHITE);
		this.addView(v, new LayoutParams(LayoutParams.MATCH_PARENT, 2));
	}
	
	public Seperator(Context context, int color) {
		this(context);
		v.setBackgroundColor(color);
	}
	
	public Seperator(Context context, int color, int height) {
		this(context, color);
		v.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, height));
	}
	
}

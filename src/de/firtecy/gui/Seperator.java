package de.firtecy.gui;

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

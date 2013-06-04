package de.firtecy.util;

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

package firtecy.android.scripts;

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
			t = s(i);
		}
		return t;
	}
	
	private int getIntResourceByName(String aString) {
	    String packageName = context.getPackageName();
	    return context.getResources().getIdentifier(aString, "string", packageName);
    }
    
    private String s(int i) {
    	return context.getResources().getString(i);
    }
}

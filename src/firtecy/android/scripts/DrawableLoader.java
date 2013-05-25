package firtecy.android.scripts;

import de.firtecy.kernelsettings.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;

/**
 * Basic Class to support some Icons
 * @author David Laprell
 */
public class DrawableLoader {
	
	private final Context con;
	
	public DrawableLoader(Context context) {
		con = context;
	}
	
	/**
	 * 
	 * @param name the Group name
	 * @return a Drawable, null when nothing is found
	 */
	public Drawable fromName(String name) {
		Drawable d = null;
		int i = 0;
		if(name.equals("Wlan")) {
			i = R.attr.wifi;
		} else if(name.equals("Brightness")) {
			i = R.attr.brightness;
		} else if(name.equals("Volume")) {
			i = R.attr.volume;
		} else if(name.equals("Charge")) {
			i = R.attr.charge;
		} else if(name.equals("Misc")) {
			i = R.attr.misc;
		} else if(name.equals("App")) {
			i = R.attr.app;
		} else if(name.equals("Performance")) {
			i = R.attr.performance;
		} else if(name.equals("Camera")) {
			i = R.attr.camera;
		}
		if(i != 0) {
			TypedArray a = con.getTheme().obtainStyledAttributes(new int[] {i});     
			int attributeResourceId = a.getResourceId(0, 0);
			d = con.getResources().getDrawable(attributeResourceId);
		}
		return d;
	}
	
	public static Drawable fromName(String name, Context con) {
		Drawable d = null;
		int i = 0;
		if(name.equals("Wlan")) {
			i = R.attr.wifi;
		} else if(name.equals("Brightness")) {
			i = R.attr.brightness;
		} else if(name.equals("Volume")) {
			i = R.attr.volume;
		} else if(name.equals("Charge")) {
			i = R.attr.charge;
		} else if(name.equals("Misc")) {
			i = R.attr.misc;
		} else if(name.equals("App")) {
			i = R.attr.app;
		} else if(name.equals("Performance")) {
			i = R.attr.performance;
		}
		if(i != 0) {
			TypedArray a = con.getTheme().obtainStyledAttributes(new int[] {i});     
			int attributeResourceId = a.getResourceId(0, 0);
			d = con.getResources().getDrawable(attributeResourceId);
		}
		return d;
	}
}

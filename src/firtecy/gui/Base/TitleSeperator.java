package firtecy.gui.Base;

import android.content.Context;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 
 * @author David Laprell
 *
 */
public class TitleSeperator extends LinearLayout{

	private TextView tv;
	private Seperator sp;
	
	public TitleSeperator(Context context) {
		super(context);
		this.setOrientation(LinearLayout.VERTICAL);
		tv = new TextView(context);
		//sp = new Seperator(context, Color.DKGRAY, 4);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
    	//tv.setTextColor(Color.CYAN);
    	tv.setPadding(30, 0, 30, 0);
    	this.setPadding(10, 10, 10, 10);
    	this.setClickable(true);
    	this.addView(tv);
    	this.addView(sp);
	}
	
	public TitleSeperator(Context context, String title) {
		this(context);
		this.setText(title);
	}
	
	public void setTextSize(float size) {
		tv.setTextSize(size);
	}
	
	public void setSeperatorColor(int color) {
		sp.setBackgroundColor(color);
	}
	
	public void setText(String text) {
		tv.setText(text);
	}
	
	public String getText() {
		return (String)tv.getText();
	}
	
}

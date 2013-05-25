package firtecy.gui.Base;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TextView;

/**
 * 
 * @author David Laprell
 *
 */
public class Enabler extends ListElement implements OnClickListener{

	private Switch sw;
	private TextView tv;

	public Enabler(Context context) {
		super(context);
		this.setOrientation(HORIZONTAL);
		this.setVisibility(VISIBLE);
		this.setWeightSum(10);
		this.setClickable(true);
		this.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		
		sw = new Switch(context);
		tv = new TextView(context);
		tv.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 9));
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, Values.TEXTSIZE);
		
		sw.setOnClickListener(this);
		tv.setOnClickListener(this);
		
		this.addView(tv, 0);
		this.addView(sw, 1);
	}
	
	public void setText(String t) {
		tv.setText(t);
	}
	
	public void setState(boolean b) {
		sw.setChecked(b);
	}
	
	public boolean getState () {
		return sw.isChecked();
	}

	@Override
	public void setStartValue(String t) {
		if(t.equals("0"))setState(false);
		else if(t.equals("1"))setState(true);
	}

	public String booleanToString(boolean b) {
		return b ? "1" : "0";
	}
	
	@Override
	public void onClick(View v) {
		if(v == tv) {
			sw.setChecked(!sw.isChecked());
		}
		this.performClick();
		super.callChange(booleanToString(sw.isChecked()));
	}
}

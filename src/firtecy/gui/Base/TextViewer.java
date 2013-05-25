package firtecy.gui.Base;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TableLayout;
import android.widget.TextView;

/**
 * 
 * @author David Laprell
 *
 */
public class TextViewer extends ListElement implements OnClickListener{
	private TextView tv;

	public TextViewer(Context context) {
		super(context);
		this.setOrientation(HORIZONTAL);
		this.setVisibility(VISIBLE);
		this.setClickable(true);
		
		tv = new TextView(context);
		//tv.setTextColor(Color.BLACK);
		tv.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 9));
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, Values.TEXTSIZE);
		
		this.addView(tv, 0);
	}
	
	public void setTextSize(int unit, float size) {
		tv.setTextSize(unit, size);
	}
	
	public void setText(String t) {
		tv.setText(t);
	}
	
	public String getText() {
		return (String) tv.getText();
	}
	
	public void setTypeface(int i) {
		tv.setTypeface(null, i);
	}
	
	public void setOnClickListenerNew(final OnClickListener lis)  {
		this.setOnClickListener(lis);
		tv.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		this.performClick();
	}
}

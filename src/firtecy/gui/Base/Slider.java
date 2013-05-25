package firtecy.gui.Base;

import android.content.Context;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

/**
 * 
 * @author David Laprell
 *
 */
public class Slider extends ListElement implements OnSeekBarChangeListener {

	private TextView tv;
	private SeekBar sb;
	private String valueText, unit;
	private boolean nb;
	private int start, steps;
	
	public Slider(Context context) {
		super(context);
		valueText = "";
		unit = "";
		this.setOrientation(LinearLayout.VERTICAL);
		
		tv = new TextView(context);
		sb = new SeekBar(context);
		sb.setOnSeekBarChangeListener(this);
		
		steps = 1;
		
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, Values.TEXTSIZE);
		tv.setText(valueText + ": 1 " + unit);
		
		this.addView(tv, 0);
		this.addView(sb, 1);
	}
	
	public void setMax(int m) {
		sb.setMax(m -  start);
		updateText();
	}
	
	public void setRange(int min, int max, int step) {
		steps = step;
		double range = 0.0;
		range = min / steps;
		start = (int)range;
		range = max / steps - start;
		//edit.setRange(min, max);
		sb.setMax((int)range);
		
	}
	
	public void setMin(int m) {
		sb.setMax(sb.getMax() + (start - m));
		sb.setProgress(sb.getProgress() + (start - m));
		updateText();
		start = m;
	}
	
	public void setText(String v) {
		valueText = v;
		updateText();
	}
	
	public void setUnit(String t) {
		unit = t;
		tv.setText(valueText + ": " + getValue() + " " + unit);
	}
	
	private void updateText() {
		tv.setText(valueText + ": " + ((sb.getProgress() + start)*steps) + " " + unit);
	}
	
	public int getValue () {
		return (sb.getProgress() + start) * steps;
	}
	
	public void setValue(int i) {
		i = (int)i/steps;
		if(i >= start && i <= (sb.getMax() + start)) {
			sb.setProgress(i - start);
			updateText();
		}
	}
	
	public void setNotifyBefore(boolean b) {
		nb = b;
	}
	
	@Override
	public void setStartValue(String t) {
		int i = start;
		try {
			i = Integer.parseInt(t);
		} catch(Exception ex) {	
		}
		this.setValue(i);
	}
	
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		updateText();
		if(nb)super.callChange(String.valueOf((sb.getProgress() + start)*steps));
	}
	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		super.callChange(String.valueOf((sb.getProgress() + start)*steps));
	}
}

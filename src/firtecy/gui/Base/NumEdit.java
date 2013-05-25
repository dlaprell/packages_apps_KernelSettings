package firtecy.gui.Base;

import android.content.Context;
import android.text.InputType;
import android.util.TypedValue;
import android.widget.EditText;

/**
 * @author David Laprell
 *
 */
public class NumEdit extends EditText{

	private int min, max;
	
	/**
	 * @param context
	 */
	public NumEdit(Context context) {
		super(context);
		this.setInputType(InputType.TYPE_CLASS_NUMBER);
		this.setRawInputType(InputType.TYPE_CLASS_NUMBER);
		this.setTextSize(TypedValue.COMPLEX_UNIT_SP, Values.TEXTSIZE);
		min = -1;
		max = -1;
	}
	
	public void setRange(int min, int max) {
		this.min = min;
		this.max = max;
	}
	
	public int getMin() {
		return min;
	}
	public int getMax() {
		return max;
	}
}

package firtecy.gui.Base;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * 
 * @author David Laprell
 *
 */
public class SwipeMenuLeft extends RelativeLayout implements OnClickListener{

	private LinearLayout list;
	private MenuTitle[] items;
	private MenuBar main;
	private LayoutParams prm1, prm2;
	private BounceScrollView scroll;
	
	private SwipeMenu controller;
	public SwipeMenuLeft(Context context, SwipeMenu menu, int m) {
		super(context);
		controller = menu;
		scroll = new BounceScrollView(context);
		main = new MenuBar(context, "Settings");
		main.setId(Values.generateViewId());
		items = new MenuTitle[m];
		list = new LinearLayout(context);
		list.setOrientation(LinearLayout.VERTICAL);
		
		prm1 = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		prm1.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		
		prm2 = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		prm2.addRule(RelativeLayout.BELOW, main.getId());
		prm2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

		scroll.addView(list, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		super.addView(main, prm1);
		super.addView(scroll, prm2);
	}
	
	public void addItem(String name) {
		MenuTitle tmp = new MenuTitle(super.getContext(), name);
		tmp.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		tmp.setClickable(true);
		tmp.setOnClickListener(this);
		list.addView(tmp);
		for(int i = 0;i < items.length;i++) {
			if(items[i] == null) {
				items[i] = tmp;
				i = items.length;
				break;
			}
		}
	}
	
	public void setTitle(String t) {
		main.setText(t);
	}
	
	public void addItem(String name, Drawable d) {
		MenuTitle tmp = new MenuTitle(super.getContext(), name);
		tmp.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		tmp.setClickable(true);
		tmp.setOnClickListener(this);
		tmp.setImage(d);
		list.addView(tmp);
		for(int i = 0;i < items.length;i++) {
			if(items[i] == null) {
				items[i] = tmp;
				i = items.length;
				break;
			}
		}
	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		String header = "";
		for(MenuTitle mnu : items)  {
			if((MenuTitle)v == mnu) {
				header = ((MenuTitle)v).getText();
				break;
			}
		}
		if(header != "")controller.changeToView(header);
	}
	
}

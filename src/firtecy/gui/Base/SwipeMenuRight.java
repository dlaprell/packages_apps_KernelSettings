package firtecy.gui.Base;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * 
 * @author David Laprell
 *
 */
public class SwipeMenuRight extends RelativeLayout implements OnClickListener{

	private SwipeMenu controller;
	private LinearLayout l;
	private View content;
	private MenuBar back;
	private String Title;
	private LayoutParams prm1, prm2;
	private BounceScrollView scroll;
	
	public SwipeMenuRight(Context context, SwipeMenu menu) {
		super(context);
		controller = menu;
		back = new MenuBar(context, "MAIN");
		scroll = new BounceScrollView(context);
		l = new LinearLayout(context);
		back.setOnClickListener(this);
		back.addImage(getResources().getDrawable(de.firtecy.kernelsettings.R.drawable.back));
		back.setId(0xFF345);
		l.setOrientation(LinearLayout.VERTICAL);
		//this.setBackgroundColor(Color.WHITE);
		
		prm1 = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		prm1.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		
		prm2 = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		prm2.addRule(RelativeLayout.BELOW, back.getId());
		prm2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		
		Title = "Einstellungen";
		scroll.addView(l, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		super.addView(back, prm1);
		super.addView(scroll, prm2);
	}
	
	public void changeContent(View v, String title) {
		if(title != "" && v != null) {
			prm2.addRule(RelativeLayout.BELOW, back.getId());
			content = null;
			content = v;
			Title = title;
			back.setText(title);
			l.removeAllViews();
			content.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			l.addView(content);
		}
	}

	public String getContentName() {
		return Title;
	}
	
	public void showBack(boolean b) {
		back.showIcon(b);
	}
	
	public int getBarHeight() {
		return back.getHeight();
	}
	
	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		if(v == back) {
			controller.tryChangeExpanded(controller.CHANGE_FROM_BACK);
		}
	}
}

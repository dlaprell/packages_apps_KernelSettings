package firtecy.gui.Base;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
/**
 * @author David Laprell
 */
public class SwipeMenu extends FrameLayout implements AnimationListener, OnClickListener {
	
	private SwipeMenuLeft left;
	private SwipeMenuRight right;
	private View[] SubMenus;
	private String[] heads;
	private String content;
	private int max;
	private float posFinal;
	private boolean expanded, sAnimation;
	private float menuScale;
	public final float SCALE = getContext().getResources().getDisplayMetrics().density;
	public final int ANIMATOR_DURATION = 200;
	public final int CHANGE_FROM_LIST = -1;
	public final int CHANGE_FROM_BACK = -2;
	public final int CHANGE_FROM_SIDE = -3;
	public final int CHANGE_FROM_HOVER = -4;
	public final int SWIPE_LEFT = -5;
	public final int SWIPE_RIGHT = -6;
	private TranslateAnimation aPreview, aExpand;   
	private LinearLayout.LayoutParams prmRight, prmLeft;
	private LinearLayout frame;
	private View overlay;
	
	public SwipeMenu(Context context, int maxTitles) {
		super(context);
		max = maxTitles;
		expanded = false;
		sAnimation = false;
		
		overlay = new View(context);
		left = new SwipeMenuLeft(context, this, max);
		right = new SwipeMenuRight(context, this);
		frame = new LinearLayout(context);
	
		heads = new String[maxTitles];
		SubMenus = new View[maxTitles];
		
		frame.setOrientation(LinearLayout.HORIZONTAL);
		frame.setWeightSum(10f);
		this.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		frame.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
		overlay.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		overlay.setBackgroundColor(Color.TRANSPARENT);
		overlay.setOnClickListener(this);
		overlay.setVisibility(View.INVISIBLE);
		
		menuScale= 0.7f;
		
		init();
		
		frame.addView(left);
		frame.addView(right);
		
		this.addView(frame);
		this.addView(overlay);
	}
	
	private void init() {
		
		prmLeft = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT);
		prmRight = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT);
		prmLeft.weight = 10.0f;
		prmRight.weight = 0.0f;
		
		left.setLayoutParams(prmLeft);
		right.setLayoutParams(prmRight);
		
	}
	
	public void setTitleLeft(String t) {
		if(left != null) {
			left.setTitle(t);
		}
	}
	
	private void setOverlay(boolean b) {
		if(b){
			overlay.setVisibility(View.VISIBLE);
		} else {
			overlay.setVisibility(View.INVISIBLE);
		}
		right.setEnabled(b);
	}
	
	public boolean getStateExpanded() {
		return expanded;
	}
	
	public void addSubMenu(String name, View display) {
		 for(int i =0;i < max;i++) {
			 if(heads[i] == null || heads[i] == "") {
				 heads[i] = name;
				 SubMenus[i] = display;
				 i = max;
				 left.addItem(name);
			 }
		 }
	}
	
	public void addSubMenu(String name, View display, Drawable d) {
		 for(int i =0;i < max;i++) {
			 if(heads[i] == null || heads[i] == "") {
				 heads[i] = name;
				 SubMenus[i] = display;
				 i = max;
				 left.addItem(name, d);
			 }
		 }
	}
	
	public void changeToView(String name) {
		View v = null;
		if(right.getContentName() != name) {
			for(int i = 0;i<max;i++) {
				if(heads[i] == name) {
					v = SubMenus[i];
					i = max;
				}
			}
			if(v != null){
				right.changeContent(v, name);
				content = name;
				tryChangeExpanded(CHANGE_FROM_LIST);
			}
		} else {
			tryChangeExpanded(CHANGE_FROM_LIST);
		}
	}
	public boolean getExpandedState() {
		return expanded;
	}
	public void tryChangeExpanded(int i) {
		switch(i) {
			case CHANGE_FROM_HOVER:
				if(!expanded && content != "" && content != null) {
					toggelMenu();
				}
			break;
			case  CHANGE_FROM_SIDE:
				if(!expanded && content != "" && content != null) {
					changeToView(content);
				}
			break;
			case  CHANGE_FROM_BACK:
				if(expanded) {
					toggelMenu();
				}
			break;
			case  CHANGE_FROM_LIST:
				if(!expanded) {
					toggelMenu();
				}
			break;
		}
	}
	
	public void handleSwipe(int swipeTyp) {
		 if(swipeTyp == SWIPE_LEFT && expanded && content != null && content != "") {
			 tryChangeExpanded(CHANGE_FROM_BACK);
		 } else if(swipeTyp == SWIPE_RIGHT && !expanded && content != null && content != "") {
			 tryChangeExpanded(CHANGE_FROM_LIST);
		 }
	}
	
	private void toggelMenu() {
		if(aExpand == null || aPreview == null) {
			
			posFinal =(-1 * this.getWidth() * menuScale);
			
			aExpand = new TranslateAnimation(0, (int)posFinal , 0, 0);
			aPreview = new TranslateAnimation((int)posFinal, 0, 0, 0);
			
			overlay.setX(posFinal * -1.0f);
			overlay.setY((float)right.getBarHeight());
			
			frame.setLayoutParams(new LayoutParams((int)(this.getWidth() * menuScale + this.getWidth()), LayoutParams.MATCH_PARENT));
			frame.setWeightSum(1.0f * menuScale + 1.0f);
			prmRight.weight = 1.0f;
			prmLeft.weight = menuScale * 1.0f;
			
			aPreview.setAnimationListener(this);
			aExpand.setAnimationListener(this);
			
			aPreview.setDuration(ANIMATOR_DURATION);
			aPreview.setFillAfter(false);
			aExpand.setDuration(ANIMATOR_DURATION);
			aExpand.setFillAfter(false);
		}
		if(!sAnimation) {
			if(expanded){
				expanded = false;
				sAnimation = true;
				frame.setX(0);
				frame.startAnimation(aPreview);
			}else{
				expanded= true;
				sAnimation = true;
				setOverlay(false);
				frame.setX(0);
				right.showBack(true);
				frame.startAnimation(aExpand);
			}
		}
	}
	
	@Override
	public void onAnimationEnd(Animation a) {
		if(a == aPreview) {
			frame.setX(0);
			setOverlay(true);
			right.showBack(false);
		} else if(a == aExpand) {
			// fix flicking
            // Source : http://stackoverflow.com/questions/9387711/android-animation-flicker
			TranslateAnimation anim = new TranslateAnimation(0.0f, 0.0f, 0.0f, 0.0f);
            anim.setDuration(1);
            frame.startAnimation(anim);
            frame.setX(posFinal);
		}
		sAnimation = false;
	}
	
	@Override
	public void onAnimationStart(Animation a) {
		
	}
	
	@Override
	public void onAnimationRepeat(Animation a) {
	}
	
	@Override
	public void onClick(View v) {
		if(v == overlay && !expanded) {
			tryChangeExpanded(CHANGE_FROM_HOVER);
		}
	}
	
	public interface newActivityRemote {
		public void startNewActivity(String path);
	}
}

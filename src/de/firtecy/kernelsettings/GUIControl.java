package de.firtecy.kernelsettings;

import java.util.ArrayList;

import firtecy.android.scripts.DrawableLoader;
import firtecy.android.scripts.Translator;
import firtecy.android.settings.Entry;
import firtecy.android.settings.Settings;
import firtecy.gui.Base.*;
import firtecy.gui.Base.ListElement.OnChangeListener;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

/**
 * @author David Laprell
 *
 */
public class GUIControl implements OnChangeListener {
	
	private final Settings SETTINGS;
	private final Context CONTEXT;
	private SwipeMenu menu;
	private ArrayList<Item> items;
	private ArrayList<String> groups;
	private boolean build;
	private DrawableLoader loader;
	private Translator translator;
	
	public GUIControl(Context context, Settings settings) {
		CONTEXT = context;
		SETTINGS = settings;
		loader = new DrawableLoader(context);
		translator = new Translator(context);
		items = new ArrayList<Item>();
		groups = new ArrayList<String>();
		menu = new SwipeMenu(CONTEXT, 20);
		build = false;
		
		menu.setTitleLeft(translator.translate("settings"));
	}
	
	/**
	 * Adds an Submenu to the SwipeMenu
	 * @param name the Name that will get Displayed on the left
	 * @param view the View that should be displayed
	 */
	public void addMenu(String name, View view) {
		menu.addSubMenu(name, view);
	}
	
	/**
	 * Adds an Submenu to the SwipeMenu
	 * @param name the Name that will get Displayed on the left
	 * @param view the View that should be displayed
	 * @param d Drawable that will be displayed on the left
	 */
	public void addMenu(String name, View view, Drawable d) {
		menu.addSubMenu(name, view, d);
	}
	
	/**
	 * Returns the generated View
	 * @return View the generated view
	 */
	public View getSwipeMenu() {
		return menu;
	}
	
	/**
	 * Returns the current Expanded State from the Menu (true for is expanded, false is not expanded)
	 * @return boolean the state
	 */
	public boolean getExpandedState() {
		if(menu != null) {
			return menu.getStateExpanded();
		}
		return false;
	}
	
	/**
	 * Show the List of the SwipeMenu
	 */
	public void showList() {
		menu.tryChangeExpanded(menu.CHANGE_FROM_BACK);
	}
	
	/**
	 * Sets a new Element to the Gui that should be gerneated (will get build at buildLayout())
	 * @param name the ELements name that should later be generated
	 */
	public void addElement(String name) {
		boolean in = false;
		for(int i= 0; i < items.size(); i++) {
			if(items.get(i).name == name) {
				in = true;
			}
		}
		if(!in) {
			Entry e = SETTINGS.get(name);
			if(e!=null) {
				items.add(new Item(name, e.getGroup()));
				if(!groups.contains(e.getGroup()))groups.add(e.getGroup());
			}
		}
	}
	/**
	 * Build the Layout, can only be called once!
	 */
	public void buildLayout() {
		if(!build) {
			LinearLayout[] lay = new LinearLayout[groups.size()];
			for(int i = 0; i < lay.length;i++) {
				lay[i] = new LinearLayout(CONTEXT);
				lay[i].setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
				lay[i].setOrientation(LinearLayout.VERTICAL);
			}
			for(Item item : items) {
				if(groups.contains(item.group)) {
					Entry e = SETTINGS.get(item.name);
					int i = groups.indexOf(item.group);
					ListElement child = null;
					if(e.isEnable()) {
						child = new Enabler(CONTEXT);
						child.setName(e.NAME);
						((Enabler)child).setText(e.getFullname());
						if(e.getFrom() != null && e.getFrom().length() > 0) {
							child.callStartValue(e.getFrom(), "t");
						}
					} else if(e.isVaule()) {
						child = new Slider(CONTEXT);
						child.setName(e.NAME);
						((Slider)child).setText(e.getFullname());
						if(e.getSteps() > 0) {
							((Slider)child).setRange(e.getMinRange(), e.getMaxRange(), e.getSteps());
						} else {
							((Slider)child).setMax(e.getMaxRange());
							((Slider)child).setMin(e.getMinRange());
						}
						if(e.getFrom() != null && e.getFrom().length() > 0) {
							child.callStartValue(e.getFrom(), "t");
						}
					} else if(e.isCombo()) {
						child = new ComboBox(CONTEXT);
						child.setName(e.NAME);
						((ComboBox)child).setText(e.getFullname());
						if(e.getValues() != null && e.getValues().length() > 0)
							((ComboBox)child).callAcceptedValues(e.getValues());
						if(e.getFrom() != null && e.getFrom().length() > 0)
							child.callStartValue(e.getFrom(), "w");
					} else if(e.isTable()) {
						child = new Table(CONTEXT);
						child.setName(e.NAME);
						if(e.getSteps() > 0) {
							((Table)child).setRange(e.getMinRange(), e.getMaxRange(), e.getSteps());
						} else {
							((Table)child).setRange(e.getMinRange(), e.getMaxRange());
						}
						child.callStartValue(e.getFrom(), " ");
						((Table)child).setText(e.getFullname());
						
					}
					if(child!=null){
						if(lay[i].getChildCount() > 0) {
							lay[i].addView(new Seperator(CONTEXT, Color.GRAY, 3));
						}
						child.setOnChangeListener(this);
						lay[i].addView(child);
						if(e.getDescription() != "") {
							TextViewer tv =new TextViewer(CONTEXT);
							tv.setText(e.getDescription());
							lay[i].addView(tv);
						}
					}//End child!=null
				}
			} // End for 
			for(int i = 0;i < lay.length;i++) {
				Drawable d  = loader.fromName(groups.get(i));
				String group = translator.translate(groups.get(i));
				if(d == null)
					menu.addSubMenu(group, lay[i]);
				else 
					menu.addSubMenu(group, lay[i], d);
			}
			build = true;
		}
	}
	
	public boolean StringToBoolean(String t) {
		if(t.equals("1"))return true;
		else if(t.equals("0"))return false;
		
		return false;
	}
	
	class Item {
		private String name, group;
		
		public Item(String n, String g) {
			name = n;
			group = g;
		}
	}
	@Override
	public void onChange(String name, String value) {
		Entry e = SETTINGS.get(name);
		if(e != null) {
			SETTINGS.setValue(e, value);
		}
	}
}

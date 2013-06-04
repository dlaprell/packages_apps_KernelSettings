package de.firtecy.kernelsetting;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.stericson.RootTools.RootTools;

import de.firtecy.gui.BounceScrollView;
import de.firtecy.settings.CapCommand.OnReturnListener;
import de.firtecy.settings.Settings;
import de.firtecy.util.Feature;
import de.firtecy.util.FeatureSupport;
import de.firtecy.util.FeatureSupport.OnFeaturesListener;
import de.firtecy.util.Utils;


public class MainActivity extends Activity implements ActionBar.OnNavigationListener, OnReturnListener, OnFeaturesListener{

	private static final int RESULT_SETTINGS = 1;
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
	private RelativeLayout main;
	private ArrayAdapter<String> adapter;
	private ActionBar actionBar;
	private GUIControl GUI;
	private Settings settings;
	private boolean access, ready;
	protected Dialog mSplashDialog;
	private int proc, codename;
	private String kernel, device, curTheme;
	private BounceScrollView scroll;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(this);
        String theme = shared.getString("Theme", "");
        
        //Apply the right theme or by default the black one
        if(theme.equals("Light")) {
        	setTheme(R.style.LightTheme);
        	curTheme = "Light";
        } else if(theme.equals("Dark")){
        	setTheme(R.style.DarkTheme);
        	curTheme = "Dark";
        } else {
        	setTheme(R.style.AppTheme);
        	curTheme = "Light";
        	
        }
		
		main = new RelativeLayout(this);
		main.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		scroll = new BounceScrollView(this);
		main.addView(scroll);
		setContentView(main);
		
		
		// Set up the action bar to show a dropdown list.
		actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setIcon(R.drawable.actionbar);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		
        try {
        	Looper.prepare();
        }catch(Exception ex){}
        showSplashScreen();
		
		access = RootTools.isAccessGiven();
        ready = false;
        settings = new Settings(this);
        settings.setOnReturnListener(this);
        GUI = new GUIControl(this, settings);
        
        proc = settings.sendCommand("cat /proc/version");//Get Kernel Version to load Features
        codename = settings.sendCommand("cat /system/build.prop | grep \"ro.product.device\"");//Get Device Codename to load Features
      
	}
	
	protected void removeSplashScreen() {
        if (mSplashDialog != null) {
            try { //Avoid force closes
            	mSplashDialog.dismiss();
            	mSplashDialog = null;
            } catch (Exception ex) {
            	
            }
        }
        if(!access){
        	Toast.makeText(this, "Without the Root permission, no settings could be applied!", Toast.LENGTH_SHORT).show();
        }
    }
	
	/**
     * Shows the splash screen over the full Activity
     */
    protected void showSplashScreen() {
        mSplashDialog = new Dialog(this, R.style.SplashScreen);
        mSplashDialog.setContentView(R.layout.splashscreen);
        mSplashDialog.setCancelable(false);
        try {
        	mSplashDialog.show();
        }catch (Exception ex) {
        }
    }
	
    protected void showAboutScreen() {
        mSplashDialog = new Dialog(this, R.style.SplashScreen);
        mSplashDialog.setContentView(R.layout.splashscreen);
        mSplashDialog.setCancelable(true);
        try {
        	mSplashDialog.show();
        }catch (Exception ex) {
        }
    }
    
	private void init() {
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,  GUI.getGroups());
		
		// Set up the dropdown list navigation in the action bar.
		actionBar.setListNavigationCallbacks(adapter, this);
		
		removeSplashScreen();
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		// Restore the previously serialized current dropdown position.
		if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
			getActionBar().setSelectedNavigationItem(
					savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// Serialize the current dropdown position.
		outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar()
				.getSelectedNavigationIndex());
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	    
	    case R.id.action_settings:
	    	Intent i = new Intent(this, SettingsActivity.class);
	    	startActivityForResult(i, RESULT_SETTINGS);
	        return true;
	        
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
 
        switch (requestCode) {
	        case RESULT_SETTINGS:
	        	SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(this);
	            String theme = shared.getString("Theme", "");
	        	if(curTheme != null && !curTheme.equals(theme)) {
	        		Utils.restartActivity(this);
	        	}
	            break;
	 
        }
 
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onNavigationItemSelected(int position, long id) {
		String group = adapter.getItem(position);
		View v = GUI.getGroup(group);
		
		if(v!= null) {
			scroll.removeAllViews();
			scroll.addView(v);
			return true;
		} else {
			return false;
		}
		
	}
	
	@Override
    protected void onStop() {
       super.onStop();
    }
    
	public String s(int i) {
		return this.getString(i);
	}
	
    private void initilize() {
    	final FeatureSupport fs = new FeatureSupport(this, kernel, device); //Build the Feature support
    	
    	fs.setLis(this);
    	Thread featureSupportThread = new Thread() { //Run in another Thread, so that the main Thread is still availbe
    		public void run() {
                try {
                    runOnUiThread(new Runnable() {
	                    @Override
	                    public void run() {
	                    	try {
	                        	Looper.prepare();
	                        }catch(Exception ex){}
	                    	fs.buildFeatureList(); //Build it from xml
	                    	Log.i(Values.LOG_TAG, "Build feature list");
	                    }
	                });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
    	};
    	featureSupportThread.start();
    }

    public String booleanToString(boolean b) {
    	return b ? "1" : "0";
    }
    
    private void loadContent() {
    	GUI.buildLayout(); //Build the GUI for the first time
    	
    	init();
    	
    }
    
    /**
     * Dummy Link to the GUIControl.addElement(String);
     * @param name
     */
    private void addElement(String name) {
    	GUI.addElement(name);
    }
	
    public void buildFeatures(Feature[]features) {
    	Log.i(Values.LOG_TAG, "Received feature list - try to build GUI");
    	
    	
    	for(Feature item : features) { //Loop through all features
    		
    		//Check if the Items contains all necessary data
    		if(item != null && item.isValid()) {
    			boolean notNull = false;
    			
    			//Add Element from type
    			if(item.getType().equals("switch")) {
    				//Add a Switch to the Control
        			settings.addEnabler(item.getName(), item.getLongname(), item.getCategory(), item.getPath());
        			addElement(item.getName());
        			
        		} else if(item.getType().equals("value")) {
        			//Add a Slider to the Control
        			try {
        				settings.addValue(item.getName(), item.getLongname(), item.getCategory(), item.getPath(), Integer.parseInt(item.getMin()), Integer.parseInt(item.getMax()));
        				addElement(item.getName());
        				
        				if(item.getSteps() != null && item.getSteps().length() > 0) {
        					settings.get(item.getName()).setSteps(item.getSteps());
        				}
        			}catch(Exception ex) {
        				
        			}
        			
        		} else if(item.getType().equals("combo")) {
        			//Add a ComboBox
        			settings.addCombo(item.getName(), item.getLongname(), item.getCategory(), item.getPath(), item.getFrom());
        			addElement(item.getName());
        			
        			//ComboBoxStuff
        			if(item.getValues() != null && item.getValues().length() > 0) {
        				settings.get(item.getName()).setValues(item.getValues());
        			}

        		} else if(item.getType().equals("table")) {
        			settings.addTable(item.getName(), item.getLongname(), item.getCategory(), item.getPath(), item.getFrom());
        			if(item.getMin() != null && item.getMin().length() > 0 && item.getMax() != null && item.getMax().length() > 0) {
        				try{
        					settings.get(item.getName()).setRange(Integer.parseInt(item.getMin()), Integer.parseInt(item.getMax()));
        					
        					if(item.getSteps() != null && item.getSteps().length() > 0) {
            					settings.get(item.getName()).setSteps(item.getSteps());
            				}
        				}catch (Exception ex){}
        			}
        			addElement(item.getName());
        		} else {
        			notNull = true;
        		}
    			
    			
    			//Settings for all Elements:
	    		if(!notNull) {
    				//Check if there should also be a description
	    			if(item.getDescription() != null && item.getDescription().length() > 0) {
	    				settings.get(item.getName()).setDescription(item.getDescription());
	    			}
	    			
	    			
	    			//Check if from can get parsed
	    			if(item.getFrom() != null && item.getFrom().length() > 0 && !item.getType().equals("table")) {
	    				settings.get(item.getName()).setFrom(item.getFrom());
	    			}
	    		}
    		}
    	}
    	features = null; //Clear Space
    	loadContent();
    }
    
	@Override
	public void onReturn(int id, String t) {	
		//When all necessary is there run the initilize progress
		if(device != null && kernel != null && kernel.length() > 0 && device.length() > 0) {
			initilize();
		} else {
			if(id == proc) { //proceed when we get an information back from /proc/version 
				kernel = t;
				Log.i(Values.LOG_TAG, "Kernel Version found: " + kernel);
				
			} else if(id == codename) {
				//If it doesn't contain an # it is a valid output, also there should be the command
				if(!t.contains("#") && !t.equals("cat /system/build.prop | grep \"ro.product.device\"")) {
					
					//Replace the "entry name" we only need the real entry
					device = t.replaceAll("ro.product.device", "");
					device = device.replaceAll("ro.build.product", "");
					
					//Replace unnecessary stuff
					device = device.replaceAll(" ", "");
					device = device.replaceAll("=", "");
					Log.i(Values.LOG_TAG, "Device found: " + device);
				} else {
					//send the "old" build.prop entry
					codename = settings.sendCommand("cat /system/build.prop | grep \"ro.build.product\"");
				}
			}
		}
	}
	
	@Override
	public void allReady(Feature[] features) {
		if(!ready) {
			this.buildFeatures(features);
		}
	}
}

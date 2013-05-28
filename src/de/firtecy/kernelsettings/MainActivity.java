package de.firtecy.kernelsettings;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.stericson.RootTools.RootTools;

import de.firtecy.kernelsettings.R;

import firtecy.android.scripts.DrawableLoader;
import firtecy.android.scripts.Feature;
import firtecy.android.scripts.FeatureSupport;
import firtecy.android.scripts.FeatureSupport.OnFeaturesListener;
import firtecy.android.settings.CapCommand.onReturnListener;
import firtecy.android.settings.Settings;
import firtecy.gui.Base.Enabler;
import firtecy.gui.Base.ListElement.OnChangeListener;
import firtecy.gui.Base.PerformanceSettings;
import firtecy.gui.Base.Seperator;
import firtecy.gui.Base.SwipeMenu.newActivityRemote;
import firtecy.gui.Base.TextViewer;
import firtecy.gui.Base.Values;
 /**
  * 
  * @author David Laprell
  *
  */
public class MainActivity extends Activity implements onReturnListener, OnFeaturesListener, newActivityRemote, OnChangeListener{

	private GUIControl GUI;
	private Settings settings;
	private boolean access, ready;
	protected Dialog mSplashDialog;
	private int proc, codename;
	private String kernel, device;
	public final String LOG_TAG = "AdvancedSettings";
	public final String PREFS_NAME = "App_Preferences";
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
  
        SharedPreferences shared = this.getSharedPreferences(PREFS_NAME, 4);
        String theme = shared.getString("Theme", "Dark");
        
        //Apply the right theme or by default the black one
        if(theme.equals("Light")) {
        	setTheme(R.style.LightTheme);
        } else if(theme.equals("Dark")){
        	setTheme(R.style.DarkTheme);
        } else {
        	setTheme(R.style.DarkTheme);
        }
        
        shared = null;
        
        getActionBar().hide(); //Hide the Action Bar, it is replaced by a custom one from SwipeMenu
        
        showSplashScreen();//Show the SplashScreen instead of a blank activity/layout
        
        access = RootTools.isAccessGiven();
        ready = false;
        settings = new Settings(this);
        settings.setOnReturnListener(this);
        GUI = new GUIControl(this, settings);
        
        proc = settings.sendCommand("cat /proc/version");//Get Kernel Version to load Features
        codename = settings.sendCommand("cat /system/build.prop | grep \"ro.product.device\"");//Get Device Codename to load Features
        
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
	                    	fs.buildFeatureList(); //Build it from xml
	                    	Log.i(LOG_TAG, "Build feature list");
	                    }
	                });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
    	};
    	featureSupportThread.run(); //Run Thread
    }

    public String booleanToString(boolean b) {
    	return b ? "1" : "0";
    }
    
    protected void removeSplashScreen() {
        if (mSplashDialog != null) {
            try { //Avoid force closes
            	mSplashDialog.dismiss();
            	mSplashDialog = null;
            } catch (Exception ex) {
            	
            }
        }
    }
     
    private void loadContent() {
    	GUI.buildLayout(); //Build the GUI for the frist time
    	
    	Enabler e = null;
    	
		GUI.addMenu(s(R.string.Performance), new PerformanceSettings(this),  DrawableLoader.fromName("Performance", this));
		e = new Enabler(this);
    	
    	
    	TextViewer tv = new TextViewer(this);
    	tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, Values.MEDIUMLETTERS);
    	tv.setText("Kernel: \n"+kernel+ "\n\nDevice:"+device);
    	
    	TextViewer tv2 = new TextViewer(this);
    	tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, Values.MEDIUMLETTERS);
    	tv2.setText(s(R.string.owner) + "\n\n" + s(R.string.link));
    	
    	if(e!=null) {
    		e.setName("PerformanceRestore");
	    	e.setText(s(R.string.warrenty));
	    	e.setState(this.getSharedPreferences("Performance", 4).getBoolean("Restore", false));
	    	e.setOnChangeListener(this);
    	}
    	
    	LayoutInflater inflater = LayoutInflater.from(this); //Add the App menu
        LinearLayout v = (LinearLayout)inflater.inflate(R.layout.main_layout, null, false);
        if(e!= null) {
        	v.addView(e);
        }
        v.addView(new Seperator(this, Color.GRAY));
        v.addView(tv);
        v.addView(new Seperator(this, Color.GRAY));
        v.addView(tv2);
        GUI.addMenu("App & " + getResources().getString(R.string.about), v,  DrawableLoader.fromName("App", this));
        
    	
        removeSplashScreen();
        
        Log.i(LOG_TAG, "Finished - Show GUI");
        this.setContentView(GUI.getSwipeMenu()); //get from the GUI Control the right View
        if(!access)Toast.makeText(this, "Can't change settings without root!", Toast.LENGTH_LONG).show();//
        ready = true;
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
    /**
     * Dummy Link to the GUIControl.addElement(String);
     * @param name
     */
    private void addElement(String name) {
    	GUI.addElement(name);
    }
	
    public void buildFeatures(Feature[]features) {
    	Log.i(LOG_TAG, "Received feature list - try to build GUI");
    	
    	if(features == null || features.length == 0)noSupport();//Show no Support if no Feature could be loaded
    	
    	
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
				Log.i(LOG_TAG, "Kernel Version found: " + kernel);
				
			} else if(id == codename) {
				//If it doesn't contain an # it is a valid output, also there should be the command
				if(!t.contains("#") && !t.equals("cat /system/build.prop | grep \"ro.product.device\"")) {
					
					//Replace the "entry name" we only need the real entry
					device = t.replaceAll("ro.product.device", "");
					device = device.replaceAll("ro.build.product", "");
					
					//Replace unnecessary stuff
					device = device.replaceAll(" ", "");
					device = device.replaceAll("=", "");
					Log.i(LOG_TAG, "Device found: " + device);
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
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		//when the back key is pressed and the SwipeMenu is Expanded, we wan't to go back to list
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	    	//Return true and open List if it is Expanded
	        if(GUI != null && GUI.getExpandedState()) {
	        	GUI.showList();
	        	return true;
	        } else { //Else if it is not Expanded, parse it to the super class
	        	return super.onKeyDown(keyCode, event);
	        }
	    } else {
	    	return super.onKeyDown(keyCode, event);
	    }
	}
	
	/**
	 * Shows up an Dialog that display the user, that the Kernel is not supported!
	 */
	public void noSupport() {
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle(s(R.string.nosupport));
		alertDialog.setMessage(" - Kernel - \n"+ kernel + "\n\n - Device - \n" + device);
		alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
		   public void onClick(DialogInterface dialog, int which) {
			   
		   }
		});
		alertDialog.setIcon(R.drawable.icon_launcher);
		alertDialog.show();
	}
	
	/**
	 * Changes the Application Theme (Dark/Light)
	 * @param v
	 */
	public void switchTheme(View v) {
		SharedPreferences shared = this.getSharedPreferences(PREFS_NAME, 4);
        SharedPreferences.Editor editor = shared.edit();
        
        String theme = shared.getString("Theme", "Dark");
        
        if(theme.equals("Light")) {
        	editor.putString("Theme", "Dark");
        } else if(theme.equals("Dark")){
        	editor.putString("Theme", "Light");
        }
        editor.commit();
        Utils.restartActivity(this);
	}
	}

	@Override
	public void onChange(String name, String value) {
		if(name.equals("PerformanceRestore")) {
			SharedPreferences.Editor edit = this.getSharedPreferences("Performance", 4).edit();
			if(value.equals("1")) {
				edit.putBoolean("Restore", true);
			} else if(value.equals("0")) {
				edit.putBoolean("Restore", false);
			}
			edit.commit();
		}
	}
}

/*
*  Copyright 2013 Firtecy
*
*  Licensed under the Apache License, Version 2.0 (the "License");
*  you may not use this file except in compliance with the License.
*  You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing, software
*  distributed under the License is distributed on an "AS IS" BASIS,
*  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*  See the License for the specific language governing permissions and
*  limitations under the License.
*/

package de.firtecy.kernelsettings;

import de.firtecy.kernelsettings.SettingsActivity;
import de.firtecy.kernelsettings.gui.BounceScrollView;
import de.firtecy.kernelsettings.gui.GUIControl;
import de.firtecy.kernelsettings.settings.CapCommand;
import de.firtecy.kernelsettings.settings.Settings;
import de.firtecy.kernelsettings.util.Feature;
import de.firtecy.kernelsettings.util.FeatureSupport;
import de.firtecy.kernelsettings.util.HelpUtils;
import de.firtecy.kernelsettings.util.Utils;
import de.firtecy.kernelsettings.util.Values;
import de.firtecy.kernelsetting.R;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

public class MainActivity extends Activity {
	
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private ActionBar mActionBar;

    
    private static final int RESULT_SETTINGS = 1;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private static GUIControl mGui;
	private Settings mSettings;
	private String[] mGroupTitles;
	
	private int mProc, mCodename;
	private String mKernel, mDevice, mCurrentTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(this);
        String theme = shared.getString("Theme", "");
        
        //Apply the right theme or by default the black one
        if(theme.equals("Light")) {
        	setTheme(R.style.LightTheme);
        	mCurrentTheme = "Light";
        } else if(theme.equals("Dark")){
        	setTheme(R.style.DarkTheme);
        	mCurrentTheme = "Dark";
        } else {
        	setTheme(R.style.AppTheme);
        	mCurrentTheme = "Light";
        	SharedPreferences.Editor editor = shared.edit();
        	editor.putString("Theme", "Light").commit();
        	editor = null;
        }
        
        setContentView(R.layout.activity_main);

        mTitle = mDrawerTitle = getTitle();
        mActionBar = getActionBar();
        
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        
        // enable ActionBar app icon to behave as action to toggle nav drawer
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setHomeButtonEnabled(true);

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
                ) {
            public void onDrawerClosed(View view) {
                mActionBar.setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                mActionBar.setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            selectItem(0);
        }
        
        mSettings = new Settings(this);
        mGui = new GUIControl(this, mSettings);
        
        mSettings.setOnReturnListener(new CapCommand.OnReturnListener() {
			@Override
			public void onReturn(int id, String t) {
				if(mDevice != null && mKernel != null && mKernel.length() > 0 && mDevice.length() > 0) {
					initilize();
				} else {
					if(id == mProc) { //proceed when we get an information back from /proc/version 
						mKernel = t;
						Log.i(Values.LOG_TAG, "Kernel Version found: " + mKernel);
						
					} else if(id == mCodename) {
						//If it doesn't contain an # it is a valid output, also there should be the command
						if(!t.contains("#") && !t.equals("cat /system/build.prop | grep \"ro.product.device\"")) {
							
							//Replace the "entry name" we only need the real entry
							mDevice = t.replaceAll("ro.product.device", "");
							mDevice = mDevice.replaceAll("ro.build.product", "");
							
							//Replace unnecessary stuff
							mDevice = mDevice.replaceAll(" ", "");
							mDevice = mDevice.replaceAll("=", "");
							Log.i(Values.LOG_TAG, "Device found: " + mDevice);
						} else {
							//send the "old" build.prop entry
							mCodename = mSettings.sendCommand("cat /system/build.prop | grep \"ro.build.product\"");
						}
					}
				}
			}
		});
        
        mProc = mSettings.sendCommand("cat /proc/version");//Get Kernel Version to load Features
        mCodename = mSettings.sendCommand("cat /system/build.prop | grep \"ro.product.device\"");//Get Device Codename to load Features
    }

    private void init() {
    	mGui.buildLayout(); //Build the GUI for the first time
    	
    	mGroupTitles = mGui.getGroups();
    	
    	// set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, mGroupTitles));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        
        selectItem(0);
    }
    
    private void initilize() {

    	final FeatureSupport fs = new FeatureSupport(this, mKernel, mDevice); //Build the Feature support
    	
    	fs.setLis(new FeatureSupport.OnFeaturesListener() {
			
			@Override
			public void allReady(Feature[] features) {
				Log.i(Values.LOG_TAG, "Received feature list - try to build GUI");
		    	for(Feature item : features) { //Loop through all features
		    		if(mSettings.addFeature(item)) {
		    			mGui.addElement(item.getName());
		    		}
		    		
		    	}
		    	features = null; //Clear Space
		    	init();
			}
		});
    	
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
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
 
        switch (requestCode) {
	        case RESULT_SETTINGS:
	        	
	        	SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(this);
	            String theme = shared.getString("Theme", "");
	            
	            //Only when it changed it is necessary to restart the activity
	        	if(mCurrentTheme != null && !mCurrentTheme.equals(theme)) {
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

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         // The action bar home/up action should open or close the drawer.
         // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        switch(item.getItemId()) {
	        case R.id.action_settings:
	        	//Initialize the Settings Activity and start it
		    	Intent i = new Intent(this, SettingsActivity.class);
		    	startActivityForResult(i, RESULT_SETTINGS);
		        return true;
		        
	        case R.id.about: //Show the About Dialog!
	        	HelpUtils.showAboutDialog(this);
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
        }
    }

    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
    	if(mGroupTitles != null) {
	        // update the main content by replacing fragments
	        Fragment fragment = new GroupFragment();
	        Bundle args = new Bundle();
	        args.putString(GroupFragment.ARG_ELEMENT_NAME, mGroupTitles[position]);
	        fragment.setArguments(args);
	
	        FragmentManager fragmentManager = getFragmentManager();
	        
	        //First remove all sub views from the Container frame, so we can use it multiple times
	        FrameLayout frame = null;
	        try {
	        	frame = ((FrameLayout)findViewById(R.id.container_fragment));
	        }catch(Exception ex){}
	        
	        if(frame != null && frame.getChildCount() > 0) {
	        	
	        	for(int i = 0;i < frame.getChildCount();i++) {//loop through all Views
	        		View v = frame.getChildAt(i);
	        		try {
	        			((ViewGroup)v).removeAllViews(); // try to remove all sub layouts
	        		}catch (Exception ex){}
	        	}
	        	
	        }
	        
	        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
	
	        // update selected item and title, then close the drawer
	        mDrawerList.setItemChecked(position, true);
	        setTitle(mGroupTitles[position]);
	        mDrawerLayout.closeDrawer(mDrawerList);
    	}
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        if(mActionBar == null)
        	mActionBar = getActionBar();
        mActionBar.setTitle(mTitle);
    }

    private static View getFragmentView(String name) {
    	if(mGui != null)
    		return mGui.getGroup(name);
    	else 
    		return null;
    }
    
    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    /**
     * Fragment that appears in the "content_frame", shows a Settings group
     */
    public static class GroupFragment extends Fragment {
        public static final String ARG_ELEMENT_NAME = "group_name";

        public GroupFragment() {
            // Empty constructor required for fragment subclasses
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_group, container, false);
            
            //get the container in the rootView
            FrameLayout frame = ((FrameLayout)rootView.findViewById(R.id.container_fragment));         
            String name = getArguments().getString(ARG_ELEMENT_NAME);
            
            BounceScrollView scroll = new BounceScrollView(rootView.getContext());
            
            View v = getFragmentView(name);
            
            //If an valid Settings Group is returned we will add it 
            if(v!=null)
            	scroll.addView(v);
            
            //Remove all View so we can use them again
            frame.removeAllViews();
            frame.addView(scroll);
            
            getActivity().setTitle(name);
            return rootView;
            
        }
    }
}
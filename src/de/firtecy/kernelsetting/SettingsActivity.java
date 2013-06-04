package de.firtecy.kernelsetting;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.MenuItem;
 
public class SettingsActivity extends PreferenceActivity {
 
    @SuppressWarnings("deprecation")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getActionBar().setDisplayHomeAsUpEnabled(true);
        this.getActionBar().setHomeButtonEnabled(true);
        
        addPreferencesFromResource(R.xml.settings);
 
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
            	case android.R.id.home:
            		this.finish();
            		//this.finishActivity(MainActivity.RESULT_SETTINGS);
            		break;
            }
            return true;
    }
    
}
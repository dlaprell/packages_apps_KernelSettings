package de.firtecy.kernelsettings;

import android.app.Activity;
import android.content.Intent;

/**
 * 
 * @author David Laprell
 *
 */
public class Utils
{

     /**
      * Set the theme of the Activity, and restart it by creating a new Activity of the same type.
      */
     public static void restartActivity(Activity activity)
     {
          Intent intent = activity.getIntent();
          activity.overridePendingTransition(0, 0);
          intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

          activity.finish();
          
          activity.overridePendingTransition(0, 0);
          activity.startActivity(new Intent(activity, activity.getClass()));
     }
}
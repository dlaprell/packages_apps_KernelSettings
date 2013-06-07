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
package de.firtecy.kernelsettings.util;

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
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

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author David Laprell
 *
 */
public class Values {
	public final static int TEXTSIZE = 15;
	public final static int MEDIUMLETTERS = 18;
	public final static int BIGLETTERS = 20;
	
	public static final String PREFS_KERNEL = "Kernel";
	public static final String PREFS_RESTORE = "Restore";
	public static final String PREFS_APP = "App_Preferences";
	public static final String LOG_TAG = "KernelSettings";
	
	private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);
	
	
	//Copied from API Level 17 to work on older API
	/**
	 * Generate a value suitable for use in a Views {@link #setId(int)}.
	 * This value will not collide with ID values generated at build time by aapt for R.id.
	 *
	 * @return a generated ID value
	 */
	public static int generateViewId() {
	    for (;;) {
	        final int result = sNextGeneratedId.get();
	        // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
	        int newValue = result + 1;
	        if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
	        if (sNextGeneratedId.compareAndSet(result, newValue)) {
	            return result;
	        }
	    }
	}
}

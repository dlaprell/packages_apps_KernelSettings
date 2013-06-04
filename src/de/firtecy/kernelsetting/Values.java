package de.firtecy.kernelsetting;

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

package firtecy.gui.Base;

import java.util.ArrayList;

import firtecy.android.settings.CapCommand.onReturnListener;
import firtecy.android.settings.Entry;
import firtecy.android.settings.Terminal;
import firtecy.gui.Base.ListElement.OnChangeListener;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.util.TypedValue;
import android.widget.LinearLayout;

/**
 * @author David Laprell
 * 
 */
public class PerformanceSettings extends LinearLayout implements OnChangeListener, onReturnListener{

	
	private ArrayList<Entry>settings;
	private Terminal term;
	private ComboBox cpumin, cpumax, screenmax, governor, scheduler, sched;
	private Table uv;
	//private int max, min, steps;
	private boolean schedA, screenmaxA;
	/**
	 * @param context Context to apply, t boolean show sched option, s boolean show Screen max CPU
	 */
	public PerformanceSettings(Context context, boolean t, boolean s) {
		super(context);
		schedA = t;
		screenmaxA = s;
		this.setOrientation(VERTICAL);
		this.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		settings = new ArrayList<Entry>();
		term = new Terminal(true);
		term.setOnReturnListener(this);
		init();
	}
	
	public PerformanceSettings (Context context) {
		this(context, false, false);
	}
	
	private void init() {
		LayoutParams params= new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		
		TextViewer tv = new TextViewer(this.getContext());
		tv.setTypeface(Typeface.BOLD);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, Values.MEDIUMLETTERS);
		tv.setText("CPU");
		tv.setPadding(10, 3, 10, 3);
		
		//Cpu min Freq
		cpumin = new ComboBox(this.getContext());
		cpumin.setText("Min. Frequency");
		cpumin.callAcceptedValues("cat /sys/devices/system/cpu/cpu0/cpufreq/scaling_available_frequencies");
		cpumin.setName("cpumin");
		cpumin.setOnChangeListener(this);
		addEntry("cpumin", "echo !VALUE! > /sys/devices/system/cpu/cpu0/cpufreq/scaling_min_freq;echo !VALUE! > /sys/devices/system/cpu/cpu1/cpufreq/scaling_min_freq", 2);
		
		//Cpu max Freq
		cpumax = new ComboBox(this.getContext());
		cpumax.setText("Max. Frequency");
		cpumax.callAcceptedValues("cat /sys/devices/system/cpu/cpu0/cpufreq/scaling_available_frequencies");
		cpumax.setName("cpumax");
		cpumax.setOnChangeListener(this);
		addEntry("cpumax", "echo !VALUE! > /sys/devices/system/cpu/cpu0/cpufreq/scaling_max_freq;echo !VALUE! > /sys/devices/system/cpu/cpu1/cpufreq/scaling_max_freq;echo !VALUE! > /sys/module/cpu_tegra/parameters/cpu_user_cap", 3);
		
		if(screenmaxA) {
			//Cpu max Screen off Freq
			screenmax = new ComboBox(this.getContext());
			screenmax.setText("Max. screen off Frequency");
			screenmax.callAcceptedValues("cat /sys/devices/system/cpu/cpu0/cpufreq/scaling_available_frequencies");
			screenmax.setName("screenmax");
			screenmax.setOnChangeListener(this);
			addEntry("screenmax", "echo !VALUE! > /sys/devices/system/cpu/cpu0/cpufreq/screen_off_max_freq;echo !VALUE! > /sys/devices/system/cpu/cpu1/cpufreq/screen_off_max_freq", 4);
		}
	
		//Cpu max Screen off Freq
		governor = new ComboBox(this.getContext());
		governor.setText("Governor");
		governor.callAcceptedValues("cat /sys/devices/system/cpu/cpu0/cpufreq/scaling_available_governors");
		governor.setName("governor");
		governor.setOnChangeListener(this);
		addEntry("governor", "echo !VALUE! > /sys/devices/system/cpu/cpu0/cpufreq/scaling_governor;echo !VALUE! > /sys/devices/system/cpu/cpu1/cpufreq/scaling_governor", 5);
		
		if(schedA) {
			//Sched settings
			sched = new ComboBox(this.getContext());
			sched.setText("Sched power saving mode");
			sched.setAcceptedValues("No power saving load balance;Fill one thread/core/package first for long running threads;bias task wakeups to semi-idle cpu package", ";");
			sched.setReturnIndex(true);
			sched.setName("sched");
			//governor.callStartValue("cat /sys/devices/system/cpu/sched_mc_power_savings", "");
			sched.setOnChangeListener(this);
			addEntry("sched", "echo !VALUE! > /sys/devices/system/cpu/sched_mc_power_savings", 6);
		}
		
		//Scheduler Settings
		scheduler = new ComboBox(this.getContext());
		scheduler.setText("I/O Scheduler");
		scheduler.setName("scheduler");
		scheduler.callAcceptedValues("cat /sys/block/mmcblk0/queue/scheduler");
		scheduler.setOnChangeListener(this);
		addEntry("scheduler", "echo \"!VALUE!\" > /sys/block/mmcblk0/queue/scheduler", 7);
		
		//UV Table
		uv = new Table(this.getContext());
		uv.setOnChangeListener(this);
		uv.setText("Voltage Table (in mV)");
		uv.setName("uv");
		uv.setRange(700, 1300, 10);
		uv.callStartValue("cat /sys/devices/system/cpu/cpu0/cpufreq/UV_mV_table", "");
		addEntry("uv", "echo \"!VALUE!\" > /sys/devices/system/cpu/cpu0/cpufreq/UV_mV_table;echo \"!VALUE!\" > /sys/devices/system/cpu/cpu1/cpufreq/UV_mV_table", 7);
		
		//Get current Values
		term.sendCommand(3, "cat /sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_min_freq", true);
		term.sendCommand(4, "cat /sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq", true);
		term.sendCommand(5, "cat /sys/devices/system/cpu/cpu0/cpufreq/scaling_governor", true);
		term.sendCommand(6, "cat /sys/devices/system/cpu/cpu0/cpufreq/screen_off_max_freq", true);
		term.sendCommand(7, "cat /sys/devices/system/cpu/sched_mc_power_savings/", true);
		term.sendCommand(8, "cat /sys/block/mmcblk0/queue/scheduler", true);
	
		//Add all to the layout
		this.addView(tv, params);
		this.addView(cpumin, params);
		this.addView(cpumax, params);
		if(screenmaxA)this.addView(screenmax, params);
		this.addView(governor, params);
		this.addView(new Seperator(this.getContext(), Color.DKGRAY));
		if(schedA)this.addView(sched);
		this.addView(scheduler);
		this.addView(new Seperator(this.getContext(), Color.DKGRAY));
		this.addView(uv);
	}
	
	private void addEntry(String name, String path, int id) {
		Entry e = new Entry(name, Entry.TABLE);
		e.setPath(path);
		e.setId(id);
		settings.add(e);
	}
	
	@Override
	public void onChange(String name, String value) {
		Entry e = null;
		for(int i = 0;i < settings.size();i++) {
			if(settings.get(i).NAME.equals(name)) {
				e = settings.get(i);
				break;
			}
		}
		if(e!= null) {
			String path = e.getPath();
			if(path != null && path.length() > 0) {
				path = e.getPath().replaceAll("!VALUE!", value);
				SharedPreferences shared = this.getContext().getSharedPreferences("Performance", 4);
				SharedPreferences.Editor editor = shared.edit();
				if(shared.getString("Values", "").contains(name)) {
					editor.putString(name, path);
				} else {
					editor.putString("Values", shared.getString("Values", "") + ";" +name);
					editor.putString(name, path);
				}
				editor.commit();
				terminal(path);
			}
		}
	}
	
	private void terminal(String t) {
		if(t.contains(";")) {
			String[] cmd = t.split(";");
			term.sendCommand(1, cmd);
			for(String s: cmd) {
				Log.i("KS", "Shell: " + s);
			}
		} else {
			term.sendCommand(1, t);
			Log.i("KS", "Shell: " + t);
		}
	}

	@Override
	public void onReturn(int id, String t) {
		if(t != null) {
			switch(id) {
			case 3:
				cpumin.setValue(t);
				break;
			case 4:
				cpumax.setValue(t);
				break;
			case 5:
				if(governor!=null)governor.setValue(t);
				break;
			case 6:
				if(screenmax!=null)screenmax.setValue(t);
				break;
			case 7:
				if(sched!=null)sched.setValue(t);
				break;
			case 8:
				t = t.substring(t.indexOf("[") + 1);
				t = t.substring(0, t.indexOf("]"));
				if(scheduler!=null)scheduler.setValue(t);
				break;
			}
		}
	}
}

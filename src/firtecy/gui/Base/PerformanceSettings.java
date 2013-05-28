package firtecy.gui.Base;

import java.io.File;
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
	private String[] cpus;
	private final String base = "/sys/devices/system/cpu/";
	
	/**
	 * @param context Context
	 */
	public PerformanceSettings(Context context) {
		super(context);
		this.setOrientation(VERTICAL);
		this.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		
		settings = new ArrayList<Entry>();
		term = new Terminal(true);
		term.setOnReturnListener(this);
		
		File main = new File("/sys/devices/system/cpu/");
		
		if(main.isDirectory()) { //Get CPU count
			int i;
			for(i = 0;new File("/sys/devices/system/cpu/cpu" + i).exists();i++);
			cpus = new String[i + 1];
			for(int x = 0;x <= i;x++) {
				cpus[x] = "/sys/devices/system/cpu/cpu" + x;
			}
			init();
		}
	}
	
	private void init() {
		LayoutParams params= new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		
		TextViewer tv = new TextViewer(this.getContext());
		tv.setTypeface(Typeface.BOLD);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, Values.MEDIUMLETTERS);
		tv.setText("CPU");
		tv.setPadding(10, 3, 10, 3);
		this.addView(tv, params);
		
		if(new File(base + "cpu0/cpufreq/scaling_min_freq").exists()) {
			//Cpu min Freq
			cpumin = new ComboBox(this.getContext());
			cpumin.setText("Min. Frequency");
			cpumin.callAcceptedValues("cat " + base + "cpu0/cpufreq/scaling_available_frequencies");
			cpumin.setName("cpumin");
			cpumin.setOnChangeListener(this);
			addEntry("cpumin", CPU("echo !VALUE! > ", "cpufreq/scaling_min_freq"), 1);
			
			this.addView(cpumin, params);
			term.sendCommand(1, "cat /sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_min_freq", true);
		}
		
		if(new File(base + "cpu0/cpufreq/scaling_max_freq").exists()) {
			//Cpu max Freq
			cpumax = new ComboBox(this.getContext());
			cpumax.setText("Max. Frequency");
			cpumax.callAcceptedValues("cat /sys/devices/system/cpu/cpu0/cpufreq/scaling_available_frequencies");
			cpumax.setName("cpumax");
			cpumax.setOnChangeListener(this); 
			addEntry("cpumax", CPU("echo !VALUE! > ", "cpufreq/scaling_max_freq"), 2);
			
			this.addView(cpumax, params);
			term.sendCommand(2, "cat /sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq", true);
		}
		
		if(new File(base + "cpu0/cpufreq/screen_off_max_freq").exists()) {
			//Cpu max Screen off Freq
			screenmax = new ComboBox(this.getContext());
			screenmax.setText("Max. screen off Frequency");
			screenmax.callAcceptedValues("cat /sys/devices/system/cpu/cpu0/cpufreq/scaling_available_frequencies");
			screenmax.setName("screenmax");
			screenmax.setOnChangeListener(this);
			addEntry("screenmax", CPU("echo !VALUE! > ", "cpufreq/screen_off_max_freq"), 3);
			
			this.addView(screenmax, params);
			term.sendCommand(3, "cat /sys/devices/system/cpu/cpu0/cpufreq/screen_off_max_freq", true);
		}
		
		if(new File(base + "cpu0/cpufreq/scaling_governor").exists()) {
			//Governor
			governor = new ComboBox(this.getContext());
			governor.setText("Governor");
			governor.callAcceptedValues("cat /sys/devices/system/cpu/cpu0/cpufreq/scaling_available_governors");
			governor.setName("governor");
			governor.setOnChangeListener(this);
			addEntry("governor", CPU("echo !VALUE! > ", "cpufreq/scaling_governor"), 4);
			
			this.addView(governor, params);
			this.addView(new Seperator(this.getContext(), Color.DKGRAY));
			term.sendCommand(4, "cat /sys/devices/system/cpu/cpu0/cpufreq/scaling_governor", true);
			
		}
		
		if(new File("/sys/devices/system/cpu/sched_mc_power_savings").exists()) {
			//Sched settings
			sched = new ComboBox(this.getContext());
			sched.setText("Sched power saving mode");
			sched.setAcceptedValues("No power saving load balance;Fill one thread/core/package first for long running threads;bias task wakeups to semi-idle cpu package", ";");
			sched.setReturnIndex(true);
			sched.setName("sched");
			sched.setOnChangeListener(this);
			addEntry("sched", "echo !VALUE! > /sys/devices/system/cpu/sched_mc_power_savings", 5);
			
			this.addView(sched);
			this.addView(new Seperator(this.getContext(), Color.DKGRAY));
			term.sendCommand(5, "cat /sys/devices/system/cpu/sched_mc_power_savings", true);
		}
		
		if(new File("/sys/block/mmcblk0/queue/scheduler").exists()) {
			//Scheduler Settings
			scheduler = new ComboBox(this.getContext());
			scheduler.setText("I/O Scheduler");
			scheduler.setName("scheduler");
			scheduler.callAcceptedValues("cat /sys/block/mmcblk0/queue/scheduler");
			scheduler.setOnChangeListener(this);
			addEntry("scheduler", "echo \"!VALUE!\" > /sys/block/mmcblk0/queue/scheduler", 6);
			
			this.addView(scheduler);
			this.addView(new Seperator(this.getContext(), Color.DKGRAY));
			term.sendCommand(6, "cat /sys/block/mmcblk0/queue/scheduler", true);
		}
		
		if(new File("/sys/devices/system/cpu/cpu0/cpufreq/UV_mV_table").exists()) {
			//UV Table
			uv = new Table(this.getContext());
			uv.setOnChangeListener(this);
			uv.setText("Voltage Table (in mV)");
			uv.setName("uv");
			uv.setRange(700, 1300, 10);
			uv.callStartValue("cat /sys/devices/system/cpu/cpu0/cpufreq/UV_mV_table", "");
			addEntry("uv", CPU("echo !VALUE! > ", "cpufreq/UV_mV_table"), 7);
			
			this.addView(uv);
			term.sendCommand(7, "cat /sys/block/mmcblk0/queue/scheduler", true);
		}
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

	private String CPU (String before, String after) {
		String r = "";
		for(String t :cpus) {
			if(r.length()>0)r += ";";
			r += before + t + after;
		}
		return r;
	}
	
	@Override
	public void onReturn(int id, String t) {
		if(t != null) {
			switch(id) {
			case 1:
				if(cpumin!=null)cpumin.setValue(t);
				break;
			case 2:
				if(cpumax!=null)cpumax.setValue(t);
				break;
			case 3:
				if(governor!=null)governor.setValue(t);
				break;
			case 4:
				if(screenmax!=null)screenmax.setValue(t);
				break;
			case 5:
				if(sched!=null)sched.setValue(t);
				break;
			case 6:
				t = t.substring(t.indexOf("[") + 1);
				t = t.substring(0, t.indexOf("]"));
				if(scheduler!=null)scheduler.setValue(t);
				break;
			}
		}
	}
}

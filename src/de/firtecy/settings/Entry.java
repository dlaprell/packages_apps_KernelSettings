package de.firtecy.settings;

/**
 * @author David Laprell
 */
public class Entry {
	
	public final String NAME;
	public final static byte ENABLE = -1;
	public final static byte VALUE = -2;
	public final static byte COMBO = -3;
	public final static byte TABLE = -4;
	private String fullname, path, from, group, description, value, values;
	private int minRange, maxRange, steps, id;

	/**
	 * @return the fullname
	 */
	public String getFullname() {
		return fullname;
	}

	/**
	 * @param fullname the fullname to set
	 */
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @return the from
	 */
	public String getFrom() {
		return from;
	}

	/**
	 * @param from the from to set
	 */
	public void setFrom(String from) {
		this.from = from;
	}

	/**
	 * @return the group
	 */
	public String getGroup() {
		return group;
	}

	/**
	 * @param group the group to set
	 */
	public void setGroup(String group) {
		this.group = group;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the steps
	 */
	public int getSteps() {
		return steps;
	}

	/**
	 * @return the minRange
	 */
	public int getMinRange() {
		return minRange;
	}

	/**
	 * @return the maxRange
	 */
	public int getMaxRange() {
		return maxRange;
	}

	/**
	 * @param steps the steps to set
	 */
	public void setSteps(String step) {
		int steps = 1;
		try {
			steps = Integer.parseInt(step);
		} catch(Exception ex){}
		this.steps = steps;
	}

	/**
	 * @return the type
	 */
	public byte getType() {
		return type;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	private byte type;
	
	public Entry(String n, byte t) {
		NAME = n;
		type = t;
		description = "";
		path = "";
		from = "";
		group = "";
		steps = 1;
		minRange = 0;
		maxRange = 1;
		
	}
	
	public boolean isEnable(){
		if(type == ENABLE) return true;
		else return false;
	}
	
	public boolean isVaule(){
		if(type == VALUE) return true;
		else return false;
	}
	
	public boolean isCombo(){
		if(type == COMBO) return true;
		else return false;
	}
	
	public boolean isTable(){
		if(type == TABLE) return true;
		else return false;
	}

	/**
	 * 
	 * @param min the min Value
	 * @param max the max Value
	 */
	public void setRange(int min, int max) {
		this.maxRange = max;
		this.minRange = min;
	}

	/**
	 * @param value the value to set
	 * @return success
	 */
	public boolean setValue(String val) {
		if(this.type == Entry.ENABLE) {
			if(val.equals("1") || val.equals("0")) {
				this.value = val;
				return true;
			}
		} else if(this.type == Entry.VALUE) {
			int i;
			try {i = Integer.parseInt(val);} catch(Exception ex){i = minRange + 10;}
			if(i >= minRange && i <= maxRange) {
				this.value = val;
				return true;
			}
		} else if(this.type == Entry.COMBO) {
			this.value = val;
			return true;
		} else if(this.type == Entry.TABLE) {
			this.value = val;
			return true;
		}
		
		return false;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the values
	 */
	public String getValues() {
		return values;
	}

	/**
	 * @param values the values to set
	 */
	public void setValues(String values) {
		this.values = values;
	}
}

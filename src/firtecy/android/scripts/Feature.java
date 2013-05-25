package firtecy.android.scripts;

/**
 * @author David Laprell
 *
 */
public class Feature {
	private String name, longname, type, path, from, min, max, category, description, steps, values;

	public Feature() {
		name = "";
		longname = "";
		type = "";
		path = "";
		from = "";
		min = "";
		max = "";
		category = "";
		description = "";
		steps = "";
	}
	
	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

	public boolean isValid() {
		return(name != null && longname != null && category != null && type != null && name.length() > 0 && longname.length() > 0 && category.length() > 0 && type.length() > 0);
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the longname
	 */
	public String getLongname() {
		return longname;
	}

	/**
	 * @param longname the longname to set
	 */
	public void setLongname(String longname) {
		this.longname = longname;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
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
	 * @return the min
	 */
	public String getMin() {
		return min;
	}

	/**
	 * @param min the min to set
	 */
	public void setMin(String min) {
		this.min = min;
	}

	/**
	 * @return the max
	 */
	public String getMax() {
		return max;
	}

	/**
	 * @param max the max to set
	 */
	public void setMax(String max) {
		this.max = max;
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
	public String getSteps() {
		return steps;
	}

	/**
	 * @param steps the steps to set
	 */
	public void setSteps(String steps) {
		this.steps = steps;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
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
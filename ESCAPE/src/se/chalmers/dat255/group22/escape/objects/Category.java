package se.chalmers.dat255.group22.escape.objects;

/**
 * Simple object class to represent a category.
 * 
 * @author Johanna & Mike
 */
public class Category {

	private String name;

	// TODO Colors are now saved as string, change in further implementations
	private String baseColor;
	private String importantColor;
	// Bool used in listviews. true if this category should be displayed. Note
	// that this should not be saved inte database.
	private boolean shouldBeDisplayed;

	/**
	 * Creates a new category
	 * 
	 * @param name
	 *            the category name
	 * @param baseColor
	 *            the base color used for this category
	 * @param importantColor
	 */
	public Category(String name, String baseColor, String importantColor) {
		this.name = name;
		this.baseColor = baseColor;
		this.importantColor = importantColor;
		this.shouldBeDisplayed = true;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the baseColor
	 */
	public String getBaseColor() {
		return baseColor;
	}

	/**
	 * @return the importantColor
	 */
	public String getImportantColor() {
		return importantColor;
	}

	/**
	 * A boolean for if this category should be displayed or not. Default value
	 * is true. Note that this variable should not be saved into database!
	 * 
	 * @return true if category should be displayed
	 */
	public boolean getShouldBeDisplayed() {
		return shouldBeDisplayed;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param baseColor
	 *            the baseColor to set
	 */
	public void setBaseColor(String baseColor) {
		this.baseColor = baseColor;
	}

	/**
	 * @param importantColor
	 *            the importantColor to set
	 */
	public void setImportantColor(String importantColor) {
		this.importantColor = importantColor;
	}

	/**
	 * A boolean for if this category should be displayed or not. Default value
	 * is true. Note that this variable should not be saved into database!
	 * 
	 * @param display
	 *            true if category should be displayed
	 */
	public void setShouldBeDisplayed(boolean display) {
		this.shouldBeDisplayed = display;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Category [name=" + name + ", baseColor=" + baseColor
				+ ", importantColor=" + importantColor + "]";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Category other = (Category) obj;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}
}

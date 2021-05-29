package net.haffel.PL.news;

import lombok.Getter;

/**
 * Template class
 * 
 * @since v1.1
 * @lastEdit v1.1
 */
public class Change
{
	@Getter String language, version, date, changes;

	/**
	 * @param version
	 *            Plugin Version
	 * @param date
	 *            Date when this version got released
	 * @param changes
	 *            All changes in this version
	 */
	public Change(String language, String version, String date, String changes)
	{
		this.language = language;
		this.version = version;
		this.date = date;
		this.changes = changes;
	}
}

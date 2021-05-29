package net.haffel.PL.exceptions;

/**
 * Template class
 * 
 * @since v1.1
 * @lastEdit v1.1
 */
public class ChangeNotFoundException extends Exception
{
	private static final long serialVersionUID = 8384502843226100499L;

	/**
	 * @param change
	 *            Name of the change which wasn't found
	 */
	public ChangeNotFoundException(String change)
	{
		super("Change " + change + " not found");
	}
}

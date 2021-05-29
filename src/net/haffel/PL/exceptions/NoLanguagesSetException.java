package net.haffel.PL.exceptions;

/**
 * Template class
 * 
 * @since v1.1
 * @lastEdit v1.1
 */
public class NoLanguagesSetException extends Exception
{
	private static final long serialVersionUID = -4907795414264455638L;

	public NoLanguagesSetException()
	{
		super("There are no default languages set");
	}
}

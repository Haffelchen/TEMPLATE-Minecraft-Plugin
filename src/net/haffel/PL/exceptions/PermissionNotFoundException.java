package net.haffel.PL.exceptions;

/**
 * Template class
 * 
 * @since v1.1
 * @lastEdit v1.1
 */
public class PermissionNotFoundException extends Exception
{
	private static final long serialVersionUID = 3795381861496248891L;

	/**
	 * @param permission
	 *            Name of the permission which wasn't found
	 */
	public PermissionNotFoundException(String permission)
	{
		super("Permission " + permission + " not found");
	}
}

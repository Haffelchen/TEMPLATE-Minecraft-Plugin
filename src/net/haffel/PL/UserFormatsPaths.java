package net.haffel.PL;

/**
 * Template class
 * 
 * @since v1.1
 * @lastEdit v1.1
 */
public enum UserFormatsPaths
{
	DEBUG_NOTIFY("DebugNotify"),
	LANGUAGE("Language");

	private final String path;

	UserFormatsPaths(String path)
	{
		this.path = path;
	}

	public String getPath()
	{
		return path;
	}
}

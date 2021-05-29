package net.haffel.PL;

import org.bukkit.ChatColor;

/**
 * Template class
 * 
 * @since v1.1
 * @lastEdit v1.1 TODO Replace template stuff
 */
public enum NoneTranslatedMessages
{
	CONSOLE_TEXT_BEGINNING_ENABLE(ChatColor.AQUA + "<---=-=-=-=-=-=-=-=-=-==TEMPLATE==-=-=-=-=-=-=-=-=-=-->"),
	CONSOLE_TEXT_ENDING_ENABLE(ChatColor.AQUA + "<---=-=-=-=-=-=-=-=-=-==Started==-=-=-=-=-=-=-=-=-=--->"),
	CONSOLE_TEXT_BEGINNING_DISABLE(ChatColor.RED + "<---=-=-=-=-=-=-=-=-=-==TEMPLATE==-=-=-=-=-=-=-=-=-=-->"),
	CONSOLE_TEXT_ENDING_DISABLE(ChatColor.RED + "<---=-=-=-=-=-=-=-=-=-==Stopped==-=-=-=-=-=-=-=-=-=--->"),
	CONSOLE_TEXT_ENDING(ChatColor.AQUA + "<---=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=--->"),
	REPORT_TO_ADMIN("§c§lAn error occurred. Please report this to an administrator"),
	REPORT_TO_ADMIN_WITH_INDEX("§c§lAn error with §e§lIndex {INDEX} §c§loccurred. Please report this to an administrator"),
	UPDATER_API_PATH("https://api.spigotmc.org/legacy/update.php?resource=" + TEMPLATE.getProjectID()),
	UPDATER_RESOURCE_PATH("https://www.spigotmc.org/resources/" + TEMPLATE.getProjectID());
	
	private final String msg;

	NoneTranslatedMessages(String msg)
	{
		this.msg = msg;
	}

	public String getMessage()
	{
		return msg;
	}
}

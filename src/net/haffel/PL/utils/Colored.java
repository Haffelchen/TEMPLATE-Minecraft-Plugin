package net.haffel.PL.utils;

import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;

import lombok.Getter;
import net.haffel.PL.TEMPLATE;

/**
 * Template class
 * 
 * @since v1.1
 * @lastEdit v1.1
 */
public class Colored
{
	@Getter private static final int width = 52;

	/**
	 * @param text
	 *            Text which will be printed to the console
	 * @param color
	 *            Color for the start and end symbols
	 */
	public static void toConsole(String text, ChatColor color)
	{
		toConsole("", text, color, true, "");
	}

	/**
	 * @param entrySpace
	 *            Indentation space befor the text
	 * @param text
	 *            Text which will be printed to the console
	 * @param color
	 *            Color for the start and end symbols
	 */
	public static void toConsole(String entrySpace, String text, ChatColor color)
	{
		toConsole(entrySpace, text, color, true, "");
	}

	/**
	 * @param text
	 *            Text which will be printed to the console
	 * @param color
	 *            Color for the start and end symbols
	 * @param send
	 *            If true, text will be printed, if false, fallbackText will be printed
	 * @param fallbackText
	 *            Fallback text if send is false
	 */
	public static void toConsole(String text, ChatColor color, boolean send, String fallbackText)
	{
		toConsole("", text, color, send, fallbackText);
	}

	/**
	 * @param entrySpace
	 *            Indentation space befor the text
	 * @param text
	 *            Text which will be printed to the console
	 * @param color
	 *            Color for the start and end symbols
	 * @param send
	 *            If true, text will be printed, if false, fallbackText will be printed
	 * @param fallbackText
	 *            Fallback text if send is false
	 */
	public static void toConsole(String entrySpace, String text, ChatColor color, boolean send, String fallbackText)
	{
		toConsole(entrySpace, text, color, send, fallbackText, true);
	}

	/**
	 * @param entrySpace
	 *            Indentation space befor the text
	 * @param text
	 *            Text which will be printed to the console
	 * @param color
	 *            Color for the start and end symbols
	 * @param send
	 *            If true, text will be printed, if false, fallbackText will be printed
	 * @param fallbackText
	 *            Fallback text if send is false
	 * @param sendFallback
	 *            If false, nothing will be printed
	 */
	public static void toConsole(String entrySpace, String text, ChatColor color, boolean send, String fallbackText, boolean sendFallback)
	{
		if(send && !text.isEmpty())
		{
			int entryLength = entrySpace.isEmpty() ? 1 : entrySpace.length() + 3;

			String[] wrapped = WordUtils.wrap(text, (getWidth() - entryLength), null, true).split(System.getProperty("line.separator"));

			for(int i = 0; i < wrapped.length; i++)
			{
				if(i == 0 && text.startsWith(entrySpace))
				{
					TEMPLATE.getInstance().getServer().getConsoleSender().sendMessage(color + "|" + ChatColor.RESET
							+ String.format("%" + 1 + "s", " ") + String.format("%-" + getWidth() + "s", wrapped[i]) + color + "|");
				} else
				{
					TEMPLATE.getInstance().getServer().getConsoleSender()
							.sendMessage(color + "|" + ChatColor.RESET + String.format("%" + entryLength + "s", " ")
									+ String.format("%-" + (getWidth() + 1 - entryLength) + "s", wrapped[i]) + color + "|");
				}
			}
		} else if(sendFallback && !fallbackText.isEmpty())
		{
			int entryLength = entrySpace.isEmpty() ? 1 : entrySpace.length() + 3;

			TEMPLATE.getInstance().getServer().getConsoleSender().sendMessage(color + "|" + ChatColor.RESET
					+ String.format("%" + entryLength + "s", " ") + String.format("%-" + (getWidth() + 1 - entryLength) + "s", text) + color + "|");
		}
	}
}

package net.haffel.PL.files;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.haffel.PL.TEMPLATE;
import net.haffel.PL.exceptions.NoLanguagesSetException;
import net.haffel.PL.utils.Colored;
import net.haffel.PL.utils.Debug;
import net.haffel.PL.utils.DebugTiming;
import net.haffel.ml.exceptions.LanguageFileNotFoundException;
import net.haffel.ml.files.UserFormatFile;
import net.haffel.ml.translate.TranslateHandler;

/**
 * Template class
 * 
 * @since v1.1
 * @lastEdit v1.1
 */
public class LanguageSettings
{
	/**
	 * Load language settings
	 * @throws NoLanguagesSetException No languages are set from the plugin developer and custom languages are disabled by the server owner
	 */
	public static void load(boolean debugEnabled)
	{
		try
		{
			long start = System.currentTimeMillis();
			boolean debug = (boolean) Config.getConfigSettings().get(ConfigSettings.DEBUG_AT_START_LANGUAGE) && debugEnabled;
			boolean fullDebug = (boolean) Config.getConfigSettings().get(ConfigSettings.DEBUG_AT_START_DETAILED) && debug;

			Colored.toConsole("Language: Loading language settings..", ChatColor.AQUA, debug, "");
			TEMPLATE.getInstance().saveResource("languages/language_TEMPLATE.lang", true);
			TEMPLATE.getInstance().saveResource("languages/language_de.lang", true);

			Colored.toConsole("Language", "Created template file", ChatColor.AQUA, fullDebug, "");

			if(TEMPLATE.getPluginLanguages().isEmpty() && !(boolean)Config.getConfigSettings().get(ConfigSettings.LANGUAGES_CUSTOM_LANGUAGES))
			{
				throw new NoLanguagesSetException();
			}
			
			TranslateHandler.registerAndLoadPlugin(TEMPLATE.getPluginName(),
					Config.getConfigSettings().get(ConfigSettings.LANGUAGE) + "",
					TEMPLATE.getPluginLanguages(), (boolean) Config.getConfigSettings().get(ConfigSettings.LANGUAGES_PER_PLAYER_LANGUAGE));

			Colored.toConsole("Language", "Set default language", ChatColor.AQUA, fullDebug, "");
			Colored.toConsole("Language", "Created and/or loaded language files", ChatColor.AQUA, fullDebug, "");

			for(Player ap : Bukkit.getOnlinePlayers())
			{
				UserFormatFile.loadPlayer(ap, TEMPLATE.getPluginName());
			}

			Colored.toConsole("Language", "Loaded user format files for all online players", ChatColor.AQUA, fullDebug, "");
			
			float time = (float) (System.currentTimeMillis() - start) / 1000;
			
			Colored.toConsole("Language", "Time elapsed: " + time + " seconds", ChatColor.AQUA,
					fullDebug, "Successfully loaded (" + time + "s)", debug);
			Colored.toConsole(" ", ChatColor.AQUA, debug, "");
			
			Debug.addTiming(DebugTiming.LANGUAGE_SETTINGS, time);

		} catch(LanguageFileNotFoundException | NoLanguagesSetException e)
		{
			Debug.addStartup(System.currentTimeMillis(), e);
		}
	}
}

package net.haffel.PL.news;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import lombok.Getter;
import net.haffel.PL.TEMPLATE;
import net.haffel.PL.exceptions.ChangeNotFoundException;
import net.haffel.PL.files.Config;
import net.haffel.PL.files.ConfigSettings;
import net.haffel.PL.utils.Colored;
import net.haffel.PL.utils.Debug;
import net.haffel.PL.utils.DebugTiming;

/**
 * Template class
 * 
 * @since v1.1
 * @lastEdit v1.1
 */
public class ChangelogManager
{
	// FileConfiguration with all changes
	@Getter private static HashMap<String, YamlConfiguration> changeConfigs = new HashMap<>();
	// File with all changes
	@Getter private static HashMap<String, File> changeFiles = new HashMap<>();
	// List with all changes
	@Getter public static List<Change> changes = new ArrayList<>();
	// List with all change names
	@Getter public static List<String> changeNames = new ArrayList<>();

	/**
	 * Load change settings
	 */
	public static void load()
	{
		try
		{
			long start = System.currentTimeMillis();
			boolean debug = (boolean) Config.getConfigSettings().get(ConfigSettings.DEBUG_AT_START_CHANGES);
			boolean fullDebug = (boolean) Config.getConfigSettings().get(ConfigSettings.DEBUG_AT_START_DETAILED) && debug;

			Colored.toConsole("Changes: Loading version changes..", ChatColor.AQUA, debug, "");

			for(String language : TEMPLATE.getPluginLanguages())
			{
				loadChangesFile(language);
			}

			Colored.toConsole("Changes", "Created and loaded change.log files", ChatColor.AQUA, fullDebug, "");

			for(String language : TEMPLATE.getPluginLanguages())
			{
				loadAllChanges(language);
			}

			Colored.toConsole("Changes", "Loaded " + changes.size() + " change(s)", ChatColor.AQUA, fullDebug, "");

			float time = (float) (System.currentTimeMillis() - start) / 1000;

			Colored.toConsole("Changes", "Time elapsed: " + time + " seconds", ChatColor.AQUA, fullDebug, "Successfully loaded (" + time + "s)",
					debug);
			Colored.toConsole(" ", ChatColor.AQUA, debug, "");

			Debug.addTiming(DebugTiming.CHANGELOG_MANAGER, time);

		} catch(ChangeNotFoundException e)
		{
			Debug.addStartup(System.currentTimeMillis(), e);
		}
	}

	/**
	 * Load File and FileConfiguration with all changes
	 * 
	 * @throws ChangeNotFoundException
	 *             If no change file is found for the language
	 */
	@SuppressWarnings("static-access")
	private static void loadChangesFile(String language) throws ChangeNotFoundException
	{
		String path = "changes/change_" + language + ".log";
		String fullPath = TEMPLATE.getInstance().getDataFolder() + File.separator + "changes" + File.separator + "change_" + language + ".log";

		if(TEMPLATE.getInstance().getResource(path) != null)
		{
			TEMPLATE.getInstance().saveResource(path, true);

			File changeFile = new File(fullPath);

			getChangeFiles().put(language, changeFile);
			getChangeConfigs().put(language, new YamlConfiguration().loadConfiguration(changeFile));
		} else
		{
			throw new ChangeNotFoundException("file for language '" + language + "'");
		}
	}

	/**
	 * Load all changes
	 */
	private static void loadAllChanges(String language)
	{
		if(getChangeConfigs().get(language) == null)
		{
			try
			{
				loadChangesFile(language);
			} catch(ChangeNotFoundException e)
			{
				Debug.addStartup(System.currentTimeMillis(), e);

				return;
			}
		}

		YamlConfiguration changeConfig = getChangeConfigs().get(language);

		for(String news : changeConfig.getKeys(false))
		{
			ConfigurationSection cs;

			if(changeConfig.isConfigurationSection(news))
			{
				cs = changeConfig.getConfigurationSection(news);

			} else
			{
				continue;
			}

			if(cs.isString("version") && cs.isString("date") && cs.isString("changes"))
			{
				getChanges().add(new Change(language, cs.getString("version"), cs.getString("date"), cs.getString("changes")));
				getChangeNames().add(cs.getString("version"));
				getChangeNames().add(cs.getString("date"));
			}
		}
	}

	/**
	 * @param version
	 *            A version of the plugin
	 * @return The change for the version
	 * @throws ChangeNotFoundException
	 *             If no change is found for the version
	 */
	public static Change getChangeByVersion(String language, String version) throws ChangeNotFoundException
	{
		for(Change change : getChanges())
		{
			if(change.getLanguage().equalsIgnoreCase(language) && change.getVersion().equalsIgnoreCase(version))
			{
				return change;
			}
		}

		throw new ChangeNotFoundException("for version '" + version + "' in language '" + language + "'");
	}

	/**
	 * @param date
	 *            A date
	 * @return The change for the date
	 * @throws ChangeNotFoundException
	 *             If no change is found for the date
	 */
	public static Change getChangeByDate(String language, String date) throws ChangeNotFoundException
	{
		for(Change change : getChanges())
		{
			if(change.getLanguage().equalsIgnoreCase(language) && change.getDate().equalsIgnoreCase(date))
			{
				return change;
			}
		}

		throw new ChangeNotFoundException("for date '" + date + "' in language '" + language + "'");
	}
}

package net.haffel.PL.files;

import java.io.File;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import lombok.Getter;
import net.haffel.PL.TEMPLATE;
import net.haffel.PL.utils.Colored;
import net.haffel.PL.utils.Debug;
import net.haffel.PL.utils.DebugTiming;

/**
 * Template class
 * 
 * @since v1.1
 * @lastEdit v1.1 TODO Replace template stuff
 */
public class Config
{
	// List of all config settings
	@Getter private static HashMap<ConfigSettings, Object> configSettings = new HashMap<>();

	/**
	 * @param debugEnabled
	 *            If true, informations get printed to console
	 */
	public static void loadConfig(boolean debugEnabled)
	{
		long start = System.currentTimeMillis();

		if(new File(TEMPLATE.getInstance().getDataFolder() + File.separator + "config.yml").exists())
		{
			int edited = 0;
			boolean debug = getConfig().getBoolean(ConfigSettings.DEBUG_AT_START_CONFIG.getPath()) && debugEnabled;
			boolean fullDebug = debug && getConfig().getBoolean(ConfigSettings.DEBUG_AT_START_DETAILED.getPath());

			Colored.toConsole("Config: Loading configuration..", ChatColor.AQUA, debug, "");

			getConfig().set(ConfigSettings.VERSION.getPath(), TEMPLATE.getPluginVersion());

			if(!getConfig().isSet(ConfigSettings.LANGUAGE.getPath()))
			{
				getConfig().set(ConfigSettings.LANGUAGE.getPath(), TEMPLATE.getPluginLanguages().get(0));
				edited++;
			}

			if(!getConfig().isSet(ConfigSettings.DEBUG_AT_START_DETAILED.getPath())
					|| (!getConfig().get(ConfigSettings.DEBUG_AT_START_DETAILED.getPath()).equals(true)
							&& !getConfig().get(ConfigSettings.DEBUG_AT_START_DETAILED.getPath()).equals(false)))
			{
				getConfig().set(ConfigSettings.DEBUG_AT_START_DETAILED.getPath(), true);
				edited++;
			}

			if(!getConfig().isSet(ConfigSettings.DEBUG_AT_START_CONFIG.getPath())
					|| (!getConfig().get(ConfigSettings.DEBUG_AT_START_CONFIG.getPath()).equals(true)
							&& !getConfig().get(ConfigSettings.DEBUG_AT_START_CONFIG.getPath()).equals(false)))
			{
				getConfig().set(ConfigSettings.DEBUG_AT_START_CONFIG.getPath(), true);
				edited++;
			}

			if(!getConfig().isSet(ConfigSettings.DEBUG_AT_START_LANGUAGE.getPath())
					|| (!getConfig().get(ConfigSettings.DEBUG_AT_START_LANGUAGE.getPath()).equals(true)
							&& !getConfig().get(ConfigSettings.DEBUG_AT_START_LANGUAGE.getPath()).equals(false)))
			{
				getConfig().set(ConfigSettings.DEBUG_AT_START_LANGUAGE.getPath(), true);
				edited++;
			}

			if(!getConfig().isSet(ConfigSettings.DEBUG_AT_START_PERMISSIONS.getPath())
					|| (!getConfig().get(ConfigSettings.DEBUG_AT_START_PERMISSIONS.getPath()).equals(true)
							&& !getConfig().get(ConfigSettings.DEBUG_AT_START_PERMISSIONS.getPath()).equals(false)))
			{
				getConfig().set(ConfigSettings.DEBUG_AT_START_PERMISSIONS.getPath(), true);
				edited++;
			}

			if(!getConfig().isSet(ConfigSettings.DEBUG_AT_START_COMMANDS.getPath())
					|| (!getConfig().get(ConfigSettings.DEBUG_AT_START_COMMANDS.getPath()).equals(true)
							&& !getConfig().get(ConfigSettings.DEBUG_AT_START_COMMANDS.getPath()).equals(false)))
			{
				getConfig().set(ConfigSettings.DEBUG_AT_START_COMMANDS.getPath(), true);
				edited++;
			}

			if(!getConfig().isSet(ConfigSettings.DEBUG_AT_START_EVENTS.getPath())
					|| (!getConfig().get(ConfigSettings.DEBUG_AT_START_EVENTS.getPath()).equals(true)
							&& !getConfig().get(ConfigSettings.DEBUG_AT_START_EVENTS.getPath()).equals(false)))
			{
				getConfig().set(ConfigSettings.DEBUG_AT_START_EVENTS.getPath(), true);
				edited++;
			}

			if(!getConfig().isSet(ConfigSettings.DEBUG_AT_START_CHANGES.getPath())
					|| (!getConfig().get(ConfigSettings.DEBUG_AT_START_CHANGES.getPath()).equals(true)
							&& !getConfig().get(ConfigSettings.DEBUG_AT_START_CHANGES.getPath()).equals(false)))
			{
				getConfig().set(ConfigSettings.DEBUG_AT_START_CHANGES.getPath(), true);
				edited++;
			}

			if(!getConfig().isSet(ConfigSettings.DEBUG_AT_START_UPDATER.getPath())
					|| (!getConfig().get(ConfigSettings.DEBUG_AT_START_UPDATER.getPath()).equals(true)
							&& !getConfig().get(ConfigSettings.DEBUG_AT_START_UPDATER.getPath()).equals(false)))
			{
				getConfig().set(ConfigSettings.DEBUG_AT_START_UPDATER.getPath(), true);
				edited++;
			}

			if(!getConfig().isSet(ConfigSettings.DEBUG_TIMINGS.getPath()) || (!getConfig().get(ConfigSettings.DEBUG_TIMINGS.getPath()).equals(true)
					&& !getConfig().get(ConfigSettings.DEBUG_TIMINGS.getPath()).equals(false)))
			{
				getConfig().set(ConfigSettings.DEBUG_TIMINGS.getPath(), true);
				edited++;
			}

			if(!getConfig().isSet(ConfigSettings.DEBUG_STARTUP.getPath()) || (!getConfig().get(ConfigSettings.DEBUG_STARTUP.getPath()).equals(true)
					&& !getConfig().get(ConfigSettings.DEBUG_STARTUP.getPath()).equals(false)))
			{
				getConfig().set(ConfigSettings.DEBUG_STARTUP.getPath(), true);
				edited++;
			}

			if(!getConfig().isSet(ConfigSettings.DEBUG_RUNNING.getPath()) || (!getConfig().get(ConfigSettings.DEBUG_RUNNING.getPath()).equals(true)
					&& !getConfig().get(ConfigSettings.DEBUG_RUNNING.getPath()).equals(false)))
			{
				getConfig().set(ConfigSettings.DEBUG_RUNNING.getPath(), true);
				edited++;
			}

			if(!getConfig().isSet(ConfigSettings.LANGUAGES_CUSTOM_LANGUAGES.getPath())
					|| (!getConfig().get(ConfigSettings.LANGUAGES_CUSTOM_LANGUAGES.getPath()).equals(true)
							&& !getConfig().get(ConfigSettings.LANGUAGES_CUSTOM_LANGUAGES.getPath()).equals(false)))
			{
				getConfig().set(ConfigSettings.LANGUAGES_CUSTOM_LANGUAGES.getPath(), true);
				edited++;
			}

			if(!getConfig().isSet(ConfigSettings.LANGUAGES_PER_PLAYER_LANGUAGE.getPath())
					|| (!getConfig().get(ConfigSettings.LANGUAGES_PER_PLAYER_LANGUAGE.getPath()).equals(true)
							&& !getConfig().get(ConfigSettings.LANGUAGES_PER_PLAYER_LANGUAGE.getPath()).equals(false)))
			{
				getConfig().set(ConfigSettings.LANGUAGES_PER_PLAYER_LANGUAGE.getPath(), true);
				edited++;
			}

			if(!getConfig().isSet(ConfigSettings.LANGUAGES_USER_FORMATS.getPath())
					|| (!getConfig().get(ConfigSettings.LANGUAGES_USER_FORMATS.getPath()).equals(true)
							&& !getConfig().get(ConfigSettings.LANGUAGES_USER_FORMATS.getPath()).equals(false)))
			{
				getConfig().set(ConfigSettings.LANGUAGES_USER_FORMATS.getPath(), true);
				edited++;
			}

			if(!getConfig().isSet(ConfigSettings.LANGUAGES_INTERNAL_PLACEHOLDERS.getPath())
					|| (!getConfig().get(ConfigSettings.LANGUAGES_INTERNAL_PLACEHOLDERS.getPath()).equals(true)
							&& !getConfig().get(ConfigSettings.LANGUAGES_INTERNAL_PLACEHOLDERS.getPath()).equals(false)))
			{
				getConfig().set(ConfigSettings.LANGUAGES_INTERNAL_PLACEHOLDERS.getPath(), true);
				edited++;
			}

			if(!getConfig().isSet(ConfigSettings.LANGUAGES_PAPI.getPath()) || (!getConfig().get(ConfigSettings.LANGUAGES_PAPI.getPath()).equals(true)
					&& !getConfig().get(ConfigSettings.LANGUAGES_PAPI.getPath()).equals(false)))
			{
				getConfig().set(ConfigSettings.LANGUAGES_PAPI.getPath(), true);
				edited++;
			}

			if(!getConfig().isSet(ConfigSettings.SEARCH_UPDATES.getPath()) || (!getConfig().get(ConfigSettings.SEARCH_UPDATES.getPath()).equals(true)
					&& !getConfig().get(ConfigSettings.SEARCH_UPDATES.getPath()).equals(false)))
			{
				getConfig().set(ConfigSettings.SEARCH_UPDATES.getPath(), true);
				edited++;
			}

			if(getConfig().isSet(ConfigSettings.SEARCH_UPDATES_WHILE_RUNNING.getPath()))
			{
				try
				{
					getConfig().set(ConfigSettings.SEARCH_UPDATES_WHILE_RUNNING.getPath(),
							Integer.parseInt(getConfig().get(ConfigSettings.SEARCH_UPDATES_WHILE_RUNNING.getPath()) + ""));

				} catch(NumberFormatException e)
				{
					getConfig().set(ConfigSettings.SEARCH_UPDATES_WHILE_RUNNING.getPath(), 1440);
					edited++;
				}
			} else
			{
				getConfig().set(ConfigSettings.SEARCH_UPDATES_WHILE_RUNNING.getPath(), 1440);
				edited++;
			}

			if(!getConfig().isSet(ConfigSettings.SEARCH_UPDATES_AT_JOIN.getPath()))
			{
				getConfig().set(ConfigSettings.SEARCH_UPDATES_AT_JOIN.getPath(), TEMPLATE.getPluginName().toLowerCase() + ".updater.notify");
				edited++;
			}

			if(!getConfig().isSet(ConfigSettings.SEARCH_UPDATES_WHEN_FOUND.getPath()))
			{
				getConfig().set(ConfigSettings.SEARCH_UPDATES_WHEN_FOUND.getPath(), TEMPLATE.getPluginName().toLowerCase() + ".updater.notify");
				edited++;
			}

			if(!getConfig().isSet(ConfigSettings.SHOW_VALUES_AS_TREE_PLAYER_SPACE.getPath()))
			{
				getConfig().set(ConfigSettings.SHOW_VALUES_AS_TREE_PLAYER_SPACE.getPath(), "§7│   §r");
				edited++;
			}

			if(!getConfig().isSet(ConfigSettings.SHOW_VALUES_AS_TREE_PLAYER_SPACE_FOR_LAST.getPath()))
			{
				getConfig().set(ConfigSettings.SHOW_VALUES_AS_TREE_PLAYER_SPACE_FOR_LAST.getPath(), "    ");
				edited++;
			}

			if(!getConfig().isSet(ConfigSettings.SHOW_VALUES_AS_TREE_PLAYER_SUBTREE.getPath()))
			{
				getConfig().set(ConfigSettings.SHOW_VALUES_AS_TREE_PLAYER_SUBTREE.getPath(), "§7├─§r");
				edited++;
			}

			if(!getConfig().isSet(ConfigSettings.SHOW_VALUES_AS_TREE_PLAYER_LAST_SUBTREE.getPath()))
			{
				getConfig().set(ConfigSettings.SHOW_VALUES_AS_TREE_PLAYER_LAST_SUBTREE.getPath(), "§7└─§r");
				edited++;
			}

			if(!getConfig().isSet(ConfigSettings.SHOW_VALUES_AS_TREE_CONSOLE_SPACE.getPath()))
			{
				getConfig().set(ConfigSettings.SHOW_VALUES_AS_TREE_CONSOLE_SPACE.getPath(), "| ");
				edited++;
			}

			if(!getConfig().isSet(ConfigSettings.SHOW_VALUES_AS_TREE_CONSOLE_SPACE_FOR_LAST.getPath()))
			{
				getConfig().set(ConfigSettings.SHOW_VALUES_AS_TREE_CONSOLE_SPACE_FOR_LAST.getPath(), "  ");
				edited++;
			}

			if(!getConfig().isSet(ConfigSettings.SHOW_VALUES_AS_TREE_CONSOLE_SUBTREE.getPath()))
			{
				getConfig().set(ConfigSettings.SHOW_VALUES_AS_TREE_CONSOLE_SUBTREE.getPath(), "+-");
				edited++;
			}

			if(!getConfig().isSet(ConfigSettings.SHOW_VALUES_AS_TREE_CONSOLE_LAST_SUBTREE.getPath()))
			{
				getConfig().set(ConfigSettings.SHOW_VALUES_AS_TREE_CONSOLE_LAST_SUBTREE.getPath(), "`-");
				edited++;
			}

			if(getConfig().isSet(ConfigSettings.COMMANDS_DELAY.getPath()))
			{
				try
				{
					getConfig().set(ConfigSettings.COMMANDS_DELAY.getPath(),
							Integer.parseInt(getConfig().get(ConfigSettings.COMMANDS_DELAY.getPath()) + ""));

				} catch(NumberFormatException e)
				{
					getConfig().set(ConfigSettings.COMMANDS_DELAY.getPath(), 1000);
					edited++;
				}
			} else
			{
				getConfig().set(ConfigSettings.COMMANDS_DELAY.getPath(), 1000);
				edited++;
			}

			if(getConfig().isSet(ConfigSettings.COMMANDS_CONFIRM_TIME.getPath()))
			{
				try
				{
					getConfig().set(ConfigSettings.COMMANDS_CONFIRM_TIME.getPath(),
							Integer.parseInt(getConfig().get(ConfigSettings.COMMANDS_CONFIRM_TIME.getPath()) + ""));

				} catch(NumberFormatException e)
				{
					getConfig().set(ConfigSettings.COMMANDS_CONFIRM_TIME.getPath(), 10000);
					edited++;
				}
			} else
			{
				getConfig().set(ConfigSettings.COMMANDS_CONFIRM_TIME.getPath(), 10000);
				edited++;
			}
			
			if(getConfig().isSet(ConfigSettings.HELP_MAX_LENGTH.getPath()))
			{
				try
				{
					getConfig().set(ConfigSettings.HELP_MAX_LENGTH.getPath(),
							Integer.parseInt(getConfig().get(ConfigSettings.HELP_MAX_LENGTH.getPath()) + ""));

				} catch(NumberFormatException e)
				{
					getConfig().set(ConfigSettings.HELP_MAX_LENGTH.getPath(), 500);
					edited++;
				}
			} else
			{
				getConfig().set(ConfigSettings.HELP_MAX_LENGTH.getPath(), 500);
				edited++;
			}

			saveConfig();

			getConfigSettings().put(ConfigSettings.VERSION, getConfig().get(ConfigSettings.VERSION.getPath()));
			getConfigSettings().put(ConfigSettings.LANGUAGE, getConfig().get(ConfigSettings.LANGUAGE.getPath()));
			getConfigSettings().put(ConfigSettings.DEBUG_AT_START_DETAILED, getConfig().get(ConfigSettings.DEBUG_AT_START_DETAILED.getPath()));
			getConfigSettings().put(ConfigSettings.DEBUG_AT_START_CONFIG, getConfig().get(ConfigSettings.DEBUG_AT_START_CONFIG.getPath()));
			getConfigSettings().put(ConfigSettings.DEBUG_AT_START_LANGUAGE, getConfig().get(ConfigSettings.DEBUG_AT_START_LANGUAGE.getPath()));
			getConfigSettings().put(ConfigSettings.DEBUG_AT_START_PERMISSIONS, getConfig().get(ConfigSettings.DEBUG_AT_START_PERMISSIONS.getPath()));
			getConfigSettings().put(ConfigSettings.DEBUG_AT_START_COMMANDS, getConfig().get(ConfigSettings.DEBUG_AT_START_COMMANDS.getPath()));
			getConfigSettings().put(ConfigSettings.DEBUG_AT_START_EVENTS, getConfig().get(ConfigSettings.DEBUG_AT_START_EVENTS.getPath()));
			getConfigSettings().put(ConfigSettings.DEBUG_AT_START_CHANGES, getConfig().get(ConfigSettings.DEBUG_AT_START_CHANGES.getPath()));
			getConfigSettings().put(ConfigSettings.DEBUG_AT_START_UPDATER, getConfig().get(ConfigSettings.DEBUG_AT_START_UPDATER.getPath()));
			getConfigSettings().put(ConfigSettings.DEBUG_TIMINGS, getConfig().get(ConfigSettings.DEBUG_TIMINGS.getPath()));
			getConfigSettings().put(ConfigSettings.DEBUG_STARTUP, getConfig().get(ConfigSettings.DEBUG_STARTUP.getPath()));
			getConfigSettings().put(ConfigSettings.DEBUG_RUNNING, getConfig().get(ConfigSettings.DEBUG_RUNNING.getPath()));
			getConfigSettings().put(ConfigSettings.LANGUAGES_CUSTOM_LANGUAGES, getConfig().get(ConfigSettings.LANGUAGES_CUSTOM_LANGUAGES.getPath()));
			getConfigSettings().put(ConfigSettings.LANGUAGES_PER_PLAYER_LANGUAGE,
					getConfig().get(ConfigSettings.LANGUAGES_PER_PLAYER_LANGUAGE.getPath()));
			getConfigSettings().put(ConfigSettings.LANGUAGES_USER_FORMATS, getConfig().get(ConfigSettings.LANGUAGES_USER_FORMATS.getPath()));
			getConfigSettings().put(ConfigSettings.LANGUAGES_INTERNAL_PLACEHOLDERS,
					getConfig().get(ConfigSettings.LANGUAGES_INTERNAL_PLACEHOLDERS.getPath()));
			getConfigSettings().put(ConfigSettings.LANGUAGES_PAPI, getConfig().get(ConfigSettings.LANGUAGES_PAPI.getPath()));
			getConfigSettings().put(ConfigSettings.SEARCH_UPDATES, getConfig().get(ConfigSettings.SEARCH_UPDATES.getPath()));
			getConfigSettings().put(ConfigSettings.SEARCH_UPDATES_WHILE_RUNNING,
					getConfig().get(ConfigSettings.SEARCH_UPDATES_WHILE_RUNNING.getPath()));
			getConfigSettings().put(ConfigSettings.SEARCH_UPDATES_AT_JOIN, getConfig().get(ConfigSettings.SEARCH_UPDATES_AT_JOIN.getPath()));
			getConfigSettings().put(ConfigSettings.SEARCH_UPDATES_WHEN_FOUND, getConfig().get(ConfigSettings.SEARCH_UPDATES_WHEN_FOUND.getPath()));
			getConfigSettings().put(ConfigSettings.SHOW_VALUES_AS_TREE_PLAYER_SPACE,
					getConfig().get(ConfigSettings.SHOW_VALUES_AS_TREE_PLAYER_SPACE.getPath()));
			getConfigSettings().put(ConfigSettings.SHOW_VALUES_AS_TREE_PLAYER_SPACE_FOR_LAST,
					getConfig().get(ConfigSettings.SHOW_VALUES_AS_TREE_PLAYER_SPACE_FOR_LAST.getPath()));
			getConfigSettings().put(ConfigSettings.SHOW_VALUES_AS_TREE_PLAYER_SUBTREE,
					getConfig().get(ConfigSettings.SHOW_VALUES_AS_TREE_PLAYER_SUBTREE.getPath()));
			getConfigSettings().put(ConfigSettings.SHOW_VALUES_AS_TREE_PLAYER_LAST_SUBTREE,
					getConfig().get(ConfigSettings.SHOW_VALUES_AS_TREE_PLAYER_LAST_SUBTREE.getPath()));
			getConfigSettings().put(ConfigSettings.SHOW_VALUES_AS_TREE_CONSOLE_SPACE,
					getConfig().get(ConfigSettings.SHOW_VALUES_AS_TREE_CONSOLE_SPACE.getPath()));
			getConfigSettings().put(ConfigSettings.SHOW_VALUES_AS_TREE_CONSOLE_SPACE_FOR_LAST,
					getConfig().get(ConfigSettings.SHOW_VALUES_AS_TREE_CONSOLE_SPACE_FOR_LAST.getPath()));
			getConfigSettings().put(ConfigSettings.SHOW_VALUES_AS_TREE_CONSOLE_SUBTREE,
					getConfig().get(ConfigSettings.SHOW_VALUES_AS_TREE_CONSOLE_SUBTREE.getPath()));
			getConfigSettings().put(ConfigSettings.SHOW_VALUES_AS_TREE_CONSOLE_LAST_SUBTREE,
					getConfig().get(ConfigSettings.SHOW_VALUES_AS_TREE_CONSOLE_LAST_SUBTREE.getPath()));
			getConfigSettings().put(ConfigSettings.COMMANDS_DELAY, getConfig().get(ConfigSettings.COMMANDS_DELAY.getPath()));
			getConfigSettings().put(ConfigSettings.COMMANDS_CONFIRM_TIME, getConfig().get(ConfigSettings.COMMANDS_CONFIRM_TIME.getPath()));
			getConfigSettings().put(ConfigSettings.HELP_MAX_LENGTH, getConfig().get(ConfigSettings.HELP_MAX_LENGTH.getPath()));

			Colored.toConsole("Config", "Edited " + edited + " of " + getConfig().getKeys(true).size() + " values", ChatColor.AQUA, debug, "");

			float time = (float) (System.currentTimeMillis() - start) / 1000;

			Colored.toConsole("Config", "Time elapsed: " + time + " seconds", ChatColor.AQUA, fullDebug, "Successfully loaded (" + time + "s)",
					debug);
			Colored.toConsole(" ", ChatColor.AQUA, debug, "");

			Debug.addTiming(DebugTiming.CONFIG, time);
		} else
		{
			Colored.toConsole("Config: Create configuration..", ChatColor.AQUA, debugEnabled, "");

			getConfig().set(ConfigSettings.VERSION.getPath(), TEMPLATE.getPluginVersion());
			getConfig().set(ConfigSettings.LANGUAGE.getPath(), TEMPLATE.getPluginLanguages().get(0));

			getConfig().set(ConfigSettings.DEBUG_AT_START_DETAILED.getPath(), true);
			getConfig().set(ConfigSettings.DEBUG_AT_START_CONFIG.getPath(), true);
			getConfig().set(ConfigSettings.DEBUG_AT_START_LANGUAGE.getPath(), true);
			getConfig().set(ConfigSettings.DEBUG_AT_START_PERMISSIONS.getPath(), true);
			getConfig().set(ConfigSettings.DEBUG_AT_START_COMMANDS.getPath(), true);
			getConfig().set(ConfigSettings.DEBUG_AT_START_EVENTS.getPath(), true);
			getConfig().set(ConfigSettings.DEBUG_AT_START_CHANGES.getPath(), true);
			getConfig().set(ConfigSettings.DEBUG_AT_START_UPDATER.getPath(), true);
			getConfig().set(ConfigSettings.DEBUG_TIMINGS.getPath(), true);
			getConfig().set(ConfigSettings.DEBUG_STARTUP.getPath(), true);
			getConfig().set(ConfigSettings.DEBUG_RUNNING.getPath(), true);

			getConfig().set(ConfigSettings.LANGUAGES_CUSTOM_LANGUAGES.getPath(), true);
			getConfig().set(ConfigSettings.LANGUAGES_PER_PLAYER_LANGUAGE.getPath(), true);
			getConfig().set(ConfigSettings.LANGUAGES_USER_FORMATS.getPath(), true);
			getConfig().set(ConfigSettings.LANGUAGES_INTERNAL_PLACEHOLDERS.getPath(), true);
			getConfig().set(ConfigSettings.LANGUAGES_PAPI.getPath(), true);

			getConfig().set(ConfigSettings.SEARCH_UPDATES.getPath(), true);
			getConfig().set(ConfigSettings.SEARCH_UPDATES_WHILE_RUNNING.getPath(), 1440);
			getConfig().set(ConfigSettings.SEARCH_UPDATES_AT_JOIN.getPath(), TEMPLATE.getPluginName().toLowerCase() + ".updater.notify");
			getConfig().set(ConfigSettings.SEARCH_UPDATES_WHEN_FOUND.getPath(), TEMPLATE.getPluginName().toLowerCase() + ".updater.notify");

			getConfig().set(ConfigSettings.SHOW_VALUES_AS_TREE_PLAYER_SPACE.getPath(), "§7│   §r");
			getConfig().set(ConfigSettings.SHOW_VALUES_AS_TREE_PLAYER_SPACE_FOR_LAST.getPath(), "    ");
			getConfig().set(ConfigSettings.SHOW_VALUES_AS_TREE_PLAYER_SUBTREE.getPath(), "§7├─§r");
			getConfig().set(ConfigSettings.SHOW_VALUES_AS_TREE_PLAYER_LAST_SUBTREE.getPath(), "§7└─§r");
			getConfig().set(ConfigSettings.SHOW_VALUES_AS_TREE_CONSOLE_SPACE.getPath(), "| ");
			getConfig().set(ConfigSettings.SHOW_VALUES_AS_TREE_CONSOLE_SPACE_FOR_LAST.getPath(), "  ");
			getConfig().set(ConfigSettings.SHOW_VALUES_AS_TREE_CONSOLE_SUBTREE.getPath(), "+-");
			getConfig().set(ConfigSettings.SHOW_VALUES_AS_TREE_CONSOLE_LAST_SUBTREE.getPath(), "`-");

			getConfig().set(ConfigSettings.COMMANDS_DELAY.getPath(), 1000);
			getConfig().set(ConfigSettings.COMMANDS_CONFIRM_TIME.getPath(), 10000);
			
			getConfig().set(ConfigSettings.HELP_MAX_LENGTH.getPath(), 500);
			saveConfig();

			getConfigSettings().put(ConfigSettings.VERSION, getConfig().get(ConfigSettings.VERSION.getPath()));
			getConfigSettings().put(ConfigSettings.LANGUAGE, getConfig().get(ConfigSettings.LANGUAGE.getPath()));
			getConfigSettings().put(ConfigSettings.DEBUG_AT_START_DETAILED, getConfig().get(ConfigSettings.DEBUG_AT_START_DETAILED.getPath()));
			getConfigSettings().put(ConfigSettings.DEBUG_AT_START_CONFIG, getConfig().get(ConfigSettings.DEBUG_AT_START_CONFIG.getPath()));
			getConfigSettings().put(ConfigSettings.DEBUG_AT_START_LANGUAGE, getConfig().get(ConfigSettings.DEBUG_AT_START_LANGUAGE.getPath()));
			getConfigSettings().put(ConfigSettings.DEBUG_AT_START_PERMISSIONS, getConfig().get(ConfigSettings.DEBUG_AT_START_PERMISSIONS.getPath()));
			getConfigSettings().put(ConfigSettings.DEBUG_AT_START_COMMANDS, getConfig().get(ConfigSettings.DEBUG_AT_START_COMMANDS.getPath()));
			getConfigSettings().put(ConfigSettings.DEBUG_AT_START_EVENTS, getConfig().get(ConfigSettings.DEBUG_AT_START_EVENTS.getPath()));
			getConfigSettings().put(ConfigSettings.DEBUG_AT_START_CHANGES, getConfig().get(ConfigSettings.DEBUG_AT_START_CHANGES.getPath()));
			getConfigSettings().put(ConfigSettings.DEBUG_AT_START_UPDATER, getConfig().get(ConfigSettings.DEBUG_AT_START_UPDATER.getPath()));
			getConfigSettings().put(ConfigSettings.DEBUG_TIMINGS, getConfig().get(ConfigSettings.DEBUG_TIMINGS.getPath()));
			getConfigSettings().put(ConfigSettings.DEBUG_STARTUP, getConfig().get(ConfigSettings.DEBUG_STARTUP.getPath()));
			getConfigSettings().put(ConfigSettings.DEBUG_RUNNING, getConfig().get(ConfigSettings.DEBUG_RUNNING.getPath()));
			getConfigSettings().put(ConfigSettings.LANGUAGES_CUSTOM_LANGUAGES, getConfig().get(ConfigSettings.LANGUAGES_CUSTOM_LANGUAGES.getPath()));
			getConfigSettings().put(ConfigSettings.LANGUAGES_PER_PLAYER_LANGUAGE,
					getConfig().get(ConfigSettings.LANGUAGES_PER_PLAYER_LANGUAGE.getPath()));
			getConfigSettings().put(ConfigSettings.LANGUAGES_USER_FORMATS, getConfig().get(ConfigSettings.LANGUAGES_USER_FORMATS.getPath()));
			getConfigSettings().put(ConfigSettings.LANGUAGES_INTERNAL_PLACEHOLDERS,
					getConfig().get(ConfigSettings.LANGUAGES_INTERNAL_PLACEHOLDERS.getPath()));
			getConfigSettings().put(ConfigSettings.LANGUAGES_PAPI, getConfig().get(ConfigSettings.LANGUAGES_PAPI.getPath()));
			getConfigSettings().put(ConfigSettings.SEARCH_UPDATES, getConfig().get(ConfigSettings.SEARCH_UPDATES.getPath()));
			getConfigSettings().put(ConfigSettings.SEARCH_UPDATES_WHILE_RUNNING,
					getConfig().get(ConfigSettings.SEARCH_UPDATES_WHILE_RUNNING.getPath()));
			getConfigSettings().put(ConfigSettings.SEARCH_UPDATES_AT_JOIN, getConfig().get(ConfigSettings.SEARCH_UPDATES_AT_JOIN.getPath()));
			getConfigSettings().put(ConfigSettings.SEARCH_UPDATES_WHEN_FOUND, getConfig().get(ConfigSettings.SEARCH_UPDATES_WHEN_FOUND.getPath()));
			getConfigSettings().put(ConfigSettings.SHOW_VALUES_AS_TREE_PLAYER_SPACE,
					getConfig().get(ConfigSettings.SHOW_VALUES_AS_TREE_PLAYER_SPACE.getPath()));
			getConfigSettings().put(ConfigSettings.SHOW_VALUES_AS_TREE_PLAYER_SPACE_FOR_LAST,
					getConfig().get(ConfigSettings.SHOW_VALUES_AS_TREE_PLAYER_SPACE_FOR_LAST.getPath()));
			getConfigSettings().put(ConfigSettings.SHOW_VALUES_AS_TREE_PLAYER_SUBTREE,
					getConfig().get(ConfigSettings.SHOW_VALUES_AS_TREE_PLAYER_SUBTREE.getPath()));
			getConfigSettings().put(ConfigSettings.SHOW_VALUES_AS_TREE_PLAYER_LAST_SUBTREE,
					getConfig().get(ConfigSettings.SHOW_VALUES_AS_TREE_PLAYER_LAST_SUBTREE.getPath()));
			getConfigSettings().put(ConfigSettings.SHOW_VALUES_AS_TREE_CONSOLE_SPACE,
					getConfig().get(ConfigSettings.SHOW_VALUES_AS_TREE_CONSOLE_SPACE.getPath()));
			getConfigSettings().put(ConfigSettings.SHOW_VALUES_AS_TREE_CONSOLE_SPACE_FOR_LAST,
					getConfig().get(ConfigSettings.SHOW_VALUES_AS_TREE_CONSOLE_SPACE_FOR_LAST.getPath()));
			getConfigSettings().put(ConfigSettings.SHOW_VALUES_AS_TREE_CONSOLE_SUBTREE,
					getConfig().get(ConfigSettings.SHOW_VALUES_AS_TREE_CONSOLE_SUBTREE.getPath()));
			getConfigSettings().put(ConfigSettings.SHOW_VALUES_AS_TREE_CONSOLE_LAST_SUBTREE,
					getConfig().get(ConfigSettings.SHOW_VALUES_AS_TREE_CONSOLE_LAST_SUBTREE.getPath()));
			getConfigSettings().put(ConfigSettings.COMMANDS_DELAY, getConfig().get(ConfigSettings.COMMANDS_DELAY.getPath()));
			getConfigSettings().put(ConfigSettings.COMMANDS_CONFIRM_TIME, getConfig().get(ConfigSettings.COMMANDS_CONFIRM_TIME.getPath()));
			getConfigSettings().put(ConfigSettings.HELP_MAX_LENGTH, getConfig().get(ConfigSettings.HELP_MAX_LENGTH.getPath()));

			Colored.toConsole("Config", "Successfully created and loaded", ChatColor.AQUA, debugEnabled, "");
			Colored.toConsole("Config", "Added " + getConfig().getKeys(true).size() + " values", ChatColor.AQUA, debugEnabled, "");

			float time = (float) (System.currentTimeMillis() - start) / 1000;

			Colored.toConsole("Config", "Time elapsed: " + ((double) (System.currentTimeMillis() - start) / 1000) + " seconds", ChatColor.AQUA,
					debugEnabled, "");
			Colored.toConsole(" ", ChatColor.AQUA, debugEnabled, "");

			Debug.addTiming(DebugTiming.CONFIG, time);
		}
	}

	/**
	 * @return Plugin FileConfiguration
	 */
	private static FileConfiguration getConfig()
	{
		return TEMPLATE.getInstance().getConfig();
	}

	/**
	 * Save plugin configuration
	 */
	private static void saveConfig()
	{
		TEMPLATE.getInstance().saveConfig();
	}

	/**
	 * Reload the configuration file
	 */
	public static void reloadConfig()
	{
		TEMPLATE.getInstance().reloadConfig();
		Config.loadConfig(false);
		LanguageSettings.load(false);
	}

	/**
	 * Reset the configuration file to default
	 */
	public static void resetConfig()
	{
		Config.deleteConfig();
		Config.loadConfig(false);
	}

	/**
	 * Delete plugin configuration
	 */
	public static void deleteConfig()
	{
		File config = new File(TEMPLATE.getInstance().getDataFolder() + File.separator + "config.yml");

		if(config.exists())
		{
			config.delete();
		}

		TEMPLATE.getInstance().reloadConfig();
	}
}

package net.haffel.PL.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import lombok.Getter;
import lombok.Setter;
import net.haffel.PL.NoneTranslatedMessages;
import net.haffel.PL.TEMPLATE;
import net.haffel.PL.files.Config;
import net.haffel.PL.files.ConfigSettings;
import net.haffel.PL.files.TranslationPaths;
import net.haffel.ml.MultiLanguages;
import net.haffel.ml.exceptions.TranslationNotFoundException;
import net.haffel.ml.perms.utils.PermissionNames;

/**
 * Template class
 * 
 * @since v1.1
 * @lastEdit v1.1
 */
public class UpdateChecker
{
	// URL to Spigot page of this plugin
	@Getter @Setter private static URL checkURL;
	// Latest version found of this plugin
	@Getter @Setter private static String latestVersion;
	// Current version of the plugin
	@Getter @Setter private static String currentVersion = TEMPLATE.getPluginVersion();
	// Update check timer
	static int timer;

	/**
	 * If updates in config are enabled, the update search starts
	 */
	public static void startCheck()
	{
		if(MultiLanguages.getProjectID() != -1)
		{
			if((boolean) Config.getConfigSettings().get(ConfigSettings.SEARCH_UPDATES))
			{
				int delay = Integer.valueOf(Config.getConfigSettings().get(ConfigSettings.SEARCH_UPDATES_WHILE_RUNNING) + "");

				if(delay > 0)
				{
					UpdateChecker.checkForUpdatesTimer(delay);

				} else
				{
					UpdateChecker.checkForUpdates(true, null);
				}
			}
		}
	}

	/**
	 * @param projectID
	 *            The project id on Spigot
	 * @param broadcast
	 *            If true, a notification is sent to all players with specific permission
	 * @param p
	 *            The player which gets a message even if broadcast is false
	 */
	public static void checkForUpdates(boolean broadcast, Player p)
	{
		long start = System.currentTimeMillis();
		boolean debug = (boolean) Config.getConfigSettings().get(ConfigSettings.DEBUG_AT_START_UPDATER) && p == null;
		boolean fullDebug = (boolean) Config.getConfigSettings().get(ConfigSettings.DEBUG_AT_START_DETAILED) && debug;

		if(MultiLanguages.getProjectID() != -1)
		{
			try
			{
				if(Long.valueOf(Config.getConfigSettings().get(ConfigSettings.SEARCH_UPDATES_WHILE_RUNNING) + "") > 0)
				{
					Colored.toConsole("Updater: Checking for updates..", ChatColor.AQUA, debug, "");

					try
					{
						setCheckURL(new URL(NoneTranslatedMessages.UPDATER_API_PATH.getMessage()));

						if(!isLatestVersion())
						{
							Colored.toConsole("Updater", "New version '" + getLatestVersion() + "' available. Download it at:", ChatColor.AQUA, debug,
									"");
							Colored.toConsole("Updater", getResourceURL(), ChatColor.AQUA, debug, "");

							if(broadcast)
							{
								String[] extraArgs =
								{ getLatestVersion(), getResourceURL() };

								for(Player ap : Bukkit.getOnlinePlayers())
								{
									if(ap.hasPermission(PermissionNames.UPDATES_WHEN_FOUND.getName()))
									{
										ap.sendMessage(getTranslatedString(p, TranslationPaths.UPDATER_NEW_VERSION_FOUND.getPath(), new String[0],
												extraArgs));
									}
								}
							}

							if(p != null)
							{
								String[] extraArgs =
								{ getLatestVersion(), getResourceURL() };

								p.sendMessage(getTranslatedString(p, TranslationPaths.UPDATER_NEW_VERSION_FOUND.getPath(), new String[0], extraArgs));
							}
						} else
						{
							Colored.toConsole("Updater", "Plugin is up to date", ChatColor.AQUA, debug, "");
						}
					} catch(IOException e)
					{
						Debug.addRunning(System.currentTimeMillis(), e);

						Colored.toConsole("Updater", "Search failed: " + e.getMessage(), ChatColor.AQUA, debug, "");
					}

					float time = (float) (System.currentTimeMillis() - start) / 1000;

					Colored.toConsole("Updater", "Time elapsed: " + time + " seconds", ChatColor.AQUA, fullDebug,
							"Successfully loaded (" + time + "s)", debug);

					Debug.addTiming(DebugTiming.UPDATE_CHECKER, time);

				} else
				{
					Colored.toConsole("Updater: Disabled in config", ChatColor.AQUA, debug, "");
				}
			} catch(TranslationNotFoundException e)
			{
				Debug.addRunning(System.currentTimeMillis(), e);
			}

			Colored.toConsole(" ", ChatColor.AQUA, debug, "");
		}
	}

	/**
	 * @return The plugin URL on Spigot
	 */
	private static String getResourceURL()
	{
		return NoneTranslatedMessages.UPDATER_RESOURCE_PATH.getMessage();
	}

	/**
	 * @return If a newer version is available or not
	 * @throws IOException
	 */
	private static boolean isLatestVersion() throws IOException
	{
		URLConnection con = getCheckURL().openConnection();
		setLatestVersion(new BufferedReader(new InputStreamReader(con.getInputStream())).readLine());

		return getCurrentVersion().equals(getLatestVersion());
	}

	/**
	 * Automatically searches for updates after custom amount of time
	 * 
	 * @param delay
	 *            The delay until new updates get searched in minutes
	 */
	private static void checkForUpdatesTimer(int delay)
	{
		delay = delay * 60 * 20;

		timer = Bukkit.getScheduler().scheduleSyncRepeatingTask(TEMPLATE.getInstance(), new Runnable()
		{
			@Override
			public void run()
			{
				boolean debug = (boolean) Config.getConfigSettings().get(ConfigSettings.DEBUG_AT_START_UPDATER);

				if(debug)
				{
					TEMPLATE.getInstance().getServer().getConsoleSender()
							.sendMessage(NoneTranslatedMessages.CONSOLE_TEXT_BEGINNING_ENABLE.getMessage());
				}

				checkForUpdates(true, null);

				if(debug)
				{
					TEMPLATE.getInstance().getServer().getConsoleSender().sendMessage(NoneTranslatedMessages.CONSOLE_TEXT_ENDING.getMessage());
				}
			}
		}, 0L, delay);
	}

	/**
	 * @param commandSender
	 *            CommandSender which used the command
	 * @param translate
	 *            The path to the translation
	 * @param args
	 *            The arguments passed at usage
	 * @param extraArgs
	 *            Extra arguments passed by the plugin
	 * @return Translated String
	 * @throws TranslationNotFoundException
	 *             For the given translation key was no translation found
	 * @throws UserFormatConfigDoesntExistException
	 *             The user formats config for the sender doesnt exist
	 * @throws SomeTranslationException
	 *             Any other error
	 */
	private static String[] getTranslatedString(CommandSender commandSender, String translate, String[] args, String[] extraArgs)
			throws TranslationNotFoundException
	{
		return TEMPLATE.getTranslatedStringSplitted(commandSender, translate, args, extraArgs);
	}
}

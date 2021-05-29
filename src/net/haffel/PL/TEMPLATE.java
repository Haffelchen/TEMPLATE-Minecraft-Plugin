package net.haffel.PL;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;
import lombok.Setter;
import net.haffel.PL.args.utils.Confirm;
import net.haffel.PL.cmds.utils.CommandHandler;
import net.haffel.PL.events.EventManager;
import net.haffel.PL.events.PlayerCommandPreprocess;
import net.haffel.PL.events.PlayerJoin;
import net.haffel.PL.files.Config;
import net.haffel.PL.files.LanguageSettings;
import net.haffel.PL.files.TranslationPaths;
import net.haffel.PL.news.ChangelogManager;
import net.haffel.PL.perms.utils.PermissionHandler;
import net.haffel.PL.utils.Colored;
import net.haffel.PL.utils.Debug;
import net.haffel.PL.utils.DebugTiming;
import net.haffel.PL.utils.UpdateChecker;import net.haffel.ml.exceptions.TranslationNotFoundException;import net.haffel.ml.translate.TranslateHandler;

/**
 * Template class
 * 
 * @since v1.1
 * @lastEdit v1.1 TODO Replace template stuff
 */
public class TEMPLATE extends JavaPlugin implements Listener
{
	@Getter @Setter static TEMPLATE instance;
	@Getter @Setter static String pluginName;
	@Getter @Setter static String pluginVersion;
	@Getter static int projectID = 92819;
	@Getter static List<String> pluginLanguages = Arrays.asList("en", "de");

	public void onEnable()
	{
		long start = System.currentTimeMillis();

		try
		{
			setInstance(this);
			setPluginName(getName());
			setPluginVersion(getDescription().getVersion());

			getServer().getConsoleSender().sendMessage(NoneTranslatedMessages.CONSOLE_TEXT_BEGINNING_ENABLE.getMessage());
			Colored.toConsole("Version: " + getPluginVersion(), ChatColor.AQUA);
			Colored.toConsole("Description", "Description: " + getDescription().getDescription(), ChatColor.AQUA);
			Colored.toConsole(" ", ChatColor.AQUA);

			Config.loadConfig(true);
			LanguageSettings.load(true);
			PermissionHandler.setupPermissions();
			CommandHandler.setupCommands();
			EventManager.load();
			ChangelogManager.load();
			UpdateChecker.startCheck();

			if(Bukkit.getOnlinePlayers().size() > 0)
			{
				Confirm.confirmTimer();
				PlayerCommandPreprocess.delayTimer();

				for(Player ap : Bukkit.getOnlinePlayers())
				{
					PlayerJoin.loadPlayerRelatedStuff(ap);
				}
			}

			float time = (float) (System.currentTimeMillis() - start) / 1000;

			Colored.toConsole("Plugin loaded. Time elapsed: " + time + " seconds", ChatColor.AQUA);
			Debug.addTiming(DebugTiming.PLUGIN_START, time);

			getServer().getConsoleSender().sendMessage(NoneTranslatedMessages.CONSOLE_TEXT_ENDING_ENABLE.getMessage());

		} catch(Exception e)
		{
			Debug.addStartup(System.currentTimeMillis(), e);
			
			e.printStackTrace();
			
			disable();
		}
	}

	public void onDisable()
	{
		getServer().getConsoleSender().sendMessage(NoneTranslatedMessages.CONSOLE_TEXT_BEGINNING_DISABLE.getMessage());

		Colored.toConsole("Version: " + getPluginVersion(), ChatColor.RED);
		Colored.toConsole("Description", "Description: " + getDescription().getDescription(), ChatColor.RED);

		getServer().getConsoleSender().sendMessage(NoneTranslatedMessages.CONSOLE_TEXT_ENDING_DISABLE.getMessage());
	}

	/**
	 * Shut down the plugin
	 */
	public static void disable()
	{
		try
		{
			for(Player ap : Bukkit.getOnlinePlayers())
			{
				if(ap.isOp())
				{
					ap.sendMessage(getTranslatedString(ap, TranslationPaths.DISABLING_PLUGIN.getPath()));

				}
			}

			getInstance().getServer().getConsoleSender().sendMessage(getTranslatedString(TranslationPaths.DISABLING_PLUGIN.getPath()));
			getInstance().getPluginLoader().disablePlugin(getInstance());

		} catch(TranslationNotFoundException e)
		{
			Debug.addRunning(System.currentTimeMillis(), e);
		}
	}

	/**
	 * @param commandSender
	 *            CommandSender which used the command
	 * @param translate
	 *            The path to the translation
	 * @return Translated String
	 * @throws TranslationNotFoundException
	 *             For the given translation key was no translation found
	 * @throws UserFormatConfigDoesntExistException
	 *             The user formats config for the sender doesnt exist
	 * @throws SomeTranslationException
	 *             Any other error
	 */
	public static String[] getTranslatedStringSplitted(CommandSender commandSender, String translate)
			throws TranslationNotFoundException
	{
		return getTranslatedStringSplitted(commandSender, getInstance().getConfig(), translate, new String[0], new String[0]);
	}

	/**
	 * @param commandSender
	 *            CommandSender which used the command
	 * @param translate
	 *            The path to the translation
	 * @param args
	 *            The arguments passed at usage
	 * @return Translated String
	 * @throws TranslationNotFoundException
	 *             For the given translation key was no translation found
	 * @throws UserFormatConfigDoesntExistException
	 *             The user formats config for the sender doesnt exist
	 * @throws SomeTranslationException
	 *             Any other error
	 */
	public static String[] getTranslatedStringSplitted(CommandSender commandSender, String translate, String[] args)
			throws TranslationNotFoundException
	{
		return getTranslatedStringSplitted(commandSender, getInstance().getConfig(), translate, args, new String[0]);
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
	public static String[] getTranslatedStringSplitted(CommandSender commandSender, String translate, String[] args, String[] extraArgs)
			throws TranslationNotFoundException
	{
		return getTranslatedStringSplitted(commandSender, getInstance().getConfig(), translate, args, extraArgs);
	}

	/**
	 * @param commandSender
	 *            CommandSender which used the command
	 * @param config
	 *            A FileConfiguration from which values can be replaced
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
	public static String[] getTranslatedStringSplitted(CommandSender commandSender, FileConfiguration config, String translate, String[] args,
			String[] extraArgs) throws TranslationNotFoundException
	{
		return getTranslatedString(commandSender, config, translate, args, extraArgs).split("%break%");
	}

	/**
	 * @param commandSender
	 *            CommandSender which used the command
	 * @param config
	 *            A FileConfiguration from which values can be replaced
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
	public static String getTranslatedString(CommandSender commandSender, FileConfiguration config, String translate, String[] args,
			String[] extraArgs) throws TranslationNotFoundException
	{
		return ChatColor.translateAlternateColorCodes('&',
				TranslateHandler.getTranslatedString(commandSender, TEMPLATE.getPluginName(), translate, config, args, extraArgs));
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
	public static String getTranslatedString(CommandSender commandSender, String translate, String[] args, String[] extraArgs)
			throws TranslationNotFoundException
	{
		return getTranslatedString(commandSender, getInstance().getConfig(), translate, args, extraArgs);
	}

	/**
	 * @param commandSender
	 *            CommandSender which used the command
	 * @param translate
	 *            The path to the translation
	 * @param args
	 *            The arguments passed at usage
	 * @return Translated String
	 * @throws TranslationNotFoundException
	 *             For the given translation key was no translation found
	 * @throws UserFormatConfigDoesntExistException
	 *             The user formats config for the sender doesnt exist
	 * @throws SomeTranslationException
	 *             Any other error
	 */
	public static String getTranslatedString(CommandSender commandSender, String translate, String[] args)
			throws TranslationNotFoundException
	{
		return getTranslatedString(commandSender, translate, args, new String[0]);
	}

	/**
	 * @param translate
	 *            The path to the translation
	 * @return Translated String
	 * @throws TranslationNotFoundException
	 *             For the given translation key was no translation found
	 * @throws UserFormatConfigDoesntExistException
	 *             The user formats config for the sender doesnt exist
	 * @throws SomeTranslationException
	 *             Any other error
	 */
	public static String getTranslatedString(CommandSender commandSender, String translate)
			throws TranslationNotFoundException
	{
		return getTranslatedString(commandSender, getInstance().getConfig(), translate, new String[0], new String[0]);
	}

	/**
	 * @param translate
	 *            The path to the translation
	 * @return Translated String
	 * @throws TranslationNotFoundException
	 *             For the given translation key was no translation found
	 * @throws UserFormatConfigDoesntExistException
	 *             The user formats config for the sender doesnt exist
	 * @throws SomeTranslationException
	 *             Any other error
	 */
	public static String getTranslatedString(String translate)
			throws TranslationNotFoundException
	{
		return getTranslatedString(null, getInstance().getConfig(), translate, new String[0], new String[0]);
	}
}

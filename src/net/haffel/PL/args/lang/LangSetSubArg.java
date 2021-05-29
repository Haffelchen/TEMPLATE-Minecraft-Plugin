package net.haffel.PL.args.lang;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import net.haffel.PL.TEMPLATE;
import net.haffel.PL.args.utils.ArgumentBase;
import net.haffel.PL.args.utils.ExtendedSubSyntax;
import net.haffel.PL.exceptions.PermissionNotFoundException;
import net.haffel.PL.files.Config;
import net.haffel.PL.files.ConfigSettings;
import net.haffel.PL.files.LanguageSettings;
import net.haffel.PL.files.TranslationPaths;
import net.haffel.PL.perms.utils.PermissionHandler;
import net.haffel.PL.perms.utils.PermissionNames;
import net.haffel.ml.exceptions.PluginNotRegisteredException;
import net.haffel.ml.exceptions.TranslationNotFoundException;
import net.haffel.ml.files.LanguageFile;
import net.haffel.ml.translate.TranslateHandler;
import net.haffel.ml.translate.UserFormat;

/**
 * Template class
 * 
 * @since v1.1
 * @lastEdit v1.1
 */
public class LangSetSubArg extends ArgumentBase
{
	/**
	 * @return Name of the sub argument
	 */
	@Override
	public String getArgumentName()
	{
		return "set";
	}

	/**
	 * @return Description of the sub argument
	 * @throws TranslationNotFoundException
	 *             For the given translation key was no translation found
	 * @throws UserFormatConfigDoesntExistException
	 *             The user formats config for the sender doesnt exist
	 * @throws SomeTranslationException
	 *             Any other error
	 */
	@Override
	public String getArgumentDescription(CommandSender sender)
			throws TranslationNotFoundException
	{
		return TEMPLATE.getTranslatedString(sender, TranslationPaths.DESCRIPTION_ARG_LANG_SET.getPath());
	}

	@Override
	public boolean execute(CommandSender sender, Command cmd, String label, String[] args)
			throws CommandException, TranslationNotFoundException
	{
		if(args.length > 3)
		{
			if(args[2].equalsIgnoreCase("custom"))
			{
				if(sender instanceof Player)
				{
					if(args[3].equalsIgnoreCase("$null"))
					{
						UserFormat.setUserSetting((Player) sender, TEMPLATE.getPluginName(), "Language", null);

						sender.sendMessage(getTranslatedStringSplitted(sender, TranslationPaths.ARG_LANG_SET_CUSTOM_RESET.getPath(), args));
						return true;

					} else if(LanguageFile.isLanguageLoaded(TEMPLATE.getPluginName(), args[3]))
					{
						UserFormat.setUserSetting((Player) sender, TEMPLATE.getPluginName(), "Language", args[3]);

						sender.sendMessage(getTranslatedStringSplitted(sender, TranslationPaths.ARG_LANG_SET_CUSTOM.getPath(), args));
						return true;
					} else
					{
						sender.sendMessage(getTranslatedStringSplitted(sender, TranslationPaths.ARG_LANG_SET_NOT_LOADED.getPath(), args));

						return false;
					}
				}
			} else if(args[2].equalsIgnoreCase("default"))
			{
				if(LanguageFile.isLanguageLoaded(TEMPLATE.getPluginName(), args[3]))
				{
					getConfig().set("Language", args[3]);
					saveConfig();

					Config.getConfigSettings().put(ConfigSettings.LANGUAGE, args[3]);
					LanguageSettings.load(false);

					sender.sendMessage(getTranslatedStringSplitted(sender, TranslationPaths.ARG_LANG_SET_DEFAULT.getPath(), args));
					return true;

				} else
				{
					sender.sendMessage(getTranslatedStringSplitted(sender, TranslationPaths.ARG_LANG_SET_NOT_LOADED.getPath(), args));

					return false;
				}

			}
		}

		String[] extraArgs =
		{ getArgumentSyntax(args, 2) };

		sender.sendMessage(TEMPLATE.getTranslatedStringSplitted(sender, TranslationPaths.SYNTAX_MESSAGE.getPath(), args, extraArgs));

		return false;
	}

	/**
	 * Adds all needed permissions for the sub argument to a list. This does NOT load the
	 * permissions
	 * 
	 * @throws PermissionNotFoundException
	 *             Permission is not loaded
	 * @see PermissionHandler#setupPermissions()
	 */
	public void loadPermissions() throws PermissionNotFoundException
	{
		loadPermission(PermissionNames.ARG_LANG_SET);
	}

	/**
	 * Register all sub arguments for this argument
	 */
	@Override
	public void registerAllSubArguments()
	{
		ExtendedSubSyntax custom = new ExtendedSubSyntax("custom", TranslationPaths.DESCRIPTION_ARG_LANG_SET_CUSTOM, false, true);
		ExtendedSubSyntax custom_null = new ExtendedSubSyntax("$null", TranslationPaths.DESCRIPTION_ARG_LANG_SET_CUSTOM_NULL, false, true);
		ExtendedSubSyntax custom_lang = new ExtendedSubSyntax("Language", TranslationPaths.DESCRIPTION_ARG_LANG_SET_CUSTOM_LANGUAGE, false, false);
		ExtendedSubSyntax defau = new ExtendedSubSyntax("default", TranslationPaths.DESCRIPTION_ARG_LANG_SET_DEFAULT, false, true);
		ExtendedSubSyntax defau_lang = new ExtendedSubSyntax("Language", TranslationPaths.DESCRIPTION_ARG_LANG_SET_DEFAULT_LANGUAGE, false, false);

		custom.addExtendedSubSyntax(custom_null);
		custom.addExtendedSubSyntax(custom_lang);
		defau.addExtendedSubSyntax(defau_lang);

		addExtendedSyntax(custom, false);
		addExtendedSyntax(defau, false);
	}

	/**
	 * @return List of tab completion options
	 * @throws TranslationNotFoundException
	 *             For the given translation key was no translation found
	 * @throws UserFormatConfigDoesntExistException
	 *             The user formats config for the sender doesnt exist
	 * @throws SomeTranslationException
	 *             Any other error
	 * @throws PluginNotRegisteredException
	 *             Plugin is not registered in Multi Languages Plugin
	 */
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args, int start)
			throws TranslationNotFoundException, PluginNotRegisteredException
	{
		if(args.length == 3)
		{
			if(sender instanceof Player)
			{
				return getListOfStringsMatchingLastWord(args, Arrays.asList("custom", "default"));
			}

			return getListOfStringsMatchingLastWord(args, Arrays.asList("default"));

		} else if(args.length == 4)
		{
			if(args[2].equalsIgnoreCase("default"))
			{
				return getListOfStringsMatchingLastWord(args, TranslateHandler.getPluginLanguages(TEMPLATE.getPluginName()));

			} else if(args[2].equalsIgnoreCase("custom"))
			{
				List<String> complete = new ArrayList<>(TranslateHandler.getPluginLanguages(TEMPLATE.getPluginName()));
				complete.add("$null");

				return getListOfStringsMatchingLastWord(args, complete);
			}
		}

		return Collections.emptyList();
	}

	/**
	 * @return Configuration file
	 */
	private FileConfiguration getConfig()
	{
		return TEMPLATE.getInstance().getConfig();
	}

	/**
	 * Saves the configuration file
	 */
	private void saveConfig()
	{
		TEMPLATE.getInstance().saveConfig();
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
	private String[] getTranslatedStringSplitted(CommandSender commandSender, String translate, String[] args)
			throws TranslationNotFoundException
	{
		return TEMPLATE.getTranslatedStringSplitted(commandSender, translate, args);
	}
}

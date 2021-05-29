package net.haffel.PL.args.lang;

import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import net.haffel.PL.TEMPLATE;
import net.haffel.PL.args.utils.ArgumentBase;
import net.haffel.PL.args.utils.ExtendedSubSyntax;
import net.haffel.PL.exceptions.PermissionNotFoundException;
import net.haffel.PL.files.Config;
import net.haffel.PL.files.ConfigSettings;
import net.haffel.PL.files.TranslationPaths;
import net.haffel.PL.perms.utils.PermissionHandler;
import net.haffel.PL.perms.utils.PermissionNames;
import net.haffel.PL.tree.Tree;
import net.haffel.ml.exceptions.LanguageNotLoadedException;
import net.haffel.ml.exceptions.PluginNotRegisteredException;
import net.haffel.ml.exceptions.TranslationNotFoundException;
import net.haffel.ml.files.LanguageFile;
import net.haffel.ml.translate.TranslateHandler;

/**
 * Template class
 * 
 * @since v1.1
 * @lastEdit v1.1
 */
public class LangTreeSubArg extends ArgumentBase
{
	/**
	 * @return Name of the sub argument
	 */
	@Override
	public String getArgumentName()
	{
		return "tree";
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
		return TEMPLATE.getTranslatedString(sender, TranslationPaths.DESCRIPTION_ARG_LANG_TREE.getPath());
	}

	@Override
	public boolean execute(CommandSender sender, Command cmd, String label, String[] args)
			throws CommandException, TranslationNotFoundException, LanguageNotLoadedException
	{
		if(args.length > 3)
		{
			if(LanguageFile.isLanguageLoaded(TEMPLATE.getPluginName(), args[2]))
			{
				if(LanguageFile.getLanguageConfig(TEMPLATE.getPluginName(), args[2]).isConfigurationSection(args[3]))
				{
					if(sender instanceof ConsoleCommandSender)
					{
						String tree = Tree.getTree(LanguageFile.getLanguageConfig(TEMPLATE.getPluginName(), args[2]).getConfigurationSection(args[3]),
								Config.getConfigSettings().get(ConfigSettings.SHOW_VALUES_AS_TREE_CONSOLE_SUBTREE) + "",
								Config.getConfigSettings().get(ConfigSettings.SHOW_VALUES_AS_TREE_CONSOLE_SPACE) + "",
								Config.getConfigSettings().get(ConfigSettings.SHOW_VALUES_AS_TREE_CONSOLE_LAST_SUBTREE) + "",
								Config.getConfigSettings().get(ConfigSettings.SHOW_VALUES_AS_TREE_CONSOLE_SPACE_FOR_LAST) + "");

						sender.sendMessage(getTranslatedStringSplitted(sender, TranslationPaths.ARG_LANG_TREE_SECTION.getPath(), args));
						sender.sendMessage(tree.split("%break%"));

					} else
					{
						String tree = Tree.getTree(LanguageFile.getLanguageConfig(TEMPLATE.getPluginName(), args[2]).getConfigurationSection(args[3]),
								Config.getConfigSettings().get(ConfigSettings.SHOW_VALUES_AS_TREE_PLAYER_SUBTREE) + "",
								Config.getConfigSettings().get(ConfigSettings.SHOW_VALUES_AS_TREE_PLAYER_SPACE) + "",
								Config.getConfigSettings().get(ConfigSettings.SHOW_VALUES_AS_TREE_PLAYER_LAST_SUBTREE) + "",
								Config.getConfigSettings().get(ConfigSettings.SHOW_VALUES_AS_TREE_PLAYER_SPACE_FOR_LAST) + "");

						sender.sendMessage(getTranslatedStringSplitted(sender, TranslationPaths.ARG_LANG_TREE_SECTION.getPath(), args));
						sender.sendMessage(tree.split("%break%"));
					}

					return true;
				} else
				{
					sender.sendMessage(getTranslatedStringSplitted(sender, TranslationPaths.ARG_LANG_TREE_NO_SECTION.getPath(), args));

					return false;
				}
			} else
			{
				sender.sendMessage(getTranslatedStringSplitted(sender, TranslationPaths.ARG_LANG_TREE_LANGUAGE_NOT_LOADED.getPath(), args));

				return false;
			}
		} else
		{
			if(sender instanceof ConsoleCommandSender)
			{
				String tree = Tree.getTree(
						LanguageFile.getLanguageConfig(TEMPLATE.getPluginName(), Config.getConfigSettings().get(ConfigSettings.LANGUAGE) + ""),
						Config.getConfigSettings().get(ConfigSettings.SHOW_VALUES_AS_TREE_CONSOLE_SUBTREE) + "",
						Config.getConfigSettings().get(ConfigSettings.SHOW_VALUES_AS_TREE_CONSOLE_SPACE) + "",
						Config.getConfigSettings().get(ConfigSettings.SHOW_VALUES_AS_TREE_CONSOLE_LAST_SUBTREE) + "",
						Config.getConfigSettings().get(ConfigSettings.SHOW_VALUES_AS_TREE_CONSOLE_SPACE_FOR_LAST) + "");

				sender.sendMessage(getTranslatedStringSplitted(sender, TranslationPaths.ARG_LANG_TREE_SECTION.getPath(), args));
				sender.sendMessage(tree.split("%break%"));

			} else
			{
				String tree = Tree.getTree(
						LanguageFile.getLanguageConfig(TEMPLATE.getPluginName(), Config.getConfigSettings().get(ConfigSettings.LANGUAGE) + ""),
						Config.getConfigSettings().get(ConfigSettings.SHOW_VALUES_AS_TREE_PLAYER_SUBTREE) + "",
						Config.getConfigSettings().get(ConfigSettings.SHOW_VALUES_AS_TREE_PLAYER_SPACE) + "",
						Config.getConfigSettings().get(ConfigSettings.SHOW_VALUES_AS_TREE_PLAYER_LAST_SUBTREE) + "",
						Config.getConfigSettings().get(ConfigSettings.SHOW_VALUES_AS_TREE_PLAYER_SPACE_FOR_LAST) + "");

				sender.sendMessage(getTranslatedStringSplitted(sender, TranslationPaths.ARG_LANG_TREE_SECTION.getPath(), args));
				sender.sendMessage(tree.split("%break%"));
			}

			return true;
		}
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
		loadPermission(PermissionNames.ARG_LANG_TREE);
	}

	/**
	 * Register all sub arguments for this argument
	 */
	@Override
	public void registerAllSubArguments()
	{
		ExtendedSubSyntax language = new ExtendedSubSyntax("Language", TranslationPaths.DESCRIPTION_ARG_LANG_TREE_LANGUAGE, true, false);
		ExtendedSubSyntax key = new ExtendedSubSyntax("Key", TranslationPaths.DESCRIPTION_ARG_LANG_TREE_KEY, true, false);

		language.addExtendedSubSyntax(key);

		addExtendedSyntax(language, true);
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
	 * @throws LanguageNotLoadedException 
	 */
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args, int start)
			throws TranslationNotFoundException, PluginNotRegisteredException, LanguageNotLoadedException
	{
		if(args.length == 3)
		{
			return getListOfStringsMatchingLastWord(args, TranslateHandler.getPluginLanguages(TEMPLATE.getPluginName()));

		} else if(args.length == 4)
		{
			if(LanguageFile.isLanguageLoaded(TEMPLATE.getPluginName(), args[2]))
			{
				return getSubSectionPaths(LanguageFile.getLanguageConfig(TEMPLATE.getPluginName(), args[2]), args, 3, "", false);
			}
		}

		return Collections.emptyList();
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

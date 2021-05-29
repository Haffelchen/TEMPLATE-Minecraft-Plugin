package net.haffel.PL.args.cfg;

import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;

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
import net.haffel.ml.exceptions.TranslationNotFoundException;

/**
 * Template class
 * 
 * @since v1.1
 * @lastEdit v1.1
 */
public class CfgTreeSubArg extends ArgumentBase
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
		return TEMPLATE.getTranslatedString(sender, TranslationPaths.DESCRIPTION_ARG_CFG_TREE.getPath());
	}

	@Override
	public boolean execute(CommandSender sender, Command cmd, String label, String[] args)
			throws CommandException, TranslationNotFoundException
	{
		if(args.length > 2)
		{
			if(getConfig().isConfigurationSection(args[2]))
			{
				if(sender instanceof ConsoleCommandSender)
				{
					String tree = Tree.getTree(getConfig().getConfigurationSection(args[2]),
							Config.getConfigSettings().get(ConfigSettings.SHOW_VALUES_AS_TREE_CONSOLE_SUBTREE) + "",
							Config.getConfigSettings().get(ConfigSettings.SHOW_VALUES_AS_TREE_CONSOLE_SPACE) + "",
							Config.getConfigSettings().get(ConfigSettings.SHOW_VALUES_AS_TREE_CONSOLE_LAST_SUBTREE) + "",
							Config.getConfigSettings().get(ConfigSettings.SHOW_VALUES_AS_TREE_CONSOLE_SPACE_FOR_LAST) + "");

					sender.sendMessage(getTranslatedStringSplitted(sender, TranslationPaths.ARG_CFG_TREE_SECTION.getPath(), args));
					sender.sendMessage(tree.split("%break%"));

				} else
				{
					String tree = Tree.getTree(getConfig().getConfigurationSection(args[2]),
							Config.getConfigSettings().get(ConfigSettings.SHOW_VALUES_AS_TREE_PLAYER_SUBTREE) + "",
							Config.getConfigSettings().get(ConfigSettings.SHOW_VALUES_AS_TREE_PLAYER_SPACE) + "",
							Config.getConfigSettings().get(ConfigSettings.SHOW_VALUES_AS_TREE_PLAYER_LAST_SUBTREE) + "",
							Config.getConfigSettings().get(ConfigSettings.SHOW_VALUES_AS_TREE_PLAYER_SPACE_FOR_LAST) + "");

					sender.sendMessage(getTranslatedStringSplitted(sender, TranslationPaths.ARG_CFG_TREE_SECTION.getPath(), args));
					sender.sendMessage(tree.split("%break%"));
				}

				return true;

			} else
			{
				sender.sendMessage(getTranslatedStringSplitted(sender,

						TranslationPaths.ARG_CFG_TREE_NO_SECTION.getPath(), args));

				return false;
			}
		} else
		{
			if(sender instanceof ConsoleCommandSender)
			{
				String tree = Tree.getTree(getConfig(), Config.getConfigSettings().get(ConfigSettings.SHOW_VALUES_AS_TREE_CONSOLE_SUBTREE) + "",
						Config.getConfigSettings().get(ConfigSettings.SHOW_VALUES_AS_TREE_CONSOLE_SPACE) + "",
						Config.getConfigSettings().get(ConfigSettings.SHOW_VALUES_AS_TREE_CONSOLE_LAST_SUBTREE) + "",
						Config.getConfigSettings().get(ConfigSettings.SHOW_VALUES_AS_TREE_CONSOLE_SPACE_FOR_LAST) + "");

				sender.sendMessage(getTranslatedStringSplitted(sender, TranslationPaths.ARG_CFG_TREE_FULL.getPath(), args));
				sender.sendMessage(tree.split("%break%"));

			} else
			{
				String tree = Tree.getTree(getConfig(), Config.getConfigSettings().get(ConfigSettings.SHOW_VALUES_AS_TREE_PLAYER_SUBTREE) + "",
						Config.getConfigSettings().get(ConfigSettings.SHOW_VALUES_AS_TREE_PLAYER_SPACE) + "",
						Config.getConfigSettings().get(ConfigSettings.SHOW_VALUES_AS_TREE_PLAYER_LAST_SUBTREE) + "",
						Config.getConfigSettings().get(ConfigSettings.SHOW_VALUES_AS_TREE_PLAYER_SPACE_FOR_LAST) + "");

				sender.sendMessage(getTranslatedStringSplitted(sender, TranslationPaths.ARG_CFG_TREE_FULL.getPath(), args));
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
		loadPermission(PermissionNames.ARG_CFG_TREE);
	}

	/**
	 * Register all sub arguments for this argument
	 */
	@Override
	public void registerAllSubArguments()
	{
		ExtendedSubSyntax value = new ExtendedSubSyntax("Key", TranslationPaths.DESCRIPTION_ARG_CFG_TREE_KEY, true, false);

		addExtendedSyntax(value, true);
	}

	/**
	 * @return List of tab completion options
	 * @throws TranslationNotFoundException
	 *             For the given translation key was no translation found
	 * @throws UserFormatConfigDoesntExistException
	 *             The user formats config for the sender doesnt exist
	 * @throws SomeTranslationException
	 *             Any other error
	 */
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args, int start)
			throws TranslationNotFoundException
	{
		if(args.length == 3)
		{
			return getSubSectionPaths(getConfig(), args, 2, "", false);
		}

		return Collections.emptyList();
	}

	/**
	 * @return Configuration file
	 */
	private static FileConfiguration getConfig()
	{
		return TEMPLATE.getInstance().getConfig();
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

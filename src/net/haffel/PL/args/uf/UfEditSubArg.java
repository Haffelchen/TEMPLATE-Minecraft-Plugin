package net.haffel.PL.args.uf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import net.haffel.PL.TEMPLATE;
import net.haffel.PL.args.UserFormatsArg;
import net.haffel.PL.args.utils.ArgumentBase;
import net.haffel.PL.args.utils.ExtendedSubSyntax;
import net.haffel.PL.exceptions.PermissionNotFoundException;
import net.haffel.PL.files.TranslationPaths;
import net.haffel.PL.perms.utils.PermissionHandler;
import net.haffel.PL.perms.utils.PermissionNames;
import net.haffel.ml.exceptions.TranslationNotFoundException;
import net.haffel.ml.files.UserFormatFile;

/**
 * Template class
 * 
 * @since v1.1
 * @lastEdit v1.1
 */
public class UfEditSubArg extends ArgumentBase
{
	/**
	 * @return Name of the sub argument
	 */
	@Override
	public String getArgumentName()
	{
		return "edit";
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
		return TEMPLATE.getTranslatedString(sender, TranslationPaths.DESCRIPTION_ARG_UF_EDIT.getPath());
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean execute(CommandSender sender, Command cmd, String label, String[] args)
			throws CommandException, TranslationNotFoundException
	{
		Player p = (Player) sender;

		if(args.length > 3)
		{
			FileConfiguration ufConfig = UserFormatFile.getUserFormatConfig(p, TEMPLATE.getPluginName());

			if(args.length > 4 && args[2].equalsIgnoreCase("$list"))
			{
				List<String> list = new ArrayList<>();

				if(UserFormatsArg.getUFChanges().containsKey(sender) && UserFormatsArg.getUFChanges().get(sender).containsKey(args[3])
						&& UserFormatsArg.getUFChanges().get(sender).get(args[3]) instanceof ArrayList<?>)
				{
					list = (List<String>) UserFormatsArg.getUFChanges().get(sender).get(args[3]);

				} else if(ufConfig.isSet("Translations." + args[3]))
				{
					if(ufConfig.isConfigurationSection("Translations." + args[3]))
					{
						sender.sendMessage(getTranslatedStringSplitted(sender, TranslationPaths.ARG_UF_EDIT_LIST_KEY_IS_SECTION.getPath(), args));
						return false;

					} else if(ufConfig.isList("Translations." + args[3]))
					{
						list = ufConfig.getStringList("Translations." + args[3]);
					}
				}

				StringBuilder edited = new StringBuilder();

				if(args.length > 5 && args[4].equalsIgnoreCase("$null"))
				{
					try
					{
						int index = Integer.parseInt(args[5]);

						if(index >= 0 && index < list.size())
						{
							String[] extraArgs =
							{ list.get(index) };

							list.remove(index);

							sender.sendMessage(
									getTranslatedStringSplitted(sender, TranslationPaths.ARG_UF_EDIT_LIST_REMOVED.getPath(), args, extraArgs));
						} else
						{
							sender.sendMessage(
									getTranslatedStringSplitted(sender, TranslationPaths.ARG_UF_EDIT_LIST_REMOVE_DOESNT_CONTAIN.getPath(), args));
						}
					} catch(NumberFormatException e)
					{
						sender.sendMessage(
								getTranslatedStringSplitted(sender, TranslationPaths.ARG_UF_EDIT_LIST_REMOVE_ONLY_INTEGER.getPath(), args));
						return false;
					}
				} else
				{
					for(int i = 4; i < args.length; i++)
					{
						edited.append(" ");
						edited.append(args[i]);
					}

					String[] extraArgs =
					{ edited.toString().replaceFirst(" ", "") };

					list.add(extraArgs[0]);

					sender.sendMessage(getTranslatedStringSplitted(sender, TranslationPaths.ARG_UF_EDIT_LIST_ADDED.getPath(), args, extraArgs));
				}

				if(!UserFormatsArg.getUFChanges().containsKey(sender))
				{
					UserFormatsArg.getUFChanges().put(sender, new HashMap<>());
				}

				UserFormatsArg.getUFChanges().get(sender).put(args[3], list);

				return true;
			} else
			{
				StringBuilder edited = new StringBuilder();

				for(int i = 3; i < args.length; i++)
				{
					edited.append(" ");
					edited.append(args[i]);
				}

				if(!UserFormatsArg.getUFChanges().containsKey(sender))
				{
					UserFormatsArg.getUFChanges().put(sender, new HashMap<>());
				}

				String[] extraArgs =
				{ edited.toString().replaceFirst(" ", "") };

				UserFormatsArg.getUFChanges().get(sender).put(args[2], extraArgs[0]);

				sender.sendMessage(getTranslatedStringSplitted(sender, TranslationPaths.ARG_UF_EDIT.getPath(), args, extraArgs));
				return true;
			}
		}

		if(args.length > 2 && args[2].equalsIgnoreCase("$list"))
		{
			String[] extraArgs =
			{ getArgumentSyntax(args, 2, 2) };

			sender.sendMessage(getTranslatedStringSplitted(sender, TranslationPaths.SYNTAX_MESSAGE.getPath(), args, extraArgs));

		} else
		{
			String[] extraArgs =
			{ getArgumentSyntax(args, 2) };

			sender.sendMessage(getTranslatedStringSplitted(sender, TranslationPaths.SYNTAX_MESSAGE.getPath(), args, extraArgs));
		}

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
		loadPermission(PermissionNames.ARG_UF_GET);
	}

	/**
	 * Register all sub arguments for this argument
	 */
	@Override
	public void registerAllSubArguments()
	{
		ExtendedSubSyntax list = new ExtendedSubSyntax("$list", TranslationPaths.DESCRIPTION_ARG_UF_EDIT_LIST, false, true);
		ExtendedSubSyntax list_key = new ExtendedSubSyntax("Key", TranslationPaths.DESCRIPTION_ARG_UF_EDIT_LIST_KEY, false, false);
		ExtendedSubSyntax list_key_null = new ExtendedSubSyntax("$null", TranslationPaths.DESCRIPTION_ARG_UF_EDIT_LIST_KEY_NULL, false, true);
		ExtendedSubSyntax list_key_null_index = new ExtendedSubSyntax("Index", TranslationPaths.DESCRIPTION_ARG_UF_EDIT_LIST_KEY_NULL_INDEX, false,
				false);
		ExtendedSubSyntax list_key_value = new ExtendedSubSyntax("Value", TranslationPaths.DESCRIPTION_ARG_UF_EDIT_LIST_KEY_VALUE, false, false);
		ExtendedSubSyntax key = new ExtendedSubSyntax("Key", TranslationPaths.DESCRIPTION_ARG_UF_EDIT_KEY, false, false);
		ExtendedSubSyntax key_null = new ExtendedSubSyntax("$null", TranslationPaths.DESCRIPTION_ARG_UF_EDIT_KEY_NULL, false, true);
		ExtendedSubSyntax key_value = new ExtendedSubSyntax("Value", TranslationPaths.DESCRIPTION_ARG_UF_EDIT_KEY_VALUE, false, false);

		list_key_null.addExtendedSubSyntax(list_key_null_index);
		list_key.addExtendedSubSyntax(list_key_null);
		list_key.addExtendedSubSyntax(list_key_value);
		list.addExtendedSubSyntax(list_key);

		key.addExtendedSubSyntax(key_null);
		key.addExtendedSubSyntax(key_value);

		addExtendedSyntax(list, false);
		addExtendedSyntax(key, true);
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
		FileConfiguration ufCfg = UserFormatFile.getUserFormatConfig((Player) sender, TEMPLATE.getPluginName());

		if(args.length == 3)
		{
			if(ufCfg.isConfigurationSection("Translations"))
			{
				return getSubKeyPaths(ufCfg, args, 2, "Translations", "$list");
			}

			return super.getListOfStringsMatchingLastWord(args, Arrays.asList("$list"));

		} else if(args.length == 4)
		{
			if(args[2].equalsIgnoreCase("$list"))
			{
				return getSubSectionPaths(ufCfg, args, 3, "Translations", true);

			} else
			{
				List<String> complete = new ArrayList<>();

				if(ufCfg.contains(args[2]))
				{
					complete.add("$null");
				}

				complete.add(getTranslatedString(sender, TranslationPaths.ARG_UF_TAB_NEW_VALUE.getPath(), args));

				return getListOfStringsMatchingLastWord(args, complete);
			}
		} else if(args.length == 5)
		{
			if(args[2].equalsIgnoreCase("$list"))
			{
				List<String> complete = new ArrayList<>();

				if(ufCfg.isList(args[3]))
				{
					complete.add("$null");
				}

				complete.add(getTranslatedString(sender, TranslationPaths.ARG_UF_TAB_NEW_VALUE.getPath(), args));

				return getListOfStringsMatchingLastWord(args, complete);
			}
		} else if(args.length == 6)
		{
			if(args[2].equalsIgnoreCase("$list") && ufCfg.contains(args[3]) && args[4].equalsIgnoreCase("$null"))
			{
				return getListOfStringsMatchingLastWord(args,
						getTranslatedString(sender, TranslationPaths.ARG_UF_TAB_INDEX_OF_VALUE.getPath(), args));
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
	private String[] getTranslatedStringSplitted(CommandSender commandSender, String translate, String[] args, String[] extraArgs)
			throws TranslationNotFoundException
	{
		return TEMPLATE.getTranslatedStringSplitted(commandSender, translate, args, extraArgs);
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
	private String getTranslatedString(CommandSender commandSender, String translate, String[] args)
			throws TranslationNotFoundException
	{
		return TEMPLATE.getTranslatedString(commandSender, translate, args);
	}
}

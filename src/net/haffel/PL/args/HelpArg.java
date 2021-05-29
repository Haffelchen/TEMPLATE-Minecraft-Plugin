package net.haffel.PL.args;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import net.haffel.PL.TEMPLATE;
import net.haffel.PL.args.utils.ArgumentBase;
import net.haffel.PL.args.utils.ExtendedSubSyntax;
import net.haffel.PL.cmds.utils.CommandHandler;
import net.haffel.PL.cmds.utils.CommandType;
import net.haffel.PL.cmds.utils.HelpPage;
import net.haffel.PL.exceptions.PermissionNotFoundException;
import net.haffel.PL.files.TranslationPaths;
import net.haffel.PL.perms.utils.PermissionHandler;
import net.haffel.PL.perms.utils.PermissionNames;
import net.haffel.ml.exceptions.PluginNotRegisteredException;
import net.haffel.ml.exceptions.TranslationNotFoundException;

/**
 * Template class
 * 
 * @since v1.1
 * @lastEdit v1.1
 */
public class HelpArg extends ArgumentBase
{
	/**
	 * @return Name of this argument
	 */
	@Override
	public String getArgumentName()
	{
		return "help";
	}

	/**
	 * @return Description of the argument
	 * @throws TranslationNotFoundException
	 *             For the given translation key was no translation found
	 * @throws UserFormatConfigDoesntExistException
	 *             The user formats config for the sender doesnt exist
	 * @throws SomeTranslationException
	 *             Any other error
	 */
	@Override
	public String getArgumentDescription(CommandSender sender) throws TranslationNotFoundException
	{
		return TEMPLATE.getTranslatedString(sender, TranslationPaths.DESCRIPTION_ARG_HELP.getPath());
	}

	/**
	 * @return List of aliases for this argument
	 */
	@Override
	public List<String> getArgumentAliases()
	{
		return Arrays.asList("h", "?");
	}

	/**
	 * @return List of types which can use this argument
	 * @see CommandType
	 */
	@Override
	public List<CommandType> getArgumentTypes()
	{
		return Arrays.asList(CommandType.PLAYER, CommandType.CONSOLE);
	}

	@Override
	public boolean execute(CommandSender sender, Command cmd, String label, String[] args)
			throws CommandException, TranslationNotFoundException
	{
		if(sender instanceof ConsoleCommandSender)
		{
			if(!CommandHandler.isSenderHelpRegistered(sender))
			{
				CommandHandler.registerHelp(sender);
			}
		}
		
		if(args.length > 1)
		{
			if(args.length > 2)
			{
				try
				{
					int page = Integer.parseInt(args[2]);

					if(page >= 0)
					{
						return sendHelp(sender, page, label + "." + args[1], args);

					} else
					{
						String[] extraArgs =
						{ args[2] };

						sender.sendMessage(getTranslatedStringSplitted(sender, TranslationPaths.ARG_HELP_INVALID_PAGE.getPath(), args, extraArgs));

						return false;
					}
				} catch(NumberFormatException e)
				{
					String[] extraArgs =
					{ args[2] };

					sender.sendMessage(getTranslatedStringSplitted(sender, TranslationPaths.ARG_HELP_INVALID_PAGE.getPath(), args, extraArgs));

					return false;
				}
			} else
			{
				try
				{
					int page = Integer.parseInt(args[1]);

					if(page >= 0)
					{
						return sendHelp(sender, page, label, args);

					} else
					{
						String[] extraArgs =
						{ args[1] };

						sender.sendMessage(getTranslatedStringSplitted(sender, TranslationPaths.ARG_HELP_INVALID_PAGE.getPath(), args, extraArgs));

						return false;
					}
				} catch(NumberFormatException e)
				{
					return sendHelp(sender, 1, label + "." + args[1], args);
				}
			}
		}

		return sendHelp(sender, 1, label, args);
	}
	
	private boolean sendHelp(CommandSender sender, int page, String path, String[] args) throws TranslationNotFoundException
	{
		if(CommandHandler.isArgumentHelpRegistered(sender, path))
		{
			List<HelpPage> helpPages = CommandHandler.getHelpForArg(sender, path);

			if(page < helpPages.size())
			{
				String[] extraArgs =
				{ "/" + path.replace(".", " "), Integer.toString(helpPages.get(page).getPage()), Integer.toString(helpPages.size() - 1),
						helpPages.get(page).getContent() };

				sender.sendMessage(getTranslatedStringSplitted(sender, TranslationPaths.ARG_HELP.getPath(), args, extraArgs));

				return true;

			} else
			{
				String[] extraArgs =
				{ Integer.toString(page) };

				sender.sendMessage(
						getTranslatedStringSplitted(sender, TranslationPaths.ARG_HELP_INVALID_PAGE.getPath(), args, extraArgs));

				return false;
			}
		} else
		{
			String[] extraArgs =
			{ path };

			sender.sendMessage(getTranslatedStringSplitted(sender, TranslationPaths.ARG_HELP_INVALID_ARG.getPath(), args, extraArgs));

			return false;
		}
	}

	/**
	 * Adds all needed permissions for this argument to a list. This does NOT load the permissions
	 * 
	 * @throws PermissionNotFoundException
	 *             Permission is not loaded
	 * @see PermissionHandler#setupPermissions()
	 */
	public void loadPermissions() throws PermissionNotFoundException
	{
		loadPermission(PermissionNames.ARG_HELP);
	}
	
	/**
	 * Register all sub arguments for this argument
	 */
	@Override
	public void registerAllSubArguments()
	{
		ExtendedSubSyntax page = new ExtendedSubSyntax("Page", TranslationPaths.DESCRIPTION_ARG_HELP_PAGE, true, false);
		
		ExtendedSubSyntax arg = new ExtendedSubSyntax("Argument", TranslationPaths.DESCRIPTION_ARG_HELP_ARGUMENT, true, false);
		ExtendedSubSyntax arg_page = new ExtendedSubSyntax("Page", TranslationPaths.DESCRIPTION_ARG_HELP_ARGUMENT_PAGE, true, false);
		
		arg.addExtendedSubSyntax(arg_page);
		
		addExtendedSyntax(page, false);
		addExtendedSyntax(arg, false);
	}

	/**
	 * @return List of tab completion options
	 * @throws TranslationNotFoundException
	 *             For the given translation key was no translation found
	 * @throws UserFormatConfigDoesntExistException
	 *             The user formats config for the sender doesnt exist
	 * @throws SomeTranslationException
	 *             Any other error
	 * @throws PluginNotRegisteredException Plugin is not registered in Multi Languages Plugin
	 */
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args, int start)
			throws TranslationNotFoundException, PluginNotRegisteredException
	{
		if(args.length == 2)
		{
			List<String> complete = new ArrayList<String>();

			int entrys = CommandHandler.getHelpForArg(sender, label).size();

			for(int i = 0; i < entrys; i++)
			{
				complete.add(Integer.toString(i));
			}

			for(String path : CommandHandler.getHelpPaths(sender))
			{
				if(!path.equalsIgnoreCase(label))
				{
					if(StringUtils.countMatches(args[1], ".") + 2 > StringUtils.countMatches(path, "."))
					{
						if(path.startsWith(label + "."))
						{
							complete.add(path.replaceFirst(label + ".", ""));
						}
					}
				}
			}

			return getListOfStringsMatchingLastWord(args, complete);

		} else if(args.length == 3)
		{
			String path = label + "." + args[1];

			if(CommandHandler.isArgumentHelpRegistered(sender, path))
			{
				List<String> complete = new ArrayList<String>();

				int entrys = CommandHandler.getHelpForArg(sender, path).size();

				for(int i = 0; i < entrys; i++)
				{
					complete.add(Integer.toString(i));
				}

				return getListOfStringsMatchingLastWord(args, complete);
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
	private static String[] getTranslatedStringSplitted(CommandSender commandSender, String translate, String[] args, String[] extraArgs)
			throws TranslationNotFoundException
	{
		return TEMPLATE.getTranslatedStringSplitted(commandSender, translate, args, extraArgs);
	}
}

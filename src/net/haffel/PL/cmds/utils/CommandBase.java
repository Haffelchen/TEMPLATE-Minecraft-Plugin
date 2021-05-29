package net.haffel.PL.cmds.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import lombok.Getter;
import lombok.Setter;
import net.haffel.PL.NoneTranslatedMessages;
import net.haffel.PL.TEMPLATE;
import net.haffel.PL.args.utils.IArgument;
import net.haffel.PL.exceptions.PermissionNotFoundException;
import net.haffel.PL.files.Config;
import net.haffel.PL.files.ConfigSettings;
import net.haffel.PL.files.TranslationPaths;
import net.haffel.PL.perms.utils.PermissionNames;
import net.haffel.PL.utils.Debug;
import net.haffel.ml.exceptions.LanguageFileNotFoundException;
import net.haffel.ml.exceptions.LanguageNotLoadedException;
import net.haffel.ml.exceptions.PluginNotRegisteredException;
import net.haffel.ml.exceptions.TranslationNotFoundException;

/**
 * Template class
 * 
 * @since v1.1
 * @lastEdit v1.1
 */
public abstract class CommandBase implements ICommand
{
	// The syntax of all sub arguments of this command
	@Getter @Setter String commandSyntaxSub;
	// List with all permissions
	@Getter @Setter List<Permission> permissions = new ArrayList<>();
	// List with all argument classes
	@Getter private List<IArgument> arguments = new ArrayList<>();
	// List with all argument names and aliases
	@Getter private Map<String, IArgument> argumentsAndAliases = new HashMap<>();

	public CommandBase()
	{
		try
		{
			loadPermissions();
			registerAllArguments();

		} catch(PermissionNotFoundException e)
		{
			Debug.addStartup(System.currentTimeMillis(), e);
		}

	}

	/**
	 * @return Syntax for the command
	 */
	public String getCommandSyntax()
	{
		return getCommandSyntax(true);
	}

	/**
	 * @return Syntax for the command
	 */
	public String getCommandSyntax(boolean withSub)
	{
		if(withSub)
		{
			if(getCommandSyntaxSub().length() > 0)
			{
				return "/" + " " + getCommandName() + " " + getCommandSyntaxSub();

			} else
			{
				return "/" + " " + getCommandName();
			}
		} else
		{
			return "/" + getCommandName();
		}
	}

	/**
	 * @return A List with all registered arguments
	 */
	public List<String> getArgumentNames()
	{
		List<String> argumentNames = new ArrayList<>();

		getArguments().forEach(arg -> argumentNames.add(arg.getArgumentName()));

		return argumentNames;
	}

	/**
	 * @param name
	 *            The name of the searched argument
	 * @return Argument with given name if one is registered
	 */
	public IArgument getArgumentByName(String name)
	{
		for(IArgument cmd : getArguments())
		{
			if(cmd.getArgumentName().equalsIgnoreCase(name) || cmd.getArgumentAliases().contains(name))
			{
				return cmd;
			}
		}

		return null;
	}

	/**
	 * @param sender
	 *            The CommandSender who used the command
	 * @return A List with all registered arguments the sender has permissions to use
	 */
	public List<IArgument> getArgumentsAndAliasesByPermission(CommandSender sender)
	{
		List<IArgument> list = new ArrayList<>();

		for(IArgument argument : getArgumentsAndAliases().values())
		{
			if(argument.checkPermissions(sender))
			{
				list.add(argument);
			}
		}

		return list;
	}

	/**
	 * @param sender
	 *            The CommandSender who used the command
	 * @return A List with the names of all registered arguments the sender has permissions to use
	 */
	public List<String> getArgumentNamesAndAliasesByPermission(CommandSender sender)
	{
		List<String> list = new ArrayList<>();

		for(IArgument argument : getArgumentsAndAliasesByPermission(sender))
		{
			list.add(argument.getArgumentName());

			for(String alias : argument.getArgumentAliases())
			{
				list.add(alias);
			}
		}

		return list;
	}

	/**
	 * @return List of aliases for the command
	 */
	public List<String> getCommandAliases()
	{
		return Collections.emptyList();
	}

	/**
	 * @return List of types which can use this command
	 * @see CommandType
	 */
	public List<CommandType> getCommandTypes()
	{
		return Arrays.asList(CommandType.PLAYER, CommandType.CONSOLE, CommandType.COMMAND_BLOCK);
	}

	/**
	 * Execute command
	 * @throws LanguageNotLoadedException 
	 */
	@Override
	public boolean execute(CommandSender sender, Command cmd, String label, String[] args) throws CommandException, TranslationNotFoundException,
			LanguageFileNotFoundException, IOException, PluginNotRegisteredException, LanguageNotLoadedException
	{
		if(args.length >= 1)
		{
			for(String arg : getArgumentsAndAliases().keySet())
			{
				if(arg.equalsIgnoreCase(args[0]))
				{
					return getArgumentsAndAliases().get(arg).tryExecute(sender, cmd, label, args);
				}
			}
		}

		String[] extraArgs =
		{ getCommandSyntax() };

		sender.sendMessage(TEMPLATE.getTranslatedStringSplitted(sender, TranslationPaths.SYNTAX_MESSAGE.getPath(), args, extraArgs));

		return false;
	}

	/**
	 * If the sender has all needed permissions the execute() method gets invoked
	 * @throws LanguageNotLoadedException 
	 */
	public boolean tryExecute(CommandSender sender, Command cmd, String label, String[] args) throws TranslationNotFoundException,
			LanguageFileNotFoundException, IOException, PluginNotRegisteredException, LanguageNotLoadedException
	{
		if(checkPermissions(sender))
		{
			try
			{
				return execute(sender, cmd, label, args);

			} catch(CommandException e)
			{
				sender.sendMessage(ChatColor.RED + e.getMessage());
			}
		} else
		{
			sender.sendMessage(TEMPLATE.getTranslatedStringSplitted(sender, TranslationPaths.CMD_TEMPLATE_NO_PERMISSION.getPath(), args));
		}

		return false;
	}

	/**
	 * @return If the sender needs all permissions in the list or only one of them
	 */
	public boolean needAllPermissions()
	{
		return false;
	}

	/**
	 * @param The
	 *            sender to check its permissions
	 * @return If the sender has permission to use this command
	 */
	public boolean checkPermissions(CommandSender sender)
	{
		if(sender instanceof ConsoleCommandSender)
		{
			return getCommandTypes().contains(CommandType.CONSOLE);

		} else if(sender instanceof BlockCommandSender)
		{
			return getCommandTypes().contains(CommandType.COMMAND_BLOCK);

		} else if(sender instanceof Player)
		{

			if(!getCommandTypes().contains(CommandType.PLAYER))
			{
				return false;

			} else if(!getPermissions().isEmpty())
			{
				for(Permission perm : getPermissions())
				{
					if(sender.hasPermission(perm))
					{
						if(!needAllPermissions())
						{
							return true;
						}
					} else if(needAllPermissions())
					{
						return false;
					}
				}

				return needAllPermissions();

			} else
			{
				return true;
			}
		}

		return false;
	}

	public void loadPermission(PermissionNames permission) throws PermissionNotFoundException
	{
		Permission add = Bukkit.getPluginManager().getPermission(permission.getName());

		if(add != null)
		{
			getPermissions().add(add);

		} else
		{
			throw new PermissionNotFoundException(permission.getName());
		}
	}

	/**
	 * @param argument
	 *            Argument class to register
	 */
	public void registerArgument(IArgument argument)
	{
		getArgumentsAndAliases().put(argument.getArgumentName(), argument);
		getArguments().add(argument);

		for(String alias : argument.getArgumentAliases())
		{
			if(!getArgumentsAndAliases().containsKey(alias) || !getArgumentsAndAliases().get(alias).getArgumentName().equals(alias))
			{
				getArgumentsAndAliases().put(alias, argument);
			}
		}
	}

	/**
	 * Generate syntax
	 */
	public void registerSyntax()
	{
		String syntaxBase = "/" + getCommandName();

		List<IArgument> notOptional = new ArrayList<IArgument>();
		List<IArgument> optional = new ArrayList<IArgument>();

		for(IArgument arg : getArguments())
		{
			arg.registerSyntax(syntaxBase);

			if(arg.isOptional())
			{
				optional.add(arg);

			} else
			{
				notOptional.add(arg);
			}
		}

		StringBuilder syntaxSub = new StringBuilder();
		StringBuilder syntaxSubNotOptional = new StringBuilder();
		StringBuilder syntaxSubOptional = new StringBuilder();

		if(!notOptional.isEmpty())
		{
			syntaxSubNotOptional.append("<");

			for(int i = 0; i < notOptional.size(); i++)
			{
				syntaxSubNotOptional.append(notOptional.get(i).getArgumentName());

				if(i < (notOptional.size() - 1))
				{
					syntaxSubNotOptional.append("¦");
				}
			}

			syntaxSubNotOptional.append(">");
		}

		if(!optional.isEmpty())
		{
			for(int i = 0; i < optional.size(); i++)
			{
				syntaxSubOptional.append(optional.get(i).getArgumentName());

				if(i < (optional.size() - 1))
				{
					syntaxSubOptional.append("¦");
				}
			}
		}

		if(syntaxSubOptional.length() > 0)
		{
			syntaxSub.append("[");

			if(syntaxSubNotOptional.length() > 0)
			{
				syntaxSub.append(syntaxSubNotOptional.toString());
				syntaxSub.append("¦");
			}

			syntaxSub.append(syntaxSubOptional.toString());
			syntaxSub.append("]");

		} else if(syntaxSubNotOptional.length() > 0)
		{
			syntaxSub.append(syntaxSubNotOptional.toString());
		}

		setCommandSyntaxSub(syntaxSub.toString());
	}

	/**
	 * Generate help
	 */
	@Override
	public String createHelp(CommandSender sender) throws TranslationNotFoundException
	{
		StringBuilder sb = new StringBuilder();

		if(!checkPermissions(sender))
		{
			return sb.toString();

		} else if(getArguments().isEmpty())
		{
			sb.append("%break%");
			sb.append(TEMPLATE.getTranslatedString(TranslationPaths.SYNTAX_COLOR.getPath()));
			sb.append(getCommandSyntax());

			if(getCommandDescription(sender).length() > 0)
			{
				sb.append(TEMPLATE.getTranslatedString(TranslationPaths.SYNTAX_DESCRIPTION_COLOR.getPath()));
				sb.append(getCommandDescription(sender));
			}
		} else
		{
			boolean allOptional = true;

			for(IArgument arg : getArguments())
			{
				if(!arg.isOptional())
				{
					allOptional = false;
					break;
				}
			}

			if(allOptional)
			{
				sb.append("%break%");
				sb.append(TEMPLATE.getTranslatedString(TranslationPaths.SYNTAX_COLOR.getPath()));
				sb.append(getCommandSyntax(false));

				if(getCommandDescription(sender).length() > 0)
				{
					sb.append(TEMPLATE.getTranslatedString(TranslationPaths.SYNTAX_DESCRIPTION_COLOR.getPath()));
					sb.append(getCommandDescription(sender));
				}
			}
		}

		if(!getArguments().isEmpty())
		{
			for(IArgument arg : getArguments())
			{
				sb.append(arg.createHelp(sender));
			}
		}

		return sb.toString();
	}

	@Override
	public void registerHelp(CommandSender sender)
	{
		if(checkPermissions(sender))
		{
			try
			{
				int maxLength = (int) Config.getConfigSettings().get(ConfigSettings.HELP_MAX_LENGTH);
				StringBuilder help = new StringBuilder();
				int page = 1;
				String path = getCommandName();

				CommandHandler.getHelpPages().get(sender).put(path, new ArrayList<HelpPage>());

				StringBuilder aliases = new StringBuilder();
				StringBuilder permissions = new StringBuilder();
				StringBuilder types = new StringBuilder();

				if(!getCommandAliases().isEmpty())
				{
					for(String alias : getCommandAliases())
					{
						String[] extraArgs =
						{ alias };

						aliases.append(
								TEMPLATE.getTranslatedString(null, TranslationPaths.ARG_HELP_DETAILS_ALIAS.getPath(), new String[0], extraArgs));
					}
				} else
				{
					aliases.append(TEMPLATE.getTranslatedString(TranslationPaths.ARG_HELP_EMPTY.getPath()));
				}

				if(!getPermissions().isEmpty())
				{
					for(Permission perm : getPermissions())
					{
						String[] extraArgs =
						{ perm.getName() };

						permissions.append(
								TEMPLATE.getTranslatedString(null, TranslationPaths.ARG_HELP_DETAILS_PERMISSION.getPath(), new String[0], extraArgs));
					}
				} else
				{
					permissions.append(TEMPLATE.getTranslatedString(TranslationPaths.ARG_HELP_EMPTY.getPath()));
				}

				if(!getCommandTypes().isEmpty())
				{
					for(CommandType type : getCommandTypes())
					{
						String[] extraArgs =
						{ type.toString() };

						types.append(TEMPLATE.getTranslatedString(null, TranslationPaths.ARG_HELP_DETAILS_TYPE.getPath(), new String[0], extraArgs));
					}
				} else
				{
					types.append(TEMPLATE.getTranslatedString(TranslationPaths.ARG_HELP_EMPTY.getPath()));
				}

				String needAllPermissions = needAllPermissions() ? TEMPLATE.getTranslatedString(TranslationPaths.VALUE_TRUE.getPath())
						: TEMPLATE.getTranslatedString(TranslationPaths.VALUE_FALSE.getPath());
				String isEqual = TEMPLATE.getTranslatedString(TranslationPaths.VALUE_TRUE.getPath());
				String isOptional = TEMPLATE.getTranslatedString(TranslationPaths.VALUE_FALSE.getPath());
				String[] extraArgs =
				{ getCommandDescription(sender), aliases.toString().replaceFirst(", ", ""), permissions.toString().replaceFirst(", ", ""),
						needAllPermissions, types.toString().replaceFirst(", ", ""), isEqual, isOptional };
				String details = TEMPLATE.getTranslatedString(null, TranslationPaths.ARG_HELP_DETAILS_PAGE.getPath(), new String[0], extraArgs);

				CommandHandler.getHelpPages().get(sender).get(path).add(new HelpPage(0, path, details));

				for(String line : createHelp(sender).split("%break%"))
				{
					if(help.length() + line.length() > maxLength)
					{
						CommandHandler.getHelpPages().get(sender).get(path).add(new HelpPage(page, path, help.toString()));
						page++;
						help = new StringBuilder();

						if(line.length() > 0)
						{
							help.append("%break%");
							help.append(line);
						}
					} else
					{
						if(line.length() > 0)
						{
							help.append("%break%");
							help.append(line);
						}
					}
				}

				if(help.length() > 0)
				{
					CommandHandler.getHelpPages().get(sender).get(path).add(new HelpPage(page, path, help.toString()));
				}

				for(IArgument arg : getArguments())
				{
					arg.registerHelp(sender, path, maxLength);
				}
			} catch(TranslationNotFoundException e)
			{
				int index = Debug.addRunning(System.currentTimeMillis(), e);

				sender.sendMessage(NoneTranslatedMessages.REPORT_TO_ADMIN_WITH_INDEX.getMessage().replace("{INDEX}", Integer.toString(index)));
			}
		}
	}

	/**
	 * @param sender
	 *            The CommandSender which used the command
	 * @param cmd
	 *            The command which was executed
	 * @param label
	 *            Executed commands name
	 * @param args
	 *            Passed arguments
	 * @return List with tab completion options
	 * @throws TranslationNotFoundException
	 *             For the given translation key was no translation found
	 * @throws UserFormatConfigDoesntExistException
	 *             The user formats config for the sender doesnt exist
	 * @throws SomeTranslationException
	 *             Any other error
	 * @throws PluginNotRegisteredException
	 * @throws LanguageNotLoadedException 
	 */
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args)
			throws TranslationNotFoundException, PluginNotRegisteredException, LanguageNotLoadedException
	{
		if(args.length == 1)
		{
			return getListOfStringsMatchingLastWord(args, getArgumentNamesAndAliasesByPermission(sender));

		} else if(args.length >= 2 && getArgumentNamesAndAliasesByPermission(sender).contains(args[0]))
		{
			return getArgumentsAndAliases().get(args[0]).onTabComplete(sender, cmd, label, args, 2);
		}

		return Collections.emptyList();
	}

	public static boolean doesStringStartWith(String original, String region)
	{
		return region.regionMatches(true, 0, original, 0, original.length());
	}

	public static List<String> getListOfStringsMatchingLastWord(String[] args, String... possibilities)
	{
		return getListOfStringsMatchingLastWord(args, Arrays.asList(possibilities));
	}

	public static List<String> getListOfStringsMatchingLastWord(String[] inputArgs, Collection<String> possibleCompletions)
	{
		String lastArg = inputArgs[inputArgs.length - 1];
		List<String> list = new ArrayList<String>();

		if(!possibleCompletions.isEmpty())
		{
			for(String option : possibleCompletions)
			{
				if(doesStringStartWith(lastArg, option))
				{
					list.add(option);
				}
			}
		}

		return list;
	}
}

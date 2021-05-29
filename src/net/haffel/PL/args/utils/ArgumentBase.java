package net.haffel.PL.args.utils;

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
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import com.google.common.base.Functions;
import com.google.common.collect.Iterables;

import lombok.Getter;
import lombok.Setter;
import net.haffel.PL.TEMPLATE;
import net.haffel.PL.cmds.utils.CommandHandler;
import net.haffel.PL.cmds.utils.CommandType;
import net.haffel.PL.cmds.utils.HelpPage;
import net.haffel.PL.exceptions.PermissionNotFoundException;
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
public abstract class ArgumentBase implements IArgument
{
	// Syntax until this argument
	@Getter @Setter String argumentSyntaxBase;
	// Syntax for the sub arguments of this argument
	@Getter @Setter String argumentSyntaxSub;
	// List with all permissions
	@Getter @Setter List<Permission> permissions = new ArrayList<>();
	// List with all argument classes
	@Getter private List<IArgument> subArguments = new ArrayList<>();
	// List with all argument names and aliases
	@Getter private Map<String, IArgument> subArgumentsAndAliases = new HashMap<>();
	// List with extended sub syntaxes
	@Getter List<ExtendedSubSyntax> extendedSyntaxes = new ArrayList<>();
	// The default extended sub syntax
	@Getter @Setter ExtendedSubSyntax defaultExtendedSyntax;

	public ArgumentBase()
	{
		try
		{
			loadPermissions();
			registerAllSubArguments();

		} catch(PermissionNotFoundException | TranslationNotFoundException e)
		{
			Debug.addStartup(System.currentTimeMillis(), e);
		}
	}

	/**
	 * @return Syntax for this argument
	 */
	public String getArgumentSyntax()
	{
		return getArgumentSyntax(true);
	}

	/**
	 * @param withSub
	 *            If false, only base syntax gets returned
	 * @return Syntax for this argument
	 */
	public String getArgumentSyntax(boolean withSub)
	{
		String name = getArgumentName();

		if(!isEqualToArg())
		{
			if(isOptional())
			{
				name = "[" + name + "]";

			} else
			{
				name = "<" + name + ">";
			}
		}

		if(withSub)
		{
			if(getArgumentSyntaxSub().length() > 0)
			{
				return getArgumentSyntaxBase() + " " + name + " " + getArgumentSyntaxSub();

			} else
			{
				return getArgumentSyntaxBase() + " " + name;
			}
		} else
		{
			return getArgumentSyntaxBase() + " " + name;
		}
	}

	/**
	 * @param args
	 *            Arguments the user passed when using a command
	 * @param start
	 *            Start checking at this argument
	 * @param withSub
	 *            If false, only base syntax gets returned
	 * @return Syntax for this argument
	 */
	public String getArgumentSyntax(String[] args, int start, boolean withSub)
	{
		if(!getExtendedSyntaxes().isEmpty() && start < args.length)
		{
			int nextStart = start + 1;

			for(ExtendedSubSyntax syntax : getExtendedSyntaxes())
			{
				if(syntax.getExtendedSubSyntaxName().equalsIgnoreCase(args[start]))
				{
					return syntax.getExtendedSubSyntax(args, nextStart, withSub);
				}
			}

			if(getDefaultExtendedSyntax() != null)
			{
				return getDefaultExtendedSyntax().getExtendedSubSyntax(withSub);
			}
		}

		return getArgumentSyntax();
	}

	/**
	 * @param moreArgs
	 *            Number of sub arguments to loop through
	 * @return Syntax for the sub arguments
	 */
	public String getArgumentSyntax(int moreArgs)
	{
		StringBuilder sb = new StringBuilder();

		if(moreArgs > 0)
		{
			int nextMoreArgs = moreArgs - 1;

			if(!getArgumentSyntaxSub().isEmpty())
			{
				sb.append(" ");
				sb.append(getArgumentSyntaxSub());
			}

			if(!getExtendedSyntaxes().isEmpty())
			{
				for(ExtendedSubSyntax subArgSyntax1 : getExtendedSyntaxes())
				{
					sb.append(subArgSyntax1.getExtendedSubSyntax(nextMoreArgs));
				}
			}
		}

		return sb.toString();
	}

	public String getArgumentSyntax(String[] args, int start)
	{
		return getArgumentSyntax(args, start, true);
	}

	public String getArgumentSyntax(String[] args, int start, int moreArgs)
	{
		StringBuilder sb = new StringBuilder();
		ExtendedSubSyntax subArgSyntax = getDefaultExtendedSyntax();

		if(!getExtendedSyntaxes().isEmpty() && start < args.length)
		{
			int nextStart = start + 1;

			for(ExtendedSubSyntax syntax : getExtendedSyntaxes())
			{
				if(syntax.getExtendedSubSyntaxName().equalsIgnoreCase(args[start]))
				{
					sb.append(syntax.getExtendedSubSyntax(args, nextStart, false));
					subArgSyntax = syntax;
					break;
				}
			}

			if(sb.length() == 0)
			{
				if(getDefaultExtendedSyntax() != null)
				{
					sb.append(getDefaultExtendedSyntax().getExtendedSubSyntax(false));
				}
			}
		}

		if(sb.length() == 0)
		{
			sb.append(getArgumentSyntax(false));
			sb.append(getArgumentSyntax(moreArgs));

		} else
		{
			sb.append(subArgSyntax.getExtendedSubSyntax(moreArgs));
		}

		return sb.toString();
	}

	public boolean isEqualToArg()
	{
		return true;
	}

	public boolean isOptional()
	{
		return false;
	}

	public List<String> getArgumentAliases()
	{
		return Collections.emptyList();
	}

	public List<CommandType> getArgumentTypes()
	{
		return Arrays.asList(CommandType.PLAYER, CommandType.CONSOLE, CommandType.COMMAND_BLOCK);
	}

	/**
	 * @return A List with all registered arguments
	 */
	public List<String> getArgumentNames()
	{
		List<String> subArgumentNames = new ArrayList<>();

		getSubArguments().forEach(subArg -> subArgumentNames.add(subArg.getArgumentName()));

		return subArgumentNames;
	}

	/**
	 * @param sender
	 *            The CommandSender who used the command
	 * @return A List with all registered arguments the sender has permissions to use
	 */
	public List<IArgument> getSubArgumentsAndAliasesByPermission(CommandSender sender)
	{
		List<IArgument> list = new ArrayList<>();

		for(IArgument subArgument : getSubArgumentsAndAliases().values())
		{
			if(subArgument.checkPermissions(sender))
			{
				list.add(subArgument);
			}
		}

		return list;
	}

	/**
	 * @param sender
	 *            The CommandSender who used the command
	 * @return A List with the names of all registered arguments the sender has permissions to use
	 */
	public List<String> getSubArgumentNamesAndAliasesByPermission(CommandSender sender)
	{
		List<String> list = new ArrayList<>();

		for(IArgument subArgument : getSubArgumentsAndAliasesByPermission(sender))
		{
			list.add(subArgument.getArgumentName());

			for(String alias : subArgument.getArgumentAliases())
			{
				list.add(alias);
			}
		}

		return list;
	}

	public List<String> getExtendedSubSyntaxNames()
	{
		List<String> list = new ArrayList<>();

		for(ExtendedSubSyntax extSyntax : getExtendedSyntaxes())
		{
			list.add(extSyntax.getExtendedSubSyntaxName());
		}

		return list;
	}

	public ExtendedSubSyntax getExtendedSubSyntaxByName(String name)
	{
		for(ExtendedSubSyntax extSyntax : getExtendedSyntaxes())
		{
			if(extSyntax.getExtendedSubSyntaxName().equals(name))
			{
				return extSyntax;
			}
		}

		return null;
	}

	public boolean execute(CommandSender sender, Command cmd, String label, String[] args) throws CommandException, TranslationNotFoundException,
			LanguageNotLoadedException, LanguageFileNotFoundException, IOException, PluginNotRegisteredException
	{
		if(args.length > 1 && !getSubArgumentsAndAliases().isEmpty())
		{
			for(String arg : getSubArgumentsAndAliases().keySet())
			{
				if(arg.equalsIgnoreCase(args[1]))
				{
					return getSubArgumentsAndAliases().get(arg).tryExecute(sender, cmd, label, args);
				}
			}
		}

		String[] extraArgs =
		{ getArgumentSyntax() };

		sender.sendMessage(TEMPLATE.getTranslatedStringSplitted(sender, TranslationPaths.SYNTAX_MESSAGE.getPath(), args, extraArgs));

		return false;
	}

	public boolean tryExecute(CommandSender sender, Command cmd, String label, String[] args) throws CommandException, TranslationNotFoundException,
	LanguageNotLoadedException, LanguageFileNotFoundException, IOException, PluginNotRegisteredException
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

	public boolean needAllPermissions()
	{
		return false;
	}

	public boolean checkPermissions(CommandSender sender)
	{
		if(sender instanceof ConsoleCommandSender)
		{
			return getArgumentTypes().contains(CommandType.CONSOLE);

		} else if(sender instanceof BlockCommandSender)
		{
			return getArgumentTypes().contains(CommandType.COMMAND_BLOCK);

		} else if(sender instanceof Player)
		{
			if(!getArgumentTypes().contains(CommandType.PLAYER))
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

	public void registerAllSubArguments() throws TranslationNotFoundException
	{}

	/**
	 * @param argument
	 *            Argument class to register
	 */
	public void registerSubArgument(IArgument subArgument)
	{
		getSubArgumentsAndAliases().put(subArgument.getArgumentName(), subArgument);
		getSubArguments().add(subArgument);

		for(String alias : subArgument.getArgumentAliases())
		{
			if(!getSubArgumentsAndAliases().containsKey(alias) || !getSubArgumentsAndAliases().get(alias).getArgumentName().equals(alias))
			{
				getSubArgumentsAndAliases().put(alias, subArgument);
			}
		}
	}

	public void registerSyntax(String syntaxBase)
	{
		setArgumentSyntaxBase(syntaxBase);

		if(isEqualToArg())
		{
			syntaxBase = syntaxBase + " " + getArgumentName();

		} else
		{
			syntaxBase = syntaxBase + " <" + getArgumentName() + ">";
		}

		List<IArgument> notOptionalArg = new ArrayList<>();
		List<IArgument> optionalArg = new ArrayList<>();
		List<ExtendedSubSyntax> notOptionalSyntax = new ArrayList<>();
		List<ExtendedSubSyntax> optionalSyntax = new ArrayList<>();

		for(IArgument arg : getSubArguments())
		{
			arg.registerSyntax(syntaxBase);

			if(arg.isOptional())
			{
				optionalArg.add(arg);

			} else
			{
				notOptionalArg.add(arg);
			}
		}

		for(ExtendedSubSyntax arg : getExtendedSyntaxes())
		{
			arg.registerExtendedSubSyntax(syntaxBase);

			if(arg.isOptional())
			{
				optionalSyntax.add(arg);

			} else
			{
				notOptionalSyntax.add(arg);
			}
		}

		StringBuilder syntaxSub = new StringBuilder();
		StringBuilder syntaxSubNotOptional = new StringBuilder();
		StringBuilder syntaxSubOptional = new StringBuilder();

		if(!notOptionalArg.isEmpty() || !notOptionalSyntax.isEmpty())
		{
			syntaxSubNotOptional.append("<");

			if(!notOptionalArg.isEmpty())
			{
				for(int i = 0; i < notOptionalArg.size(); i++)
				{
					syntaxSubNotOptional.append(notOptionalArg.get(i).getArgumentName());

					if(i < (notOptionalArg.size() - 1) || !notOptionalSyntax.isEmpty())
					{
						syntaxSubNotOptional.append("¦");
					}
				}
			}

			if(!notOptionalSyntax.isEmpty())
			{
				for(int i = 0; i < notOptionalSyntax.size(); i++)
				{
					syntaxSubNotOptional.append(notOptionalSyntax.get(i).getExtendedSubSyntaxName());

					if(i < (notOptionalSyntax.size() - 1))
					{
						syntaxSubNotOptional.append("¦");
					}
				}
			}

			syntaxSubNotOptional.append(">");
		}

		if(!optionalArg.isEmpty() || !optionalSyntax.isEmpty())
		{
			if(!optionalArg.isEmpty())
			{
				for(int i = 0; i < optionalArg.size(); i++)
				{
					syntaxSubOptional.append(optionalArg.get(i).getArgumentName());

					if(i < (optionalArg.size() - 1) || !optionalSyntax.isEmpty())
					{
						syntaxSubOptional.append("¦");
					}
				}
			}

			if(!optionalSyntax.isEmpty())
			{
				for(int i = 0; i < optionalSyntax.size(); i++)
				{
					syntaxSubOptional.append(optionalSyntax.get(i).getExtendedSubSyntaxName());

					if(i < (optionalSyntax.size() - 1))
					{
						syntaxSubOptional.append("¦");
					}
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

		setArgumentSyntaxSub(syntaxSub.toString());
	}

	@Override
	public String createHelp(CommandSender sender) throws TranslationNotFoundException
	{
		StringBuilder sb = new StringBuilder();

		if(!checkPermissions(sender))
		{
			return sb.toString();

		} else if(getSubArguments().isEmpty() && getExtendedSyntaxes().isEmpty())
		{
			sb.append("%break%");
			sb.append(TEMPLATE.getTranslatedString(TranslationPaths.SYNTAX_COLOR.getPath()));
			sb.append(getArgumentSyntax());

			if(getArgumentDescription(sender).length() > 0)
			{
				sb.append(TEMPLATE.getTranslatedString(TranslationPaths.SYNTAX_DESCRIPTION_COLOR.getPath()));
				sb.append(getArgumentDescription(sender));
			}
		} else
		{
			boolean allOptional = true;

			for(IArgument arg : getSubArguments())
			{
				if(!arg.isOptional())
				{
					allOptional = false;
					break;
				}
			}

			if(allOptional)
			{
				for(ExtendedSubSyntax arg : getExtendedSyntaxes())
				{
					if(!arg.isOptional)
					{
						allOptional = false;
						break;
					}
				}
			}

			if(allOptional)
			{
				sb.append("%break%");
				sb.append(TEMPLATE.getTranslatedString(TranslationPaths.SYNTAX_COLOR.getPath()));
				sb.append(getArgumentSyntax(false));

				if(getArgumentDescription(sender).length() > 0)
				{
					sb.append(TEMPLATE.getTranslatedString(TranslationPaths.SYNTAX_DESCRIPTION_COLOR.getPath()));
					sb.append(getArgumentDescription(sender));
				}
			}
		}

		if(!getSubArguments().isEmpty())
		{
			for(IArgument arg : getSubArguments())
			{
				sb.append(arg.createHelp(sender));
			}
		}

		if(!getExtendedSyntaxes().isEmpty())
		{
			for(ExtendedSubSyntax arg : getExtendedSyntaxes())
			{
				sb.append(arg.createHelp(sender));
			}
		}

		return sb.toString();
	}

	@Override
	public void registerHelp(CommandSender sender, String parentPath, int maxLength)
	{
		if(checkPermissions(sender))
		{
			try
			{
				StringBuilder help = new StringBuilder();
				int page = 1;
				String path = parentPath + "." + getArgumentName();

				CommandHandler.getHelpPages().get(sender).put(path, new ArrayList<HelpPage>());

				StringBuilder aliases = new StringBuilder();
				StringBuilder permissions = new StringBuilder();
				StringBuilder types = new StringBuilder();

				if(!getArgumentAliases().isEmpty())
				{
					for(String alias : getArgumentAliases())
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

				if(!getArgumentTypes().isEmpty())
				{
					for(CommandType type : getArgumentTypes())
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
				String isEqual = isEqualToArg() ? TEMPLATE.getTranslatedString(TranslationPaths.VALUE_TRUE.getPath())
						: TEMPLATE.getTranslatedString(TranslationPaths.VALUE_FALSE.getPath());
				String isOptional = isOptional() ? TEMPLATE.getTranslatedString(TranslationPaths.VALUE_TRUE.getPath())
						: TEMPLATE.getTranslatedString(TranslationPaths.VALUE_FALSE.getPath());
				String[] extraArgs =
				{ getArgumentDescription(sender), aliases.toString().replaceFirst(", ", ""), permissions.toString().replaceFirst(", ", ""),
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

				for(IArgument arg : getSubArguments())
				{
					arg.registerHelp(sender, path, maxLength);
				}

				for(ExtendedSubSyntax extended : getExtendedSyntaxes())
				{
					extended.registerHelp(sender, path, maxLength);
				}
			} catch(TranslationNotFoundException e)
			{
				Debug.addRunning(System.currentTimeMillis(), e);
			}
		}
	}

	public void addExtendedSyntax(ExtendedSubSyntax subArgumentSyntax, boolean isDefault)
	{
		getExtendedSyntaxes().add(subArgumentSyntax);

		if(isDefault)
		{
			setDefaultExtendedSyntax(subArgumentSyntax);
		}
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
	 * @throws LanguageNotLoadedException 
	 */
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args, int start)
			throws TranslationNotFoundException, PluginNotRegisteredException, LanguageNotLoadedException
	{
		if(args.length == start)
		{
			List<String> complete = new ArrayList<String>(getSubArgumentNamesAndAliasesByPermission(sender));

			for(ExtendedSubSyntax extSyntax : getExtendedSyntaxes())
			{
				if(extSyntax.isEqualToArg())
				{
					complete.add(extSyntax.getExtendedSubSyntaxName());

				} else
				{
					if(extSyntax.isOptional)
					{
						complete.add("[" + extSyntax.getExtendedSubSyntaxName() + "]");

					} else
					{
						complete.add("<" + extSyntax.getExtendedSubSyntaxName() + ">");
					}
				}
			}

			return getListOfStringsMatchingLastWord(args, complete);

		} else if(args.length > start)
		{
			if(getSubArgumentNamesAndAliasesByPermission(sender).contains(args[start - 1]))
			{
				return getSubArgumentsAndAliases().get(args[start - 1]).onTabComplete(sender, cmd, label, args, start + 1);

			} else if(getExtendedSubSyntaxNames().contains(args[start - 1]))
			{
				return getExtendedSubSyntaxByName(args[start - 1]).onTabComplete(sender, cmd, label, args, start + 1);
			}
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

	public static List<String> getListOfStringsMatchingLastWord(String[] inputArgs, Collection<?> possibleCompletions)
	{
		String s = inputArgs[inputArgs.length - 1];
		List<String> list = new ArrayList<>();

		if(!possibleCompletions.isEmpty())
		{
			for(String s1 : Iterables.transform(possibleCompletions, Functions.toStringFunction()))
			{
				if(doesStringStartWith(s, s1))
				{
					list.add(s1);
				}
			}
		}

		return list;
	}

	public static List<String> getSubKeyPaths(FileConfiguration config, String[] args, int start, String startSection, String... addMoreOptions)
	{
		if(start < args.length)
		{
			String cfgPath = args[start];

			if(!startSection.isEmpty())
			{
				cfgPath = startSection + "." + args[start];
			}

			int lastDot = cfgPath.lastIndexOf('.');

			if(lastDot > 0)
			{
				String beforeDot = cfgPath.substring(0, lastDot);

				if(config.isConfigurationSection(beforeDot))
				{
					List<String> cfgPaths = new ArrayList<>();

					for(String entry : config.getConfigurationSection(beforeDot).getKeys(false))
					{
						if(startSection.isEmpty())
						{
							cfgPaths.add((beforeDot + "." + entry));

						} else
						{
							cfgPaths.add((beforeDot + "." + entry).replaceFirst(startSection + "\\.", ""));
						}
					}

					cfgPaths.addAll(Arrays.asList(addMoreOptions));

					return getListOfStringsMatchingLastWord(args, cfgPaths);
				}
			}

			List<String> cfgPaths = new ArrayList<>();

			for(String key : config.getKeys(false))
			{
				if(startSection.isEmpty())
				{
					cfgPaths.add(key);

				} else
				{
					cfgPaths.add(key.replaceFirst(startSection + "\\.", ""));
				}
			}

			cfgPaths.addAll(Arrays.asList(addMoreOptions));

			return getListOfStringsMatchingLastWord(args, cfgPaths);
		}

		return Collections.emptyList();
	}

	public static List<String> getSubSectionPaths(FileConfiguration config, String[] args, int start, String startSection, boolean addLists,
			String... addMoreOptions)
	{
		if(start < args.length)
		{
			String cfgPath = args[start];

			if(!startSection.isEmpty())
			{
				cfgPath = startSection + "." + args[start];
			}

			int lastDot = cfgPath.lastIndexOf('.');

			if(lastDot > 0)
			{
				String beforeDot = cfgPath.substring(0, lastDot);

				if(config.isConfigurationSection(beforeDot))
				{
					List<String> cfgPaths = new ArrayList<>();

					for(String entry : config.getConfigurationSection(beforeDot).getKeys(false))
					{
						if(config.isConfigurationSection(beforeDot + "." + entry) || (addLists && config.isList(beforeDot + "." + entry)))
						{
							if(startSection.isEmpty())
							{
								cfgPaths.add(beforeDot + "." + entry);

							} else
							{
								cfgPaths.add((beforeDot + "." + entry).replaceFirst(startSection + "\\.", ""));
							}
						}
					}

					cfgPaths.addAll(Arrays.asList(addMoreOptions));

					return getListOfStringsMatchingLastWord(args, cfgPaths);
				}
			}

			List<String> cfgPaths = new ArrayList<>();

			for(String key : config.getKeys(false))
			{
				if(config.isConfigurationSection(key) || (addLists && config.isList(key)))
				{
					if(startSection.isEmpty())
					{
						cfgPaths.add(key);

					} else
					{
						cfgPaths.add(key.replaceFirst(startSection + "\\.", ""));
					}
				}
			}

			cfgPaths.addAll(Arrays.asList(addMoreOptions));

			return getListOfStringsMatchingLastWord(args, cfgPaths);
		}

		return Collections.emptyList();
	}
}

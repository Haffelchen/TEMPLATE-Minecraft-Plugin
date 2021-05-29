package net.haffel.PL.args.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.google.common.base.Functions;
import com.google.common.collect.Iterables;

import lombok.Getter;
import lombok.Setter;
import net.haffel.PL.TEMPLATE;
import net.haffel.PL.cmds.utils.CommandHandler;
import net.haffel.PL.cmds.utils.HelpPage;
import net.haffel.PL.files.TranslationPaths;
import net.haffel.PL.utils.Debug;
import net.haffel.ml.exceptions.PluginNotRegisteredException;
import net.haffel.ml.exceptions.TranslationNotFoundException;

/**
 * Template class
 * 
 * @since v1.1
 * @lastEdit v1.1
 */
public class ExtendedSubSyntax
{
	// List with all virtual sub arguments
	@Getter List<ExtendedSubSyntax> extendedSubSyntaxes = new ArrayList<>();
	// Name of this virtual sub argument
	@Getter String extendedSubSyntaxName;
	// Syntax of this virtual sub argument
	@Getter @Setter String extendedSubSyntaxBase;
	// Syntax of this virtual sub argument with all virtual sub arguments
	@Getter @Setter String extendedSubSyntaxSub;
	// Description of this virtual sub argument
	@Getter TranslationPaths descriptionPath;
	// Is this argument neccessary for correct command usage?
	@Getter boolean isOptional;
	// Has the user input to be equal to this argument or not?
	@Getter boolean equalToArg;

	/**
	 * @param name
	 *            Virtual argument key
	 * @param description
	 *            Description of this virtual argument
	 * @param isOptional
	 *            Is this argument neccessary for correct command usage?
	 * @param isEqualToArg
	 *            Has the user input to be equal to this argument or not?
	 */
	public ExtendedSubSyntax(String name, TranslationPaths descriptionPath, boolean isOptional, boolean isEqualToArg)
	{
		this.extendedSubSyntaxName = name;
		this.descriptionPath = descriptionPath;
		this.isOptional = isOptional;
		this.equalToArg = isEqualToArg;
	}

	public String getDescription(CommandSender sender)
			throws TranslationNotFoundException
	{
		return TEMPLATE.getTranslatedString(sender, getDescriptionPath().getPath());
	}

	/**
	 * @return Syntax of this virtual argument
	 */
	public String getExtendedSubSyntax()
	{
		return getExtendedSubSyntax(true);
	}

	/**
	 * @param withSub
	 *            Add sub syntax of this virtual argument?
	 * @return Syntax of this virtual argument
	 */
	public String getExtendedSubSyntax(boolean withSub)
	{
		String name = getExtendedSubSyntaxName();

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
			if(getExtendedSubSyntaxSub().length() > 0)
			{
				return getExtendedSubSyntaxBase() + " " + name + " " + getExtendedSubSyntaxSub();

			} else
			{
				return getExtendedSubSyntaxBase() + " " + name;
			}
		} else
		{
			return getExtendedSubSyntaxBase() + " " + name;
		}
	}

	public List<String> getExtendedSubSyntaxNames()
	{
		List<String> list = new ArrayList<>();

		for(ExtendedSubSyntax extSyntax : getExtendedSubSyntaxes())
		{
			list.add(extSyntax.getExtendedSubSyntaxName());
		}

		return list;
	}

	public ExtendedSubSyntax getExtendedSubSyntaxByName(String name)
	{
		for(ExtendedSubSyntax extSyntax : getExtendedSubSyntaxes())
		{
			if(extSyntax.getExtendedSubSyntaxName().equals(name))
			{
				return extSyntax;
			}
		}

		return null;
	}

	/**
	 * @param args
	 *            Arguments the sender entered
	 * @param start
	 *            Where to start search for correct syntax
	 * @param withSub
	 *            Add sub syntax of this virtual argument?
	 * @return Syntax of this virtual argument
	 */
	public String getExtendedSubSyntax(String[] args, int start, boolean withSub)
	{
		if(!getExtendedSubSyntaxes().isEmpty() && start < args.length)
		{
			int nextStart = start + 1;

			for(ExtendedSubSyntax syntax : getExtendedSubSyntaxes())
			{
				if(syntax.getExtendedSubSyntaxName().equalsIgnoreCase(args[start]))
				{
					return syntax.getExtendedSubSyntax(args, nextStart, withSub);
				}
			}
		}

		return getExtendedSubSyntax(withSub);
	}

	/**
	 * @param moreArgs
	 *            Number of virtual sub arguments to loop through to get the syntax
	 * @return Syntax of sub argument
	 */
	public String getExtendedSubSyntax(int moreArgs)
	{
		StringBuilder sb = new StringBuilder();

		if(moreArgs > 0)
		{
			int nextMoreArgs = moreArgs - 1;

			if(!getExtendedSubSyntaxSub().isEmpty())
			{
				sb.append(" ");
				sb.append(getExtendedSubSyntaxSub());
			}

			if(!getExtendedSubSyntaxes().isEmpty())
			{
				for(ExtendedSubSyntax subArgSyntax1 : getExtendedSubSyntaxes())
				{
					sb.append(subArgSyntax1.getExtendedSubSyntax(nextMoreArgs));
				}
			}
		}

		return sb.toString();
	}

	/**
	 * @param subArgumentSyntax
	 *            The virtual argument to add to this
	 */
	public void addExtendedSubSyntax(ExtendedSubSyntax subArgumentSyntax)
	{
		getExtendedSubSyntaxes().add(subArgumentSyntax);
	}

	/**
	 * @param syntaxBase
	 *            Syntax of the parent argument
	 */
	public void registerExtendedSubSyntax(String syntaxBase)
	{
		setExtendedSubSyntaxBase(syntaxBase);

		if(isEqualToArg())
		{
			syntaxBase = syntaxBase + " " + getExtendedSubSyntaxName();

		} else
		{
			syntaxBase = syntaxBase + " <" + getExtendedSubSyntaxName() + ">";
		}

		List<ExtendedSubSyntax> notOptional = new ArrayList<>();
		List<ExtendedSubSyntax> optional = new ArrayList<>();

		for(ExtendedSubSyntax arg : getExtendedSubSyntaxes())
		{
			arg.registerExtendedSubSyntax(syntaxBase);

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
				syntaxSubNotOptional.append(notOptional.get(i).getExtendedSubSyntaxName());

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
				syntaxSubOptional.append(optional.get(i).getExtendedSubSyntaxName());

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

		setExtendedSubSyntaxSub(syntaxSub.toString());
	}

	/**
	 * @return Help for this and all virtual sub arguments
	 * @throws TranslationNotFoundException
	 *             For the given translation key was no translation found
	 * @throws UserFormatConfigDoesntExistException
	 *             The user formats config for the sender doesnt exist
	 * @throws SomeTranslationException
	 *             Any other error
	 */
	public String createHelp(CommandSender sender) throws TranslationNotFoundException
	{
		StringBuilder sb = new StringBuilder();

		if(getExtendedSubSyntaxes().isEmpty())
		{
			sb.append("%break%");
			sb.append(TEMPLATE.getTranslatedString(TranslationPaths.SYNTAX_COLOR.getPath()));
			sb.append(getExtendedSubSyntax());

			if(getDescription(sender).length() > 0)
			{
				sb.append(TEMPLATE.getTranslatedString(TranslationPaths.SYNTAX_DESCRIPTION_COLOR.getPath()));
				sb.append(getDescription(sender));
			}
		} else
		{
			boolean allOptional = true;

			for(ExtendedSubSyntax arg : getExtendedSubSyntaxes())
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
				sb.append(getExtendedSubSyntax(false));

				if(getDescription(sender).length() > 0)
				{
					sb.append(TEMPLATE.getTranslatedString(TranslationPaths.SYNTAX_DESCRIPTION_COLOR.getPath()));
					sb.append(getDescription(sender));
				}
			}

			for(ExtendedSubSyntax arg : getExtendedSubSyntaxes())
			{
				sb.append(arg.createHelp(sender));
			}
		}

		return sb.toString();
	}

	public void registerHelp(CommandSender sender, String parentPath, int maxLength)
	{
		try
		{
			StringBuilder help = new StringBuilder();
			int page = 1;
			String path = parentPath + "." + getExtendedSubSyntaxName();

			CommandHandler.getHelpPages().get(sender).put(path, new ArrayList<HelpPage>());

			String aliases = TEMPLATE.getTranslatedString(TranslationPaths.ARG_HELP_EMPTY.getPath());
			String permissions = TEMPLATE.getTranslatedString(TranslationPaths.ARG_HELP_EMPTY.getPath());
			String types = TEMPLATE.getTranslatedString(TranslationPaths.ARG_HELP_EMPTY.getPath());
			String needAllPermissions = TEMPLATE.getTranslatedString(TranslationPaths.VALUE_FALSE.getPath());
			String isEqual = isEqualToArg() ? TEMPLATE.getTranslatedString(TranslationPaths.VALUE_TRUE.getPath())
					: TEMPLATE.getTranslatedString(TranslationPaths.VALUE_FALSE.getPath());
			String isOptional = isOptional() ? TEMPLATE.getTranslatedString(TranslationPaths.VALUE_TRUE.getPath())
					: TEMPLATE.getTranslatedString(TranslationPaths.VALUE_FALSE.getPath());
			String[] extraArgs =
			{ getDescription(sender), aliases, permissions, needAllPermissions, types, isEqual, isOptional };
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

			for(ExtendedSubSyntax extended : getExtendedSubSyntaxes())
			{
				extended.registerHelp(sender, path, maxLength);
			}
		} catch(TranslationNotFoundException e)
		{
			Debug.addRunning(System.currentTimeMillis(), e);
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
	 */
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args, int start)
			throws TranslationNotFoundException, PluginNotRegisteredException
	{
		if(args.length == start)
		{
			List<String> complete = new ArrayList<>();

			for(ExtendedSubSyntax extSyntax : getExtendedSubSyntaxes())
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
			if(getExtendedSubSyntaxNames().contains(args[start - 1]))
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
}

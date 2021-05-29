package net.haffel.PL.args;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.haffel.PL.TEMPLATE;
import net.haffel.PL.args.utils.ArgumentBase;
import net.haffel.PL.args.utils.ExtendedSubSyntax;
import net.haffel.PL.cmds.utils.CommandType;
import net.haffel.PL.exceptions.ChangeNotFoundException;
import net.haffel.PL.exceptions.PermissionNotFoundException;
import net.haffel.PL.files.TranslationPaths;
import net.haffel.PL.news.Change;
import net.haffel.PL.news.ChangelogManager;
import net.haffel.PL.perms.utils.PermissionHandler;
import net.haffel.PL.perms.utils.PermissionNames;
import net.haffel.ml.exceptions.PluginNotRegisteredException;
import net.haffel.ml.exceptions.TranslationNotFoundException;
import net.haffel.ml.translate.TranslateHandler;
import net.haffel.ml.translate.UserFormat;

/**
 * Template class
 * 
 * @since v1.1
 * @lastEdit v1.1
 */
public class NewsArg extends ArgumentBase
{
	/**
	 * @return Name of this argument
	 */
	@Override
	public String getArgumentName()
	{
		return "news";
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
	public String getArgumentDescription(CommandSender sender)
			throws TranslationNotFoundException
	{
		return TEMPLATE.getTranslatedString(sender, TranslationPaths.DESCRIPTION_ARG_NEWS.getPath());
	}

	/**
	 * @return List of aliases for this argument
	 */
	@Override
	public List<String> getArgumentAliases()
	{
		return Arrays.asList("updates", "changes", "changelog");
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
	public boolean execute(CommandSender sender, Command cmd, String label, String[] args) throws CommandException, PluginNotRegisteredException,
			TranslationNotFoundException
	{
		String language = TranslateHandler.getDefaultPluginLanguage(TEMPLATE.getPluginName());

		if(UserFormat.isEitherSettingSet((Player) sender, TEMPLATE.getPluginName(), "Language"))
		{
			language = UserFormat.getEitherUserSetting((Player) sender, TEMPLATE.getPluginName(), "Language");
		}

		if(args.length > 1)
		{
			if(args[1].equalsIgnoreCase("list"))
			{
				StringBuilder sb = new StringBuilder();

				sb.append(getTranslatedString(sender, TranslationPaths.ARG_NEWS_LIST.getPath(), args));
				sb.append("%break%");

				for(int i = 0; i < ChangelogManager.getChanges().size(); i++)
				{
					sb.append(getTranslatedString(sender, TranslationPaths.ARG_NEWS_LIST_VERSION.getPath(), args));
					sb.append(ChangelogManager.getChanges().get(i).getVersion());
					sb.append(getTranslatedString(sender, TranslationPaths.ARG_NEWS_LIST_PUBLISHED.getPath(), args));
					sb.append(ChangelogManager.getChanges().get(i).getDate());
					sb.append("%break%");
				}

				sender.sendMessage(sb.toString().split("%break%"));

				return true;

			} else
			{
				String searchChange = "";

				for(int i = 1; i < args.length; i++)
				{
					searchChange += " " + args[i];
				}

				try
				{
					Change change = ChangelogManager.getChangeByVersion(language, searchChange.replaceFirst(" ", ""));
					String[] extraArgs =
					{ change.getLanguage(), change.getVersion(), change.getDate(), change.getChanges() };

					sender.sendMessage(getTranslatedStringSplitted(sender, TranslationPaths.ARG_NEWS_GET.getPath(), args, extraArgs));

					return true;

				} catch(ChangeNotFoundException e)
				{
					try
					{
						Change change = ChangelogManager.getChangeByDate(language, searchChange.replaceFirst(" ", ""));
						String[] extraArgs =
						{ change.getLanguage(), change.getVersion(), change.getDate(), change.getChanges() };

						sender.sendMessage(getTranslatedStringSplitted(sender, TranslationPaths.ARG_NEWS_GET.getPath(), args, extraArgs));

						return true;

					} catch(ChangeNotFoundException e1)
					{
						sender.sendMessage(getTranslatedStringSplitted(sender, TranslationPaths.ARG_NEWS_NO_CHANGES_FOUND.getPath(), args));

						return false;
					}
				}
			}
		} else
		{
			try
			{
				Change change = ChangelogManager.getChangeByVersion(language, TEMPLATE.getPluginVersion());
				String[] extraArgs =
				{ change.getLanguage(), change.getVersion(), change.getDate(), change.getChanges() };

				sender.sendMessage(getTranslatedStringSplitted(sender, TranslationPaths.ARG_NEWS_GET.getPath(), args, extraArgs));

				return true;

			} catch(ChangeNotFoundException e)
			{
				sender.sendMessage(getTranslatedStringSplitted(sender, TranslationPaths.ARG_NEWS_NEWEST_CHANGE_NOT_FOUND.getPath(), args));

				return false;
			}
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
		loadPermission(PermissionNames.ARG_NEWS);
	}

	/**
	 * Register all sub arguments for this argument
	 */
	@Override
	public void registerAllSubArguments()
	{
		ExtendedSubSyntax list = new ExtendedSubSyntax("list", TranslationPaths.DESCRIPTION_ARG_NEWS_LIST, true, true);
		ExtendedSubSyntax specific = new ExtendedSubSyntax("Date or Version", TranslationPaths.DESCRIPTION_ARG_NEWS_SPECIFIC, true, false);

		addExtendedSyntax(list, false);
		addExtendedSyntax(specific, false);
	}

	/**
	 * @return List of tab completion options
	 */
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args, int start)
	{
		if(args.length == 2)
		{
			ArrayList<String> complete = new ArrayList<>(ChangelogManager.getChangeNames());
			complete.add("list");

			return ArgumentBase.getListOfStringsMatchingLastWord(args, complete);
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

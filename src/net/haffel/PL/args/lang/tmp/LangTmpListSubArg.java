package net.haffel.PL.args.lang.tmp;

import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;

import net.haffel.PL.TEMPLATE;
import net.haffel.PL.args.LanguageArg;
import net.haffel.PL.args.utils.ArgumentBase;
import net.haffel.PL.exceptions.PermissionNotFoundException;
import net.haffel.PL.files.TranslationPaths;
import net.haffel.PL.perms.utils.PermissionHandler;
import net.haffel.PL.perms.utils.PermissionNames;
import net.haffel.ml.exceptions.TranslationNotFoundException;

/**
 * Template class
 * 
 * @since v1.1
 * @lastEdit v1.1
 */
public class LangTmpListSubArg extends ArgumentBase
{
	/**
	 * @return Name of the sub argument
	 */
	@Override
	public String getArgumentName()
	{
		return "list";
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
	public String getArgumentDescription(CommandSender sender) throws TranslationNotFoundException
	{
		return TEMPLATE.getTranslatedString(sender, TranslationPaths.DESCRIPTION_ARG_LANG_TMP_LIST.getPath());
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean execute(CommandSender sender, Command cmd, String label, String[] args)
			throws CommandException, TranslationNotFoundException
	{
		StringBuilder changed = new StringBuilder();

		if(LanguageArg.getLanguageChanges().containsKey(sender))
		{
			for(String edited : LanguageArg.getLanguageChanges().get(sender).keySet())
			{
				if(LanguageArg.getLanguageChanges().get(sender).get(edited) instanceof ArrayList<?>)
				{
					StringBuilder list = new StringBuilder();
					int index = 0;

					for(String listValue : ((ArrayList<String>) LanguageArg.getLanguageChanges().get(sender).get(edited)))
					{
						String[] extraArgs =
						{ Integer.toString(index), listValue };

						list.append(getTranslatedString(sender, TranslationPaths.ARG_LANG_TMP_LIST_ENTRY_LIST.getPath(), args, extraArgs));
						index++;
					}

					String[] extraArgs =
					{ edited, list.toString().replaceFirst(", ", "") };

					changed.append(getTranslatedString(sender, TranslationPaths.ARG_LANG_TMP_LIST_ENTRY.getPath(), args, extraArgs));

				} else
				{
					String[] extraArgs =
					{ edited, (String) LanguageArg.getLanguageChanges().get(sender).get(edited) };

					changed.append(getTranslatedString(sender, TranslationPaths.ARG_LANG_TMP_LIST_ENTRY.getPath(), args, extraArgs));
				}
			}
		}

		String emptyList = getTranslatedString(sender, TranslationPaths.ARG_LANG_TMP_LIST_EMPTY.getPath(), args);
		String[] extraArgs =
		{ changed.toString().isEmpty() ? emptyList : changed.toString().replaceFirst(", ", "") };

		sender.sendMessage(getTranslatedStringSplitted(sender, TranslationPaths.ARG_LANG_TMP_CHANGED_VALUES.getPath(), args, extraArgs));
		return true;
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
		loadPermission(PermissionNames.ARG_LANG_TMP_LIST);
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
	private String getTranslatedString(CommandSender commandSender, String translate, String[] args, String[] extraArgs)
			throws TranslationNotFoundException
	{
		return TEMPLATE.getTranslatedString(commandSender, translate, args, extraArgs);
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
	private String getTranslatedString(CommandSender commandSender, String translate, String[] args)
			throws TranslationNotFoundException
	{
		return TEMPLATE.getTranslatedString(commandSender, translate, args);
	}
}

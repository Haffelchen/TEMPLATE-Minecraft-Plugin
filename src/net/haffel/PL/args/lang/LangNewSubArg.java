package net.haffel.PL.args.lang;

import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;

import net.haffel.PL.TEMPLATE;
import net.haffel.PL.args.utils.ArgumentBase;
import net.haffel.PL.args.utils.ExtendedSubSyntax;
import net.haffel.PL.exceptions.PermissionNotFoundException;
import net.haffel.PL.files.TranslationPaths;
import net.haffel.PL.perms.utils.PermissionHandler;
import net.haffel.PL.perms.utils.PermissionNames;
import net.haffel.ml.exceptions.LanguageFileNotFoundException;
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
public class LangNewSubArg extends ArgumentBase
{
	/**
	 * @return Name of the sub argument
	 */
	@Override
	public String getArgumentName()
	{
		return "new";
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
		return TEMPLATE.getTranslatedString(sender, TranslationPaths.DESCRIPTION_ARG_LANG_NEW.getPath());
	}

	@Override
	public boolean execute(CommandSender sender, Command cmd, String label, String[] args) throws CommandException, LanguageFileNotFoundException,
			TranslationNotFoundException
	{
		if(args.length > 2)
		{
			if(!LanguageFile.isLanguageLoaded(TEMPLATE.getPluginName(), args[2]))
			{
				TranslateHandler.registerAndLoadLanguage(TEMPLATE.getPluginName(), args[2]);

				sender.sendMessage(getTranslatedStringSplitted(sender, TranslationPaths.ARG_LANG_NEW_LANGUAGE_CREATED.getPath(), args));

				return true;

			} else
			{
				sender.sendMessage(getTranslatedStringSplitted(sender, TranslationPaths.ARG_LANG_NEW_ALREADY_EXISTS.getPath(), args));

				return false;
			}
		} else
		{
			sender.sendMessage(getTranslatedStringSplitted(sender, TranslationPaths.ARG_LANG_NEW_ENTER_NAME.getPath(), args));

			return false;
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
		loadPermission(PermissionNames.ARG_LANG_NEW);
	}

	/**
	 * Register all sub arguments for this argument
	 */
	@Override
	public void registerAllSubArguments()
	{
		ExtendedSubSyntax language = new ExtendedSubSyntax("Language", TranslationPaths.DESCRIPTION_ARG_LANG_NEW_LANGUAGE, false, false);

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
	 */
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args, int start)
			throws TranslationNotFoundException, PluginNotRegisteredException
	{
		if(args.length == 3)
		{
			return getListOfStringsMatchingLastWord(args, getTranslatedString(sender, TranslationPaths.ARG_LANG_TAB_NEW_LANGUAGE.getPath(), args));
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

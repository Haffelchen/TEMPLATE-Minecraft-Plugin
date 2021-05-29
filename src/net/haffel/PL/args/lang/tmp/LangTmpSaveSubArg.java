package net.haffel.PL.args.lang.tmp;

import java.io.IOException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;

import net.haffel.PL.NoneTranslatedMessages;
import net.haffel.PL.TEMPLATE;
import net.haffel.PL.args.LanguageArg;
import net.haffel.PL.args.utils.ArgumentBase;
import net.haffel.PL.exceptions.PermissionNotFoundException;
import net.haffel.PL.files.TranslationPaths;
import net.haffel.PL.perms.utils.PermissionHandler;
import net.haffel.PL.perms.utils.PermissionNames;
import net.haffel.PL.utils.Debug;
import net.haffel.ml.exceptions.LanguageNotLoadedException;
import net.haffel.ml.exceptions.TranslationNotFoundException;
import net.haffel.ml.files.LanguageFile;

/**
 * Template class
 * 
 * @since v1.1
 * @lastEdit v1.1
 */
public class LangTmpSaveSubArg extends ArgumentBase
{
	/**
	 * @return Name of the sub argument
	 */
	@Override
	public String getArgumentName()
	{
		return "save";
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
		return TEMPLATE.getTranslatedString(sender, TranslationPaths.DESCRIPTION_ARG_LANG_TMP_SAVE.getPath());
	}

	@Override
	public boolean execute(CommandSender sender, Command cmd, String label, String[] args)
			throws CommandException, TranslationNotFoundException
	{
		if(LanguageArg.getLanguageChanges().containsKey(sender))
		{
			LanguageArg.getLanguageChanges().get(sender).forEach((key, value) ->
			{
				String[] splitKey = key.split("\\.", 2);

				try
				{
					if(LanguageFile.isLanguageLoaded(TEMPLATE.getPluginName(), splitKey[0]))
					{
						if(value.equals("$null"))
						{
							LanguageFile.getLanguageConfig(TEMPLATE.getPluginName(), splitKey[0]).set(splitKey[1], null);

						} else if(value.equals("true"))
						{
							LanguageFile.getLanguageConfig(TEMPLATE.getPluginName(), splitKey[0]).set(splitKey[1], true);

						} else if(value.equals("false"))
						{
							LanguageFile.getLanguageConfig(TEMPLATE.getPluginName(), splitKey[0]).set(splitKey[1], false);

						} else
						{
							try
							{
								LanguageFile.getLanguageConfig(TEMPLATE.getPluginName(), splitKey[0]).set(splitKey[1], Integer.parseInt(value + ""));

							} catch(NumberFormatException e)
							{
								LanguageFile.getLanguageConfig(TEMPLATE.getPluginName(), splitKey[0]).set(splitKey[1], value);
							}
						}

						LanguageFile.saveLanguageConfig(TEMPLATE.getPluginName(), splitKey[0]);
					}
				} catch(IOException | LanguageNotLoadedException e)
				{
					int index = Debug.addRunning(System.currentTimeMillis(), e);
					
					sender.sendMessage(NoneTranslatedMessages.REPORT_TO_ADMIN_WITH_INDEX.getMessage().replace("{INDEX}", Integer.toString(index)));
				}
			});

			String[] extraArgs =
			{ Integer.toString(LanguageArg.getLanguageChanges().get(sender).size()) };

			sender.sendMessage(getTranslatedStringSplitted(sender, TranslationPaths.ARG_LANG_TMP_SAVED_CHANGES.getPath(), args, extraArgs));

			LanguageArg.getLanguageChanges().remove(sender);
			return true;

		} else
		{
			sender.sendMessage(getTranslatedStringSplitted(sender, TranslationPaths.ARG_LANG_TMP_NOTHING_TO_SAVE.getPath(), args));
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
		loadPermission(PermissionNames.ARG_LANG_TMP_SAVE);
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
}

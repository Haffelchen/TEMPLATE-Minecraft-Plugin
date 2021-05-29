package net.haffel.PL.args.cfg.tmp;

import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;

import net.haffel.PL.TEMPLATE;
import net.haffel.PL.args.ConfigArg;
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
public class CfgTmpClearSubArg extends ArgumentBase
{
	/**
	 * @return Name of the sub argument
	 */
	@Override
	public String getArgumentName()
	{
		return "clear";
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
		return TEMPLATE.getTranslatedString(sender, TranslationPaths.DESCRIPTION_ARG_CFG_TMP_CLEAR.getPath());
	}

	@Override
	public boolean execute(CommandSender sender, Command cmd, String label, String[] args)
			throws CommandException, TranslationNotFoundException
	{
		if(ConfigArg.getConfigChanges().containsKey(sender))
		{
			ConfigArg.getConfigChanges().remove(sender);

			sender.sendMessage(getTranslatedStringSplitted(sender, TranslationPaths.ARG_CFG_TMP_CLEARED.getPath(), args));
			return true;

		} else
		{
			sender.sendMessage(getTranslatedStringSplitted(sender, TranslationPaths.ARG_CFG_TMP_NOTHING_TO_CLEAR.getPath(), args));
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
		loadPermission(PermissionNames.ARG_CFG_TMP_CLEAR);
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

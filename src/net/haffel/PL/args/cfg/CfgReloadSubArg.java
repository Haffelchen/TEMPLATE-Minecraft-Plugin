package net.haffel.PL.args.cfg;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;

import net.haffel.PL.TEMPLATE;
import net.haffel.PL.args.utils.ArgumentBase;
import net.haffel.PL.exceptions.PermissionNotFoundException;
import net.haffel.PL.files.Config;
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
public class CfgReloadSubArg extends ArgumentBase
{
	/**
	 * @return Name of the sub argument
	 */
	@Override
	public String getArgumentName()
	{
		return "reload";
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
		return TEMPLATE.getTranslatedString(sender, TranslationPaths.DESCRIPTION_ARG_CFG_RELOAD.getPath());
	}

	/**
	 * @return List of aliases for the sub argument
	 */
	@Override
	public List<String> getArgumentAliases()
	{
		return Arrays.asList("rl");
	}

	@Override
	public boolean execute(CommandSender sender, Command cmd, String label, String[] args)
			throws CommandException, TranslationNotFoundException
	{
		sender.sendMessage(getTranslatedStringSplitted(sender, TranslationPaths.ARG_CFG_RELOADING.getPath(), args));

		Config.reloadConfig();

		sender.sendMessage(getTranslatedStringSplitted(sender, TranslationPaths.ARG_CFG_RELOADED.getPath(), args));

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
		loadPermission(PermissionNames.ARG_CFG_RL);
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

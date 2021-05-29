package net.haffel.PL.args.debug.startup;

import java.io.IOException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;

import net.haffel.PL.TEMPLATE;
import net.haffel.PL.args.utils.ArgumentBase;
import net.haffel.PL.exceptions.PermissionNotFoundException;
import net.haffel.PL.files.TranslationPaths;
import net.haffel.PL.perms.utils.PermissionHandler;
import net.haffel.PL.perms.utils.PermissionNames;
import net.haffel.PL.utils.Debug;
import net.haffel.ml.exceptions.LanguageFileNotFoundException;
import net.haffel.ml.exceptions.PluginNotRegisteredException;
import net.haffel.ml.exceptions.TranslationNotFoundException;

/**
 * Template class
 * 
 * @since v1.1
 * @lastEdit v1.1
 */
public class StartupListSubArg extends ArgumentBase
{
	/**
	 * @return Name of this argument
	 */
	@Override
	public String getArgumentName()
	{
		return "list";
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
		return TEMPLATE.getTranslatedString(sender, TranslationPaths.DESCRIPTION_ARG_DEBUG_STARTUP_LIST.getPath());
	}
	
	@Override
	public boolean execute(CommandSender sender, Command cmd, String label, String[] args) throws CommandException, TranslationNotFoundException,
			LanguageFileNotFoundException, IOException, PluginNotRegisteredException
	{
		String emptyList = TEMPLATE.getTranslatedString(TranslationPaths.ARG_DEBUG_STARTUP_LIST_EMPTY.getPath());
		String list = Debug.getListOfStartups();
		String[] extraArgs = { list.isEmpty() ? emptyList : list };
		
		sender.sendMessage(TEMPLATE.getTranslatedStringSplitted(sender, TranslationPaths.ARG_DEBUG_STARTUP_LIST.getPath(), args, extraArgs));

		return true;
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
		loadPermission(PermissionNames.ARG_DEBUG_STARTUP_LIST);
	}
}

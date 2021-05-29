package net.haffel.PL.args.debug.running;

import java.io.IOException;

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
public class RunningGetSubArg extends ArgumentBase
{
	/**
	 * @return Name of this argument
	 */
	@Override
	public String getArgumentName()
	{
		return "get";
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
		return TEMPLATE.getTranslatedString(sender, TranslationPaths.DESCRIPTION_ARG_DEBUG_RUNNING_GET.getPath());
	}

	@Override
	public boolean execute(CommandSender sender, Command cmd, String label, String[] args) throws CommandException, TranslationNotFoundException,
			LanguageFileNotFoundException, IOException, PluginNotRegisteredException
	{
		if(args.length > 3)
		{
			try
			{
				String exception = Debug.getRunningByIndex(Integer.parseInt(args[3]));

				if(!exception.isEmpty())
				{
					String[] extraArgs =
					{ exception };

					sender.sendMessage(
							TEMPLATE.getTranslatedStringSplitted(sender, TranslationPaths.ARG_DEBUG_RUNNING_GET.getPath(), args, extraArgs));

					return true;
				} else
				{
					sender.sendMessage(TEMPLATE.getTranslatedStringSplitted(sender, TranslationPaths.ARG_DEBUG_RUNNING_GET_INVALID.getPath(), args));

					return false;
				}
			} catch(NumberFormatException e)
			{
				String[] extraArgs =
				{ args[3] };

				sender.sendMessage(TEMPLATE.getTranslatedStringSplitted(sender, TranslationPaths.VALUE_NO_INTEGER.getPath(), args, extraArgs));

				return false;
			}
		}

		String[] extraArgs =
		{ getArgumentSyntax() };

		sender.sendMessage(TEMPLATE.getTranslatedStringSplitted(sender, TranslationPaths.SYNTAX_MESSAGE.getPath(), args, extraArgs));

		return false;
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
		loadPermission(PermissionNames.ARG_DEBUG_RUNNING_GET);
	}

	/**
	 * Register all sub arguments for this argument
	 */
	@Override
	public void registerAllSubArguments()
	{
		ExtendedSubSyntax index = new ExtendedSubSyntax("Index", TranslationPaths.DESCRIPTION_ARG_DEBUG_RUNNING_GET_INDEX, false, false);

		addExtendedSyntax(index, true);
	}

}

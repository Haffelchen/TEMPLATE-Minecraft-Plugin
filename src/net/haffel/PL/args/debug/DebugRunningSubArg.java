package net.haffel.PL.args.debug;

import java.io.IOException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;

import net.haffel.PL.TEMPLATE;
import net.haffel.PL.args.debug.running.RunningFalseSubArg;
import net.haffel.PL.args.debug.running.RunningGetSubArg;
import net.haffel.PL.args.debug.running.RunningListSubArg;
import net.haffel.PL.args.debug.running.RunningPrintSubArg;
import net.haffel.PL.args.debug.running.RunningTrueSubArg;
import net.haffel.PL.args.utils.ArgumentBase;
import net.haffel.PL.exceptions.PermissionNotFoundException;
import net.haffel.PL.files.Config;
import net.haffel.PL.files.ConfigSettings;
import net.haffel.PL.files.TranslationPaths;
import net.haffel.PL.perms.utils.PermissionHandler;
import net.haffel.PL.perms.utils.PermissionNames;
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
public class DebugRunningSubArg extends ArgumentBase
{
	/**
	 * @return Name of this argument
	 */
	@Override
	public String getArgumentName()
	{
		return "running";
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
		return TEMPLATE.getTranslatedString(sender, TranslationPaths.DESCRIPTION_ARG_DEBUG_RUNNING.getPath());
	}

	@Override
	public boolean execute(CommandSender sender, Command cmd, String label, String[] args) throws TranslationNotFoundException,
			LanguageFileNotFoundException, IOException, PluginNotRegisteredException, CommandException, LanguageNotLoadedException
	{
		if(args.length > 2 && !getSubArgumentsAndAliases().isEmpty())
		{
			for(String arg : getSubArgumentsAndAliases().keySet())
			{
				if(arg.equalsIgnoreCase(args[2]))
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

	/**
	 * Adds all needed permissions for this argument to a list. This does NOT load the permissions
	 * 
	 * @throws PermissionNotFoundException
	 *             Permission is not loaded
	 * @see PermissionHandler#setupPermissions()
	 */
	public void loadPermissions() throws PermissionNotFoundException
	{
		loadPermission(PermissionNames.ARG_DEBUG_RUNNING);
	}

	/**
	 * Register all sub arguments for this argument
	 */
	@Override
	public void registerAllSubArguments()
	{
		if((boolean) Config.getConfigSettings().get(ConfigSettings.DEBUG_RUNNING))
		{
			registerSubArgument(new RunningFalseSubArg());
			registerSubArgument(new RunningGetSubArg());
			registerSubArgument(new RunningListSubArg());
			registerSubArgument(new RunningPrintSubArg());
		}

		registerSubArgument(new RunningTrueSubArg());
	}
}

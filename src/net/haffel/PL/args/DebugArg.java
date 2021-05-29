package net.haffel.PL.args;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.CommandSender;

import net.haffel.PL.TEMPLATE;
import net.haffel.PL.args.debug.DebugNotifySubArg;
import net.haffel.PL.args.debug.DebugRunningSubArg;
import net.haffel.PL.args.debug.DebugStartupSubArg;
import net.haffel.PL.args.debug.DebugTimingsSubArg;
import net.haffel.PL.args.utils.ArgumentBase;
import net.haffel.PL.cmds.utils.CommandType;
import net.haffel.PL.exceptions.PermissionNotFoundException;
import net.haffel.PL.files.Config;
import net.haffel.PL.files.ConfigSettings;
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
public class DebugArg extends ArgumentBase
{
	/**
	 * @return Name of this argument
	 */
	@Override
	public String getArgumentName()
	{
		return "debug";
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
		return TEMPLATE.getTranslatedString(sender, TranslationPaths.DESCRIPTION_ARG_DEBUG.getPath());
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

	/**
	 * Adds all needed permissions for this argument to a list. This does NOT load the permissions
	 * 
	 * @throws PermissionNotFoundException
	 *             Permission is not loaded
	 * @see PermissionHandler#setupPermissions()
	 */
	public void loadPermissions() throws PermissionNotFoundException
	{
		loadPermission(PermissionNames.ARG_DEBUG);
	}

	/**
	 * Register all sub arguments for this argument
	 */
	@Override
	public void registerAllSubArguments()
	{
		if((boolean) Config.getConfigSettings().get(ConfigSettings.DEBUG_RUNNING) || (boolean) Config.getConfigSettings().get(ConfigSettings.DEBUG_STARTUP) || (boolean) Config.getConfigSettings().get(ConfigSettings.DEBUG_TIMINGS))
		{
			registerSubArgument(new DebugNotifySubArg());
		}
		
		registerSubArgument(new DebugRunningSubArg());
		registerSubArgument(new DebugStartupSubArg());
		registerSubArgument(new DebugTimingsSubArg());
	}
}

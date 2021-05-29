package net.haffel.PL.args;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.command.CommandSender;

import lombok.Getter;
import net.haffel.PL.TEMPLATE;
import net.haffel.PL.args.cfg.CfgEditSubArg;
import net.haffel.PL.args.cfg.CfgGetSubArg;
import net.haffel.PL.args.cfg.CfgReloadSubArg;
import net.haffel.PL.args.cfg.CfgResetSubArg;
import net.haffel.PL.args.cfg.CfgTemporarySubArg;
import net.haffel.PL.args.cfg.CfgTreeSubArg;
import net.haffel.PL.args.utils.ArgumentBase;
import net.haffel.PL.cmds.utils.CommandType;
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
public class ConfigArg extends ArgumentBase
{
	// List with all unsaved edited config keys and values
	@Getter static Map<CommandSender, Map<String, Object>> configChanges = new HashMap<>();

	/**
	 * @return Name of this argument
	 */
	@Override
	public String getArgumentName()
	{
		return "config";
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
		return TEMPLATE.getTranslatedString(sender, TranslationPaths.DESCRIPTION_ARG_CFG.getPath());
	}

	/**
	 * @return List of aliases for this argument
	 */
	@Override
	public List<String> getArgumentAliases()
	{
		return Arrays.asList("conf", "cfg");
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
		loadPermission(PermissionNames.ARG_CFG);
	}

	/**
	 * Register all sub arguments for this argument
	 */
	@Override
	public void registerAllSubArguments()
	{
		registerSubArgument(new CfgEditSubArg());
		registerSubArgument(new CfgGetSubArg());
		registerSubArgument(new CfgReloadSubArg());
		registerSubArgument(new CfgResetSubArg());
		registerSubArgument(new CfgTemporarySubArg());
		registerSubArgument(new CfgTreeSubArg());
	}
}

package net.haffel.PL.args;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.command.CommandSender;

import lombok.Getter;
import net.haffel.PL.TEMPLATE;
import net.haffel.PL.args.uf.UfEditSubArg;
import net.haffel.PL.args.uf.UfGetSubArg;
import net.haffel.PL.args.uf.UfResetSubArg;
import net.haffel.PL.args.uf.UfTemporarySubArg;
import net.haffel.PL.args.uf.UfTreeSubArg;
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
public class UserFormatsArg extends ArgumentBase
{
	// List with all unsaved edited config keys and values
	@Getter static Map<CommandSender, Map<String, Object>> UFChanges = new HashMap<>();

	/**
	 * @return Name of this argument
	 */
	@Override
	public String getArgumentName()
	{
		return "userformats";
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
		return TEMPLATE.getTranslatedString(sender, TranslationPaths.DESCRIPTION_ARG_UF.getPath());
	}

	/**
	 * @return List of aliases for this argument
	 */
	@Override
	public List<String> getArgumentAliases()
	{
		return Arrays.asList("userformat", "uf");
	}

	/**
	 * @return List of types which can use this argument
	 * @see CommandType
	 */
	@Override
	public List<CommandType> getArgumentTypes()
	{
		return Arrays.asList(CommandType.PLAYER);
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
		loadPermission(PermissionNames.ARG_UF);
	}

	/**
	 * Register all sub arguments for this argument
	 */
	@Override
	public void registerAllSubArguments()
	{
		registerSubArgument(new UfEditSubArg());
		registerSubArgument(new UfGetSubArg());
		registerSubArgument(new UfResetSubArg());
		registerSubArgument(new UfTemporarySubArg());
		registerSubArgument(new UfTreeSubArg());
	}
}

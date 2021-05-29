package net.haffel.PL.args;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.command.CommandSender;

import lombok.Getter;
import net.haffel.PL.TEMPLATE;
import net.haffel.PL.args.lang.LangEditSubArg;
import net.haffel.PL.args.lang.LangGetSubArg;
import net.haffel.PL.args.lang.LangInfoSubArg;
import net.haffel.PL.args.lang.LangListSubArg;
import net.haffel.PL.args.lang.LangNewSubArg;
import net.haffel.PL.args.lang.LangReloadSubArg;
import net.haffel.PL.args.lang.LangResetSubArg;
import net.haffel.PL.args.lang.LangSetSubArg;
import net.haffel.PL.args.lang.LangTemporarySubArg;
import net.haffel.PL.args.lang.LangTreeSubArg;
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
public class LanguageArg extends ArgumentBase
{
	// List with all unsaved edited config keys and values
	@Getter static Map<CommandSender, Map<String, Object>> languageChanges = new HashMap<>();

	/**
	 * @return Name of this argument
	 */
	@Override
	public String getArgumentName()
	{
		return "language";
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
		return TEMPLATE.getTranslatedString(sender, TranslationPaths.DESCRIPTION_ARG_LANG.getPath());
	}

	/**
	 * @return List of aliases for this argument
	 */
	@Override
	public List<String> getArgumentAliases()
	{
		return Arrays.asList("lang");
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
		loadPermission(PermissionNames.ARG_LANG);
	}

	/**
	 * Register all sub arguments for this argument
	 */
	@Override
	public void registerAllSubArguments()
	{
		registerSubArgument(new LangEditSubArg());
		registerSubArgument(new LangGetSubArg());
		registerSubArgument(new LangInfoSubArg());
		registerSubArgument(new LangListSubArg());

		if((boolean) Config.getConfigSettings().get(ConfigSettings.LANGUAGES_CUSTOM_LANGUAGES))
		{
			registerSubArgument(new LangNewSubArg());
		}

		registerSubArgument(new LangReloadSubArg());
		registerSubArgument(new LangResetSubArg());
		registerSubArgument(new LangSetSubArg());
		registerSubArgument(new LangTemporarySubArg());
		registerSubArgument(new LangTreeSubArg());
	}
}

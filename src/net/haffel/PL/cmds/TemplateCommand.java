package net.haffel.PL.cmds;

import org.bukkit.command.CommandSender;

import net.haffel.PL.TEMPLATE;
import net.haffel.PL.args.ConfigArg;
import net.haffel.PL.args.ConfirmArg;
import net.haffel.PL.args.DebugArg;
import net.haffel.PL.args.HelpArg;
import net.haffel.PL.args.InfoArg;
import net.haffel.PL.args.LanguageArg;
import net.haffel.PL.args.NewsArg;
import net.haffel.PL.args.StopArg;
import net.haffel.PL.args.UserFormatsArg;
import net.haffel.PL.cmds.utils.CommandBase;
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
 * @lastEdit v1.1 TODO Replace template stuff
 */
public class TemplateCommand extends CommandBase
{
	/**
	 * @return Name of this command
	 */
	public String getCommandName()
	{
		return "template";
	}

	/**
	 * @return Description of the command
	 * @throws TranslationNotFoundException
	 *             For the given translation key was no translation found
	 * @throws UserFormatConfigDoesntExistException
	 *             The user formats config for the sender doesnt exist
	 * @throws SomeTranslationException
	 *             Any other error
	 */
	public String getCommandDescription(CommandSender sender)
			throws TranslationNotFoundException
	{
		return TEMPLATE.getTranslatedString(sender, TranslationPaths.DESCRIPTION_CMD_TEMPLATE.getPath());
	}

	/**
	 * Adds all needed permissions for this command to a list. This does NOT load the permissions
	 * 
	 * @throws PermissionNotFoundException
	 *             Permission is not loaded
	 * @see PermissionHandler#setupPermissions()
	 */
	public void loadPermissions() throws PermissionNotFoundException
	{
		loadPermission(PermissionNames.CMD_TEMPLATE);
	}

	/**
	 * Register all arguments for this command
	 */
	public void registerAllArguments()
	{
		registerArgument(new ConfigArg());
		registerArgument(new ConfirmArg());
		registerArgument(new DebugArg());
		registerArgument(new HelpArg());
		registerArgument(new InfoArg());
		registerArgument(new LanguageArg());
		registerArgument(new NewsArg());
		registerArgument(new StopArg());

		if((boolean) Config.getConfigSettings().get(ConfigSettings.LANGUAGES_USER_FORMATS))
		{
			registerArgument(new UserFormatsArg());
		}
	}
}

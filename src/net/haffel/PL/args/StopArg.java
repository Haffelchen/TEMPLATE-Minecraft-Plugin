package net.haffel.PL.args;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;

import net.haffel.PL.TEMPLATE;
import net.haffel.PL.args.utils.ArgumentBase;
import net.haffel.PL.args.utils.Confirm;
import net.haffel.PL.args.utils.ConfirmedCmd;
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
public class StopArg extends ArgumentBase
{
	/**
	 * @return Name of this argument
	 */
	@Override
	public String getArgumentName()
	{
		return "stop";
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
		return TEMPLATE.getTranslatedString(sender, TranslationPaths.DESCRIPTION_ARG_STOP.getPath());
	}

	/**
	 * @return List of aliases for this argument
	 */
	@Override
	public List<String> getArgumentAliases()
	{
		return Arrays.asList("shutdown");
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

	@Override
	public boolean execute(CommandSender sender, Command cmd, String label, String[] args)
			throws CommandException, TranslationNotFoundException
	{
		String[] extraArgs =
		{ Integer.toString((int) Config.getConfigSettings().get(ConfigSettings.COMMANDS_CONFIRM_TIME) / 1000) };

		Confirm.requestConfirmation(sender, this.getClass(),
				getTranslatedString(sender, TranslationPaths.ARG_STOP_CONFIRM.getPath(), args, extraArgs));

		return true;
	}

	/**
	 * Shuts down the plugin after confirmation
	 */
	@ConfirmedCmd
	public void disablePlugin()
	{
		TEMPLATE.disable();
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
		loadPermission(PermissionNames.ARG_STOP);
	}

	/**
	 * @param commandSender
	 *            CommandSender which used the command
	 * @param translate
	 *            The path to the translation
	 * @param args
	 *            The arguments passed at usage
	 * @param extraArgs
	 *            Extra arguments passed by the plugin
	 * @return Translated String
	 * @throws TranslationNotFoundException
	 *             For the given translation key was no translation found
	 * @throws UserFormatConfigDoesntExistException
	 *             The user formats config for the sender doesnt exist
	 * @throws SomeTranslationException
	 *             Any other error
	 */
	private String[] getTranslatedString(CommandSender commandSender, String translate, String[] args, String[] extraArgs)
			throws TranslationNotFoundException
	{
		return TEMPLATE.getTranslatedStringSplitted(commandSender, translate, args, extraArgs);
	}
}

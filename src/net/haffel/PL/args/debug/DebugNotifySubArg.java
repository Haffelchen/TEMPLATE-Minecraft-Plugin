package net.haffel.PL.args.debug;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.haffel.PL.TEMPLATE;
import net.haffel.PL.UserFormatsPaths;
import net.haffel.PL.args.utils.ArgumentBase;
import net.haffel.PL.args.utils.ExtendedSubSyntax;
import net.haffel.PL.cmds.utils.CommandType;
import net.haffel.PL.exceptions.PermissionNotFoundException;
import net.haffel.PL.files.TranslationPaths;
import net.haffel.PL.perms.utils.PermissionHandler;
import net.haffel.PL.perms.utils.PermissionNames;
import net.haffel.PL.utils.Debug;
import net.haffel.ml.exceptions.LanguageFileNotFoundException;
import net.haffel.ml.exceptions.PluginNotRegisteredException;
import net.haffel.ml.exceptions.TranslationNotFoundException;
import net.haffel.ml.translate.UserFormat;

/**
 * Template class
 * 
 * @since v1.1
 * @lastEdit v1.1
 */
public class DebugNotifySubArg extends ArgumentBase
{
	/**
	 * @return Name of this argument
	 */
	@Override
	public String getArgumentName()
	{
		return "notify";
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
		return TEMPLATE.getTranslatedString(sender, TranslationPaths.DESCRIPTION_ARG_DEBUG_NOTIFY.getPath());
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

	@Override
	public boolean execute(CommandSender sender, Command cmd, String label, String[] args) throws CommandException, TranslationNotFoundException,
			LanguageFileNotFoundException, IOException, PluginNotRegisteredException
	{
		if(args.length == 3)
		{
			if(args[2].equalsIgnoreCase("true"))
			{
				Debug.addNotify(sender);
				UserFormat.setUserSetting((Player) sender, TEMPLATE.getPluginName(), UserFormatsPaths.DEBUG_NOTIFY.getPath(), "true");

				sender.sendMessage(TEMPLATE.getTranslatedStringSplitted(sender, TranslationPaths.ARG_DEBUG_NOTIFY_ENABLED.getPath(), args));

				return true;

			} else if(args[2].equalsIgnoreCase("false"))
			{
				Debug.removeNotify(sender);
				UserFormat.setUserSetting((Player) sender, TEMPLATE.getPluginName(), UserFormatsPaths.DEBUG_NOTIFY.getPath(), "false");

				sender.sendMessage(TEMPLATE.getTranslatedStringSplitted(sender, TranslationPaths.ARG_DEBUG_NOTIFY_DISABLED.getPath(), args));

				return true;
			}
		}

		String[] extraArgs =
		{ getArgumentSyntax() };

		sender.sendMessage(TEMPLATE.getTranslatedStringSplitted(sender, TranslationPaths.SYNTAX_MESSAGE.getPath(), args, extraArgs));

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
		loadPermission(PermissionNames.ARG_DEBUG_NOTIFY);
	}

	/**
	 * Register all sub arguments for this argument
	 */
	@Override
	public void registerAllSubArguments()
	{
		ExtendedSubSyntax enable = new ExtendedSubSyntax("true", TranslationPaths.DESCRIPTION_ARG_DEBUG_NOTIFY_ENABLE, false, true);
		ExtendedSubSyntax disable = new ExtendedSubSyntax("false", TranslationPaths.DESCRIPTION_ARG_DEBUG_NOTIFY_DISABLE, false, true);

		addExtendedSyntax(enable, false);
		addExtendedSyntax(disable, false);
	}
}

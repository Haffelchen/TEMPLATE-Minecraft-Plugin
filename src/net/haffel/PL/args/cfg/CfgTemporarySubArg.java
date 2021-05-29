package net.haffel.PL.args.cfg;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;

import net.haffel.PL.TEMPLATE;
import net.haffel.PL.args.cfg.tmp.CfgTmpClearSubArg;
import net.haffel.PL.args.cfg.tmp.CfgTmpListSubArg;
import net.haffel.PL.args.cfg.tmp.CfgTmpRemoveSubArg;
import net.haffel.PL.args.cfg.tmp.CfgTmpSaveSubArg;
import net.haffel.PL.args.utils.ArgumentBase;
import net.haffel.PL.exceptions.PermissionNotFoundException;
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
public class CfgTemporarySubArg extends ArgumentBase
{
	/**
	 * @return Name of the sub argument
	 */
	@Override
	public String getArgumentName()
	{
		return "temporary";
	}

	/**
	 * @return Description of the sub argument
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
		return TEMPLATE.getTranslatedString(sender, TranslationPaths.DESCRIPTION_ARG_CFG_TMP.getPath());
	}

	/**
	 * @return List of aliases for this sub argument
	 */
	@Override
	public List<String> getArgumentAliases()
	{
		return Arrays.asList("temp", "tmp");
	}

	@Override
	public boolean execute(CommandSender sender, Command cmd, String label, String[] args) throws CommandException, TranslationNotFoundException,
			LanguageFileNotFoundException, IOException, PluginNotRegisteredException, LanguageNotLoadedException
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
	 * Adds all needed permissions for the sub argument to a list. This does NOT load the
	 * permissions
	 * 
	 * @throws PermissionNotFoundException
	 *             Permission is not loaded
	 * @see PermissionHandler#setupPermissions()
	 */
	public void loadPermissions() throws PermissionNotFoundException
	{
		loadPermission(PermissionNames.ARG_CFG_TMP);
	}

	/**
	 * Loads all sub arguments for the sub argument
	 */
	@Override
	public void registerAllSubArguments()
	{
		registerSubArgument(new CfgTmpClearSubArg());
		registerSubArgument(new CfgTmpListSubArg());
		registerSubArgument(new CfgTmpRemoveSubArg());
		registerSubArgument(new CfgTmpSaveSubArg());
	}
}

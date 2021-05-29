package net.haffel.PL.args.uf;

import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import net.haffel.PL.TEMPLATE;
import net.haffel.PL.args.utils.ArgumentBase;
import net.haffel.PL.args.utils.ExtendedSubSyntax;
import net.haffel.PL.exceptions.PermissionNotFoundException;
import net.haffel.PL.files.TranslationPaths;
import net.haffel.PL.perms.utils.PermissionHandler;
import net.haffel.PL.perms.utils.PermissionNames;
import net.haffel.ml.exceptions.TranslationNotFoundException;
import net.haffel.ml.files.UserFormatFile;

/**
 * Template class
 * 
 * @since v1.1
 * @lastEdit v1.1
 */
public class UfGetSubArg extends ArgumentBase
{
	/**
	 * @return Name of the sub argument
	 */
	@Override
	public String getArgumentName()
	{
		return "get";
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
		return TEMPLATE.getTranslatedString(sender, TranslationPaths.DESCRIPTION_ARG_UF_GET.getPath());
	}

	@Override
	public boolean execute(CommandSender sender, Command cmd, String label, String[] args)
			throws CommandException, TranslationNotFoundException
	{
		Player p = (Player) sender;

		if(args.length > 2)
		{
			FileConfiguration ufConfig = UserFormatFile.getUserFormatConfig(p, TEMPLATE.getPluginName());

			if(ufConfig.contains("Translations." + args[2]))
			{
				if(ufConfig.isConfigurationSection("Translations." + args[2]))
				{
					String[] extraArgs =
					{ Integer.toString(ufConfig.getConfigurationSection("Translations." + args[2]).getKeys(true).size()) };

					sender.sendMessage(getTranslatedStringSplitted(sender, ufConfig, TranslationPaths.ARG_UF_GET_SECTION.getPath(), args, extraArgs));

					return true;

				} else
				{
					String[] extraArgs =
					{ ufConfig.get("Translations." + args[2]).getClass().getSimpleName() };

					sender.sendMessage(getTranslatedStringSplitted(sender, ufConfig, TranslationPaths.ARG_UF_GET_VALUE.getPath(), args, extraArgs));

					return true;
				}
			} else
			{
				sender.sendMessage(getTranslatedStringSplitted(sender, TranslationPaths.ARG_UF_GET_CANT_FIND_VALUE.getPath(), args));
				return false;
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
		loadPermission(PermissionNames.ARG_UF_GET);
	}

	/**
	 * Register all sub arguments for this argument
	 */
	@Override
	public void registerAllSubArguments()
	{
		ExtendedSubSyntax key = new ExtendedSubSyntax("Key", TranslationPaths.DESCRIPTION_ARG_UF_GET_VALUE, false, false);

		addExtendedSyntax(key, false);
	}

	/**
	 * @return List of tab completion options
	 * @throws TranslationNotFoundException
	 *             For the given translation key was no translation found
	 * @throws UserFormatConfigDoesntExistException
	 *             The user formats config for the sender doesnt exist
	 * @throws SomeTranslationException
	 *             Any other error
	 */
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args, int start)
			throws TranslationNotFoundException
	{
		if(args.length == 3)
		{
			FileConfiguration ufCfg = UserFormatFile.getUserFormatConfig((Player) sender, TEMPLATE.getPluginName());

			if(ufCfg.isConfigurationSection("Translations"))
			{
				return getSubKeyPaths(ufCfg, args, 2, "Translations");
			}
		}

		return Collections.emptyList();
	}

	/**
	 * @param commandSender
	 *            CommandSender which used the command
	 * @param translate
	 *            The path to the translation
	 * @param args
	 *            The arguments passed at usage
	 * @return Translated String
	 * @throws TranslationNotFoundException
	 *             For the given translation key was no translation found
	 * @throws UserFormatConfigDoesntExistException
	 *             The user formats config for the sender doesnt exist
	 * @throws SomeTranslationException
	 *             Any other error
	 */
	private String[] getTranslatedStringSplitted(CommandSender commandSender, String translate, String[] args)
			throws TranslationNotFoundException
	{
		return TEMPLATE.getTranslatedStringSplitted(commandSender, translate, args);
	}

	/**
	 * @param commandSender
	 *            CommandSender which used the command
	 * @param config
	 *            A FileConfiguration from which values can be replaced
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
	public static String[] getTranslatedStringSplitted(CommandSender commandSender, FileConfiguration config, String translate, String[] args,
			String[] extraArgs) throws TranslationNotFoundException
	{
		return TEMPLATE.getTranslatedStringSplitted(commandSender, config, translate, args, extraArgs);
	}
}

package net.haffel.PL.args.uf.tmp;

import java.io.IOException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import net.haffel.PL.TEMPLATE;
import net.haffel.PL.args.UserFormatsArg;
import net.haffel.PL.args.utils.ArgumentBase;
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
public class UfTmpSaveSubArg extends ArgumentBase
{
	/**
	 * @return Name of the sub argument
	 */
	@Override
	public String getArgumentName()
	{
		return "save";
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
	public String getArgumentDescription(CommandSender sender) throws TranslationNotFoundException
	{
		return TEMPLATE.getTranslatedString(sender, TranslationPaths.DESCRIPTION_ARG_UF_TMP_SAVE.getPath());
	}

	@Override
	public boolean execute(CommandSender sender, Command cmd, String label, String[] args)
			throws CommandException, TranslationNotFoundException, IOException
	{
		Player p = (Player) sender;

		if(UserFormatsArg.getUFChanges().containsKey(sender))
		{
			FileConfiguration ufConfig = UserFormatFile.getUserFormatConfig(p, TEMPLATE.getPluginName());

			UserFormatsArg.getUFChanges().get(sender).forEach((key, value) ->
			{
				if(value.equals("$null"))
				{
					ufConfig.set("Translations." + key, null);

				} else if(value.equals("true"))
				{
					ufConfig.set("Translations." + key, true);

				} else if(value.equals("false"))
				{
					ufConfig.set("Translations." + key, false);

				} else
				{
					try
					{
						ufConfig.set("Translations." + key, Integer.parseInt(value + ""));

					} catch(NumberFormatException e)
					{
						ufConfig.set("Translations." + key, value);
					}
				}
			});

			ufConfig.save(UserFormatFile.getUserFormatFiles().get(p.getUniqueId().toString() + "." + TEMPLATE.getPluginName()));

			String[] extraArgs =
			{ Integer.toString(UserFormatsArg.getUFChanges().get(sender).size()) };

			sender.sendMessage(getTranslatedStringSplitted(sender, TranslationPaths.ARG_UF_TMP_SAVED_CHANGES.getPath(), args, extraArgs));

			UserFormatsArg.getUFChanges().remove(sender);
			return true;

		} else
		{
			sender.sendMessage(getTranslatedStringSplitted(sender, TranslationPaths.ARG_UF_TMP_NOTHING_TO_SAVE.getPath(), args));
			return false;
		}
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
		loadPermission(PermissionNames.ARG_UF_TMP_SAVE);
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
	private String[] getTranslatedStringSplitted(CommandSender commandSender, String translate, String[] args, String[] extraArgs)
			throws TranslationNotFoundException
	{
		return TEMPLATE.getTranslatedStringSplitted(commandSender, translate, args, extraArgs);
	}
}

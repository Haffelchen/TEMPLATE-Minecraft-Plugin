package net.haffel.PL.args;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;

import net.haffel.PL.TEMPLATE;
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
public class InfoArg extends ArgumentBase
{
	/**
	 * @return Name of this argument
	 */
	@Override
	public String getArgumentName()
	{
		return "info";
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
		return TEMPLATE.getTranslatedString(sender, TranslationPaths.DESCRIPTION_ARG_INFO.getPath());
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
	public boolean execute(CommandSender sender, Command cmd, String label, String[] args) throws CommandException
	{
		sender.sendMessage("");
		sender.sendMessage("§b▇▇▇▇▇▇▇▇ ");
		sender.sendMessage("§b▇§3▇▇▇▇▇▇§b▇ §r§9" + TEMPLATE.getPluginName() + " Plugin §ev" + TEMPLATE.getPluginVersion());
		sender.sendMessage("§b▇§0▇▇§3▇▇§0▇▇§b▇");
		sender.sendMessage("§b▇§0▇▇§3▇▇§0▇▇§b▇ §r§6§lCreated by");
		sender.sendMessage("§b▇§3▇▇▇▇▇▇§b▇ §r§b§lHaffel");
		sender.sendMessage("§b▇§3▇▇▇§0▇§3▇▇§b▇");
		sender.sendMessage("§b▇§3▇▇▇▇▇▇§b▇ §r§7haffel.jimdo.com/plugins/" + TEMPLATE.getPluginName().toLowerCase().replace(" ", "-") + "/");
		sender.sendMessage("§b▇▇▇▇▇▇▇▇");
		sender.sendMessage("");

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
		loadPermission(PermissionNames.ARG_INFO);
	}
}

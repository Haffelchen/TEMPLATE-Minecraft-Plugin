package net.haffel.PL.cmds.utils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

import net.haffel.PL.args.utils.IArgument;
import net.haffel.PL.exceptions.PermissionNotFoundException;
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
public interface ICommand
{
	String getCommandName();
	String getCommandDescription(CommandSender sender) throws TranslationNotFoundException;
	String getCommandSyntax();
	String getCommandSyntaxSub();
	void setCommandSyntaxSub(String newSyntaxSub);

	List<String> getCommandAliases();
	List<CommandType> getCommandTypes();

	List<Permission> getPermissions();

	List<IArgument> getArguments();
	List<String> getArgumentNames();
	IArgument getArgumentByName(String name);
	List<IArgument> getArgumentsAndAliasesByPermission(CommandSender sender);
	List<String> getArgumentNamesAndAliasesByPermission(CommandSender sender);
	Map<String, IArgument> getArgumentsAndAliases();

	boolean execute(CommandSender sender, Command cmd, String label, String[] args) throws CommandException, TranslationNotFoundException,
			LanguageFileNotFoundException, IOException, PluginNotRegisteredException, LanguageNotLoadedException;
	boolean tryExecute(CommandSender sender, Command cmd, String label, String[] args) throws TranslationNotFoundException,
			LanguageFileNotFoundException, IOException, PluginNotRegisteredException, LanguageNotLoadedException;

	boolean needAllPermissions();
	boolean checkPermissions(CommandSender sender);
	void loadPermissions() throws PermissionNotFoundException;
	void loadPermission(PermissionNames permission) throws PermissionNotFoundException;

	void registerAllArguments();
	void registerArgument(IArgument argument);
	void registerSyntax();
	String createHelp(CommandSender sender) throws TranslationNotFoundException;
	void registerHelp(CommandSender sender);

	List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args)
			throws TranslationNotFoundException, PluginNotRegisteredException, LanguageNotLoadedException;
}

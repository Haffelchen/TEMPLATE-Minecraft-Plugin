package net.haffel.PL.args.utils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

import net.haffel.PL.cmds.utils.CommandType;
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
public interface IArgument
{
	String getArgumentName();
	String getArgumentDescription(CommandSender sender) throws TranslationNotFoundException;
	String getArgumentSyntax();
	String getArgumentSyntaxBase();
	String getArgumentSyntaxSub();
	String getArgumentSyntax(boolean withSub);
	String getArgumentSyntax(String[] args, int start, boolean withSub);
	String getArgumentSyntax(int moreArgs);
	String getArgumentSyntax(String[] args, int start);
	String getArgumentSyntax(String[] args, int start, int moreArgs);
	void setArgumentSyntaxBase(String newSyntaxBase);
	void setArgumentSyntaxSub(String newSyntaxSub);
	boolean isEqualToArg();
	boolean isOptional();

	List<String> getArgumentAliases();
	List<CommandType> getArgumentTypes();

	List<Permission> getPermissions();

	List<IArgument> getSubArguments();
	List<String> getArgumentNames();
	List<IArgument> getSubArgumentsAndAliasesByPermission(CommandSender sender);
	List<String> getSubArgumentNamesAndAliasesByPermission(CommandSender sender);
	Map<String, IArgument> getSubArgumentsAndAliases();
	List<ExtendedSubSyntax> getExtendedSyntaxes();
	ExtendedSubSyntax getDefaultExtendedSyntax();

	boolean execute(CommandSender sender, Command cmd, String label, String[] args) throws CommandException, TranslationNotFoundException, LanguageNotLoadedException, LanguageFileNotFoundException, IOException, PluginNotRegisteredException;
	boolean tryExecute(CommandSender sender, Command cmd, String label, String[] args) throws TranslationNotFoundException,
			LanguageFileNotFoundException, IOException, PluginNotRegisteredException, CommandException, LanguageNotLoadedException;

	boolean needAllPermissions();
	boolean checkPermissions(CommandSender sender);
	void loadPermissions() throws PermissionNotFoundException;
	void loadPermission(PermissionNames permission) throws PermissionNotFoundException;

	void registerAllSubArguments() throws TranslationNotFoundException;
	void registerSubArgument(IArgument subArgument);
	void registerSyntax(String syntaxBase);
	String createHelp(CommandSender sender) throws TranslationNotFoundException;
	void registerHelp(CommandSender sender, String path, int maxLength);
	void addExtendedSyntax(ExtendedSubSyntax subArgumentSyntax, boolean isDefault);

	List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args, int start)
			throws TranslationNotFoundException, PluginNotRegisteredException, LanguageNotLoadedException;
}

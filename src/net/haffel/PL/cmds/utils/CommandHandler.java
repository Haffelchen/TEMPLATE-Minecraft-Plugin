package net.haffel.PL.cmds.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import lombok.Getter;
import net.haffel.PL.NoneTranslatedMessages;
import net.haffel.PL.TEMPLATE;
import net.haffel.PL.args.utils.Confirm;
import net.haffel.PL.cmds.TemplateCommand;
import net.haffel.PL.events.PlayerCommandPreprocess;
import net.haffel.PL.files.Config;
import net.haffel.PL.files.ConfigSettings;
import net.haffel.PL.utils.Colored;
import net.haffel.PL.utils.Debug;
import net.haffel.PL.utils.DebugTiming;
import net.haffel.ml.exceptions.LanguageNotLoadedException;
import net.haffel.ml.exceptions.PluginNotRegisteredException;
import net.haffel.ml.exceptions.TranslationNotFoundException;

/**
 * Template class
 * 
 * @since v1.1
 * @lastEdit v1.1 TODO Replace template stuff
 */
public class CommandHandler implements CommandExecutor, TabCompleter
{
	// List with all commands and aliases
	@Getter private final static Map<String, ICommand> commandsWithAlias = new HashMap<>();
	// List with all command classes
	@Getter private final static Set<ICommand> commands = new HashSet<>();
	@Getter static HashMap<CommandSender, HashMap<String, List<HelpPage>>> helpPages = new HashMap<>();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		try
		{
			for(String alias : getCommandsWithAlias().keySet())
			{
				if(alias.equalsIgnoreCase(label))
				{
					return getCommandsWithAlias().get(alias).tryExecute(sender, cmd, label, args);
				}
			}
		} catch(Exception e)
		{
			int index = Debug.addRunning(System.currentTimeMillis(), e);
			
			sender.sendMessage(NoneTranslatedMessages.REPORT_TO_ADMIN_WITH_INDEX.getMessage().replace("{INDEX}", Integer.toString(index)));
		}

		return false;
	}

	/**
	 * Setup all commands
	 */
	public static void setupCommands()
	{
		long start = System.currentTimeMillis();
		boolean debug = (boolean) Config.getConfigSettings().get(ConfigSettings.DEBUG_AT_START_COMMANDS);
		boolean fullDebug = (boolean) Config.getConfigSettings().get(ConfigSettings.DEBUG_AT_START_DETAILED) && debug;

		Colored.toConsole("Commands: Register commands and arguments..", ChatColor.AQUA, debug, "");

		TemplateCommand cmdPl = new TemplateCommand();
		cmdPl.registerSyntax();

		TEMPLATE.getInstance().getCommand("template").setExecutor(new CommandHandler());
		CommandHandler.registerCommand(cmdPl);
		Colored.toConsole("Commands", "Registered " + CommandHandler.getCommands().size() + " command(s)", ChatColor.AQUA, fullDebug, "");
		Colored.toConsole("Commands", "Added " + cmdPl.getArguments().size() + " argument(s)", ChatColor.AQUA, fullDebug, "");

		if(Bukkit.getOnlinePlayers().size() > 0)
		{
			Confirm.confirmTimer();
			PlayerCommandPreprocess.delayTimer();
		}

		float time = (float) (System.currentTimeMillis() - start) / 1000;

		Colored.toConsole("Commands", "Time elapsed: " + time + " seconds", ChatColor.AQUA, fullDebug, "Successfully registered (" + time + "s)",
				debug);
		Colored.toConsole(" ", ChatColor.AQUA, debug, "");

		Debug.addTiming(DebugTiming.COMMAND_HANDLER, time);
	}

	/**
	 * @param command
	 *            Command class to register
	 */
	public static void registerCommand(ICommand command)
	{
		getCommandsWithAlias().put(command.getCommandName(), command);
		getCommands().add(command);

		for(String alias : TEMPLATE.getInstance().getCommand(command.getCommandName()).getAliases())
		{
			if(!getCommandsWithAlias().containsKey(alias) || !getCommandsWithAlias().get(alias).getCommandName().equals(alias))
			{
				getCommandsWithAlias().put(alias, command);
			}
		}
	}

	public static ICommand getCommandByName(String name)
	{
		for(ICommand cmd : getCommands())
		{
			if(cmd.getCommandName().equalsIgnoreCase(name) || cmd.getCommandAliases().contains(name))
			{
				return cmd;
			}
		}

		return null;
	}

	/**
	 * @param sender
	 *            The CommandSender who used the command
	 * @return A List with all registered commands the sender has permissions to use
	 */
	public List<ICommand> getCommandsWithPermission(CommandSender sender)
	{
		List<ICommand> list = new ArrayList<>();

		for(ICommand command : getCommands())
		{
			if(command.checkPermissions(sender))
			{
				list.add(command);
			}
		}

		return list;
	}

	/**
	 * @return A List with all registered commands
	 */
	public static List<String> getCommandNames()
	{
		List<String> commandNames = new ArrayList<>();

		getCommands().forEach(cmd -> commandNames.add(cmd.getCommandName()));

		return commandNames;
	}

	/**
	 * @return A List with all registered commands and aliases
	 */
	public Map<String, ICommand> getCommandsAndAliases()
	{
		return getCommandsWithAlias();
	}

	/**
	 * @param sender
	 *            The CommandSender which used the command
	 * @param cmd
	 *            The command which was executed
	 * @param label
	 *            Executed commands name
	 * @param args
	 *            Passed arguments
	 * @return List with tab completion options
	 */
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args)
	{
		try
		{
			if(getCommandsWithAlias().containsKey(label))
			{
				return getCommandsWithAlias().get(label).onTabComplete(sender, cmd, label, args);
			}
		} catch(TranslationNotFoundException | PluginNotRegisteredException | LanguageNotLoadedException e)
		{
			int index = Debug.addRunning(System.currentTimeMillis(), e);
			
			sender.sendMessage(NoneTranslatedMessages.REPORT_TO_ADMIN_WITH_INDEX.getMessage().replace("{INDEX}", Integer.toString(index)));
		}

		return Collections.emptyList();
	}

	public static void registerHelp(CommandSender sender)
	{
		getHelpPages().put(sender, new HashMap<String, List<HelpPage>>());

		for(ICommand cmd : getCommands())
		{
			cmd.registerHelp(sender);
		}
	}
	
	public static boolean isSenderHelpRegistered(CommandSender sender)
	{
		return getHelpPages().containsKey(sender);
	}

	public static boolean isArgumentHelpRegistered(CommandSender sender, String arg)
	{
		return isSenderHelpRegistered(sender) && getHelpPages().get(sender).containsKey(arg);
	}

	public static Set<String> getHelpPaths(CommandSender sender)
	{
		if(isSenderHelpRegistered(sender))
		{
			return getHelpPages().get(sender).keySet();
		}

		return Collections.emptySet();
	}

	public static List<HelpPage> getHelpForArg(CommandSender sender, String arg)
	{
		if(isArgumentHelpRegistered(sender, arg))
		{
			return getHelpPages().get(sender).get(arg);
		}

		return Collections.emptyList();
	}

	public static HashMap<String, List<HelpPage>> getHelp(CommandSender sender)
	{
		if(isSenderHelpRegistered(sender))
		{
			return getHelpPages().get(sender);
		}

		return new HashMap<String, List<HelpPage>>();
	}
}

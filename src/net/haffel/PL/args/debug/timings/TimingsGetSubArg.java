package net.haffel.PL.args.debug.timings;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;

import net.haffel.PL.TEMPLATE;
import net.haffel.PL.args.utils.ArgumentBase;
import net.haffel.PL.args.utils.ExtendedSubSyntax;
import net.haffel.PL.exceptions.PermissionNotFoundException;
import net.haffel.PL.files.TranslationPaths;
import net.haffel.PL.perms.utils.PermissionHandler;
import net.haffel.PL.perms.utils.PermissionNames;
import net.haffel.PL.utils.Debug;
import net.haffel.PL.utils.DebugTiming;
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
public class TimingsGetSubArg extends ArgumentBase
{
	/**
	 * @return Name of this argument
	 */
	@Override
	public String getArgumentName()
	{
		return "get";
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
		return TEMPLATE.getTranslatedString(sender, TranslationPaths.DESCRIPTION_ARG_DEBUG_TIMINGS_GET.getPath());
	}

	@Override
	public boolean execute(CommandSender sender, Command cmd, String label, String[] args) throws CommandException, TranslationNotFoundException,
			LanguageFileNotFoundException, IOException, PluginNotRegisteredException
	{
		if(args.length > 3)
		{
			try
			{
				float timing = Debug.getTiming(DebugTiming.valueOf(args[3]));

				if(timing != -1L)
				{
					String[] extraArgs =
					{ TEMPLATE.getTranslatedString(sender, DebugTiming.valueOf(args[3]).getDescription().getPath()), Float.toString(timing) };

					sender.sendMessage(
							TEMPLATE.getTranslatedStringSplitted(sender, TranslationPaths.ARG_DEBUG_TIMINGS_GET.getPath(), args, extraArgs));

					return true;
				} else
				{
					sender.sendMessage(TEMPLATE.getTranslatedStringSplitted(sender, TranslationPaths.ARG_DEBUG_TIMINGS_GET_INVALID.getPath(), args));

					return false;
				}
			} catch(IllegalArgumentException e)
			{
				sender.sendMessage(TEMPLATE.getTranslatedStringSplitted(sender, TranslationPaths.ARG_DEBUG_TIMINGS_GET_INVALID.getPath(), args));

				return false;
			}
		}

		String[] extraArgs =
		{ getArgumentSyntax() };

		sender.sendMessage(TEMPLATE.getTranslatedStringSplitted(sender, TranslationPaths.SYNTAX_MESSAGE.getPath(), args, extraArgs));

		return false;
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
		loadPermission(PermissionNames.ARG_DEBUG_TIMINGS_GET);
	}

	/**
	 * Register all sub arguments for this argument
	 */
	@Override
	public void registerAllSubArguments()
	{
		ExtendedSubSyntax index = new ExtendedSubSyntax("Index", TranslationPaths.DESCRIPTION_ARG_DEBUG_TIMINGS_GET_INDEX, false, false);

		addExtendedSyntax(index, true);
	}

	/**
	 * @return List of tab completion options
	 * @throws TranslationNotFoundException
	 *             For the given translation key was no translation found
	 * @throws UserFormatConfigDoesntExistException
	 *             The user formats config for the sender doesnt exist
	 * @throws SomeTranslationException
	 *             Any other error
	 * @throws PluginNotRegisteredException
	 *             Plugin is not registered in Multi Languages Plugin
	 * @throws LanguageNotLoadedException 
	 */
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args, int start)
			throws TranslationNotFoundException, PluginNotRegisteredException, LanguageNotLoadedException
	{
		if(args.length == 4)
		{
			List<String> complete = new ArrayList<>();

			for(DebugTiming timing : DebugTiming.values())
			{
				complete.add(timing.toString());
			}

			return getListOfStringsMatchingLastWord(args, complete);
		}

		return super.onTabComplete(sender, cmd, label, args, start);
	}
}

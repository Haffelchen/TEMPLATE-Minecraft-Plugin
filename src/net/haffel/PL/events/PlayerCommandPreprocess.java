package net.haffel.PL.events;

import java.util.HashMap;
import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import lombok.Getter;
import net.haffel.PL.NoneTranslatedMessages;
import net.haffel.PL.TEMPLATE;
import net.haffel.PL.cmds.utils.CommandHandler;
import net.haffel.PL.files.Config;
import net.haffel.PL.files.ConfigSettings;
import net.haffel.PL.files.TranslationPaths;
import net.haffel.PL.perms.utils.PermissionNames;
import net.haffel.PL.utils.Debug;
import net.haffel.ml.exceptions.TranslationNotFoundException;

/**
 * Template class
 * 
 * @since v1.1
 * @lastEdit v1.1
 */
public class PlayerCommandPreprocess implements Listener
{
	// List with player and time when someone used a command of this plugin
	@Getter static HashMap<Player, Long> firstTime = new HashMap<>();
	// List with player and time when command delay is over
	@Getter static HashMap<Player, Long> delay = new HashMap<>();
	// Timer "object"
	public static int timer;
	// Time to wait if a command was used to fast
	@Getter private static int delayTime = (int) Config.getConfigSettings().get(ConfigSettings.COMMANDS_DELAY);

	@EventHandler
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event)
	{
		if(getDelayTime() > 0)
		{
			Player p = event.getPlayer();

			if(CommandHandler.getCommandsWithAlias().keySet().contains(event.getMessage().split(" ")[0].replace("/", ""))
					&& !p.hasPermission(Bukkit.getPluginManager().getPermission(PermissionNames.NO_COMMAND_DELAY.getName())))
			{
				if(getFirstTime().containsKey(p))
				{
					getDelay().put(p, System.currentTimeMillis() + getDelayTime());
					getFirstTime().remove(p);

				} else if(getDelay().containsKey(p))
				{
					long wait = (getDelay().get(p) - System.currentTimeMillis()) / 1000;

					String[] extraArgs =
					{ Long.toString(wait + 1) };

					try
					{
						p.sendMessage(getTranslatedString(p, TranslationPaths.EVENT_PCP_WAIT.getPath(), new String[0], extraArgs));

					} catch(TranslationNotFoundException e)
					{
						int index = Debug.addRunning(System.currentTimeMillis(), e);
						
						p.sendMessage(NoneTranslatedMessages.REPORT_TO_ADMIN_WITH_INDEX.getMessage().replace("{INDEX}", Integer.toString(index)));
					}

					event.setCancelled(true);

				} else
				{
					getFirstTime().put(p, System.currentTimeMillis() + 1000);
				}
			}
		}
	}

	/**
	 * Command delay timer
	 */
	public static void delayTimer()
	{
		if(getDelayTime() > 0)
		{
			timer = Bukkit.getScheduler().scheduleSyncRepeatingTask(TEMPLATE.getInstance(), new Runnable()
			{
				@Override
				public void run()
				{
					if(!getFirstTime().isEmpty())
					{
						Iterator<Long> it = getFirstTime().values().iterator();

						while(it.hasNext())
						{
							long time = it.next();

							if(time <= System.currentTimeMillis())
							{
								it.remove();
							}
						}
					}

					if(!getDelay().isEmpty())
					{
						Iterator<Long> it = getDelay().values().iterator();

						while(it.hasNext())
						{
							long time = it.next();

							if(time <= System.currentTimeMillis())
							{
								it.remove();
							}
						}
					}
				}
			}, 0L, 20L);
		}
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

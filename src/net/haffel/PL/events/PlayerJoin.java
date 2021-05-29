package net.haffel.PL.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import net.haffel.PL.NoneTranslatedMessages;
import net.haffel.PL.TEMPLATE;
import net.haffel.PL.UserFormatsPaths;
import net.haffel.PL.args.utils.Confirm;
import net.haffel.PL.cmds.utils.CommandHandler;
import net.haffel.PL.perms.utils.PermissionNames;
import net.haffel.PL.utils.Debug;
import net.haffel.PL.utils.UpdateChecker;
import net.haffel.ml.exceptions.LanguageFileNotFoundException;


import net.haffel.ml.files.LanguageFile;
import net.haffel.ml.files.UserFormatFile;
import net.haffel.ml.translate.UserFormat;

/**
 * Template class
 * 
 * @since v1.1
 * @lastEdit v1.1
 */
public class PlayerJoin implements Listener
{
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		Player p = event.getPlayer();

		loadPlayerRelatedStuff(p);
		
		if(!Bukkit.getScheduler().isCurrentlyRunning(Confirm.timer) && !Bukkit.getScheduler().isQueued(Confirm.timer))
		{
			Confirm.confirmTimer();
		}

		if(!Bukkit.getScheduler().isCurrentlyRunning(PlayerCommandPreprocess.timer) && !Bukkit.getScheduler().isQueued(PlayerCommandPreprocess.timer))
		{
			PlayerCommandPreprocess.delayTimer();
		}

		if(p.hasPermission(Bukkit.getPluginManager().getPermission(PermissionNames.UPDATES_AT_JOIN.getName())))
		{
			UpdateChecker.checkForUpdates(false, p);
		}
	}
	
	public static void loadPlayerRelatedStuff(Player p)
	{
		UserFormatFile.loadPlayer(p, TEMPLATE.getPluginName());

		try
		{
			if(UserFormat.isEitherSettingSet(p, TEMPLATE.getPluginName(), UserFormatsPaths.DEBUG_NOTIFY.getPath()))
			{
				if(Boolean.parseBoolean(UserFormat.getEitherUserSetting(p, TEMPLATE.getPluginName(), UserFormatsPaths.DEBUG_NOTIFY.getPath())))
				{
					Debug.addNotify(p);
				}
			}
			
			if(UserFormat.isEitherSettingSet(p, TEMPLATE.getPluginName(), UserFormatsPaths.LANGUAGE.getPath()))
			{
				if(!LanguageFile.isLanguageLoaded(TEMPLATE.getPluginName(),
						UserFormat.getEitherUserSetting(p, TEMPLATE.getPluginName(), UserFormatsPaths.LANGUAGE.getPath())))
				{
					LanguageFile.loadLanguage(TEMPLATE.getPluginName(),
							UserFormat.getEitherUserSetting(p, TEMPLATE.getPluginName(), UserFormatsPaths.LANGUAGE.getPath()));
				}
			}
		} catch(LanguageFileNotFoundException e)
		{
			int index = Debug.addRunning(System.currentTimeMillis(), e);
			
			p.sendMessage(NoneTranslatedMessages.REPORT_TO_ADMIN_WITH_INDEX.getMessage().replace("{INDEX}", Integer.toString(index)));
		}

		CommandHandler.registerHelp(p);
	}
}

package net.haffel.PL.events;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import net.haffel.PL.TEMPLATE;
import net.haffel.PL.args.utils.Confirm;
import net.haffel.ml.files.UserFormatFile;

/**
 * Template class
 * 
 * @since v1.1
 * @lastEdit v1.1
 */
public class PlayerQuit implements Listener
{
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event)
	{
		if(Bukkit.getOnlinePlayers().size() == 1)
		{
			Bukkit.getScheduler().cancelTask(Confirm.timer);
			Bukkit.getScheduler().cancelTask(PlayerCommandPreprocess.timer);
		}

		UserFormatFile.unloadPlayer(event.getPlayer(), TEMPLATE.getPluginName());
	}
}

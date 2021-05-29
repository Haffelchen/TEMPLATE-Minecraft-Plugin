package net.haffel.PL.events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

import net.haffel.PL.TEMPLATE;
import net.haffel.PL.files.Config;
import net.haffel.PL.files.ConfigSettings;
import net.haffel.PL.utils.Colored;
import net.haffel.PL.utils.Debug;
import net.haffel.PL.utils.DebugTiming;

/**
 * Template class
 * 
 * @since v1.1
 * @lastEdit v1.1
 */
public class EventManager
{
	/**
	 * Load all events
	 */
	public static void load()
	{
		long start = System.currentTimeMillis();
		boolean debug = (boolean) Config.getConfigSettings().get(ConfigSettings.DEBUG_AT_START_EVENTS);
		boolean fullDebug = (boolean) Config.getConfigSettings().get(ConfigSettings.DEBUG_AT_START_EVENTS) && debug;

		Colored.toConsole("Events: Loading events..", ChatColor.AQUA, debug, "");

		PluginManager pm = Bukkit.getPluginManager();
		List<Listener> registerListener = new ArrayList<>();
		registerListener.add(new PlayerCommandPreprocess());
		registerListener.add(new PlayerJoin());
		registerListener.add(new PlayerQuit());

		for(int i = 0; i < registerListener.size(); i++)
		{
			pm.registerEvents(registerListener.get(i), TEMPLATE.getInstance());
		}

		Colored.toConsole("Events", "Loaded " + registerListener.size() + " event(s)", ChatColor.AQUA, fullDebug, "");

		float time = (float) (System.currentTimeMillis() - start) / 1000;

		Colored.toConsole("Events", "Time elapsed: " + time + " seconds", ChatColor.AQUA, fullDebug, "Successfully loaded (" + time + "s)", debug);
		Colored.toConsole(" ", ChatColor.AQUA, debug, "");

		Debug.addTiming(DebugTiming.EVENT_MANAGER, time);
	}
}

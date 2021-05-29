package net.haffel.PL.perms.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import net.haffel.PL.files.Config;
import net.haffel.PL.files.ConfigSettings;
import net.haffel.PL.utils.Colored;
import net.haffel.PL.utils.Debug;
import net.haffel.PL.utils.DebugTiming;

/**
 * Template class
 * 
 * @since v1.1
 * @lastEdit v1.1 TODO Replace template stuff
 */
public class PermissionHandler
{
	/**
	 * Setup all permissions of the plugin
	 */
	public static void setupPermissions()
	{
		long start = System.currentTimeMillis();
		int registered = 0;
		boolean debug = (boolean) Config.getConfigSettings().get(ConfigSettings.DEBUG_AT_START_PERMISSIONS);
		boolean fullDebug = (boolean) Config.getConfigSettings().get(ConfigSettings.DEBUG_AT_START_DETAILED) && debug;

		Colored.toConsole("Permissions: Register permissions..", ChatColor.AQUA, debug, "");

		List<Permission> permissions = new ArrayList<>();
		permissions.add(new Permission(PermissionNames.CMD_TEMPLATE.getName(), PermissionDefault.TRUE));

		permissions.add(new Permission(PermissionNames.ARG_CFG.getName(), PermissionDefault.OP));
		permissions.add(new Permission(PermissionNames.ARG_CFG_EDIT.getName(), PermissionDefault.OP));
		permissions.add(new Permission(PermissionNames.ARG_CFG_GET.getName(), PermissionDefault.OP));
		permissions.add(new Permission(PermissionNames.ARG_CFG_RL.getName(), PermissionDefault.OP));
		permissions.add(new Permission(PermissionNames.ARG_CFG_RESET.getName(), PermissionDefault.OP));
		permissions.add(new Permission(PermissionNames.ARG_CFG_TMP.getName(), PermissionDefault.OP));
		permissions.add(new Permission(PermissionNames.ARG_CFG_TMP_CLEAR.getName(), PermissionDefault.OP));
		permissions.add(new Permission(PermissionNames.ARG_CFG_TMP_LIST.getName(), PermissionDefault.OP));
		permissions.add(new Permission(PermissionNames.ARG_CFG_TMP_REMOVE.getName(), PermissionDefault.OP));
		permissions.add(new Permission(PermissionNames.ARG_CFG_TMP_SAVE.getName(), PermissionDefault.OP));
		permissions.add(new Permission(PermissionNames.ARG_CFG_TREE.getName(), PermissionDefault.OP));

		permissions.add(new Permission(PermissionNames.ARG_CONFIRM.getName(), PermissionDefault.TRUE));

		permissions.add(new Permission(PermissionNames.ARG_DEBUG.getName(), PermissionDefault.OP));
		permissions.add(new Permission(PermissionNames.ARG_DEBUG_NOTIFY.getName(), PermissionDefault.OP));
		permissions.add(new Permission(PermissionNames.ARG_DEBUG_RUNNING.getName(), PermissionDefault.OP));
		permissions.add(new Permission(PermissionNames.ARG_DEBUG_RUNNING_ENABLED.getName(), PermissionDefault.OP));
		permissions.add(new Permission(PermissionNames.ARG_DEBUG_RUNNING_GET.getName(), PermissionDefault.OP));
		permissions.add(new Permission(PermissionNames.ARG_DEBUG_RUNNING_LIST.getName(), PermissionDefault.OP));
		permissions.add(new Permission(PermissionNames.ARG_DEBUG_RUNNING_PRINT.getName(), PermissionDefault.OP));
		permissions.add(new Permission(PermissionNames.ARG_DEBUG_STARTUP.getName(), PermissionDefault.OP));
		permissions.add(new Permission(PermissionNames.ARG_DEBUG_STARTUP_ENABLED.getName(), PermissionDefault.OP));
		permissions.add(new Permission(PermissionNames.ARG_DEBUG_STARTUP_GET.getName(), PermissionDefault.OP));
		permissions.add(new Permission(PermissionNames.ARG_DEBUG_STARTUP_LIST.getName(), PermissionDefault.OP));
		permissions.add(new Permission(PermissionNames.ARG_DEBUG_STARTUP_PRINT.getName(), PermissionDefault.OP));
		permissions.add(new Permission(PermissionNames.ARG_DEBUG_TIMINGS.getName(), PermissionDefault.OP));
		permissions.add(new Permission(PermissionNames.ARG_DEBUG_TIMINGS_ENABLED.getName(), PermissionDefault.OP));
		permissions.add(new Permission(PermissionNames.ARG_DEBUG_TIMINGS_GET.getName(), PermissionDefault.OP));
		permissions.add(new Permission(PermissionNames.ARG_DEBUG_TIMINGS_LIST.getName(), PermissionDefault.OP));

		permissions.add(new Permission(PermissionNames.ARG_HELP.getName(), PermissionDefault.TRUE));

		permissions.add(new Permission(PermissionNames.ARG_INFO.getName(), PermissionDefault.TRUE));

		permissions.add(new Permission(PermissionNames.ARG_LANG.getName(), PermissionDefault.TRUE));
		permissions.add(new Permission(PermissionNames.ARG_LANG_EDIT.getName(), PermissionDefault.OP));
		permissions.add(new Permission(PermissionNames.ARG_LANG_GET.getName(), PermissionDefault.TRUE));
		permissions.add(new Permission(PermissionNames.ARG_LANG_INFO.getName(), PermissionDefault.TRUE));
		permissions.add(new Permission(PermissionNames.ARG_LANG_LIST.getName(), PermissionDefault.TRUE));
		permissions.add(new Permission(PermissionNames.ARG_LANG_NEW.getName(), PermissionDefault.OP));
		permissions.add(new Permission(PermissionNames.ARG_LANG_RL.getName(), PermissionDefault.OP));
		permissions.add(new Permission(PermissionNames.ARG_LANG_RESET.getName(), PermissionDefault.OP));
		permissions.add(new Permission(PermissionNames.ARG_LANG_SET.getName(), PermissionDefault.OP));
		permissions.add(new Permission(PermissionNames.ARG_LANG_TMP.getName(), PermissionDefault.OP));
		permissions.add(new Permission(PermissionNames.ARG_LANG_TMP_CLEAR.getName(), PermissionDefault.OP));
		permissions.add(new Permission(PermissionNames.ARG_LANG_TMP_LIST.getName(), PermissionDefault.OP));
		permissions.add(new Permission(PermissionNames.ARG_LANG_TMP_REMOVE.getName(), PermissionDefault.OP));
		permissions.add(new Permission(PermissionNames.ARG_LANG_TMP_SAVE.getName(), PermissionDefault.OP));
		permissions.add(new Permission(PermissionNames.ARG_LANG_TREE.getName(), PermissionDefault.OP));

		permissions.add(new Permission(PermissionNames.ARG_NEWS.getName(), PermissionDefault.TRUE));

		permissions.add(new Permission(PermissionNames.ARG_STOP.getName(), PermissionDefault.OP));

		permissions.add(new Permission(PermissionNames.ARG_UF.getName(), PermissionDefault.TRUE));
		permissions.add(new Permission(PermissionNames.ARG_UF_EDIT.getName(), PermissionDefault.TRUE));
		permissions.add(new Permission(PermissionNames.ARG_UF_GET.getName(), PermissionDefault.TRUE));
		permissions.add(new Permission(PermissionNames.ARG_UF_RESET.getName(), PermissionDefault.TRUE));
		permissions.add(new Permission(PermissionNames.ARG_UF_TMP.getName(), PermissionDefault.TRUE));
		permissions.add(new Permission(PermissionNames.ARG_UF_TMP_CLEAR.getName(), PermissionDefault.TRUE));
		permissions.add(new Permission(PermissionNames.ARG_UF_TMP_LIST.getName(), PermissionDefault.TRUE));
		permissions.add(new Permission(PermissionNames.ARG_UF_TMP_REMOVE.getName(), PermissionDefault.TRUE));
		permissions.add(new Permission(PermissionNames.ARG_UF_TMP_SAVE.getName(), PermissionDefault.TRUE));
		permissions.add(new Permission(PermissionNames.ARG_UF_TREE.getName(), PermissionDefault.TRUE));

		permissions.add(new Permission(PermissionNames.NO_COMMAND_DELAY.getName(), PermissionDefault.FALSE));

		permissions.add(new Permission(PermissionNames.UPDATES_AT_JOIN.getName(), PermissionDefault.OP));
		permissions.add(new Permission(PermissionNames.UPDATES_WHEN_FOUND.getName(), PermissionDefault.OP));

		for(int i = 0; i < permissions.size(); i++)
		{
			if(Bukkit.getPluginManager().getPermission(permissions.get(i).getName()) == null)
			{
				Bukkit.getPluginManager().addPermission(permissions.get(i));
				registered++;
			}
		}

		float time = (float) (System.currentTimeMillis() - start) / 1000;

		Colored.toConsole("Permissions", "Registered " + registered + " of " + permissions.size() + " permission(s)", ChatColor.AQUA, fullDebug, "");
		Colored.toConsole("Permissions", "Time elapsed: " + time + " seconds", ChatColor.AQUA, fullDebug, "Successfully registered (" + time + "s)",
				debug);
		Colored.toConsole(" ", ChatColor.AQUA, debug, "");

		Debug.addTiming(DebugTiming.PERMISSION_HANDLER, time);
	}
}

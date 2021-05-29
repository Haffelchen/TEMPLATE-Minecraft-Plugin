package net.haffel.PL.perms.utils;

import net.haffel.PL.TEMPLATE;
import net.haffel.PL.files.Config;
import net.haffel.PL.files.ConfigSettings;

/**
 * Template class
 * 
 * @since v1.1
 * @lastEdit v1.1 TODO Replace template stuff
 */
public enum PermissionNames
{
	CMD_TEMPLATE(TEMPLATE.getPluginName().toLowerCase() + ".cmd.TEMPLATE"),

	ARG_CFG(TEMPLATE.getPluginName().toLowerCase() + ".arg.cfg"),
	ARG_CFG_EDIT(TEMPLATE.getPluginName().toLowerCase() + ".arg.cfg.edit"),
	ARG_CFG_GET(TEMPLATE.getPluginName().toLowerCase() + ".arg.cfg.get"),
	ARG_CFG_RL(TEMPLATE.getPluginName().toLowerCase() + ".arg.cfg.rl"),
	ARG_CFG_RESET(TEMPLATE.getPluginName().toLowerCase() + ".arg.cfg.reset"),
	ARG_CFG_TMP(TEMPLATE.getPluginName().toLowerCase() + ".arg.cfg.tmp"),
	ARG_CFG_TMP_CLEAR(TEMPLATE.getPluginName().toLowerCase() + ".arg.cfg.tmp.clear"),
	ARG_CFG_TMP_LIST(TEMPLATE.getPluginName().toLowerCase() + ".arg.cfg.tmp.list"),
	ARG_CFG_TMP_REMOVE(TEMPLATE.getPluginName().toLowerCase() + ".arg.cfg.tmp.remove"),
	ARG_CFG_TMP_SAVE(TEMPLATE.getPluginName().toLowerCase() + ".arg.cfg.tmp.save"),
	ARG_CFG_TREE(TEMPLATE.getPluginName().toLowerCase() + ".arg.cfg.tree"),

	ARG_CONFIRM(TEMPLATE.getPluginName().toLowerCase() + ".arg.confirm"),

	ARG_DEBUG(TEMPLATE.getPluginName().toLowerCase() + ".arg.debug"),
	ARG_DEBUG_NOTIFY(TEMPLATE.getPluginName().toLowerCase() + ".arg.debug.notify"),
	ARG_DEBUG_RUNNING(TEMPLATE.getPluginName().toLowerCase() + ".arg.debug.running"),
	ARG_DEBUG_RUNNING_ENABLED(TEMPLATE.getPluginName().toLowerCase() + ".arg.debug.running.enabled"),
	ARG_DEBUG_RUNNING_GET(TEMPLATE.getPluginName().toLowerCase() + ".arg.debug.running.get"),
	ARG_DEBUG_RUNNING_LIST(TEMPLATE.getPluginName().toLowerCase() + ".arg.debug.running.list"),
	ARG_DEBUG_RUNNING_PRINT(TEMPLATE.getPluginName().toLowerCase() + ".arg.debug.running.print"),
	ARG_DEBUG_STARTUP(TEMPLATE.getPluginName().toLowerCase() + ".arg.debug.startup"),
	ARG_DEBUG_STARTUP_ENABLED(TEMPLATE.getPluginName().toLowerCase() + ".arg.debug.startup.enabled"),
	ARG_DEBUG_STARTUP_GET(TEMPLATE.getPluginName().toLowerCase() + ".arg.debug.startup.get"),
	ARG_DEBUG_STARTUP_LIST(TEMPLATE.getPluginName().toLowerCase() + ".arg.debug.startup.list"),
	ARG_DEBUG_STARTUP_PRINT(TEMPLATE.getPluginName().toLowerCase() + ".arg.debug.startup.print"),
	ARG_DEBUG_TIMINGS(TEMPLATE.getPluginName().toLowerCase() + ".arg.debug.timings"),
	ARG_DEBUG_TIMINGS_ENABLED(TEMPLATE.getPluginName().toLowerCase() + ".arg.debug.timings.enabled"),
	ARG_DEBUG_TIMINGS_GET(TEMPLATE.getPluginName().toLowerCase() + ".arg.debug.timings.get"),
	ARG_DEBUG_TIMINGS_LIST(TEMPLATE.getPluginName().toLowerCase() + ".arg.debug.timings.list"),

	ARG_HELP(TEMPLATE.getPluginName().toLowerCase() + ".arg.help"),

	ARG_INFO(TEMPLATE.getPluginName().toLowerCase() + ".arg.info"),

	ARG_LANG(TEMPLATE.getPluginName().toLowerCase() + ".arg.lang"),
	ARG_LANG_EDIT(TEMPLATE.getPluginName().toLowerCase() + ".arg.lang.edit"),
	ARG_LANG_GET(TEMPLATE.getPluginName().toLowerCase() + ".arg.lang.get"),
	ARG_LANG_INFO(TEMPLATE.getPluginName().toLowerCase() + ".arg.lang.info"),
	ARG_LANG_LIST(TEMPLATE.getPluginName().toLowerCase() + ".arg.lang.list"),
	ARG_LANG_NEW(TEMPLATE.getPluginName().toLowerCase() + ".arg.lang.new"),
	ARG_LANG_RL(TEMPLATE.getPluginName().toLowerCase() + ".arg.lang.rl"),
	ARG_LANG_RESET(TEMPLATE.getPluginName().toLowerCase() + ".arg.lang.reset"),
	ARG_LANG_SET(TEMPLATE.getPluginName().toLowerCase() + ".arg.lang.set"),
	ARG_LANG_TMP(TEMPLATE.getPluginName().toLowerCase() + ".arg.lang.tmp"),
	ARG_LANG_TMP_CLEAR(TEMPLATE.getPluginName().toLowerCase() + ".arg.lang.tmp.clear"),
	ARG_LANG_TMP_LIST(TEMPLATE.getPluginName().toLowerCase() + ".arg.lang.tmp.list"),
	ARG_LANG_TMP_REMOVE(TEMPLATE.getPluginName().toLowerCase() + ".arg.lang.tmp.remove"),
	ARG_LANG_TMP_SAVE(TEMPLATE.getPluginName().toLowerCase() + ".arg.lang.tmp.save"),
	ARG_LANG_TREE(TEMPLATE.getPluginName().toLowerCase() + ".arg.lang.tree"),

	ARG_NEWS(TEMPLATE.getPluginName().toLowerCase() + ".arg.news"),

	ARG_STOP(TEMPLATE.getPluginName().toLowerCase() + ".arg.stop"),

	ARG_UF(TEMPLATE.getPluginName().toLowerCase() + ".arg.uf"),
	ARG_UF_EDIT(TEMPLATE.getPluginName().toLowerCase() + ".arg.uf.edit"),
	ARG_UF_GET(TEMPLATE.getPluginName().toLowerCase() + ".arg.uf.get"),
	ARG_UF_RESET(TEMPLATE.getPluginName().toLowerCase() + ".arg.uf.reset"),
	ARG_UF_TMP(TEMPLATE.getPluginName().toLowerCase() + ".arg.uf.tmp"),
	ARG_UF_TMP_CLEAR(TEMPLATE.getPluginName().toLowerCase() + ".arg.uf.tmp.clear"),
	ARG_UF_TMP_LIST(TEMPLATE.getPluginName().toLowerCase() + ".arg.uf.tmp.list"),
	ARG_UF_TMP_REMOVE(TEMPLATE.getPluginName().toLowerCase() + ".arg.uf.tmp.remove"),
	ARG_UF_TMP_SAVE(TEMPLATE.getPluginName().toLowerCase() + ".arg.uf.tmp.save"),
	ARG_UF_TREE(TEMPLATE.getPluginName().toLowerCase() + ".arg.uf.tree"),

	NO_COMMAND_DELAY(TEMPLATE.getPluginName().toLowerCase() + ".nodelay.TEMPLATE"),

	UPDATES_AT_JOIN(Config.getConfigSettings().get(ConfigSettings.SEARCH_UPDATES_AT_JOIN) + ""),
	UPDATES_WHEN_FOUND(Config.getConfigSettings().get(ConfigSettings.SEARCH_UPDATES_WHEN_FOUND) + "");

	private final String name;

	PermissionNames(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}
}

package net.haffel.PL.utils;

import net.haffel.PL.files.TranslationPaths;

/**
 * Template class
 * 
 * @since v1.1
 * @lastEdit v1.1
 */
public enum DebugTiming
{
	PLUGIN_START(TranslationPaths.DEBUG_TIMINGS_PLUGIN_START),
	COMMAND_HANDLER(TranslationPaths.DEBUG_TIMINGS_COMMAND_HANDLER),
	EVENT_MANAGER(TranslationPaths.DEBUG_TIMINGS_EVENT_MANAGER),
	CONFIG(TranslationPaths.DEBUG_TIMINGS_CONFIG),
	LANGUAGE_SETTINGS(TranslationPaths.DEBUG_TIMINGS_LANGUAGE_SETTINGS),
	CHANGELOG_MANAGER(TranslationPaths.DEBUG_TIMINGS_CHANGELOG_MANAGER),
	PERMISSION_HANDLER(TranslationPaths.DEBUG_TIMINGS_PERMISSION_HANDLER),
	UPDATE_CHECKER(TranslationPaths.DEBUG_TIMINGS_UPDATE_CHECKER);

	private final TranslationPaths description;

	DebugTiming(TranslationPaths description)
	{
		this.description = description;
	}

	public TranslationPaths getDescription()
	{
		return description;
	}
}

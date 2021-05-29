package net.haffel.PL.files;

/**
 * Template class
 * 
 * @since v1.1
 * @lastEdit v1.1
 */
public enum ConfigSettings
{
	VERSION("Version"),
	LANGUAGE("Language"),
	DEBUG_AT_START_DETAILED("Debug.AtStart.Detailed"),
	DEBUG_AT_START_CONFIG("Debug.AtStart.Config"),
	DEBUG_AT_START_LANGUAGE("Debug.AtStart.Language"),
	DEBUG_AT_START_PERMISSIONS("Debug.AtStart.Permissions"),
	DEBUG_AT_START_COMMANDS("Debug.AtStart.Commands"),
	DEBUG_AT_START_EVENTS("Debug.AtStart.Events"),
	DEBUG_AT_START_CHANGES("Debug.AtStart.Changes"),
	DEBUG_AT_START_UPDATER("Debug.AtStart.Updater"),
	DEBUG_TIMINGS("Debug.Timings"),
	DEBUG_STARTUP("Debug.Startup"),
	DEBUG_RUNNING("Debug.Running"),
	LANGUAGES_CUSTOM_LANGUAGES("Languages.CustomLanguages"),
	LANGUAGES_PER_PLAYER_LANGUAGE("Languages.PerPlayerLanguage"),
	LANGUAGES_USER_FORMATS("Languages.UserFormats"),
	LANGUAGES_INTERNAL_PLACEHOLDERS("Languages.InternalPlaceholders"),
	LANGUAGES_PAPI("Languages.PlaceholderAPI"),
	SEARCH_UPDATES("SearchUpdates.Enabled"),
	SEARCH_UPDATES_WHILE_RUNNING("SearchUpdates.WhileRunning"),
	SEARCH_UPDATES_AT_JOIN("SearchUpdates.Notify.AtJoin"),
	SEARCH_UPDATES_WHEN_FOUND("SearchUpdates.Notify.WhenFound"),
	SHOW_VALUES_AS_TREE_PLAYER_SPACE("ShowValuesAsTree.Players.Space"),
	SHOW_VALUES_AS_TREE_PLAYER_SPACE_FOR_LAST("ShowValuesAsTree.Players.SpaceForLast"),
	SHOW_VALUES_AS_TREE_PLAYER_SUBTREE("ShowValuesAsTree.Players.Subtree"),
	SHOW_VALUES_AS_TREE_PLAYER_LAST_SUBTREE("ShowValuesAsTree.Players.LastSubtree"),
	SHOW_VALUES_AS_TREE_CONSOLE_SPACE("ShowValuesAsTree.Console.Space"),
	SHOW_VALUES_AS_TREE_CONSOLE_SPACE_FOR_LAST("ShowValuesAsTree.Console.SpaceForLast"),
	SHOW_VALUES_AS_TREE_CONSOLE_SUBTREE("ShowValuesAsTree.Console.Subtree"),
	SHOW_VALUES_AS_TREE_CONSOLE_LAST_SUBTREE("ShowValuesAsTree.Console.LastSubtree"),
	COMMANDS_DELAY("Commands.Delay"),
	COMMANDS_CONFIRM_TIME("Commands.ConfirmTime"),
	HELP_MAX_LENGTH("Help.MaxLength");

	private final String path;

	ConfigSettings(String path)
	{
		this.path = path;
	}

	public String getPath()
	{
		return path;
	}
}

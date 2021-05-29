package net.haffel.PL.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.bukkit.command.CommandSender;

import lombok.Getter;
import net.haffel.PL.NoneTranslatedMessages;
import net.haffel.PL.TEMPLATE;
import net.haffel.PL.files.Config;
import net.haffel.PL.files.ConfigSettings;
import net.haffel.PL.files.TranslationPaths;
import net.haffel.ml.exceptions.TranslationNotFoundException;

/**
 * Template class
 * 
 * @since v1.1
 * @lastEdit v1.1
 */
public class Debug
{
	@Getter private static LinkedHashMap<DebugTiming, Float> timings = new LinkedHashMap<>();
	@Getter private static LinkedHashMap<Long, Exception> startup = new LinkedHashMap<>();
	@Getter private static LinkedHashMap<Long, Exception> running = new LinkedHashMap<>();
	@Getter private static List<CommandSender> notify = new ArrayList<>();

	public static void addTiming(DebugTiming timing, float duration)
	{
		if((boolean) Config.getConfigSettings().get(ConfigSettings.DEBUG_TIMINGS))
		{
			getTimings().put(timing, duration);

			for(CommandSender notify : getNotify())
			{
				try
				{
					notify.sendMessage(TEMPLATE.getTranslatedString(notify, TranslationPaths.DEBUG_TIMINGS_NOTIFY.getPath()));

				} catch(TranslationNotFoundException e)
				{
					notify.sendMessage(NoneTranslatedMessages.REPORT_TO_ADMIN.getMessage());

					e.printStackTrace();
				}
			}
		}
	}

	public static String getListOfTimings()
	{
		try
		{
			StringBuilder sb = new StringBuilder();

			for(DebugTiming timing : getTimings().keySet())
			{
				String[] extraArgs =
				{ TEMPLATE.getTranslatedString(timing.getDescription().getPath()), Float.toString(getTimings().get(timing)) };

				sb.append(TEMPLATE.getTranslatedString(null, TranslationPaths.DEBUG_TIMINGS_LIST_ENTRY.getPath(), new String[0], extraArgs));
			}

			return sb.toString();
		} catch(TranslationNotFoundException e)
		{
			int index = Debug.addRunning(System.currentTimeMillis(), e);

			return NoneTranslatedMessages.REPORT_TO_ADMIN_WITH_INDEX.getMessage().replace("{INDEX}", Integer.toString(index));
		}
	}

	public static float getTiming(DebugTiming timing)
	{
		if(getTimings().containsKey(timing))
		{
			return getTimings().get(timing);
		}

		return -1L;
	}

	public static int addStartup(long time, Exception e)
	{
		if((boolean) Config.getConfigSettings().get(ConfigSettings.DEBUG_STARTUP))
		{
			getStartup().put(time, e);
			
			for(CommandSender notify : getNotify())
			{
				try
				{
					notify.sendMessage(TEMPLATE.getTranslatedString(notify, TranslationPaths.DEBUG_STARTUP_NOTIFY.getPath()));

				} catch(TranslationNotFoundException e1)
				{
					notify.sendMessage(NoneTranslatedMessages.REPORT_TO_ADMIN.getMessage());

					e1.printStackTrace();
				}
			}

			return getRunning().size() - 1;
		} else
		{
			e.printStackTrace();
		}

		return -1;
	}

	public static String getListOfStartups()
	{
		try
		{
			StringBuilder sb = new StringBuilder();
			int i = 0;

			for(long time : getStartup().keySet())
			{
				SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
				Date date = new Date(time);

				String[] extraArgs =
				{ Integer.toString(i), formatter.format(date), getStartup().get(time).getClass().getSimpleName(),
						getStartup().get(time).getMessage() };

				sb.append(TEMPLATE.getTranslatedString(null, TranslationPaths.DEBUG_STARTUP_LIST_ENTRY.getPath(), new String[0], extraArgs));

				i++;
			}

			return sb.toString();

		} catch(TranslationNotFoundException e)
		{
			int index = Debug.addRunning(System.currentTimeMillis(), e);

			return NoneTranslatedMessages.REPORT_TO_ADMIN_WITH_INDEX.getMessage().replace("{INDEX}", Integer.toString(index));
		}
	}

	public static String getStartupByIndex(int startupIndex)
	{
		try
		{
			StringBuilder sb = new StringBuilder();
			int i = 0;

			for(long time : getStartup().keySet())
			{
				if(startupIndex == i)
				{
					SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
					Date date = new Date(time);

					String[] extraArgs =
					{ Integer.toString(i), formatter.format(date), getStartup().get(time).getClass().getSimpleName(),
							getStartup().get(time).getMessage() };

					sb.append(TEMPLATE.getTranslatedString(null, TranslationPaths.DEBUG_STARTUP_LIST_ENTRY.getPath(), new String[0], extraArgs));
					
					break;
				}
				
				i++;
			}

			return sb.toString();

		} catch(TranslationNotFoundException e)
		{
			int index = Debug.addRunning(System.currentTimeMillis(), e);

			return NoneTranslatedMessages.REPORT_TO_ADMIN_WITH_INDEX.getMessage().replace("{INDEX}", Integer.toString(index));
		}
	}
	
	public static String printStartup(int startupIndex, boolean toConsole)
	{
		int i = 0;

		for(Exception e : getStartup().values())
		{
			if(startupIndex == i)
			{
				if(toConsole)
				{
					e.printStackTrace();
				}
				
				return ExceptionUtils.getStackTrace(e);
			}
			
			i++;
		}
		
		return "";
	}

	public static int addRunning(long time, Exception e)
	{
		if((boolean) Config.getConfigSettings().get(ConfigSettings.DEBUG_RUNNING))
		{
			getRunning().put(time, e);

			for(CommandSender notify : getNotify())
			{
				try
				{
					notify.sendMessage(TEMPLATE.getTranslatedString(notify, TranslationPaths.DEBUG_RUNNING_NOTIFY.getPath()));

				} catch(TranslationNotFoundException e1)
				{
					notify.sendMessage(NoneTranslatedMessages.REPORT_TO_ADMIN.getMessage());

					e1.printStackTrace();
				}
			}

			return getRunning().size() - 1;
		} else
		{
			e.printStackTrace();
		}

		return -1;
	}

	public static String getListOfRunnings()
	{
		try
		{
			StringBuilder sb = new StringBuilder();
			int i = 0;

			for(long time : getRunning().keySet())
			{
				SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
				Date date = new Date(time);

				String[] extraArgs =
				{ Integer.toString(i), formatter.format(date), getRunning().get(time).getClass().getSimpleName(),
						getRunning().get(time).getMessage() };

				sb.append(TEMPLATE.getTranslatedString(null, TranslationPaths.DEBUG_RUNNING_LIST_ENTRY.getPath(), new String[0], extraArgs));

				i++;
			}

			return sb.toString();

		} catch(TranslationNotFoundException e)
		{
			int index = Debug.addRunning(System.currentTimeMillis(), e);

			return NoneTranslatedMessages.REPORT_TO_ADMIN_WITH_INDEX.getMessage().replace("{INDEX}", Integer.toString(index));
		}
	}
	
	public static String getRunningByIndex(int runningIndex)
	{
		try
		{
			StringBuilder sb = new StringBuilder();
			int i = 0;

			for(long time : getRunning().keySet())
			{
				if(runningIndex == i)
				{
					SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
					Date date = new Date(time);

					String[] extraArgs =
					{ Integer.toString(i), formatter.format(date), getRunning().get(time).getClass().getSimpleName(),
						getRunning().get(time).getMessage() };

					sb.append(TEMPLATE.getTranslatedString(null, TranslationPaths.DEBUG_STARTUP_LIST_ENTRY.getPath(), new String[0], extraArgs));
					
					break;
				}
				
				i++;
			}

			return sb.toString();

		} catch(TranslationNotFoundException e)
		{
			int index = Debug.addRunning(System.currentTimeMillis(), e);

			return NoneTranslatedMessages.REPORT_TO_ADMIN_WITH_INDEX.getMessage().replace("{INDEX}", Integer.toString(index));
		}
	}

	public static String printRunning(int runningIndex, boolean toConsole)
	{
		int i = 0;

		for(Exception e : getRunning().values())
		{
			if(runningIndex == i)
			{
				if(toConsole)
				{
					e.printStackTrace();
				}
				
				return ExceptionUtils.getStackTrace(e);
			}
			
			i++;
		}
		
		return "";
	}
	
	public static void addNotify(CommandSender sender)
	{
		getNotify().add(sender);
	}

	public static void removeNotify(CommandSender sender)
	{
		if(getNotify().contains(sender))
		{
			getNotify().remove(sender);
		}
	}
}

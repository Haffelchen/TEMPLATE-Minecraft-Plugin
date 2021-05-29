package net.haffel.PL.args.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import lombok.Getter;
import net.haffel.PL.NoneTranslatedMessages;
import net.haffel.PL.TEMPLATE;
import net.haffel.PL.files.Config;
import net.haffel.PL.files.ConfigSettings;
import net.haffel.PL.files.TranslationPaths;
import net.haffel.PL.utils.Debug;
import net.haffel.ml.exceptions.TranslationNotFoundException;

/**
 * Template class
 * 
 * @since v1.1
 * @lastEdit v1.1
 */
public class Confirm
{
	// List of outstanding confirmations
	@Getter public static HashMap<CommandSender, Class<?>> outstanding = new HashMap<>();
	// List of remaining time of outstanding confirmations
	@Getter public static HashMap<CommandSender, Long> confirmTime = new HashMap<>();
	// Confirmation timer
	public static int timer;

	/**
	 * @param sender
	 *            The command sender who needs to confirm something
	 * @param confirmed
	 *            The class to invoke the @ConfirmedCmd method
	 * @param text
	 *            This text will be sent to the command sender
	 */
	public static void requestConfirmation(CommandSender sender, Class<?> confirmed, String[] text)
	{
		sender.sendMessage(text);
		getOutstanding().put(sender, confirmed);
		getConfirmTime().put(sender, System.currentTimeMillis() + (int) Config.getConfigSettings().get(ConfigSettings.COMMANDS_CONFIRM_TIME));
	}

	/**
	 * @param sender
	 *            The command sender who confirmed something
	 */
	@SuppressWarnings("deprecation")
	public static void confirm(CommandSender sender)
	{
		if(getOutstanding().containsKey(sender))
		{
			for(Method m : getOutstanding().get(sender).getDeclaredMethods())
			{
				if(m.isAnnotationPresent(ConfirmedCmd.class))
				{
					try
					{
						m.invoke(getOutstanding().get(sender).newInstance());

					} catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException e)
					{
						int index = Debug.addRunning(System.currentTimeMillis(), e);

						sender.sendMessage(
								NoneTranslatedMessages.REPORT_TO_ADMIN_WITH_INDEX.getMessage().replace("{INDEX}", Integer.toString(index)));
					}
				}
			}

			getOutstanding().remove(sender);
			getConfirmTime().remove(sender);

		} else
		{
			try
			{
				sender.sendMessage(TEMPLATE.getTranslatedStringSplitted(sender, TranslationPaths.ARG_CONFIRM_NOTHING.getPath()));

			} catch(TranslationNotFoundException e)
			{
				int index = Debug.addRunning(System.currentTimeMillis(), e);
				
				sender.sendMessage(NoneTranslatedMessages.REPORT_TO_ADMIN_WITH_INDEX.getMessage().replace("{INDEX}", Integer.toString(index)));
			}
		}
	}

	/**
	 * @param sender
	 *            The CommandSender who didnt confirmed at the given time
	 */
	public static void timeIsOver(CommandSender sender)
	{
		try
		{
			sender.sendMessage(TEMPLATE.getTranslatedStringSplitted(sender, TranslationPaths.ARG_CONFIRM_TIME_IS_OVER.getPath()));

		} catch(TranslationNotFoundException e)
		{
			Debug.addRunning(System.currentTimeMillis(), e);
		}
	}

	/**
	 * Confirmation timer
	 */
	public static void confirmTimer()
	{
		timer = Bukkit.getScheduler().scheduleSyncRepeatingTask(TEMPLATE.getInstance(), new Runnable()
		{
			@Override
			public void run()
			{
				Iterator<CommandSender> iterate = getConfirmTime().keySet().iterator();

				while(iterate.hasNext())
				{
					CommandSender p = iterate.next();
					long time = getConfirmTime().get(p);

					if(time <= System.currentTimeMillis())
					{
						timeIsOver(p);
						getOutstanding().remove(p);
						iterate.remove();
					}
				}
			}
		}, 0L, 20L);
	}
}

package net.haffel.PL.tree;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import net.haffel.PL.TEMPLATE;
import net.haffel.PL.files.TranslationPaths;
import net.haffel.ml.exceptions.TranslationNotFoundException;

/**
 * Template class
 * 
 * @since v1.1
 * @lastEdit v1.1
 */
public class Tree
{
	/**
	 * Info: Minecraft deletes normal multiple spaces. I use 'non-breaking space U+00A0' from this
	 * website: https://www.brunildo.org/test/space-chars.html
	 * 
	 * @param config
	 *            Any FileConfiguration
	 * @return All Keys of a FileConfiguration listed
	 * @throws TranslationNotFoundException
	 *             For the given translation key was no translation found
	 * @throws UserFormatConfigDoesntExistException
	 *             The user formats config for the sender doesnt exist
	 * @throws SomeTranslationException
	 *             Any other error
	 */
	public static String getTree(FileConfiguration config, String showSubtree, String showSpace, String showLastSubtree, String showLastSpace)
			throws TranslationNotFoundException
	{
		StringBuilder tree = new StringBuilder();
		Iterator<String> keys = config.getKeys(false).iterator();

		while(keys.hasNext())
		{
			String key = keys.next();

			if(config.isConfigurationSection(key))
			{
				if(keys.hasNext())
				{
					tree.append(showSubtree);
					tree.append(getTree(config.getConfigurationSection(key), showSubtree, showSpace, showLastSubtree, showLastSpace)
							.replace("%break%", "%break%" + showSpace));
					tree.append("%break%");

				} else
				{
					tree.append(showLastSubtree);
					tree.append(getTree(config.getConfigurationSection(key), showSubtree, showSpace, showLastSubtree, showLastSpace)
							.replace("%break%", "%break%" + showLastSpace));
				}
			} else
			{
				if(keys.hasNext())
				{
					tree.append(showSubtree);
					tree.append(key);
					tree.append("%break%");

				} else
				{
					tree.append(showLastSubtree);
					tree.append(key);
				}
			}
		}

		if(tree.length() == 0)
		{
			return TEMPLATE.getTranslatedString(TranslationPaths.TREE_EMPTY.getPath());

		} else
		{
			return tree.toString();
		}
	}

	/**
	 * @param cs
	 *            Any FileConfiguration section
	 * @return All Keys of a section listed
	 * @throws TranslationNotFoundException
	 *             For the given translation key was no translation found
	 * @throws UserFormatConfigDoesntExistException
	 *             The user formats config for the sender doesnt exist
	 * @throws SomeTranslationException
	 *             Any other error
	 */
	public static String getTree(ConfigurationSection cs, String showSubtree, String showSpace, String showLastSubtree, String showLastSpace)
			throws TranslationNotFoundException
	{
		if(cs != null)
		{
			TreeNode<String> csTree = new TreeNode<String>(cs.getName());
			recursiveGet(cs, csTree);

			return renderTree(csTree, showSubtree, showSpace, showLastSubtree, showLastSpace);
		}

		return TEMPLATE.getTranslatedString(TranslationPaths.TREE_EMPTY.getPath());
	}

	public static void recursiveGet(ConfigurationSection cs, TreeNode<String> tree)
	{
		for(String key : cs.getKeys(false))
		{
			if(cs.isConfigurationSection(key))
			{
				TreeNode<String> subTree = tree.addChild(key);
				recursiveGet(cs.getConfigurationSection(key), subTree);

			} else
			{
				tree.addChild(key);
			}
		}
	}

	public static String renderTree(TreeNode<String> tree, String showSubtree, String showSpace, String showLastSubtree, String showLastSpace)
	{
		List<StringBuilder> lines = renderTreeLines(tree, showSubtree, showSpace, showLastSubtree, showLastSpace);
		String newline = "%break%";
		StringBuilder sbTree = new StringBuilder();

		for(int i = 0; i < lines.size(); i++)
		{
			StringBuilder line = lines.get(i);

			sbTree.append(line);

			if(i < lines.size() - 1)
			{
				sbTree.append(newline);
			}
		}

		return sbTree.toString();
	}

	public static List<StringBuilder> renderTreeLines(TreeNode<String> tree, String showSubtree, String showSpace, String showLastSubtree,
			String showLastSpace)
	{
		List<StringBuilder> result = new LinkedList<>();
		Iterator<TreeNode<String>> iterator = tree.children.iterator();

		result.add(new StringBuilder().append(tree.data));

		while(iterator.hasNext())
		{
			List<StringBuilder> subtree = renderTreeLines(iterator.next(), showSubtree, showSpace, showLastSubtree, showLastSpace);

			if(iterator.hasNext())
			{
				addSubtree(result, subtree, showSubtree, showSpace);

			} else
			{
				addLastSubtree(result, subtree, showLastSubtree, showLastSpace);
			}
		}

		return result;
	}

	private static void addSubtree(List<StringBuilder> result, List<StringBuilder> subtree, String showSubtree, String showSpace)
	{
		Iterator<StringBuilder> iterator = subtree.iterator();
		result.add(iterator.next().insert(0, showSubtree));

		while(iterator.hasNext())
		{
			result.add(iterator.next().insert(0, showSpace));
		}
	}

	private static void addLastSubtree(List<StringBuilder> result, List<StringBuilder> subtree, String showLastSubtree, String showLastSpace)
	{
		Iterator<StringBuilder> iterator = subtree.iterator();
		result.add(iterator.next().insert(0, showLastSubtree));

		while(iterator.hasNext())
		{
			result.add(iterator.next().insert(0, showLastSpace));
		}
	}
}

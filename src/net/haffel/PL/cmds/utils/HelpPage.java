package net.haffel.PL.cmds.utils;

import lombok.Getter;

public class HelpPage
{
	@Getter int page;
	@Getter String path;
	@Getter String content;

	public HelpPage(int page, String path, String content)
	{
		this.page = page;
		this.path = path;
		this.content = content;
	}
}

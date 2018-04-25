package com.matha.util;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogUtil
{

	private static final Logger LOGGER = LogManager.getLogger(LogUtil.class);
	
	public static <T> void logListDebug(List<T> tList)
	{
		for (T t : tList)
		{
			LOGGER.debug(t);
		}
	}
}

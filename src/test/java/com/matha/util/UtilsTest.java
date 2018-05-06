package com.matha.util;

import static org.junit.Assert.*;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.spi.LoggerContext;
import org.junit.Test;

public class UtilsTest
{	
	
	@Test
	public void testConvertDouble()
	{
		Configurator.setRootLevel(Level.DEBUG);
		
		double dbl = 19600.80;
		System.out.println(Utils.convertDouble(dbl));

	}

	@Test
	public void testInt()
	{
		int intVal = Integer.MAX_VALUE/ 10;
		System.out.println(intVal);
	}

}

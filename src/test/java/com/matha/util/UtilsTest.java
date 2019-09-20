package com.matha.util;

import static org.junit.Assert.*;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.spi.LoggerContext;
import org.junit.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UtilsTest
{

	@Test
	public void testConvertDouble()
	{
		Stream<String> cats = Stream.of("leopard","lynx","ocelot","puma").parallel();
		Stream<String> bears = Stream.of("panda","grizzly","polar").parallel();
		ConcurrentMap<Boolean, List<String>> data = Stream.of(cats,bears)
		.flatMap(s -> s)
		.collect(Collectors.groupingByConcurrent(s -> !s.startsWith("p")));
		System.out.println(data.get(false).size()+" "+data.get(true).size());
	}

	@Test
	public void testInt()
	{
		int intVal = Integer.MAX_VALUE/ 10;
		System.out.println(intVal);

		String date = LocalDate
				.parse("2014-05-04")
				.format(DateTimeFormatter.ISO_DATE_TIME) ;
		System.out.println(date);

	}

}

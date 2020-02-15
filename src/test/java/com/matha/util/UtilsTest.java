package com.matha.util;

import static org.junit.Assert.*;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.spi.LoggerContext;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class UtilsTest
{

	@Test
	public void testConvertDouble()
	{
		LocalDateTime dt = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
		System.out.println(dt);

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

	public static void main(String[] args) throws IOException
	{

		BufferedReader nStr = new BufferedReader((new InputStreamReader(System.in)));
		String fLine = nStr.readLine();
		String[] fVals = fLine.split(" ");
		int lineCnt = Integer.parseInt(fVals[0]);
		int divisor = Integer.parseInt(fVals[1]);

		long res = IntStream.range(0, lineCnt).filter(i -> {
			try
			{
				int currLine = Integer.parseInt(nStr.readLine());
				return currLine % divisor == 0;
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			return false;
		}).count();
		System.out.println(res);
	}

}

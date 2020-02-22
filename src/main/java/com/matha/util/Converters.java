package com.matha.util;

import static com.matha.util.UtilConstants.DATE_FORMAT;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.matha.domain.*;

import javafx.util.StringConverter;
import javafx.util.converter.LocalDateStringConverter;
import javafx.util.converter.LocalDateTimeStringConverter;

public class Converters
{

	public static StringConverter<State> getStateConverter()
	{
		StringConverter<State> c = new StringConverter<State>() {

			@Override
			public String toString(State state)
			{

				return state.getId();
			}

			@Override
			public State fromString(String string)
			{
				// TODO Auto-generated method stub
				return null;
			}
		};
		return c;
	}

	public static StringConverter<District> getDistrictConverter()
	{
		StringConverter<District> c = new StringConverter<District>() {

			@Override
			public String toString(District state)
			{

				return state.getId();
			}

			@Override
			public District fromString(String string)
			{
				// TODO Auto-generated method stub
				return null;
			}
		};
		return c;
	}

	public static StringConverter<BookCategory> getCategoryConverter()
	{
		StringConverter<BookCategory> c = new StringConverter<BookCategory>() {

			@Override
			public String toString(BookCategory state)
			{

				return state.getName();
			}

			@Override
			public BookCategory fromString(String string)
			{
				// TODO Auto-generated method stub
				return null;
			}
		};
		return c;
	}

	public static StringConverter<Publisher> getPublisherConverter()
	{
		StringConverter<Publisher> c = new StringConverter<Publisher>() {

			@Override
			public String toString(Publisher state)
			{

				return state.getName();
			}

			@Override
			public Publisher fromString(String string)
			{
				// TODO Auto-generated method stub
				return null;
			}
		};
		return c;
	}

	public static StringConverter<CashHead> getCashHeadConverter()
	{
		StringConverter<CashHead> c = new StringConverter<CashHead>() {

			@Override
			public String toString(CashHead state)
			{

				return state.getCashHeadName();
			}

			@Override
			public CashHead fromString(String string)
			{
				// TODO Auto-generated method stub
				return null;
			}
		};
		return c;
	}

	public static StringConverter<LocalDate> getLocalDateConverter()
	{
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern(DATE_FORMAT);
		StringConverter<LocalDate> dateConv = new LocalDateStringConverter(fmt, fmt);
		return dateConv;
	}
	
	public static StringConverter<LocalDateTime> getLocalDateTimeConverter()
	{
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern(DATE_FORMAT);
		StringConverter<LocalDateTime> dateConv = new LocalDateTimeStringConverter(fmt, fmt);
		return dateConv;
	}

	public static StringConverter<Integer> getIntegerConverter()
	{
		StringConverter<Integer> c = new StringConverter<Integer>() {

			@Override
			public String toString(Integer state)
			{

				return state == null ? null : state.toString();
			}

			@Override
			public Integer fromString(String str)
			{			
				return Integer.parseInt(str);
			}
		};
		return c;
	}
	
}

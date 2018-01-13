package com.matha.util;

import com.matha.domain.BookCategory;
import com.matha.domain.District;
import com.matha.domain.Publisher;
import com.matha.domain.State;

import javafx.util.StringConverter;

public class Converters {

	public static StringConverter<State> getStateConverter() {
		StringConverter<State> c = new StringConverter<State>() {

			@Override
			public String toString(State state) {

				return state.getId();
			}

			@Override
			public State fromString(String string) {
				// TODO Auto-generated method stub
				return null;
			}
		};
		return c;
	}

	public static StringConverter<District> getDistrictConverter() {
		StringConverter<District> c = new StringConverter<District>() {

			@Override
			public String toString(District state) {

				return state.getId();
			}

			@Override
			public District fromString(String string) {
				// TODO Auto-generated method stub
				return null;
			}
		};
		return c;
	}

	public static StringConverter<BookCategory> getCategoryConverter() {
		StringConverter<BookCategory> c = new StringConverter<BookCategory>() {

			@Override
			public String toString(BookCategory state) {

				return state.getName();
			}

			@Override
			public BookCategory fromString(String string) {
				// TODO Auto-generated method stub
				return null;
			}
		};
		return c;
	}

	public static StringConverter<Publisher> getPublisherConverter() {
		StringConverter<Publisher> c = new StringConverter<Publisher>() {

			@Override
			public String toString(Publisher state) {

				return state.getName();
			}

			@Override
			public Publisher fromString(String string) {
				// TODO Auto-generated method stub
				return null;
			}
		};
		return c;
	}
}

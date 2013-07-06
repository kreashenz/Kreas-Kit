package me.kreashenz.kitpvp.horse;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public enum HorseVariant {

	/*
	 * Thanks to
	 * @author DarkBladee12 for doing this!
	 */

	WHITE("white", 0),
	CREAMY("creamy", 1),
	CHESTNUT("chestnut", 2),
	BROWN("brown", 3),
	BLACK("black", 4),
	GRAY("gray", 5),
	DARK_BROWN("dark brown", 6),
	INVISIBLE("invisible", 7),
	WHITE_WHITE("white-white", 256),
	CREAMY_WHITE("creamy-white", 257),
	CHESTNUT_WHITE("chestnut-white", 258),
	BROWN_WHITE("brown-white", 259),
	BLACK_WHITE("black-white", 260),
	GRAY_WHITE("gray-white", 261),
	DARK_BROWN_WHITE("dark brown-white", 262),
	WHITE_WHITE_FIELD("white-white field", 512),
	CREAMY_WHITE_FIELD("creamy-white field", 513),
	CHESTNUT_WHITE_FIELD("chestnut-white field", 514),
	BROWN_WHITE_FIELD("brown-white field", 515),
	BLACK_WHITE_FIELD("black-white field", 516),
	GRAY_WHITE_FIELD("gray-white field", 517),
	DARK_BROWN_WHITE_FIELD("dark brown-white field", 518),
	WHITE_WHITE_DOTS("white-white dots", 768),
	CREAMY_WHITE_DOTS("creamy-white dots", 769),
	CHESTNUT_WHITE_DOTS("chestnut-white dots", 770),
	BROWN_WHITE_DOTS("brown-white dots", 771),
	BLACK_WHITE_DOTS("black-white dots", 772),
	GRAY_WHITE_DOTS("gray-white dots", 773),
	DARK_BROWN_WHITE_DOTS("dark brown-white dots", 774),
	WHITE_BLACK_DOTS("white-black dots", 1024),
	CREAMY_BLACK_DOTS("creamy-black dots", 1025),
	CHESTNUT_BLACK_DOTS("chestnut-black dots", 1026),
	BROWN_BLACK_DOTS("brown-black dots", 1027),
	BLACK_BLACK_DOTS("black-black dots", 1028),
	GRAY_BLACK_DOTS("gray-black dots", 1029),
	DARK_BROWN_BLACK_DOTS("dark brown-black dots", 1030);

	private String name;
	private int id;

	HorseVariant(String name, int id) {
		this.name = name;
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}

	private static final Map<String, HorseVariant> NAME_MAP = new HashMap<String, HorseVariant>();
	private static final Map<Integer, HorseVariant> ID_MAP = new HashMap<Integer, HorseVariant>();
	static {
		for (HorseVariant effect : values()) {
			NAME_MAP.put(effect.name, effect);
			ID_MAP.put(effect.id, effect);
		}
	}

	public static HorseVariant fromName(String name) {
		if (name == null) {
			return null;
		}
		for (Entry<String, HorseVariant> e : NAME_MAP.entrySet()) {
			if (e.getKey().equalsIgnoreCase(name)) {
				return e.getValue();
			}
		}
		return null;
	}

	public static HorseVariant fromId(int id) {
		return ID_MAP.get(id);
	}
}
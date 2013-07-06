package me.kreashenz.kitpvp.horse;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public enum HorseType {

	/*
	 * Thanks to
	 * @author DarkBladee12 for doing this!
	 */

	NORMAL("normal", 0), DONKEY("donkey", 1), MULE("mule", 2), UNDEAD("undead", 3), SKELETAL("skeletal", 4);

	private String name;
	private int id;

	HorseType(String name, int id) {
		this.name = name;
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}

	private static final Map<String, HorseType> NAME_MAP = new HashMap<String, HorseType>();
	private static final Map<Integer, HorseType> ID_MAP = new HashMap<Integer, HorseType>();
	static {
		for (HorseType effect : values()) {
			NAME_MAP.put(effect.name, effect);
			ID_MAP.put(effect.id, effect);
		}
	}

	public static HorseType fromName(String name) {
		if (name == null) {
			return null;
		}
		for (Entry<String, HorseType> e : NAME_MAP.entrySet()) {
			if (e.getKey().equalsIgnoreCase(name)) {
				return e.getValue();
			}
		}
		return null;
	}

	public static HorseType fromId(int id) {
		return ID_MAP.get(id);
	}
}
package me.kreashenz.kitpvp.horse;

import java.lang.reflect.Method;

public class NBTUtil {

	/*
	 * Thanks to
	 * @author DarkBladee12 for doing this!
	 */

	public static Object getNBTTagCompound(Object entity) {
		try {
			Object nbtTagCompound = ReflectionUtil.getClass("NBTTagCompound");
			for (Method m : entity.getClass().getMethods()) {
				Class<?>[] pt = m.getParameterTypes();
				if (m.getName().equals("b") && pt.length == 1 && pt[0].getName().contains("NBTTagCompound")) {
					m.invoke(entity, nbtTagCompound);
				}
			}
			return nbtTagCompound;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void updateNBTTagCompound(Object entity, Object nbtTagCompound) {
		try {
			for (Method m : entity.getClass().getMethods()) {
				Class<?>[] pt = m.getParameterTypes();
				if (m.getName().equals("a") && pt.length == 1 && pt[0].getName().contains("NBTTagCompound")) {
					m.invoke(entity, nbtTagCompound);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void setValue(Object nbtTagCompound, String key, Object value) {
		try {
			if (value instanceof Integer) {
				ReflectionUtil.getMethod("setInt", nbtTagCompound.getClass(), 2).invoke(nbtTagCompound, key, (Integer) value);
				return;
			} else if (value instanceof Boolean) {
				ReflectionUtil.getMethod("setBoolean", nbtTagCompound.getClass(), 2).invoke(nbtTagCompound, key, (Boolean) value);
				return;
			} else {
				ReflectionUtil.getMethod("set", nbtTagCompound.getClass(), 2).invoke(nbtTagCompound, key, value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Object getValue(Object nbtTagCompound, Class<?> c, String key) {
		try {
			if (c == Integer.class) {
				return ReflectionUtil.getMethod("getInt", nbtTagCompound.getClass(), 1).invoke(nbtTagCompound, key);
			} else if (c == Boolean.class) {
				return ReflectionUtil.getMethod("getBoolean", nbtTagCompound.getClass(), 1).invoke(nbtTagCompound, key);
			} else {
				return ReflectionUtil.getMethod("getCompound", nbtTagCompound.getClass(), 1).invoke(nbtTagCompound, key);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static boolean hasKey(Object nbtTagCompound, String key) {
		try {
			return (Boolean) ReflectionUtil.getMethod("hasKey", nbtTagCompound.getClass(), 1).invoke(nbtTagCompound, key);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean hasKeys(Object nbtTagCompound, String[] keys) {
		for (String key : keys) {
			if (!hasKey(nbtTagCompound, key)) {
				return false;
			}
		}
		return true;
	}
}

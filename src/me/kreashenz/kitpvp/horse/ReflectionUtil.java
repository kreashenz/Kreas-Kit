package me.kreashenz.kitpvp.horse;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;


public class ReflectionUtil {

	/*
	 * Thanks to
	 * @author DarkBladee12 for doing this!
	 */

	public static Object getClass(String name, Object... args) throws Exception {
		Class<?> c = Class.forName(ReflectionUtil.getPackageName() + "." + name);
		int params = 0;
		if (args != null) {
			params = args.length;
		}
		for (Constructor<?> co : c.getConstructors()) {
			if (co.getParameterTypes().length == params) {
				return co.newInstance(args);
			}
		}
		return null;
	}

	public static Method getMethod(String name, Class<?> c, int params) {
		for (Method m : c.getMethods()) {
			if (m.getName().equals(name) && m.getParameterTypes().length == params) {
				return m;
			}
		}
		return null;
	}

	public static String getPackageName() {
		return "net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
	}
}
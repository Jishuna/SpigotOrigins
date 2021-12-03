package me.jishuna.spigotorigins.api;

public class ParseUtils {

	public static void checkLength(String[] args, int min) throws InvalidOriginException {
		if (args.length < min)
			throw new InvalidOriginException("This effect requires at least " + min + " arguments");
	}

	public static int readInt(String string, String name) throws InvalidOriginException {
		try {
			return Integer.parseInt(string);
		} catch (NumberFormatException ex) {
			throw new InvalidOriginException(
					"Expected a whole number for the value of \"" + name + "\" but found: " + string);
		}
	}

	public static long readLong(String string, String name) throws InvalidOriginException {
		try {
			return Long.parseLong(string);
		} catch (NumberFormatException ex) {
			throw new InvalidOriginException(
					"Expected a whole number for the value of \"" + name + "\" but found: " + string);
		}
	}

	public static double readDouble(String string, String name) throws InvalidOriginException {
		try {
			return Double.parseDouble(string);
		} catch (NumberFormatException ex) {
			throw new InvalidOriginException(
					"Expected a number for the value of \"" + name + "\" but found: " + string);
		}
	}

	public static float readFloat(String string, String name) throws InvalidOriginException {
		try {
			return Float.parseFloat(string);
		} catch (NumberFormatException ex) {
			throw new InvalidOriginException(
					"Expected a number for the value of \"" + name + "\" but found: " + string);
		}
	}
}

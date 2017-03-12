package io.github.successhawk.java7.switchstatement;

public class SwitchStatementWithStringExample {

/**
 * Parses the string argument as a boolean. 
 * For an alternative behavior see {@link Boolean#parseBoolean(String)} 
 * @param value
 * @return boolean 
 * @throws IllegalArgumentException if value is not "true", "yes",
 *  "false", or "no"
 * @see Boolean#parseBoolean(String) 
 */
public static boolean parseBoolean_Java6(String value) {
	if ( value != null ) {
		if ( "true".equals(value) 
				|| "yes".equals(value)) {
			return true;
		} else if ("false".equals(value)
				|| "no".equals(value)) {
			return false;
		}
	}
	throw new IllegalArgumentException("value (" + value + ") is not a boolean.");
}
	
	
/**
 * Parses the string argument as a boolean. 
 * For an alternative behavior see {@link Boolean#parseBoolean(String)} 
 * @param value
 * @return boolean 
 * @throws IllegalArgumentException if value is not "true", "yes",
 *  "false", or "no"
 * @see Boolean#parseBoolean(String) 
 */
public static boolean parseBoolean(String value) {
	if ( value != null ) {
		switch(value) {
		case "true":
		case "yes":
			return true;
		case "false":
		case "no":
			return false;
		}
	}
	throw new IllegalArgumentException("value (" + value + ") is not a boolean.");
}

/**
 * Parses the string argument as a boolean, ignoring case. 
 * @param value
 * @return boolean 
 * @throws IllegalArgumentException if value, ignoring case, is not
 * 	 "true", "yes", "false", or "no"
 * @see Boolean#parseBoolean(String) for an alternative behavior.
 */
public static boolean parseBooleanIgnoreCase(String value) {
	if ( value != null ) {
		switch(value.toLowerCase()) {
		case "true":
		case "yes":
			return true;
		case "false":
		case "no":
			return false;
		}
	}
	throw new IllegalArgumentException("value (" + value + ") is not a boolean.");
}


enum Bool{ TRUE, FALSE };




	public static void main(String[] args) {
		parseBoolean_Java6("no");
	}
}

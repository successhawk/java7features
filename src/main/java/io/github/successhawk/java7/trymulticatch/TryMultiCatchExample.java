package io.github.successhawk.java7.trymulticatch;

import java.io.IOException;

import org.apache.log4j.Logger;

public class TryMultiCatchExample {

	private static final Logger logger = Logger
			.getLogger(TryMultiCatchExample.class);

	public int readPropertyAsInt_Encapsulation_Java6() throws ConfigException {
	try {
		return Integer.parseInt(readProperty());
	} catch (NumberFormatException e) {
		throw new ConfigException("Failed to read property as int", e);
	} catch (IOException e) {
		/* DUPLICATE CODE! */
		throw new ConfigException("Failed to read property as int", e);
	}
}

public int readPropertyAsInt_Encapsulation_Java7() throws ConfigException {
	try {
		return Integer.parseInt(readProperty());
	} catch (NumberFormatException | IOException e) {
		/* Catches expected exceptions. No duplicate code! */
		throw new ConfigException("Failed to read property as int", e);
	}
}

/**
 * 
 * @return
 * @throws IOException
 * @throws NumberFormatException
 */
public int readPropertyAsInt_Deduplicate() throws IOException {
	try {
		return Integer.parseInt(readProperty());
	} catch (Exception e) {
		logger.debug(e.getMessage(), e);
		throw e;  
		/* In Java 6 this would require you to "throws Exception" */
	}
}



	private String readProperty() throws IOException {
		/* Details not shown */
		return "someValue";
	}
	
	public static class ConfigException extends Exception {
		private static final long serialVersionUID = 1L;
		public ConfigException(String msg, Exception e) {
			super(msg,e);
		}
	}
}

package io.github.successhawk.java7.trywithresources;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.helpers.LogLog;

/**
 * This example shows how to automatically ignore (but log) close errors using a generic AutoCloseable handler.
 * 
 * @author nwh02
 *
 */
public class PropertiesLoadExample {
	
	private static final Logger logger = Logger
			.getLogger(PropertiesLoadExample.class);
	private static final String ORANGELOG_PROPERTIES_FILENAME = "C:/opt/isv/tomcat-7.0/grid/conf/logging.properties"; // not the right one, but it doesn't matter for my test. Just has to exist.
	private static final Properties ORANGELOG_PROPERTIES_FILE = new Properties();

	public static void main(String[] args) {
		java6();
		java7();
	}
	
	static void java6() {
		logger.info("running java6 version.");
		InputStream inFileStream = null;
	    try {
	           inFileStream = new FileInputStream(ORANGELOG_PROPERTIES_FILENAME);
	           ORANGELOG_PROPERTIES_FILE.load(inFileStream);
	           LogLog.warn("OrangeLog using properties file " + ORANGELOG_PROPERTIES_FILENAME + " plus default values for properties not found in the file");
	    }
	    catch (IOException ioe) {
	           LogLog.debug("OrangeLog (no cause for alarm, default properties will be used) Couldn't open properties file " + ORANGELOG_PROPERTIES_FILENAME + " " + ioe.toString());
	    }
	    finally {
	           if (inFileStream != null) {
	                  try {
	                        inFileStream.close();
	                  } catch (IOException ioe) {
	                        // not much can be done.
	                        LogLog.error("OrangeLog failed to close properties file " + ORANGELOG_PROPERTIES_FILENAME + " " + ioe.toString());
	                  }
	           }
	    }
		
	}
	
	static void java7() {
		logger.info("running java7 version.");
	    try (IgnoreCloseErrors<FileInputStream> closeable = new IgnoreCloseErrors<>( new FileInputStream(ORANGELOG_PROPERTIES_FILENAME) ) ) {
	           ORANGELOG_PROPERTIES_FILE.load(closeable.get());
	           LogLog.warn("OrangeLog using properties file " + ORANGELOG_PROPERTIES_FILENAME + " plus default values for properties not found in the file");
	    }
	    catch (IOException ioe) {
	           LogLog.debug("OrangeLog (no cause for alarm, default properties will be used) Couldn't open properties file " + ORANGELOG_PROPERTIES_FILENAME + " " + ioe.toString());
	    }
	}

	/** 
	 * Jesse and Cade, this is a first stab at a reuseable class.  I would think about it more before publishing it.
	 * 
	 * Logs any errors that are thrown during closing the given closeable.
	 * WARNING! Do not use this if you are not sure that the given AutoCloseable's close method can be ignored.  
	 * @author nwh02
	 *
	 */
	public static final class IgnoreCloseErrors<C extends AutoCloseable> implements AutoCloseable {
		private static final Logger logger = Logger
				.getLogger(PropertiesLoadExample.class);
		private final Logger log;
		private final Level level;
		private final String errorMessage;
		private final C closeable;
		
		/**
		 * Convenience constructor that logs any exceptions to this class's default logger at the debug level.
		 * @param closeable
		 */
		public IgnoreCloseErrors(C closeable) {
			this(closeable, logger, Level.DEBUG, (String)null);
		}
		
		/**
		 * 
		 * @param logger to log any exception to if it occurs.
		 * @param level to log at.
		 * @param closeable
		 * @param errorMessage if null, then the exception's message will be logged.
		 */
		public IgnoreCloseErrors(C closeable, Logger logger, Level level, String errorMessage) {
			this.closeable = closeable;
			this.log = logger;
			this.level = level;
			this.errorMessage = errorMessage;
		}
		
		/**
		 * Returns the wrapped AutoCloseable.
		 * @return
		 */
		public C get() {
			return closeable;
		}
		
		@Override
		public void close() {
			try {
				closeable.close();
			} catch (Exception e) {
				log.log(level, ( errorMessage == null ? e.getMessage() : errorMessage ), e);
			}
		}
	}
	
}

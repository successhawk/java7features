package io.github.successhawk.java7.numericliterals;

import org.apache.log4j.Logger;

public class BinaryLiteralsExample {
	private static final Logger logger = Logger.getLogger(BinaryLiteralsExample.class);

	public static void main(String[] args){
	    
int decimal = 100;   // 100 in decimal.
int octal = 0144;    // decimal 100 represented in octal base (literal starts with 0)
int hex = 0x64;      // decimal 100 represented in hexadecimal base (literal starts with 0x)
logger.info(decimal); // Prints '100'
logger.info(octal);   // Prints '100'
logger.info(hex);     // Prints '100'

logger.warn("The following binary literals only works in Java 7 or higher.");

/* Java 7+ */
int bin = 0b1100100; // decimal 100 represented in binary base (literal starts with 0b)
logger.info(bin);     // Prints '100'

  byte  bBin  =  0b00000001; logger.info(bBin);  // Prints '1'
//byte  bBin2 =  0b10000000; logger.info(bBin);  // 128 is too large to fit in a byte.
  short sBin  =  0B11111111; logger.info(sBin);  // Prints '255'
  short sBin2 = -0b11111111; logger.info(sBin2); // Prints '-255'
  long  lBin  =  0b01111111_11111111_11111111_11111111_11111111_11111111_11111111_11111111L; 
  logger.info(lBin); 	// Prints Long.MAX_VALUE 9223372036854775807
  
  char mystery = 0B1;	logger.info(mystery);// ????



		long largestBinaryLiteralWithoutOverflow // Long.MAX_VALUE
			= 0b111111111111111111111111111111111111111111111111111111111111111L;
		long smallestBinaryLiteralWithoutOverflow // Long.MIN_VALUE
			= 0b1000000000000000000000000000000000000000000000000000000000000000L;
		long smallestBinaryLiteralWithoutOverflow2 // Long.MIN_VALUE
			= 0b111111111111111111111111111111111111111111111111111111111111111L;

		logger.info(largestBinaryLiteralWithoutOverflow);
	    logger.info(Long.MAX_VALUE);
	    logger.info(Long.toBinaryString(Long.MAX_VALUE));
	    
	    logger.info(smallestBinaryLiteralWithoutOverflow);
	    logger.info(smallestBinaryLiteralWithoutOverflow2);
	    logger.info(Long.MIN_VALUE);
	    logger.info(Long.toBinaryString(Long.MIN_VALUE));
	    
	    
	    float f = 0b01;
	    double d = 0b01;
	    logger.info(f);
	    logger.info(d);
	    // The following doesn't compile as you might think.
	    //float f2 = 0b01f;
	    //double d2 = 0b01d;

	    long test // Long.MAX_VALUE
		= -0b111111111111111111111111111111111111111111111111111111111111111L; 
	    logger.info(test);
	    
	}
}

package io.github.successhawk.java7.numericliterals;

import org.apache.log4j.Logger;

public class UnderscoreExamples {

	private static final Logger logger = Logger
			.getLogger(UnderscoreExamples.class);

	public static void main(String[] args) {
		
		
int count = 1_000_000; // thousands separator
//separated by bytes
int INT_MAX_VALUE = 0x7f_ff_ff_ff; 
//separated by byte pairs
long LONG_MAX_VALUE = 0x7fff_ffff_ffff_ffffL; 
long ssn = 123_45_6789L;
long phoneNbr = 404_992_7865L;
double pi18 = 3.141_592_653_589_793_238d;

// separated by byte
long LONG_MAX_VALUE_BIN = 0b01111111_11111111_11111111_11111111_11111111_11111111_11111111_11111111L; 

logger.info(LONG_MAX_VALUE_BIN);
// Following don't compile.
//long phoneNbr = 404_992_7865_L;
//double pi18 = 3._141_592_653_589_793_238d;
		
		
		logger.info(count + ssn + phoneNbr + INT_MAX_VALUE + LONG_MAX_VALUE + pi18 );
	}
}

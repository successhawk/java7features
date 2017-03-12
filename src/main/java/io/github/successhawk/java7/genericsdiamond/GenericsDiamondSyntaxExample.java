package io.github.successhawk.java7.genericsdiamond;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public class GenericsDiamondSyntaxExample {

	private static final Logger logger = Logger
			.getLogger(GenericsDiamondSyntaxExample.class);

	public static void main(String[] args) {
		
		
		Map java4Map = new HashMap(); // In Java 5 and later this is referred to as a Raw Type reference
		
		Map<String,Integer> java5Map = new HashMap<String,Integer>();  // Prior to Java 7, it was required to repeat the Type Parameter Arguments
		
		Map<String,Integer> java7Map = new HashMap<>(); // Note the diamond syntax. The type is inferred by the compiler.
		
		Map<String,Map<Integer,Collection<BigDecimal>>> complexMap = new HashMap<>();
		

		// The following is just to reference them and make the warning go away.
		logger.info("" + java4Map + java5Map + java7Map + complexMap);
	}
}

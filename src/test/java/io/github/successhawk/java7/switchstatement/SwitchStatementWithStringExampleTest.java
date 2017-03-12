package io.github.successhawk.java7.switchstatement;
import org.junit.Test;

import static io.github.successhawk.java7.switchstatement.SwitchStatementWithStringExample.*;
import static org.junit.Assert.*;

public class SwitchStatementWithStringExampleTest {

	@Test(expected=IllegalArgumentException.class)
	public void testParseBoolean_null() {
		parseBoolean((String)null);
	}

	@Test public void testParseBoolean_true() {
		assertTrue( parseBoolean("true"));
	}

	@Test public void testParseBoolean_false() {
		assertFalse( parseBoolean("false"));
	}

	@Test(expected=IllegalArgumentException.class)
	public void testParseBoolean_True() {
		parseBoolean("True");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testParseBoolean_False() {
		parseBoolean("False");
	}

	@Test(expected=IllegalArgumentException.class)
	public void testParseBoolean_garbage() {
		parseBoolean("garbage");
	}

	@Test(expected=IllegalArgumentException.class)
	public void testParseBooleanIgnoreCase_null() {
		parseBooleanIgnoreCase((String)null);
	}

	@Test public void testParseBooleanIgnoreCase_true() {
		assertTrue( parseBooleanIgnoreCase("true"));
	}

	@Test public void testParseBooleanIgnoreCase_false() {
		assertFalse( parseBooleanIgnoreCase("false"));
	}

	@Test public void testParseBooleanIgnoreCase_True() {
		assertTrue(parseBooleanIgnoreCase("True"));
	}
	
	@Test public void testParseBooleanIgnoreCase_False() {
		assertFalse( parseBooleanIgnoreCase("False"));
	}

	@Test(expected=IllegalArgumentException.class)
	public void testParseBooleanIgnoreCase_garbage() {
		parseBooleanIgnoreCase("garbage");
	}

	
	
	
	@Test(expected=IllegalArgumentException.class)
	public void testParseBoolean_Java6_null() {
		parseBoolean_Java6((String)null);
	}

	@Test public void testParseBoolean_Java6_true() {
		assertTrue( parseBoolean_Java6("true"));
	}

	@Test public void testParseBoolean_Java6_false() {
		assertFalse( parseBoolean_Java6("false"));
	}

	@Test(expected=IllegalArgumentException.class)
	public void testParseBoolean_Java6_True() {
		parseBoolean_Java6("True");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testParseBoolean_Java6_False() {
		parseBoolean_Java6("False");
	}

	@Test(expected=IllegalArgumentException.class)
	public void testParseBoolean_Java6_garbage() {
		parseBoolean_Java6("garbage");
	}
	
//	@Test public void testJUnitError()
//	{
//		throw new RuntimeException("testJUnitError");
//	}
//	
//	/* This is a Failure, just like testFail() */
//	@Test public void testJUnitFailedAssertion()
//	{
//		assertTrue(false); 
//	}
	
	
//	@Test public void testJUnitFail()
//	{
//		fail();
//	}
}

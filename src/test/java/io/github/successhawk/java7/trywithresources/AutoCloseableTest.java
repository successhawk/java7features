package io.github.successhawk.java7.trywithresources;

import static org.junit.Assert.*;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

/**
 * These aren't good examples of how to code with try-with-resources syntax.  This code just demonstrates and proves the execution flow.
 * 
 * @author nwh02
 *
 */
public class AutoCloseableTest {
	
	private static final Logger logger = Logger.getLogger(AutoCloseableTest.class);

	/** Used to drive the behavior of the instance. */
	public enum Behavior{
		NoException,
		UncheckedException,
		CheckedException,
		NotApplicable
	}

	public static class UncheckedException extends RuntimeException {
		private static final long serialVersionUID = 1L;
		public UncheckedException(String context) {
			super(context);
		}
	}
	
	public static class CheckedException extends Exception {
		private static final long serialVersionUID = 1L;
		public CheckedException(String context) {
			super(context);
		}
	}
	
	/** The constructor, doSomething() and close() all throw IOException, which is a checked Exception */
	public static class AutoCloseableWithCheckedExceptions implements AutoCloseable
	{
		protected Behavior closeBehavior;
		private boolean isDone;
		private boolean isClosed;

		public AutoCloseableWithCheckedExceptions() throws CheckedException {
			this(Behavior.NoException, Behavior.NoException);
		}

		public AutoCloseableWithCheckedExceptions(Behavior constructorBehavior,
				Behavior closeBehavior) throws CheckedException{
			this.closeBehavior = closeBehavior;
			behave("constructor", constructorBehavior);
		}

		protected AutoCloseableWithCheckedExceptions(Behavior closeBehavior) {
			this.closeBehavior = closeBehavior;
		}
		
		protected void behave(String context, Behavior behavior) throws CheckedException {
			logger.info("Context=" + context + " behavior=" + behavior);
			switch(behavior)
			{
			case NoException:
				return;
			case UncheckedException:
				throw new UncheckedException(context);
			case CheckedException:
				throw new CheckedException(context);
			case NotApplicable:
			default:
				throw new Error("Programming Error!");
			}
		}

		public void doSomething(Behavior behavior) throws CheckedException
		{
			isDone = true;
			behave("doSomething", behavior);
		}
		
		@Override
		public void close() throws CheckedException  
		{
			isClosed = true;
			behave("close", closeBehavior);
		}

		public final boolean isDone(){
			return isDone;
		}
		
		public final boolean isClosed() {
			return isClosed;
		}
		
	}

	/** This is a closeable that does not throw any checked exceptions. */
	public static class AutoCloseableWithoutCheckedExceptions extends AutoCloseableWithCheckedExceptions
	{
		public AutoCloseableWithoutCheckedExceptions() {
			super(Behavior.NoException);
		}
		
		public AutoCloseableWithoutCheckedExceptions(Behavior constructorBehavior, Behavior closeBehavior) {
			super(closeBehavior);
			try {			
				behave("constructor", constructorBehavior);
			} catch (CheckedException e) {
				throw new Error("this does not support behavior " + constructorBehavior);
			}
		}

		@Override
		public void doSomething(Behavior behavior)
		{
			try {
				super.doSomething(behavior);
			} catch (CheckedException e) {
				throw new Error("this does not support behavior " + behavior);
			}
		}
		
		@Override
		public void close() 
		{
			try {
				super.close();
			} catch (CheckedException e) {
				throw new Error("this does not support behavior " + closeBehavior);
			}
		}
	}
	
	
	@Rule public TestName name = new TestName();
	
	@Before
	public void before()
	{
		logger.info("START executing test " + name.getMethodName());
	}
	
	@After
	public void after()
	{
		logger.info("FINISHED executing test " + name.getMethodName());
	}
	
	public void examplesThatDontCompile()
	{
		/* Uncomment any of the following to see their compilation error. */
		
		/* try's without resources require a catch or finally clause! */
//		try {
//		}

		/* try-with-resource must define and assign the resource within the ResourceSpecification
		 * Note: the AutoCloseable object can be created prior.
		 */
//		final AutoCloseableWithoutCheckedExceptions resource5 = new AutoCloseableWithoutCheckedExceptions();
//		try(resource5) {
//		}
		
//		final AutoCloseableWithoutCheckedExceptions resource6;
//		try(resource6 = new AutoCloseableWithoutCheckedExceptions()) {
//		}


		/* If the Resource type's close method is declared to throw a checked exception then the exception must be caught.
		 * Note that the implementation does not throw a checked Exception. The compilation is based on the reference type. */  
//		try(AutoCloseableWithCheckedExceptions resource8 = new AutoCloseableWithoutCheckedExceptions()) {
//		}
		
		/* The Resource type must be a java.lang.AutoCloseable. */  
//		try(Object resource9 = new AutoCloseableWithoutCheckedExceptions()) {
//		}
		
		/* The resource is implicitly defined as final and cannot be assigned within the try block. */
//		try(AutoCloseableWithoutCheckedExceptions resource10 = new AutoCloseableWithoutCheckedExceptions()) {
//			resource10 = null;
//		}
		
		/* The resource is locally scoped to the try block and cannot be accessed in a catch or finally block. */
//		try(AutoCloseableWithoutCheckedExceptions resource10 = new AutoCloseableWithoutCheckedExceptions()) {
//		} catch (RuntimeException e) {
//			logger.info(resource10);
//		} finally {
//			logger.info(resource10);
//		}

		/* The resource is implicitly defined as final and cannot be assigned within the try block. */
//		try(AutoCloseableWithoutCheckedExceptions resource10 = new AutoCloseableWithoutCheckedExceptions()) {
//			resource10 = null;
//		}

		/** Cannot declare more than one reference like you can with regular variable declarations.
		 * Note: you can declare multiple resources separated by semicolon, and multiple resources could actually point to the same
		 * AutoCloseable object. See test_multiResources_SameAutoCloseable  */
//		try(AutoCloseableWithoutCheckedExceptions resource, resource2= new AutoCloseableWithoutCheckedExceptions()) 
//		{
//		}
		
		
	}
	
	@Test
	public void test_basic_tryWithoutCatchOrFinallyClauses_NoException()
	{
		AutoCloseableWithoutCheckedExceptions resourceRef;
		try(AutoCloseableWithoutCheckedExceptions resource= new AutoCloseableWithoutCheckedExceptions()) {
			resourceRef = resource;
			resource.doSomething(Behavior.NoException);
		} 
		assertTrue(resourceRef.isDone());
		assertTrue(resourceRef.isClosed());
	}
	
	@Test
	public void test_basic_initUncheckedException(){
		boolean finallyExecuted = false;
		boolean catchExecuted = false;
		try(AutoCloseableWithoutCheckedExceptions resource= new AutoCloseableWithoutCheckedExceptions(Behavior.UncheckedException, Behavior.NotApplicable)) {
			fail("try block should not execute.");
		}
		catch(UncheckedException e) {
			catchExecuted = true;
		} finally {
			finallyExecuted = true;
		}
		assertTrue(catchExecuted);
		assertTrue(finallyExecuted);
	}
	
	@Test
	public void test_basic_initCheckedException(){
		boolean finallyExecuted = false;
		boolean catchExecuted = false;
		try(AutoCloseableWithCheckedExceptions resource= new AutoCloseableWithCheckedExceptions(Behavior.CheckedException, Behavior.NotApplicable)) {
			fail("try block should not execute.");
		}
		catch(CheckedException e) {
			catchExecuted = true;
		} finally {
			finallyExecuted = true;
		}
		assertTrue(catchExecuted);
		assertTrue(finallyExecuted);
		
	}

	@Test
	public void test_basic_tryNoException_closeUncheckedException(){
		boolean finallyExecuted = false;
		boolean catchExecuted = false;
		try(AutoCloseableWithCheckedExceptions resource= new AutoCloseableWithCheckedExceptions(Behavior.NoException, Behavior.UncheckedException)) {
			resource.doSomething(Behavior.NoException);
		}
		catch(UncheckedException e) {
			assertEquals("close", e.getMessage());
			catchExecuted = true;
		} catch (CheckedException e) {
			fail("Not expected");
		} finally {
			finallyExecuted = true;
		}
		assertTrue(catchExecuted);
		assertTrue(finallyExecuted);
	}

	@Test
	public void test_basic_tryNoException_closeCheckedException(){
		boolean finallyExecuted = false;
		boolean catchExecuted = false;
		try(AutoCloseableWithCheckedExceptions resource= new AutoCloseableWithCheckedExceptions(Behavior.NoException, Behavior.CheckedException)) {
			resource.doSomething(Behavior.NoException);
		}
		catch(UncheckedException e) {
			fail("Not expected");
		} catch (CheckedException e) {
			assertEquals("close", e.getMessage());
			catchExecuted = true;
		} finally {
			finallyExecuted = true;
		}
		assertTrue(catchExecuted);
		assertTrue(finallyExecuted);
	}

	@Test
	public void test_basic_tryUncheckedException_SuppressedCloseUncheckedException(){
		boolean finallyExecuted = false;
		boolean catchExecuted = false;
		try(AutoCloseableWithoutCheckedExceptions resource= new AutoCloseableWithoutCheckedExceptions(Behavior.NoException, Behavior.UncheckedException)) {
			resource.doSomething(Behavior.UncheckedException);
		}
		catch(UncheckedException e) {
			logger.info("caught " + e);
			assertEquals("doSomething", e.getMessage());
			assertEquals(1, e.getSuppressed().length);
			assertEquals("close", e.getSuppressed()[0].getMessage());
			assertEquals(UncheckedException.class, e.getSuppressed()[0].getClass());
			catchExecuted = true;
		} finally {
			logger.info("finally");
			finallyExecuted = true;
		}
		assertTrue(catchExecuted);
		assertTrue(finallyExecuted);
	}

	@Test
	public void test_basic_tryUncheckedException_SuppressedCloseCheckedException(){
		boolean finallyExecuted = false;
		boolean catchExecuted = false;
		try(AutoCloseableWithCheckedExceptions resource= new AutoCloseableWithCheckedExceptions(Behavior.NoException, Behavior.CheckedException)) {
			resource.doSomething(Behavior.UncheckedException);
		}
		catch(UncheckedException e) {
			logger.info("caught " + e);
			assertEquals("doSomething", e.getMessage());
			assertEquals(1, e.getSuppressed().length);
			assertEquals("close", e.getSuppressed()[0].getMessage());
			assertEquals(CheckedException.class, e.getSuppressed()[0].getClass());
			catchExecuted = true;
		} catch (CheckedException e) {
			fail("not expected");
		} finally {
			logger.info("finally");
			finallyExecuted = true;
		}
		assertTrue(catchExecuted);
		assertTrue(finallyExecuted);
	}

	@Test
	public void test_basic_tryCheckedException_SuppressedCloseCheckedException(){
		boolean finallyExecuted = false;
		boolean catchExecuted = false;
		try(AutoCloseableWithCheckedExceptions resource= new AutoCloseableWithCheckedExceptions(Behavior.NoException, Behavior.CheckedException)) {
			resource.doSomething(Behavior.CheckedException);
		} catch (CheckedException e) {
			logger.info("caught " + e);
			assertEquals("doSomething", e.getMessage());
			assertEquals(1, e.getSuppressed().length);
			assertEquals("close", e.getSuppressed()[0].getMessage());
			assertEquals(CheckedException.class, e.getSuppressed()[0].getClass());
			catchExecuted = true;
		} finally {
			logger.info("finally");
			finallyExecuted = true;
		}
		assertTrue(catchExecuted);
		assertTrue(finallyExecuted);
	}

	@Test
	public void test_basic_tryCheckedException_closeUncheckedExceptionSuppressed(){
		boolean finallyExecuted = false;
		boolean catchExecuted = false;
		try(AutoCloseableWithCheckedExceptions resource= new AutoCloseableWithCheckedExceptions(Behavior.NoException, Behavior.UncheckedException)) {
			resource.doSomething(Behavior.CheckedException);
		} catch (CheckedException e) {
			logger.info("caught " + e);
			assertEquals("doSomething", e.getMessage());
			assertEquals(1, e.getSuppressed().length);
			assertEquals("close", e.getSuppressed()[0].getMessage());
			assertEquals(UncheckedException.class, e.getSuppressed()[0].getClass());
			catchExecuted = true;
		} finally {
			logger.info("finally");
			finallyExecuted = true;
		}
		assertTrue(catchExecuted);
		assertTrue(finallyExecuted);
	}
	
	@Test
	public void test_multiResources_NoException()
	{
		AutoCloseableWithoutCheckedExceptions ref1, ref2;
		try(AutoCloseableWithoutCheckedExceptions resource1 = new AutoCloseableWithoutCheckedExceptions()
			;AutoCloseableWithoutCheckedExceptions resource2 = new AutoCloseableWithoutCheckedExceptions()
		 ) {
			ref1 = resource1;
			ref2 = resource2;
			resource1.doSomething(Behavior.NoException);
			resource2.doSomething(Behavior.NoException);
		} 
		assertTrue(ref1.isDone());
		assertTrue(ref1.isClosed());
		assertTrue(ref2.isDone());
		assertTrue(ref2.isClosed());
	}

	@Test
	public void test_multiResources_SameAutoCloseable()
	{
		AutoCloseableWithoutCheckedExceptions ref1, ref2;
		AutoCloseableWithoutCheckedExceptions closeable = new AutoCloseableWithoutCheckedExceptions();
		try(AutoCloseableWithoutCheckedExceptions resource1 = closeable
			; AutoCloseableWithoutCheckedExceptions resource2= closeable) 
		{
			ref1 = resource1;
			ref2 = resource2;
			resource1.doSomething(Behavior.NoException);
			resource2.doSomething(Behavior.NoException);
		} 
		assertTrue(ref1.isDone());
		assertTrue(ref1.isClosed());
		assertTrue(ref2.isDone());
		assertTrue(ref2.isClosed());
	}

	@Test
	public void test_multiResources_initSecondResourceUncheckedException()
	{
		AutoCloseableWithoutCheckedExceptions ref1 = new AutoCloseableWithoutCheckedExceptions();
		AutoCloseableWithoutCheckedExceptions ref2 = null;
		try(AutoCloseableWithoutCheckedExceptions resource1 = ref1 
			;AutoCloseableWithoutCheckedExceptions resource2 = new AutoCloseableWithoutCheckedExceptions(Behavior.UncheckedException, Behavior.NotApplicable)
		 ) {
			ref2 = resource2;
			fail("Should not happen due to ResourceSpecification exception");
		} catch (UncheckedException e){
			logger.info("expected " + e);
			assertEquals("constructor", e.getMessage());
		}
		assertFalse(ref1.isDone());
		assertTrue(ref1.isClosed());
		assertNull(ref2); /* Note: ref2 was not set because resource2 was not initialized due to exception, therefore the try block didn't execute.  Also, since resource2 was never initialized it couldn't have been closed. */
	}

	@Test
	public void test_multiResources_initFirstResourceUncheckedException()
	{
		AutoCloseableWithoutCheckedExceptions ref1 = null;
		AutoCloseableWithoutCheckedExceptions ref2 = null;
		try(AutoCloseableWithoutCheckedExceptions resource1 = new AutoCloseableWithoutCheckedExceptions(Behavior.UncheckedException, Behavior.NotApplicable) 
		/* Note that resource2 is not attempted to be initialized, otherwise an Error would be thrown. */
			;AutoCloseableWithoutCheckedExceptions resource2 = new AutoCloseableWithoutCheckedExceptions(Behavior.NotApplicable, Behavior.NotApplicable)
		 ) {
			ref1=resource1; //won't happen
			ref2=resource2; //won't happen
			fail("Should not happen due to expected init exception");
		} catch (UncheckedException e){
			logger.info("expected " + e);
			assertEquals("constructor", e.getMessage());
			assertEquals("expect nothing to be suppressed.", 0, e.getSuppressed().length);
		}
		/* Note: ref1 and ref2 were not set because resource1 was not initialized due to exception, therefore the try block didn't execute.  Also, since resource2 was never initialized it couldn't have been closed. */		
		assertNull(ref1); 
		assertNull(ref2); 
	}
	
	@Test
	public void test_multiResources_tryUncheckedException_closeUncheckedExceptionSuppressed()
	{
		/* Configure both to throw exceptions in their close method. These will be suppressed. */
		AutoCloseableWithoutCheckedExceptions ref1 = new AutoCloseableWithoutCheckedExceptions(Behavior.NoException, Behavior.UncheckedException);
		AutoCloseableWithoutCheckedExceptions ref2 = new AutoCloseableWithoutCheckedExceptions(Behavior.NoException, Behavior.UncheckedException);
		boolean expectedOccurred = false;
		try(AutoCloseableWithoutCheckedExceptions resource1=ref1; AutoCloseableWithoutCheckedExceptions resource2=ref2) {
			throw new RuntimeException("try failed");
		} catch (RuntimeException e){
			logger.info("expected " + e);
			assertEquals("try failed", e.getMessage());
			assertEquals("expect 2 suppressed exceptions. 1 for each resource.", 2, e.getSuppressed().length);
			assertTrue(ref1.isClosed());
			assertTrue(ref2.isClosed());
			expectedOccurred = true;
		}
		assertTrue(expectedOccurred);
	}

	@Test
	public void test_multiResources_tryNoException_closeUncheckedExceptionSuppressed()
	{
		/* Configure both to throw exceptions in their close method. These will be suppressed. */
		AutoCloseableWithCheckedExceptions ref1 = null;
		AutoCloseableWithCheckedExceptions ref2 = null;
		boolean expectedOccurred = false;
		try(AutoCloseableWithCheckedExceptions resource1=new AutoCloseableWithCheckedExceptions(Behavior.NoException, Behavior.CheckedException);
			AutoCloseableWithCheckedExceptions resource2=new AutoCloseableWithCheckedExceptions(Behavior.NoException, Behavior.CheckedException)) {
			ref1 = resource1;
			ref2 = resource2;
			resource1.doSomething(Behavior.NoException);
			resource2.doSomething(Behavior.NoException);
		} catch (CheckedException e){
			logger.info("expected ", e);
			assertEquals("close", e.getMessage());
			assertEquals("expect 1 suppressed exceptions. The 2nd resource closed.", 1, e.getSuppressed().length);
			assertTrue(ref1.isDone());
			assertTrue(ref2.isDone());
			assertTrue(ref1.isClosed());
			assertTrue(ref2.isClosed());
			expectedOccurred = true;
		}
		assertTrue(expectedOccurred);
		assertTrue(ref1.isDone());
		assertTrue(ref2.isDone());
		assertTrue(ref1.isClosed());
		assertTrue(ref2.isClosed());
	}
	
	@Test
	public void test_nestedTryWithResources_tryUncheckedException()
	{
		/* Configure both to throw exceptions in their close method. These will be suppressed. */
		AutoCloseableWithoutCheckedExceptions outerResourceRef = new AutoCloseableWithoutCheckedExceptions(Behavior.NoException, Behavior.UncheckedException);
		AutoCloseableWithoutCheckedExceptions innerResourceRef = new AutoCloseableWithoutCheckedExceptions(Behavior.NoException, Behavior.UncheckedException);
		boolean expectedOccurred = false;
		try(AutoCloseableWithoutCheckedExceptions outerResource=outerResourceRef) {
			try (AutoCloseableWithoutCheckedExceptions innerResource=innerResourceRef) {
				throw new RuntimeException("try failed");
			} catch (RuntimeException e){
				logger.info("Inner expected " + e);
				assertEquals("try failed", e.getMessage());
				assertEquals("expect 1 suppressed exceptions.", 1, e.getSuppressed().length);
				assertFalse(outerResourceRef.isClosed());
				assertTrue(innerResourceRef.isClosed());
				expectedOccurred = true;
				throw e;
			}
		} catch (RuntimeException e){
			logger.info("Outer expected " + e);
			assertEquals("try failed", e.getMessage());
			assertEquals("expect 2 suppressed exceptions. 1 for inner close, and 1 for outer close.", 2, e.getSuppressed().length);
			assertTrue(outerResourceRef.isClosed());
			assertTrue(innerResourceRef.isClosed());
			expectedOccurred = true;
		}
		assertTrue(expectedOccurred);
	}
	
	@SuppressWarnings("finally")
	public void testFinallyException() {
		try {
			try{
				throw new RuntimeException("try");
			} finally {
				throw new RuntimeException("finally");
			}
		} catch(RuntimeException e) {
			assertEquals("finally", e.getMessage());
			assertEquals(0, e.getSuppressed().length); /* This shows that the original "try" exception was not suppressed and is lost. */
		}
	}
	
	
}

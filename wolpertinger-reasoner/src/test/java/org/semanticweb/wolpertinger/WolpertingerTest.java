package org.semanticweb.wolpertinger;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class WolpertingerTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public WolpertingerTest( String testName )
    {
        super( testName );
    }
    
    @Override
    protected void setUp() throws Exception {
    	super.setUp();
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( WolpertingerTest.class );
    }
    
    /**
     * Atomic clash
     */
    public void testUnsatifiabilityDueToClashInABoxAssertions() {
    	assertTrue(false);
    }
    
    /**
     * Smth like:
     *    A subClassOf r some B
     *    A subClassOf r only C
     *    C disjoint with B
     *    ...
     */
    public void testUnsatisfiabilityDueToConflictingAxioms1() {
    	assertTrue(false);
    }
    
    /**
     * Smth like
     *    A subClassOf r min 5 B
     *  But we have only a domain with 4 elements ...
     */
    public void testUnsatisfiabilityDoToFixedDomain1() {
    	assertTrue(false);
    }
    
    public void testUnsatisfiabilityDoToFixedDomain2() {
    	assertTrue(false);
    }
    
    public void testSimple3ColoringHas4AnswerSets() {
    	assertTrue(false);
    }
    
    

}

package com.ibm.mla.mapping.concept;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author psheikh
 *
 */
public class TestDescriptiveTerm {

	/** set lemma from constructor */
	@Test
	public void testConstructor() {
		DescriptiveTerm dt = new DescriptiveTerm("hallo");
		Assert.assertEquals(dt.toString(), "hallo");
	}
	
	/** null as parm --> initialize with empty string */
	@Test
	public void testConstructor2() {
		DescriptiveTerm dt = new DescriptiveTerm(null);
		Assert.assertEquals(dt.toString(), "");
	}
	
	/** equal object are equal */
	@Test
	public void testEquals() {
		DescriptiveTerm dt = new DescriptiveTerm("A");
		DescriptiveTerm dt2 = new DescriptiveTerm("A");
		Assert.assertEquals(dt, dt2);
	}
	
	/** non equal object are not equal */
	@Test
	public void testEquals2() {
		DescriptiveTerm dt = new DescriptiveTerm("A");
		DescriptiveTerm dt2 = new DescriptiveTerm("B");
		Assert.assertNotEquals(dt, dt2);
	}

}

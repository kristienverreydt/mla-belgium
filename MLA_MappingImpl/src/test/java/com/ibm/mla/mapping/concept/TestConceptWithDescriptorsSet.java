package com.ibm.mla.mapping.concept;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test logic needed for the mapper.

 * @author philip.sheikh@at.ibm.com
 *
 */
public class TestConceptWithDescriptorsSet {

	// a concept is same or more specific to itself and to a
	// more specific concept.
	@Test
	public void isSameOrMoreSpecific1() {
		BodyPartSet bps = new BodyPartSet("Kopf#hinten");
		BodyPartSet bps2 = new BodyPartSet("Kopf");
		bps.isSameOrMoreSpecific(bps2);
		assertTrue(bps.isSameOrMoreSpecific(bps2));
		assertTrue(bps.isSameOrMoreSpecific(bps));
	}
	
	// a concept is not more specific than a
	// more specific concept.
	@Test
	public void isSameOrMoreSpecific2() {
		BodyPartSet bps = new BodyPartSet("Kopf#hinten");
		BodyPartSet bps2 = new BodyPartSet("Kopf");
		assertFalse(bps2.isSameOrMoreSpecific(bps));
	}
	
	// a concept is not more specific than a
	// more specific concept with differing attributes.
	@Test
	public void isSameOrMoreSpecific3() {
		BodyPartSet bps = new BodyPartSet("Kopf#hinten#links#unten#neben");
		BodyPartSet bps2 = new BodyPartSet("Kopf#hinten#rechts#unten#neben");
		assertFalse(bps.isSameOrMoreSpecific(bps2));
	}
	
	// a concept with an empty tailing # is the same as a concept without  a #
	@Test
	public void isSameOrMoreSpecific7() {
		BodyPartSet bps = new BodyPartSet("Kopf#");
		BodyPartSet bps2 = new BodyPartSet("Kopf");
		assertTrue(bps.isSameOrMoreSpecific(bps2));
		assertTrue(bps2.isSameOrMoreSpecific(bps));
	}
	
	// a concept with multiple body parts is same more specific than a set
	// with a singe body part
	@Test
	public void isSameOrMoreSpecific8() {
		BodyPartSet bps = new BodyPartSet("Kopf|Hals");
		BodyPartSet bps2 = new BodyPartSet("Kopf");
		assertTrue(bps.isSameOrMoreSpecific(bps2));
		assertFalse(bps2.isSameOrMoreSpecific(bps));
	}
	
	// a concept with multiple attributes and two bP is more specific than a
	// concept with only two body parts
	@Test
	public void isSameOrMoreSpecific9() {
		BodyPartSet bps = new BodyPartSet("Kopf#unten#links|Hals#oben#rechts");
		BodyPartSet bps2 = new BodyPartSet("Kopf|Hals");
		assertTrue(bps.isSameOrMoreSpecific(bps2));
		assertFalse(bps2.isSameOrMoreSpecific(bps));
	}
	
	// a concept with with a pipe at the end is the same as a concept
	// without a pipe
	@Test
	public void isSameOrMoreSpecific10() {
		BodyPartSet bps = new BodyPartSet("Kopf|");
		BodyPartSet bps2 = new BodyPartSet("Kopf");
		assertTrue(bps.isSameOrMoreSpecific(bps2));
		assertTrue(bps2.isSameOrMoreSpecific(bps));
	}
	
	// a concept with with a pipe and hash at the end is the same as a concept
	// without a pipe
	@Test
	public void isSameOrMoreSpecific11() {
		BodyPartSet bps = new BodyPartSet("Kopf#|");
		BodyPartSet bps2 = new BodyPartSet("Kopf");
		assertTrue(bps.isSameOrMoreSpecific(bps2));
		assertTrue(bps2.isSameOrMoreSpecific(bps));
	}
	
	// a concept with with a pipe and hash at the end is the same as a concept
	// without a pipe both have tailing whitespace
	@Test
	public void isSameOrMoreSpecific12() {
		BodyPartSet bps = new BodyPartSet("Kopf# | ");
		BodyPartSet bps2 = new BodyPartSet("Kopf");
		assertTrue(bps.isSameOrMoreSpecific(bps2));
		assertTrue(bps2.isSameOrMoreSpecific(bps));
	}
	
	// test toString
	@Test
	public void testToString() {
		String input = "Kopf#unten#links|Hals#oben#rechts";
		BodyPartSet bps = new BodyPartSet(input);
		Assert.assertEquals(bps.toString(), input);
	}
	
	// equal objects are equal
	@Test
	public void testEquals() {
		String input = "Kopf#unten#links|Hals#oben#rechts";
		BodyPartSet bps = new BodyPartSet(input);
		BodyPartSet bps2 = new BodyPartSet(input);
		Assert.assertEquals(bps, bps2);
	}
	
	// non equal objects are not equal
	@Test
	public void testEquals2() {
		String input = "Kopf#unten#links|Hals#oben#rechts";
		String input2 = "Kopf#unten#links";
		BodyPartSet bps = new BodyPartSet(input);
		BodyPartSet bps2 = new BodyPartSet(input2);
		Assert.assertNotEquals(bps, bps2);
	}
	
	// non equal objects are not equal
	@Test
	public void testEquals3() {
		String input = "Kopf#unten";
		String input2 = "Kopf";
		BodyPartSet bps = new BodyPartSet(input);
		BodyPartSet bps2 = new BodyPartSet(input2);
		Assert.assertNotEquals(bps, bps2);
	}
	
	// one empty pipe is ignores
	@Test
	public void testEquals4() {
		String input = "Kopf#unten|";
		String input2 = "Kopf#unten";
		BodyPartSet bps = new BodyPartSet(input);
		BodyPartSet bps2 = new BodyPartSet(input2);
		Assert.assertEquals(bps, bps2);
	}
	
	// multiple pipe symbols are ignored
	@Test
	public void testEquals5() {
		String input = " ||||Kopf#unten|||||||||||||||||||||";
		String input2 = "Kopf#unten";
		BodyPartSet bps = new BodyPartSet(input);
		BodyPartSet bps2 = new BodyPartSet(input2);
		System.err.println(bps);
		Assert.assertEquals(bps, bps2);
	}
}

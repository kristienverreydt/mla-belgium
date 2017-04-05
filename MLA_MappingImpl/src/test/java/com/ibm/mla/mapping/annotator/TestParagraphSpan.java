package com.ibm.mla.mapping.annotator;

import org.apache.uima.jcas.tcas.Annotation;
import org.junit.Assert;
import org.junit.Test;

public class TestParagraphSpan {

	/** test constructor */
	@Test
	public void testConstructor() {
		ParagraphSpan<Annotation, Annotation> ps;
		ps = new ParagraphSpan<Annotation, Annotation>(1,4,"IBM");
		Assert.assertTrue(ps.getBegin()==1);
		Assert.assertTrue(ps.getEnd()==4);
		Assert.assertTrue(ps.getCoveredText().equals("IBM"));
	}
	
	/** copying one object works */
	@Test
	public void testCopyConstructor() {
		ParagraphSpan<Annotation, Annotation> ps;
		ParagraphSpan<Annotation, Annotation> ps2;
		ps = new ParagraphSpan<Annotation, Annotation>(1,4,"IBM");
		ps2 = new ParagraphSpan<Annotation, Annotation>(ps);
		Assert.assertTrue(ps2.getBegin()==1);
		Assert.assertTrue(ps2.getEnd()==4);
		Assert.assertTrue(ps2.getCoveredText().equals("IBM"));
	}
	
	/** equal object equals */
	@Test
	public void testEquals() {
		ParagraphSpan<Annotation, Annotation> ps;
		ParagraphSpan<Annotation, Annotation> ps2;
		ps = new ParagraphSpan<Annotation, Annotation>(1,4,"WATSON");
		ps2 = new ParagraphSpan<Annotation, Annotation>(ps);
		Assert.assertTrue(ps.equals(ps2));
	}
	
	/** non equal object does not equal */
	@Test
	public void testEquals2() {
		ParagraphSpan<Annotation, Annotation> ps;
		ParagraphSpan<Annotation, Annotation> ps2;
		ps = new ParagraphSpan<Annotation, Annotation>(1,4,"WATSON");
		ps2 = new ParagraphSpan<Annotation, Annotation>(1,4,"IBM");
		Assert.assertFalse(ps.equals(ps2));
	}
	
	/** bug toString has no ] hat end */
	@Test
	public void testToString() {
		ParagraphSpan<Annotation, Annotation> ps;
		ps = new ParagraphSpan<Annotation, Annotation>(1,4,"WATSON");
		//System.out.println(ps.toString);
		Assert.assertTrue(ps.toString().equals("ParagraphSpan [begin=1, end=4, covered text=WATSON]"));
	}

}

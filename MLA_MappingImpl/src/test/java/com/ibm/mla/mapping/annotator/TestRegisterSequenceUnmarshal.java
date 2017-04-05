package com.ibm.mla.mapping.annotator;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import javax.xml.bind.JAXB;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

public class TestRegisterSequenceUnmarshal {

	@Test
	public void test() throws IOException {
		
		// tmp output file
		File output = new File("src/test/resources/com/ibm/mapping/annotator/marshall_test_output.txt");
		// baseline to compare to
		File input = new File("src/test/resources/com/ibm/mapping/annotator/marshall_test_input.txt");
		
		String xml_content = FileUtils.readFileToString(input, "UTF-8");
		Reader xmlReader = new StringReader(xml_content);
		@SuppressWarnings("rawtypes")
		RegisterSequence regSeq = JAXB.unmarshal(xmlReader, RegisterSequence.class);
		
		JAXB.marshal(regSeq, output);
		// compare to be and as is file
		Assert.assertEquals(xml_content, FileUtils.readFileToString(output, "UTF-8"));
		// clean up
		FileUtils.deleteQuietly(output);
	}

}

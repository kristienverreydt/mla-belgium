/**
 * 
 */
package com.ibm.mla.mapping.annotator;

import java.io.IOException;
import java.io.InputStream;

import com.ibm.mla.mapping.register.RegisterKey;
import com.ibm.mla.mapping.register.RegisterValue;
import com.ibm.mla.mapping.register.csv.CSVRegister;

/**
 * @author AT00955
 *
 */
public interface CSVRegisterFactory <K extends RegisterKey<K>, V extends RegisterValue<V>>{
	public abstract CSVRegister<K, V> newRegisterFromInputStream(InputStream inIS, String codeSystem) throws IOException;

}

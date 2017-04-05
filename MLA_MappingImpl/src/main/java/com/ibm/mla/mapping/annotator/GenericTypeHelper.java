/**
 *
 */
package com.ibm.mla.mapping.annotator;

import java.lang.reflect.ParameterizedType;

import org.apache.uima.cas.Type;
import org.apache.uima.cas.TypeSystem;
import org.apache.uima.jcas.JCas;

/**
 * @author Georg Temnitschka
 *
 */
class GenericTypeHelper {

	// ################ HELPER METHODS RESOLVE GENERIC TYPES ################
	/** TODO GT: find better solution and replace
	 * Gets the actual type of the Generic type S.</br>
	 * Retrieves the JAVA Type of the generic Type S and
	 * looks it up in the UIMA TypeSystem to resolve it to a
	 * UIMA Type which is stored in the field
	 * {@link AbstractWriter#annotationType}</br><ol>
	 * <li>Get the superclass of the current class (this class)</li>
	 * <li>Cast the Super type to a ParameterizedType</li>
	 * <li>get the actual type argument for the nth type argument as Class</li>
	 * <li>get the UIMA Type system</li>
	 * <li>get the UIMA Type from the Class by looking it up in the
	 * UIMA Type system</li>
	 * <li> </li></ol>
	 * @param jcas
	 */
	static Type getAnnotationType(Object that, JCas jcas, int argNo) {
		java.lang.reflect.Type type = that.getClass().getGenericSuperclass();
       ParameterizedType paramType = (ParameterizedType) type;
       Class<?> zz = (Class<?>) paramType.getActualTypeArguments()[argNo];
       TypeSystem ts = jcas.getTypeSystem();
       return ts.getType(zz.getTypeName()); // lookup
	}

}

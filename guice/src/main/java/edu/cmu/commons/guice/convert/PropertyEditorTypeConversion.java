package edu.cmu.commons.guice.convert;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.TypeLiteral;

/**
 * Support conversion from String to any type for which a {@link PropertyEditor}
 * can be found via {@link PropertyEditorManager#findEditor(Class)}.
 * 
 * @author hazen
 */
public class PropertyEditorTypeConversion extends AbstractTypeConversion {
	private static final Logger log = LoggerFactory
			.getLogger(PropertyEditorTypeConversion.class);

	@Override
	public boolean matches(TypeLiteral<?> typeLiteral) {
		boolean matches = getPropertyEditor(typeLiteral) != null;
		log.debug("Type '" + typeLiteral + "' "
				+ (matches ? "matches" : "does not match"));
		return matches;
	}

	@Override
	public Object convert(String text, TypeLiteral<?> typeLiteral) {
		log.debug("Converting text '" + text + "' into '" + typeLiteral + "'");
		PropertyEditor pe = getPropertyEditor(typeLiteral);
		pe.setAsText(text);
		return pe.getValue();
	}

	/**
	 * @param typeLiteral
	 * @return a PropertyEditor supporting the type referenced by typeLiteral, or
	 * <code>null</code> if no supporting PropertyEditor could be found.
	 */
	private PropertyEditor getPropertyEditor(TypeLiteral<?> typeLiteral) {
		return PropertyEditorManager.findEditor(typeLiteral.getClass());
	}
}

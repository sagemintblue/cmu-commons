package edu.cmu.commons.data.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Reflection utilities.
 * @author hazen
 */
public class ReflectionUtils {
	private ReflectionUtils() {}

	/**
	 * @param <E> Enum type.
	 * @param <T> Annotation type.
	 * @param value
	 * @param annotationClass
	 * @return instance of {@code T} associated with {@code value}, or
	 * {@code null} if none exists.
	 */
	public static <E extends Enum<E>, T extends Annotation> T getEnumAnnotation(
			E value, Class<T> annotationClass) {
		Field field;
		try {
			field = value.getDeclaringClass().getField(value.name());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return field.getAnnotation(annotationClass);
	}

	/**
	 * @param <T> Annotation type.
	 * @param cls
	 * @param annotationClass
	 * @return all annotations of type {@code T} found on {@code cls} and its
	 * ancestor types, ordered by first occurance.
	 */
	public static <T extends Annotation> List<T> getAnnotations(Class<?> cls,
			Class<T> annotationClass) {
		List<T> annotations = new LinkedList<T>();
		T annotation = null;
		while (cls != null) {
			annotation = cls.getAnnotation(annotationClass);
			if (annotation != null) annotations.add(annotation);
			cls = cls.getSuperclass();
		}
		Collections.reverse(annotations);
		return annotations;
	}
}

package edu.cmu.commons.util;

import java.util.Collection;
import java.util.Iterator;

/**
 * Collection of string utilities.
 * @author hazen
 */
public class StringUtils {
	public static <E> String join(String separator, Collection<E> collection) {
		if (collection.isEmpty()) return "";
		StringBuilder sb = new StringBuilder();
		Iterator<E> itr = collection.iterator();
		sb.append(itr.next());
		while (itr.hasNext())
			sb.append(separator).append(itr.next());
		return sb.toString();
	}

	public String join(String separator, Object... objs) {
		if (objs.length == 0) return "";
		StringBuilder sb = new StringBuilder();
		sb.append(objs[0]);
		for (int i = 1; i < objs.length; ++i)
			sb.append(separator).append(objs[i]);
		return sb.toString();
	}
}

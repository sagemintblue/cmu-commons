package cmu.edu.commons.hadoop.tuple.schema;

import java.util.Collections;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.util.ReflectionUtils;

import cmu.edu.commons.hadoop.tuple.BaseTuple;
import cmu.edu.commons.hadoop.tuple.BaseTupleList;

public class TupleSchemaUtils {
	private TupleSchemaUtils() {
	// hide default ctor
	}

	public static <E extends Writable> boolean initialize(BaseTuple<E> tuple,
			int i, Class<E> elementClass) {
		if (tuple.size() <= i) {
			if (!(BaseTupleList.class.isInstance(tuple)))
			// tuple is not expandable
			return false;

			// increase size of tuple
			BaseTupleList<E> t = (BaseTupleList<E>) tuple;
			t.addAll(Collections.<E> nCopies(i - tuple.size() + 1, null));
		}

		// retrieve and test existing element
		E element = tuple.get(i);
		if (element != null && element.getClass() == elementClass) return true;

		// initialize new element instance
		element = ReflectionUtils.newInstance(elementClass, null);
		tuple.set(i, element);
		return true;
	}
}

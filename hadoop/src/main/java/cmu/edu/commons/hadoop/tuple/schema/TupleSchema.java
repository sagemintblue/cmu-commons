package cmu.edu.commons.hadoop.tuple.schema;

import java.util.Collections;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.util.ReflectionUtils;

import cmu.edu.commons.hadoop.tuple.BaseListTuple;
import cmu.edu.commons.hadoop.tuple.BaseTuple;

public abstract class TupleSchema<E extends Writable> {
	public static <E extends Writable> boolean initialize(BaseTuple<E> tuple,
			int i, Class<E> elementClass) {
		if (tuple.size() <= i) {
			if (!(BaseListTuple.class.isInstance(tuple)))
			// tuple is not expandable
			return false;

			// increase size of tuple
			BaseListTuple<E> t = (BaseListTuple<E>) tuple;
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

	public abstract void initialize(BaseTuple<E> tuple);

	public abstract boolean initialize(BaseTuple<E> tuple, int i);
}

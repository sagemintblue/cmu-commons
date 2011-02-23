package cmu.edu.commons.hadoop.tuple.schema;

import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.io.Writable;

import cmu.edu.commons.hadoop.tuple.BaseListTuple;
import cmu.edu.commons.hadoop.tuple.BaseTuple;

public class SimpleTupleSchema<E extends Writable> extends TupleSchema<E> {

	private List<Class<E>> elementClasses = new ArrayList<Class<E>>();

	public SimpleTupleSchema<E> add(Class<E> elementClass) {
		elementClasses.add(elementClass);
		return this;
	}

	public void initialize(BaseTuple<E> tuple) {
		int i = -1;
		for (Class<E> elementClass : elementClasses)
			if (!initialize(tuple, ++i, elementClass)) throw new IllegalStateException(
					"Failed to initialize element " + i + " of '"
							+ tuple.getClass().getName() + "' tuple with '"
							+ elementClass.getName() + "' instance");

		if (tuple.size() > elementClasses.size()) {
			if (BaseListTuple.class.isInstance(tuple)) {
				// reduce tuple size to match number of element classes
				BaseListTuple<E> t = (BaseListTuple<E>) tuple;
				List<E> elements =
						new ArrayList<E>(tuple.subList(0, elementClasses.size()));
				t.clear();
				t.addAll(elements);
			} else {
				// set remaining elements to null
				for (int j = elementClasses.size(); j < tuple.size(); ++j)
					tuple.set(j, null);
			}
		}
	}

	public boolean initialize(BaseTuple<E> tuple, int i) {
		if (i < elementClasses.size()) return true;
		return false;
	}
}

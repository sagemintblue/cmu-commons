package cmu.edu.commons.hadoop.tuple.impl;

import java.util.Iterator;

import org.apache.hadoop.io.WritableComparable;

import cmu.edu.commons.hadoop.tuple.KeyTuple;

public abstract class KeyTupleImpl //
		extends BaseTupleListImpl<WritableComparable<?>> //
		implements KeyTuple {
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	public static int compare(KeyTuple t1, KeyTuple t2) {
		if (t1 == t2) return 0;
		if (t1 == null) {
			if (t2 != null) return -1;
		} else {
			if (t2 == null) return 1;
		}
		Iterator<WritableComparable<?>> itr1 = t1.iterator();
		Iterator<WritableComparable<?>> itr2 = t2.iterator();
		while (itr1.hasNext()) {
			if (!itr2.hasNext()) return 1;
			WritableComparable e1 = itr1.next();
			WritableComparable e2 = itr2.next();
			if (e1 == e2) continue;
			if (e1 == null) {
				if (e2 != null) return -1;
			} else {
				if (e2 == null) return 1;
				int c = e1.compareTo(e2);
				if (c != 0) return c;
			}
		}
		if (itr2.hasNext()) return -1;
		return 0;
	}

	@Override
	public int compareTo(KeyTuple o) {
		return compare(this, o);
	}
}

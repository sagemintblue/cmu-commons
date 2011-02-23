package cmu.edu.commons.hadoop.tuple;

import org.apache.hadoop.io.Writable;

public interface BaseTuple3< //
E extends Writable, //
E1 extends E, //
E2 extends E, //
E3 extends E> //
		extends BaseTuple2<E, E1, E2> {
	public E3 get3();

	public E3 set3(E3 e3);

	public E3 getThird();

	public E3 setThird(E3 e3);
}

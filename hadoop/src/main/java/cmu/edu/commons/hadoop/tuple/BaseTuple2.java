package cmu.edu.commons.hadoop.tuple;

import org.apache.hadoop.io.Writable;

public interface BaseTuple2< //
E extends Writable, //
E1 extends E, //
E2 extends E> //
		extends BaseTuple<E> {
	public E1 get1();

	public E1 set1(E1 e1);

	public E1 getFirst();

	public E1 setFirst(E1 e1);

	public E2 get2();

	public E2 set2(E2 e2);

	public E2 getSecond();

	public E2 setSecond(E2 e2);
}

package edu.cmu.commons.util;

/**
 * A Builder allows (1) configuration of a {@code T} instance--the Builder's
 * "prototype" instance--and (2) repeated construction of duplicates of the
 * prototype instance.
 * 
 * @author hazen
 * @param <T> Type which supports {@link Duplicable} interface.
 */
public abstract class Builder<T extends Duplicable<T>, B extends Builder<T, B>> {
	protected T proto;

	/**
	 * @param proto an initial T instance to clone and use as this Builder's
	 * prototype instance.
	 */
	public Builder(T proto) {
		super();
		if (proto == null) throw new IllegalArgumentException(
				"Argument 'proto' is null");
		this.proto = proto.clone();
	}

	/**
	 * Creates a new (unconfigured) prototype instance. The old prototype instance
	 * is discarded.
	 */
	@SuppressWarnings("unchecked")
	public B reset() {
		try {
			proto = (T) proto.getClass().newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return (B) this;
	}

	/**
	 * @return a duplicate of this Builder's prototype. Generally, this means that
	 * the following boolean expressions are {@code true}:
	 * {@code Builder.build() != Builder.build()},
	 * {@code Builder.build().equals(Builder.build())}, and
	 * {@code Builder.build().hashCode() == Builder.build().hashCode()}. However,
	 * this behavior is not required. Derived classes should document their
	 * behavior.
	 */
	public T build() {
		return proto.clone();
	}
}

package edu.cmu.commons.data.dao.mongo.reflect;

import org.bson.types.Symbol;

import edu.cmu.commons.data.dao.mongo.Marshaller;

/**
 * @author hazen
 * @param <T>
 */
class EnumMarshaller<T extends Enum<T>> implements Marshaller<T, Symbol> {
	private final Class<T> sourceClass;

	public EnumMarshaller(Class<T> sourceClass) {
		this.sourceClass = sourceClass;
	}

	@Override
	public Symbol marshal(T source, Symbol target) {
		return new Symbol(source.name());
	}

	@Override
	public T unmarshal(Symbol target, T source) {
		return Enum.valueOf(sourceClass, target.getSymbol());
	}
}

package edu.cmu.commons.collections;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Immutable List view of a CharSequence.
 * 
 * @author hazen
 */
public class ImmutableCharacterList implements List<Character> {

	private CharSequence chars;

	public ImmutableCharacterList(CharSequence chars) {
		this.chars = chars;
	}

	@Override
	public int hashCode() {
		return chars.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (obj instanceof ImmutableCharacterList) {
			ImmutableCharacterList other = (ImmutableCharacterList) obj;
			return chars.equals(other.chars);
		}
		if (obj instanceof Collection) {
			Collection<?> other = (Collection<?>) obj;
			Iterator<Character> itr1 = iterator();
			Iterator<?> itr2 = other.iterator();
			while (itr1.hasNext()) {
				if (!itr2.hasNext())
					return false;
				if (!itr1.next().equals(itr2.next()))
					return false;
			}
			if (itr2.hasNext())
				return false;
			return true;
		}
		return false;
	}

	protected CharSequence getChars() {
		return chars;
	}

	protected void setChars(CharSequence chars) {
		this.chars = chars;
	}

	@Override
	public boolean isEmpty() {
		return chars.length() == 0;
	}

	@Override
	public int size() {
		return chars.length();
	}

	@Override
	public Character get(int index) {
		return chars.charAt(index);
	}

	@Override
	public boolean contains(Object o) {
		if (!(o instanceof Character))
			throw new IllegalArgumentException("Invalid argument type '"
					+ o.getClass().getName() + "'");
		char c = (Character) o;
		for (int i = 0; i < chars.length(); ++i)
			if (chars.charAt(i) == c)
				return true;
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		for (Object o : c)
			if (!contains(o))
				return false;
		return true;
	}

	@Override
	public int indexOf(Object o) {
		if (!(o instanceof Character))
			throw new IllegalArgumentException("Invalid argument type '"
					+ o.getClass().getName() + "'");
		char c = (Character) o;
		for (int i = 0; i < chars.length(); ++i)
			if (chars.charAt(i) == c)
				return i;
		return -1;
	}

	@Override
	public int lastIndexOf(Object o) {
		if (!(o instanceof Character))
			throw new IllegalArgumentException("Invalid argument type '"
					+ o.getClass().getName() + "'");
		Character c = (Character) o;
		for (int i = size() - 1; i >= 0; --i)
			if (chars.charAt(i) == c)
				return i;
		return -1;
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean add(Character e) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void add(int index, Character element) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addAll(Collection<? extends Character> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addAll(int index, Collection<? extends Character> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean remove(Object o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Character remove(int index) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Character set(int index, Character element) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<Character> subList(int fromIndex, int toIndex) {
		return new ImmutableCharacterList(chars.subSequence(fromIndex, toIndex));
	}

	protected class ImmutableCharacterIterator implements
			ListIterator<Character> {
		protected int cursor;

		public ImmutableCharacterIterator() {
			cursor = -1;
		}

		protected ImmutableCharacterIterator(int index) {
			super();
			this.cursor = index - 1;
		}

		@Override
		public boolean hasNext() {
			return cursor + 1 < chars.length();
		}

		@Override
		public boolean hasPrevious() {
			return cursor >= 0;
		}

		@Override
		public Character next() {
			if (!hasNext())
				throw new NoSuchElementException();
			return chars.charAt(++cursor);
		}

		@Override
		public int nextIndex() {
			if (!hasNext())
				return size();
			return cursor + 1;
		}

		@Override
		public Character previous() {
			if (!hasPrevious())
				throw new NoSuchElementException();
			return chars.charAt(cursor--);
		}

		@Override
		public int previousIndex() {
			if (!hasPrevious())
				return -1;
			return cursor;
		}

		@Override
		public void add(Character e) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

		@Override
		public void set(Character e) {
			throw new UnsupportedOperationException();
		}
	}

	@Override
	public Iterator<Character> iterator() {
		return listIterator();
	}

	@Override
	public ListIterator<Character> listIterator() {
		return new ImmutableCharacterIterator();
	}

	@Override
	public ListIterator<Character> listIterator(int index) {
		if (index < 0 || index >= size())
			throw new IndexOutOfBoundsException();
		return new ImmutableCharacterIterator(index);
	}

	@Override
	public Object[] toArray() {
		Character[] arr = new Character[chars.length()];
		for (int i = 0; i < chars.length(); ++i)
			arr[i] = chars.charAt(i);
		return arr;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T[] toArray(T[] a) {
		if (a.length < chars.length())
			a = (T[]) Array.newInstance(a[0].getClass(), chars.length());
		for (int i = 0; i < chars.length(); ++i)
			Array.setChar(a, i, chars.charAt(i));
		if (a.length > chars.length())
			a[chars.length()] = null;
		return a;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("[");
		if (!isEmpty()) {
			Iterator<Character> itr = iterator();
			sb.append(itr.next());
			while (itr.hasNext())
				sb.append(", ").append(itr.next());
		}
		return sb.append("]").toString();
	}
}

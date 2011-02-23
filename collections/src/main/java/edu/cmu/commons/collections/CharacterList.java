package edu.cmu.commons.collections;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

/**
 * Modifiable List view of a CharSequence. Uses StringBuilder internally.
 * @author hazen
 */
public class CharacterList extends ImmutableCharacterList {

	public CharacterList(CharSequence chars) {
		super(chars instanceof StringBuilder ? chars : new StringBuilder(chars));
	}

	@Override
	protected StringBuilder getChars() {
		return (StringBuilder) super.getChars();
	}

	@Override
	public void clear() {
		setChars(new StringBuilder());
	}

	@Override
	public boolean add(Character e) {
		getChars().append(e);
		return true;
	}

	@Override
	public void add(int index, Character element) {
		getChars().insert(index, element);
	}

	@Override
	public boolean addAll(Collection<? extends Character> c) {
		if (c.isEmpty()) return false;
		for (Character e : c)
			getChars().append(e);
		return true;
	}

	@Override
	public boolean addAll(int index, Collection<? extends Character> c) {
		if (c.isEmpty()) return false;
		StringBuilder sb = new StringBuilder();
		for (Character e : c)
			sb.append(e);
		getChars().insert(index, sb);
		return true;
	}

	@Override
	public boolean remove(Object o) {
		if (o == null) return false;
		int i = getChars().indexOf(String.valueOf(o));
		if (i < 0) return false;
		getChars().deleteCharAt(i);
		return true;
	}

	@Override
	public Character remove(int index) {
		char c = getChars().charAt(index);
		getChars().deleteCharAt(index);
		return c;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		Set<Object> set = new HashSet<Object>(c);
		Iterator<Character> itr = iterator();
		boolean changed = false;
		while (itr.hasNext()) {
			Character e = itr.next();
			if (set.contains(e)) {
				itr.remove();
				changed = true;
			}
		}
		return changed;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		Set<Object> set = new HashSet<Object>(c);
		Iterator<Character> itr = iterator();
		boolean changed = false;
		while (itr.hasNext()) {
			Character e = itr.next();
			if (!set.contains(e)) {
				itr.remove();
				changed = true;
			}
		}
		return changed;
	}

	@Override
	public Character set(int index, Character element) {
		char c = getChars().charAt(index);
		getChars().setCharAt(index, element);
		return c;
	}

	@Override
	public List<Character> subList(int fromIndex, int toIndex) {
		return new CharacterList(getChars().subSequence(fromIndex, toIndex));
	}

	protected class CharacterIterator extends ImmutableCharacterIterator {
		public CharacterIterator() {
			super();
		}

		protected CharacterIterator(int index) {
			super(index);
		}

		@Override
		public void add(Character e) {
			getChars().insert(cursor, e);
		}

		@Override
		public void remove() {
			getChars().deleteCharAt(cursor);
		}

		@Override
		public void set(Character e) {
			getChars().setCharAt(cursor, e);
		}
	}

	@Override
	public ListIterator<Character> listIterator() {
		return new CharacterIterator();
	}

	@Override
	public ListIterator<Character> listIterator(int index) {
		return new CharacterIterator(index);
	}
}

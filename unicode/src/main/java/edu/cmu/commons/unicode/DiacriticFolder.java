package edu.cmu.commons.unicode;

import java.io.IOException;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 * A Folder which strips input characters of all diacritics.
 */
public class DiacriticFolder
{
	protected static class Node
	{
		char result = 0;
		Map<Character, Node> mapping = null;
	}

	protected Node root = new Node();
	protected int maxDepth = 0;

	public DiacriticFolder() throws IOException
	{
		this(Folding.getDiacriticFolding());
	}

	public DiacriticFolder(Folding folding)
	{
		parse(folding);
	}

	protected void parse(Folding folding)
	{
		for (Folding.Entry e : folding.entries) {
			Node parent = root;
			Node child = null;
			for (char c : e.key) {
				if (parent.mapping == null) parent.mapping =
						new HashMap<Character, Node>();
				child = parent.mapping.get(c);
				if (child == null) {
					child = new Node();
					parent.mapping.put(c, child);
				}
				parent = child;
			}
			child.result = e.value;
			if (maxDepth < e.key.size()) maxDepth = e.key.size();
		}
	}

	public String fold(String s)
	{
		if (s.length() == 0) return s;

		StringBuilder sb = new StringBuilder();
		Queue<Character> chars = new LinkedList<Character>();
		CharacterIterator itr = new StringCharacterIterator(s);
		while (true) {

			// while we have more input, top off the char sequence.

			char c = itr.current();
			while (chars.size() < maxDepth && c != CharacterIterator.DONE) {
				chars.add(c);
				c = itr.next();
			}

			// find longest sequence of chars for which a mapping is defined.

			Node parent = root;
			int n = 0;
			for (char c1 : chars) {
				if (parent.mapping == null) break;
				Node child = parent.mapping.get(c1);
				if (child == null) break;
				parent = child;
				n++;
			}

			// parent now points at Node supporting mapping of longest char
			// sequence and n is the number of chars supported by this mapping.
			// now output the mapping result and remove those chars from
			// sequence which have been mapped.

			if (n == 0 || parent.result == 0) {

				// there was no mapping for this char sequence. output the first
				// char in sequence and shift.

				sb.append(chars.poll());

			} else {

				// remove mapped sequence of chars from queue and append mapping
				// result to output.

				for (int i = 0; i < n; i++)
					chars.poll();
				sb.append(parent.result);

			}

			// terminate when there is no more input to process.

			if (c == CharacterIterator.DONE && chars.isEmpty()) break;
		}

		return sb.toString();
	}

	public static void main(String[] args) throws IOException
	{
		DiacriticFolder folder = new DiacriticFolder();
		for (char c = 0x00C0; c <= 0x00ff; c++) {
			String s = Character.toString(c);
			System.out.print(s + ": ");
			for (char c1 : s.toCharArray())
				System.out.print(String.format("%04x ", (int) c1));
			System.out.println(": " + folder.fold(s));
		}
	}
}

package edu.cmu.commons.unicode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * A Folder which effectively strips input characters of all diacritics and case
 * distinctions, based on the UNICODE "DUCET" (Default UNICODE Collation Element
 * Table).
 */
public class CollationFolder implements Folder
{
	protected Collation.Version collationVersion;

	/**
	 * UTF-32 code points mapped to replacement strings.
	 */
	protected Map<Integer, String> mapping;

	public CollationFolder(Collation collation) throws Exception
	{
		collationVersion = collation.version;
		parseData(collation);
	}

	public CollationFolder() throws Exception
	{
		this(Collation.getDefaultInstance());
	}

	protected void parseData(Collation collation) throws Exception
	{
		// initialize transient data structures needed during parsing. These
		// should be garbage collected once parsing is complete.

		// recursive map from sequence of primaryWeights to single code point.
		class Node
		{
			Integer result = null;
			Map<Integer, Node> mapping = new HashMap<Integer, Node>();
		}
		Node root = new Node();

		// map from code point to String containing one or more code points.
		mapping = new HashMap<Integer, String>();

		// our collation contains a sequence of entries which map a utf32 code
		// point to a sequence of collation elements, each having a sequence of
		// 3 or more weights (equivalence classes).

		// The first (or "primary") equivalence class of each collation element
		// can be mapped back to a specific utf32 code point as follows: Define
		// the utf32 code point (U) supporting a given primary equivalence class
		// (C) as the utf32 code point of the first entry found in the collation
		// which contains a single collation element whose primary equivalence
		// class is equal to C.

		// we want to produce a mapping of utf32 code points to 1-or-more utf32
		// code points (int to string). contraction mappings are ignored at
		// present (there may not be any in DUCET anyway). these code points
		// might then be expanded into sequences of code units (utf16) for
		// direct application to Java chars.

		// another wrinkle: some of the equivalence class to string mappings
		// must be defined in terms of multiple integers-- a sequence of
		// classes. This implies that collation entries should be processed in
		// ascending size of their collation element lists, then document order.
		// This could be accomplished by repeatedly iterating over a work queue
		// of entries, processing only those entries of current length (starting
		// from 1). once a given entry has been processed it is removed from the
		// queue. TODO this algorithm might be improved by properly sorting
		// collation entries first.

		List<Collation.Entry> entries = new ArrayList<Collation.Entry>(
				collation.entries);
		int numElements = 1;
		while (!entries.isEmpty()) {
			Iterator<Collation.Entry> itr = entries.iterator();
			while (itr.hasNext()) {
				Collation.Entry entry = itr.next();
				if (entry.elements.size() != numElements) continue;

				// we're now processing collation entries which have numElements
				// elements. we've already processed all collation entries
				// having fewer elements. we want to figure out which, if any,
				// existing equivalence classes support this entry's elements.
				// When consuming elements, we need to find the longest matching
				// sequence which is supported by some existing mapping. If no
				// mapping is found (i.e. we check for all possible matches of
				// length numElements and shorter but find nothing), then we
				// should create a new mapping whose length is numElements.
				// Collation elements with zero primary weight should be
				// ignored.

				int codePoint = entry.codePoint;
				StringBuilder sb = new StringBuilder();

				Queue<Collation.Element> elements = new LinkedList<Collation.Element>();
				for (Collation.Element e : entry.elements)
					if (e.weights.get(0) != 0) elements.add(e);
				while (!elements.isEmpty()) {

					// find max length mapping.

					int depth = 0;
					Node parent = root;
					for (Collation.Element e : elements) {
						int primaryWeight = e.weights.get(0);
						Node child = parent.mapping.get(primaryWeight);
						if (child == null) break;
						parent = child;
						depth++;
					}

					// parent is now max length mapping for depth collation
					// elements.

					if (depth == 0 || parent.result == null) {

						// no mapping was found for elements in queue. create
						// new mapping using all elements.

						parent = root;
						for (Collation.Element e : elements) {
							Node child = new Node();
							int primaryWeight = e.weights.get(0);
							parent.mapping.put(primaryWeight, child);
							parent = child;
						}
						parent.result = codePoint;
						break;

						// we don't append anything here to result string
						// because this would result in an identity mapping,
						// which we'd just remove at a later stage.

					} else {

						// depth collation elements were accounted for by some
						// existing mapping. append code point to result string.

						for (int i = 0; i < depth; i++)
							elements.poll();
						sb.append(Character.toChars(parent.result));

					}
				}

				if (sb.length() != 0) {

					// result string is not empty, so we have a non-identity
					// mapping for the current code point. add it to global
					// mapping.

					mapping.put(codePoint, sb.toString());

				}

				itr.remove();
			}

			// we've made a full pass of current entry list, processing all
			// entries having numElements elements. time to iterate.

			numElements++;
		}
	}

	public Collation.Version getCollationVersion()
	{
		return collationVersion;
	}

	public String fold(String s)
	{
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < s.length(); ++i) {
			int codePoint = s.codePointAt(i);
			String value = mapping.get(codePoint);
			if (value != null) {
				sb.append(value);
			} else {
				sb.append(s.charAt(i));
				if (codePoint > 0xFFFF) {
					// codePoint represents a supplementary character. append following
					// low surrogate, which is guaranteed to exist due to the semantics of
					// String.codePointAt().
					sb.append(s.charAt(++i));
				}
			}
		}
		return sb.toString();
	}

	public static void main(String[] args) throws Exception
	{
		CollationFolder folder = new CollationFolder();
		System.out.println("Version: " + folder.getCollationVersion());
		for (char c = 0x0041; c <= 0x00ff; c++) {
			String s = Character.toString(c);
			print(s, folder);
		}
		char[] supplemental = Character.toChars(0x2F800);
		print(new String(supplemental), folder);
		char[] malformed = new char[1];
		malformed[0] = supplemental[0];
		print(new String(malformed), folder);
	}

	public static void print(String s, Folder folder)
	{
		System.out.print(s + ": ");
		for (char c1 : s.toCharArray())
			System.out.print(String.format("%04x ", (int) c1));
		System.out.println(": " + folder.fold(s));
	}
}

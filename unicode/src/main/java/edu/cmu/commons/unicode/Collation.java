package edu.cmu.commons.unicode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class encapsulates collation data as defined by
 * http://www.unicode.org/reports/tr10/#File_Format.
 */
public class Collation {
	/**
	 * Collation version.
	 */
	public static class Version {
		public int major = 0;
		public int minor = 0;
		public int variant = 0;

		public String toString() {
			return major + "." + minor + "." + variant;
		}
	}

	/**
	 * A collation entry.
	 */
	public static class Entry {
		public int codePoint = 0;
		public List<Element> elements = new ArrayList<Element>();

		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append(String.format("%04x : ", codePoint));
			for (Element e : elements)
				sb.append(e);
			return sb.toString();
		}
	}

	/**
	 * A collation element.
	 */
	public static class Element {
		/**
		 * Valid collation element weighting schemes as defined by
		 * http://www.unicode.org/reports/tr10/
		 */
		public static enum Weighting {
			DEFAULT, VARIABLE;
		}

		public Weighting weighting = Weighting.DEFAULT;
		public List<Integer> weights = new ArrayList<Integer>();

		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("[");
			if (weighting == Weighting.DEFAULT) sb.append('.');
			else sb.append('*');
			Iterator<Integer> itr = weights.iterator();
			if (itr.hasNext()) {
				int w = itr.next();
				sb.append(String.format("%04x", w));
				while (itr.hasNext()) {
					w = itr.next();
					sb.append(String.format(".%04x", w));
				}
			}
			sb.append("]");
			return sb.toString();
		}
	}

	public Version version = new Version();
	public List<Entry> entries = new ArrayList<Entry>();

	protected Collation() {}

	public static Collation getDefaultInstance() throws IOException {
		return parse(Collation.class.getResource("data/unicode-ducet.txt"));
	}

	/**
	 * Parses a collation data file at the specified url, returning a Collation
	 * object.
	 * @param url a URL from which to load and parse a collation data file.
	 * @return the parsed Collation object.
	 * @throws IOException
	 */
	public static Collation parse(URL url) throws IOException {
		Collation collation = new Collation();

		// initialize transient data structures needed during parsing. These
		// should be garbage collected once parsing is complete.

		Pattern versionPattern =
				Pattern.compile("^@version\\s+(\\d+)\\.(\\d+)\\.(\\d+)");
		Pattern commentPattern = Pattern.compile("#.*$");
		Pattern entryPattern =
				Pattern.compile("^(\\p{XDigit}+)\\s*;((?:\\s*\\[.+?\\])+)");
		Pattern elementPattern =
				Pattern.compile("\\[([\\.\\*])"
						+ "(\\p{XDigit}+(?:\\.\\p{XDigit}+){2,})\\]");

		// open collation data for input.

		BufferedReader br =
				new BufferedReader(new InputStreamReader(url.openStream()));
		String line = null;

		// read header and version info.

		while ((line = br.readLine()) != null) {
			if (line.isEmpty()) continue;
			line = commentPattern.matcher(line).replaceAll("").trim();
			if (line.isEmpty()) continue;
			Matcher matcher = versionPattern.matcher(line);
			if (!matcher.find()) throw new IOException(
					"No leading version line found in data file");
			collation.version.major = Integer.parseInt(matcher.group(1));
			collation.version.minor = Integer.parseInt(matcher.group(2));
			collation.version.variant = Integer.parseInt(matcher.group(3));
			break;
		}

		// now read entries.

		while ((line = br.readLine()) != null) {
			if (line.isEmpty()) continue;
			line = commentPattern.matcher(line).replaceAll("").trim();
			if (line.isEmpty()) continue;
			Matcher matcher = entryPattern.matcher(line);
			if (!matcher.find()) continue;
			Entry entry = new Entry();
			collation.entries.add(entry);
			entry.codePoint = Integer.parseInt(matcher.group(1), 16);
			String elements = matcher.group(2);
			matcher = elementPattern.matcher(elements);
			while (matcher.find()) {
				Element element = new Element();
				entry.elements.add(element);
				if (matcher.group(1).equals("*")) element.weighting =
						Element.Weighting.VARIABLE;
				for (String weight : matcher.group(2).split("\\."))
					element.weights.add(Integer.parseInt(weight, 16));
			}
		}

		// close input and return collation.

		br.close();
		return collation;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Version: ").append(version).append("\n");
		for (Entry e : entries)
			sb.append(e).append("\n");
		return sb.toString();
	}

	public static void main(String[] args) throws IOException {
		Collation collation = Collation.getDefaultInstance();
		System.out.println(collation);
	}
}

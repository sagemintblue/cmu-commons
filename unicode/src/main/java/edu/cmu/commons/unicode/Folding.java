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
 * This class encapsulates folding data as defined by
 * http://www.unicode.org/reports/tr30/.
 */
public class Folding
{
	public static class Entry
	{
		public List<Character> key = new ArrayList<Character>();
		public char value;

		public String toString()
		{
			StringBuilder sb = new StringBuilder();
			Iterator<Character> itr = key.iterator();
			if (itr.hasNext()) sb.append(String.format("%04x", (int) itr.next()));
			while (itr.hasNext())
				sb.append(String.format(" %04x", (int) itr.next()));
			sb.append(String.format("; %04x", (int) value));
			return sb.toString();
		}
	}

	public List<Entry> entries = new ArrayList<Entry>();

	protected Folding()
	{}

	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		for (Entry e : entries)
			sb.append(e).append("\n");
		return sb.toString();
	}

	/**
	 * @return Folding containing data from local copy of
	 * http://www.unicode.org/reports/tr30/#DiacriticFolding
	 * @throws IOException
	 */
	public static Folding getDiacriticFolding() throws IOException
	{
		return parse(Folding.class.getResource("data/unicode-diacritics.txt"));
	}

	/**
	 * Parses a folding data file at the specified url, returning a Folding
	 * object.
	 * @param url the URL from which to load the folding data.
	 * @return a Folding object parsed from the data.
	 * @throws IOException
	 */
	public static Folding parse(URL url) throws IOException
	{
		Folding folding = new Folding();
		Pattern commentPattern = Pattern.compile("#.*$");
		Pattern entryPattern =
				Pattern.compile("^\\s*" + "(\\p{XDigit}{4}(?:\\s+\\p{XDigit}{4})*)"
						+ "\\s*;\\s*" + "(\\p{XDigit}{4})" + "\\s*$");
		BufferedReader br =
				new BufferedReader(new InputStreamReader(url.openStream()));
		String line = null;
		while ((line = br.readLine()) != null) {
			if (line.isEmpty()) continue;
			line = commentPattern.matcher(line).replaceAll("").trim();
			if (line.isEmpty()) continue;
			Matcher matcher = entryPattern.matcher(line);
			if (!matcher.matches()) throw new IOException(
					"Invalid entry line encountered: '" + line + "'");
			Entry entry = new Entry();
			folding.entries.add(entry);
			entry.value = (char) Integer.parseInt(matcher.group(2), 16);
			for (String codeUnit : matcher.group(1).split("\\s+"))
				entry.key.add((char) Integer.parseInt(codeUnit, 16));
		}
		return folding;
	}

	public static void main(String[] args) throws IOException
	{
		Folding folding = Folding.getDiacriticFolding();
		System.out.println(folding);
	}
}

package edu.cmu.commons.collections.trie;

import edu.cmu.commons.collections.trie.TreeTrie;

/**
 * @author hazen
 */
public class TreeTrieTest extends
		AbstractNavigableTrieTest<TreeTrie<Character, Integer>> {
	@Override
	public TreeTrie<Character, Integer> createTrie() {
		return new TreeTrie<Character, Integer>();
	}
}

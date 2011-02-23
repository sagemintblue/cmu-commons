package edu.cmu.commons.collections.trie;

import edu.cmu.commons.collections.trie.HashTrie;

/**
 * @author hazen
 */
public class HashTrieTest extends
		AbstractTrieTest<HashTrie<Character, Integer>> {
	@Override
	public HashTrie<Character, Integer> createTrie() {
		return new HashTrie<Character, Integer>();
	}
}

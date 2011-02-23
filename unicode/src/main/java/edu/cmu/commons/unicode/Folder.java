package edu.cmu.commons.unicode;

/**
 * A Folder "folds" an input string into some simpler representation. For
 * instance, a CaseFolder might fold all input characters to a specific case
 * (upper or lower), no matter what their original case. Usually a fold
 * operation results in information loss.
 */
public interface Folder
{
	/**
	 * @param string an input string to fold.
	 * @return a folded version of the input string.
	 */
	public String fold(String string);
}

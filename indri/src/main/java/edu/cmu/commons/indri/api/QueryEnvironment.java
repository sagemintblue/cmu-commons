package edu.cmu.commons.indri.api;

import java.rmi.Remote;

/**
 * A Remote interface for selected entry points on Indri JNI's QueryEnvironment
 * class. These methods rely on Serializable alternatives to Indri JNI's data
 * structures.
 */
public interface QueryEnvironment extends Remote {
	/**
	 * @param request a QueryRequest containing all search parameters.
	 * @return a QueryResult containing all search results.
	 * @throws Exception
	 */
	public QueryResult runQuery(QueryRequest request) throws Exception;

	/**
	 * Returns scores on extents instead of documents.
	 * @param queryString an Indri query string.
	 * @param numResults number of scored extents to return.
	 * @return an array of scored extent objects.
	 * @throws Exception
	 */
	public ScoredExtent[] runQuery(String queryString, int numResults)
			throws Exception;

	/**
	 * Returns scores on extents within a specific set of documents.
	 * @param queryString an Indri query string.
	 * @param documentIds an array of integer document identifiers.
	 * @param numResults number of scored extents to return.
	 * @return an array of scored extent objects.
	 * @throws Exception
	 */
	public ScoredExtent[] runQuery(String queryString, int[] documentIds,
			int numResults) throws Exception;

	/**
	 * @return an array of field names defined within the the aggregated
	 * collection.
	 * @throws Exception
	 */
	public String[] fieldList() throws Exception;

	/**
	 * @return total number of documents in the aggregated collection.
	 * @throws Exception
	 */
	public long documentCount() throws Exception;

	/**
	 * @param term a term.
	 * @return total number of documents containing the term in the aggregated
	 * collection.
	 * @throws Exception
	 */
	public long documentCount(String term) throws Exception;

	/**
	 * @param documentId a document identifier.
	 * @return the length of the document identified by <code>documentId</code>.
	 * @throws Exception
	 */
	public int documentLength(int documentId) throws Exception;

	/**
	 * @param documentIds an array of document identifiers.
	 * @param attributeName a metadata attribute name.
	 * @return an array of attribute values for <code>attributeName</code>, one
	 * for each input document.
	 * @throws Exception
	 */
	public String[] documentMetadata(int[] documentIds, String attributeName)
			throws Exception;

	/**
	 * @param attributeName a metadata attribute name.
	 * @param attributeValues an array of values associated with
	 * <code>attributeName</code>.
	 * @return an array of document IDs where the document has a metadata key that
	 * matches <code>attributeName</code>, with a value matching one of the
	 * <code>attributeValues</code>.
	 * @throws Exception
	 */
	public int[] documentIDsFromMetadata(String attributeName,
			String[] attributeValues) throws Exception;

	/**
	 * @return total number of terms in the aggregated collection.
	 * @throws Exception
	 */
	public long termCount() throws Exception;

	/**
	 * @param term a term.
	 * @return total frequency of this term in the aggregated collection.
	 * @throws Exception
	 */
	public long termCount(String term) throws Exception;

	/**
	 * @param term
	 * @param fieldName
	 * @return total frequency of this term within the field specified by
	 * <code>fieldName</code> in the aggregated collection.
	 * @throws Exception
	 */
	public long termFieldCount(String term, String fieldName) throws Exception;

	/**
	 * @param expression a valid Indri query.
	 * @return total frequency of this expression in the aggregated collection.
	 * @throws Exception
	 */
	public double expressionCount(String expression) throws Exception;

	/**
	 * @param expression a valid Indri query.
	 * @param expressionType unknown-- not specified by Indri JNI.
	 * @return total frequency of this expression in the aggregated collection.
	 * @throws Exception
	 */
	public double expressionCount(String expression, String expressionType)
			throws Exception;
}

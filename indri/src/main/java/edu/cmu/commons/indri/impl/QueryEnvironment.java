package edu.cmu.commons.indri.impl;

import java.util.ArrayList;
import java.util.List;

import edu.cmu.commons.indri.api.QueryRequest;
import edu.cmu.commons.indri.api.QueryResult;
import edu.cmu.commons.indri.api.ScoredDocument;
import edu.cmu.commons.indri.api.ScoredExtent;
import edu.cmu.commons.indri.api.QueryRequest.SmoothingRule;

/**
 * Wraps a lemurproject.indri.QueryEnvironment object to support the RMI
 * interface specified by {@link edu.cmu.commons.indri.api.QueryEnvironment}.
 */
public class QueryEnvironment implements
		edu.cmu.commons.indri.api.QueryEnvironment {
	private String indexPath;
	private lemurproject.indri.QueryEnvironment env;

	public String getIndexPath() {
		return indexPath;
	}

	public void setIndexPath(String indexPath) {
		this.indexPath = indexPath;
	}

	public void initialize() throws Exception {
		env = new lemurproject.indri.QueryEnvironment();
		env.addIndex(indexPath);
	}

	protected ScoredExtent[] convert(
			lemurproject.indri.ScoredExtentResult[] results) {
		ScoredExtent[] scoredExtents = new ScoredExtent[results.length];
		int i = 0;
		for (lemurproject.indri.ScoredExtentResult result : results)
			scoredExtents[i++] =
					new ScoredExtent(result.document, result.begin, result.end,
							result.score);
		return scoredExtents;
	}

	protected lemurproject.indri.QueryRequest convert(QueryRequest request) {
		lemurproject.indri.QueryRequest convertedRequest =
				new lemurproject.indri.QueryRequest();
		convertedRequest.query = request.getQueryString();
		convertedRequest.resultsRequested = request.getNumResults();
		convertedRequest.startNum = request.getStartOffset();
		convertedRequest.formulators = request.getFormulators();
		convertedRequest.metadata = request.getMetadata();
		convertedRequest.options = 0;
		switch (request.getSnippetType()) {
		case HTML:
			convertedRequest.options = lemurproject.indri.QueryRequest.HTMLSnippet;
			break;
		case TEXT:
			convertedRequest.options = lemurproject.indri.QueryRequest.TextSnippet;
			break;
		}
		return convertedRequest;
	}

	protected QueryResult convert(lemurproject.indri.QueryResults results) {
		List<ScoredDocument> scoredDocuments =
				new ArrayList<ScoredDocument>(results.results.length);
		for (lemurproject.indri.QueryResult result : results.results)
			scoredDocuments.add(new ScoredDocument(result.docid, result.documentName,
					result.score));
		QueryResult convertedResult =
				new QueryResult(results.estimatedMatches, results.parseTime,
						results.executeTime, results.documentsTime, scoredDocuments);
		return convertedResult;
	}

	public synchronized QueryResult runQuery(QueryRequest request)
			throws Exception {
		synchronized (env) {
			try {
				SmoothingRule smoothingRule = request.getSmoothingRule();
				if (smoothingRule != null) {
					// set smoothing (scoring) rule
					env.setScoringRules(new String[] { "method:"
							+ smoothingRule.toString() });
				}
				return convert(env.runQuery(convert(request)));
			} finally {
				// reset scoring rules
				env.setScoringRules(new String[] {});
			}
		}
	}

	public synchronized ScoredExtent[] runQuery(String queryString, int numResults)
			throws Exception {
		synchronized (env) {
			return convert(env.runQuery(queryString, numResults));
		}
	}

	public synchronized ScoredExtent[] runQuery(String queryString,
			int[] documentIds, int numResults) throws Exception {
		synchronized (env) {
			return convert(env.runQuery(queryString, documentIds, numResults));
		}
	}

	public synchronized String[] fieldList() throws Exception {
		synchronized (env) {
			return env.fieldList();
		}
	}

	public synchronized long documentCount() throws Exception {
		synchronized (env) {
			return env.documentCount();
		}
	}

	public synchronized long documentCount(String term) throws Exception {
		synchronized (env) {
			return env.documentCount(term);
		}
	}

	public synchronized int[] documentIDsFromMetadata(String attributeName,
			String[] attributeValues) throws Exception {
		synchronized (env) {
			return env.documentIDsFromMetadata(attributeName, attributeValues);
		}
	}

	public synchronized int documentLength(int documentId) throws Exception {
		synchronized (env) {
			return env.documentLength(documentId);
		}
	}

	public synchronized String[] documentMetadata(int[] documentIds,
			String attributeName) throws Exception {
		synchronized (env) {
			return env.documentMetadata(documentIds, attributeName);
		}
	}

	public synchronized long termCount() throws Exception {
		synchronized (env) {
			return env.termCount();
		}
	}

	public synchronized long termCount(String term) throws Exception {
		synchronized (env) {
			return env.termCount(term);
		}
	}

	public synchronized long termFieldCount(String term, String fieldName)
			throws Exception {
		synchronized (env) {
			return env.termFieldCount(term, fieldName);
		}
	}

	public synchronized double expressionCount(String expression,
			String expressionType) throws Exception {
		synchronized (env) {
			return env.expressionCount(expression, expressionType);
		}
	}

	public synchronized double expressionCount(String expression)
			throws Exception {
		synchronized (env) {
			return env.expressionCount(expression);
		}
	}
}

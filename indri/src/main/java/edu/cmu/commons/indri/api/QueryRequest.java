package edu.cmu.commons.indri.api;

import java.io.Serializable;

public class QueryRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	public static interface SmoothingRule extends Serializable {}

	public static class DirichletSmoothingRule implements SmoothingRule {
		private static final long serialVersionUID = 1L;
		private double mu = 2500;
		private double documentMu = 1e30;

		public double getMu() {
			return mu;
		}

		public void setMu(double mu) {
			this.mu = mu;
		}

		public double getDocumentMu() {
			return documentMu;
		}

		public void setDocumentMu(double documentMu) {
			this.documentMu = documentMu;
		}

		@Override
		public String toString() {
			return "dirichlet,mu:" + mu + "docmu:" + documentMu;
		}
	}

	public static class JelinekMercerSmoothingRule implements SmoothingRule {
		private static final long serialVersionUID = 1L;
		private double collectionLambda = 0.4;
		private double documentLambda = 0.0;

		public double getCollectionLambda() {
			return collectionLambda;
		}

		public void setCollectionLambda(double collectionLambda) {
			this.collectionLambda = collectionLambda;
		}

		public double getDocumentLambda() {
			return documentLambda;
		}

		public void setDocumentLambda(double documentLambda) {
			this.documentLambda = documentLambda;
		}

		@Override
		public String toString() {
			return "linear,collectionLambda:" + collectionLambda + "documentLambda:"
					+ documentLambda;
		}
	}

	public static class TwoStageSmoothingRule implements SmoothingRule {
		private static final long serialVersionUID = 1L;
		private double mu = 2500;
		private double lambda = 0.4;

		public double getMu() {
			return mu;
		}

		public void setMu(double mu) {
			this.mu = mu;
		}

		public double getLambda() {
			return lambda;
		}

		public void setLambda(double lambda) {
			this.lambda = lambda;
		}

		@Override
		public String toString() {
			return "twostage,mu:" + mu + "lambda:" + lambda;
		}
	}

	public static enum SnippetType {
		NONE, TEXT, HTML
	}

	protected String queryString = null;
	protected SmoothingRule smoothingRule = null;
	protected int numResults = 10;
	protected int startOffset = 0;
	protected String[] formulators = null;
	protected String[] metadata = null;
	protected SnippetType snippetType = SnippetType.NONE;

	public QueryRequest() {}

	public QueryRequest(String queryString) {
		this.queryString = queryString;
	}

	public QueryRequest(String queryString, int numResults) {
		this.queryString = queryString;
		this.numResults = numResults;
	}

	public QueryRequest(String queryString, int numResults, int startOffset) {
		this.queryString = queryString;
		this.numResults = numResults;
		this.startOffset = startOffset;
	}

	public String getQueryString() {
		return queryString;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	public SmoothingRule getSmoothingRule() {
		return smoothingRule;
	}

	public void setSmoothingRule(SmoothingRule smoothingRule) {
		this.smoothingRule = smoothingRule;
	}

	public int getNumResults() {
		return numResults;
	}

	public void setNumResults(int numResults) {
		this.numResults = numResults;
	}

	public int getStartOffset() {
		return startOffset;
	}

	public void setStartOffset(int startOffset) {
		this.startOffset = startOffset;
	}

	public String[] getFormulators() {
		return formulators;
	}

	public void setFormulators(String[] formulators) {
		this.formulators = formulators;
	}

	public String[] getMetadata() {
		return metadata;
	}

	public void setMetadata(String[] metadata) {
		this.metadata = metadata;
	}

	public SnippetType getSnippetType() {
		return snippetType;
	}

	public void setSnippetType(SnippetType snippetType) {
		this.snippetType = snippetType;
	}
}

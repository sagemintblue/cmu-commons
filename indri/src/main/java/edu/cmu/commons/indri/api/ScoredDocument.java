package edu.cmu.commons.indri.api;

import java.io.Serializable;

public class ScoredDocument implements Serializable
{
	private static final long serialVersionUID = 1L;

	protected int documentId;
	protected String documentName;
	protected double score;

	public ScoredDocument(int documentId, String documentName, double score)
	{
		this.documentId = documentId;
		this.documentName = documentName;
		this.score = score;
	}

	public ScoredDocument()
	{}

	public int getDocumentId()
	{
		return documentId;
	}

	public String getDocumentName()
	{
		return documentName;
	}

	public double getScore()
	{
		return score;
	}

	public String toString()
	{
		return "QueryResult[documentId=" + documentId + ", documentName="
				+ documentName + ", score=" + score + "]";
	}
}

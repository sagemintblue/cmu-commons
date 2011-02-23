package edu.cmu.commons.indri.api;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

public class QueryResult implements Serializable, Iterable<ScoredDocument>
{
	private static final long serialVersionUID = 1L;

	protected int estimatedMatches;
	protected double parseTime;
	protected double executeTime;
	protected double documentsTime;
	protected List<ScoredDocument> scoredDocuments;

	public QueryResult(int estimatedMatches, double parseTime,
			double executeTime, double documentsTime,
			List<ScoredDocument> scoredDocuments)
	{
		this.estimatedMatches = estimatedMatches;
		this.parseTime = parseTime;
		this.executeTime = executeTime;
		this.documentsTime = documentsTime;
		this.scoredDocuments = scoredDocuments;
	}

	public QueryResult()
	{}

	public int getEstimatedMatches()
	{
		return estimatedMatches;
	}

	public double getParseTime()
	{
		return parseTime;
	}

	public double getExecuteTime()
	{
		return executeTime;
	}

	public double getDocumentsTime()
	{
		return documentsTime;
	}

	public List<ScoredDocument> getScoredDocuments()
	{
		return scoredDocuments;
	}

	public Iterator<ScoredDocument> iterator()
	{
		return scoredDocuments.iterator();
	}

	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName() + "[\n  estimatedMatches="
				+ estimatedMatches + ",\n  parseTime=" + parseTime
				+ ",\n  executeTime=" + executeTime + ",\n  documentsTime="
				+ documentsTime + ",");
		int i = 0;
		for (ScoredDocument result : scoredDocuments) {
			sb.append("\n  ").append(result);
			if (++i > 10) {
				sb.append("\n  ... (" + scoredDocuments.size() + " total)");
				break;
			}
		}
		sb.append("\n]\n");
		return sb.toString();
	}
}

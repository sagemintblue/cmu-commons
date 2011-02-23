package edu.cmu.commons.indri.api;

import java.io.Serializable;

public class ScoredExtent implements Serializable
{
	private static final long serialVersionUID = 1L;

	protected int docid;
	protected int begin;
	protected int end;
	protected double score;

	public ScoredExtent(int docid, int begin, int end, double score)
	{
		this.docid = docid;
		this.begin = begin;
		this.end = end;
		this.score = score;
	}

	public ScoredExtent()
	{}

	public int getDocid()
	{
		return docid;
	}

	public int getBegin()
	{
		return begin;
	}

	public int getEnd()
	{
		return end;
	}

	public double getScore()
	{
		return score;
	}
}

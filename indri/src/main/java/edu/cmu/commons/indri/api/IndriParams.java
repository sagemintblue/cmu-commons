package edu.cmu.commons.indri.api;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import edu.cmu.commons.indri.api.QueryRequest.DirichletSmoothingRule;
import edu.cmu.commons.indri.api.QueryRequest.JelinekMercerSmoothingRule;
import edu.cmu.commons.indri.api.QueryRequest.SmoothingRule;
import edu.cmu.commons.indri.api.QueryRequest.TwoStageSmoothingRule;

public class IndriParams implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * from Indri src/Repository.cpp:
	 * 
	 * <pre>
	 * const static int defaultMemory = 100*1024*1024;
	 * </pre>
	 */
	public static final long DEFAULT_MEMORY = 100 * 1024 * 1024;

	/**
	 * from Indri runquery/runquery.cpp:
	 * 
	 * <pre>
	 * _requested = _parameters.get(&quot;count&quot;, 1000);
	 * </pre>
	 */
	public static final int DEFAULT_COUNT = 1000;

	public static IndriParams parseXML(InputStream inputStream) throws Exception {

		// parse XML
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder parser = factory.newDocumentBuilder();
		Document indriParamsDocument = parser.parse(inputStream);

		IndriParams params = new IndriParams();

		// init index path
		Element indexPathElement =
				(Element) indriParamsDocument.getElementsByTagName("index").item(0);
		if (indexPathElement == null) throw new IllegalArgumentException(
				"Missing required element 'index'");
		params.indexPath = new File(indexPathElement.getTextContent());

		// init memory
		Element memoryElement =
				(Element) indriParamsDocument.getElementsByTagName("memory").item(0);
		if (memoryElement != null) {
			String memoryString = memoryElement.getTextContent();
			if (!memoryString.isEmpty()) params.memory = parseMemory(memoryString);
		}

		// init count
		Element countElement =
				(Element) indriParamsDocument.getElementsByTagName("count").item(0);
		if (countElement != null) {
			String countString = countElement.getTextContent();
			if (!countString.isEmpty()) {
				params.count = Integer.parseInt(countString);
			}
		}

		// init smoothing rule
		Element ruleElement =
				(Element) indriParamsDocument.getElementsByTagName("rule").item(0);
		if (ruleElement != null) {
			String ruleString = ruleElement.getTextContent();
			if (!ruleString.isEmpty()) params.smoothingRule =
					parseSmoothingRule(ruleString);
		}

		// init queries
		params.queryStringsByTopicId = parseQueries(indriParamsDocument);

		return params;
	}

	private static long parseMemory(String memoryString) throws Exception {
		memoryString = memoryString.toLowerCase();
		long scale = 1;
		char suffix = memoryString.charAt(memoryString.length() - 1);
		if (Character.isLetter(suffix)) {
			switch (suffix) {
			case 'g':
				scale = 1024 * 1024 * 1024;
				break;
			case 'm':
				scale = 1024 * 1024;
				break;
			case 'k':
				scale = 1024;
				break;
			default:
				throw new IllegalArgumentException("Invalid memory string '"
						+ memoryString + "'");
			}
			memoryString = memoryString.substring(0, memoryString.length() - 1);
		}
		return Long.parseLong(memoryString) * scale;
	}

	private static SmoothingRule parseSmoothingRule(String ruleString)
			throws Exception {
		final Set<String> DIRICHLET_METHOD_NAMES =
				new HashSet<String>(Arrays.asList(new String[] { "d", "dir",
						"dirichlet" }));
		final Set<String> JELINEK_MERCER_METHOD_NAMES =
				new HashSet<String>(Arrays.asList(new String[] { "jm", "linear",
						"jelinek-mercer" }));
		final Set<String> TWO_STAGE_METHOD_NAMES =
				new HashSet<String>(Arrays.asList(new String[] { "two", "twostage",
						"two-stage" }));
		final String METHOD_PARAMETER_NAME = "method:";
		final String MU_PARAMETER_NAME = "mu:";
		final String DOCUMENT_MU_PARAMETER_NAME = "documentMu:";
		final String DOCUMENT_LAMBDA_PARAMETER_NAME = "documentLambda:";
		final String COLLECTION_LAMBDA_PARAMETER_NAME = "collectionLambda:";
		final String LAMBDA_PARAMETER_NAME = "lambda:";

		SmoothingRule smoothingRule = null;

		String[] items = ruleString.split(",");
		try {
			if (items == null || items.length == 0) throw new Exception("No items");
			if (!items[0].startsWith(METHOD_PARAMETER_NAME)) throw new Exception(
					"First item is not a method parameter");
			String methodName = items[0].substring(METHOD_PARAMETER_NAME.length());

			if (DIRICHLET_METHOD_NAMES.contains(methodName)) {
				smoothingRule = new DirichletSmoothingRule();
				for (int i = 1; i < items.length; ++i) {
					String item = items[i];
					if (item.startsWith(MU_PARAMETER_NAME)) {
						((DirichletSmoothingRule) smoothingRule).setMu(Double
								.parseDouble(item.substring(MU_PARAMETER_NAME.length())));
					} else if (item.startsWith(DOCUMENT_MU_PARAMETER_NAME)) {
						((DirichletSmoothingRule) smoothingRule).setDocumentMu(Double
								.parseDouble(item
										.substring(DOCUMENT_MU_PARAMETER_NAME.length())));
					} else if (item.startsWith(METHOD_PARAMETER_NAME)) {
						break;
					}
				}

			} else if (JELINEK_MERCER_METHOD_NAMES.contains(methodName)) {
				smoothingRule = new JelinekMercerSmoothingRule();
				for (int i = 1; i < items.length; ++i) {
					String item = items[i];
					if (item.startsWith(DOCUMENT_LAMBDA_PARAMETER_NAME)) {
						((JelinekMercerSmoothingRule) smoothingRule)
								.setDocumentLambda(Double.parseDouble(item
										.substring(DOCUMENT_LAMBDA_PARAMETER_NAME.length())));
					} else if (item.startsWith(COLLECTION_LAMBDA_PARAMETER_NAME)) {
						((JelinekMercerSmoothingRule) smoothingRule)
								.setCollectionLambda(Double.parseDouble(item
										.substring(COLLECTION_LAMBDA_PARAMETER_NAME.length())));
					} else if (item.startsWith(LAMBDA_PARAMETER_NAME)) {
						((JelinekMercerSmoothingRule) smoothingRule)
								.setCollectionLambda(Double.parseDouble(item
										.substring(LAMBDA_PARAMETER_NAME.length())));
					} else if (item.startsWith(METHOD_PARAMETER_NAME)) {
						break;
					}
				}

			} else if (TWO_STAGE_METHOD_NAMES.contains(methodName)) {
				smoothingRule = new TwoStageSmoothingRule();
				for (int i = 1; i < items.length; ++i) {
					String item = items[i];
					if (item.startsWith(MU_PARAMETER_NAME)) {
						((TwoStageSmoothingRule) smoothingRule).setMu(Double
								.parseDouble(item.substring(MU_PARAMETER_NAME.length())));
					} else if (item.startsWith(LAMBDA_PARAMETER_NAME)) {
						((TwoStageSmoothingRule) smoothingRule).setLambda(Double
								.parseDouble(item.substring(LAMBDA_PARAMETER_NAME.length())));
					} else if (item.startsWith(METHOD_PARAMETER_NAME)) {
						break;
					}
				}

			} else {
				throw new Exception("Invalid method name '" + methodName + "'");
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("Invalid rule string '" + ruleString
					+ "'", e);
		}

		return smoothingRule;
	}

	private static Map<String, String> parseQueries(Document dom)
			throws Exception {
		Map<String, String> queryStringsByTopicId =
				new LinkedHashMap<String, String>();
		NodeList queryElements = dom.getElementsByTagName("query");
		for (int i = 0; i < queryElements.getLength(); ++i) {
			Element queryElement = (Element) queryElements.item(i);
			Element numberElement =
					(Element) queryElement.getElementsByTagName("number").item(0);
			String topicId = numberElement.getTextContent();
			Element textElement =
					(Element) queryElement.getElementsByTagName("text").item(0);
			String queryString = textElement.getTextContent();
			queryStringsByTopicId.put(topicId, queryString);
		}
		return queryStringsByTopicId;
	}

	private File indexPath;
	private long memory = DEFAULT_MEMORY;
	private int count = DEFAULT_COUNT;
	private SmoothingRule smoothingRule;
	private Map<String, String> queryStringsByTopicId;

	public IndriParams() {}

	public File getIndexPath() {
		return indexPath;
	}

	public void setIndexPath(File indexPath) {
		this.indexPath = indexPath;
	}

	public long getMemory() {
		return memory;
	}

	public void setMemory(long memory) {
		this.memory = memory;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public SmoothingRule getSmoothingRule() {
		return smoothingRule;
	}

	public void setSmoothingRule(SmoothingRule smoothingRule) {
		this.smoothingRule = smoothingRule;
	}

	public Map<String, String> getQueryStringsByTopicId() {
		return queryStringsByTopicId;
	}

	public void setQueryStringsByTopicId(Map<String, String> queryStringsByTopicId) {
		this.queryStringsByTopicId = queryStringsByTopicId;
	}
}

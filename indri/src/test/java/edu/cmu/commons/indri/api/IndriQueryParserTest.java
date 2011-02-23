package edu.cmu.commons.indri.api;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.TokenStream;
import org.junit.Test;

public class IndriQueryParserTest {

	public IndriQuery parseIndriQueryString(String queryString) throws Exception {
		CharStream charStream = new ANTLRStringStream(queryString);
		IndriQueryLexer lexer = new IndriQueryLexer(charStream);
		TokenStream tokenStream = new CommonTokenStream(lexer);
		IndriQueryParser parser = new IndriQueryParser(tokenStream);
		try {
			return parser.indriQuery();
		} catch (Exception e) {
			throw new Exception("Failed to parse queryString '" + queryString + "'",
					e);
		}
	}

	@Test
	public void parsePriorBeliefOperator() throws Exception {
		parseIndriQueryString("#prior(priorName)");
	}

	@Test(expected = Exception.class)
	public void parsePriorBeliefOperatorMissingRParen() throws Exception {
		parseIndriQueryString("#prior(priorName");
	}

	@Test(expected = Exception.class)
	public void parsePriorBeliefOperatorDangling() throws Exception {
		parseIndriQueryString("#prior");
	}

	@Test(expected = Exception.class)
	public void parsePriorBeliefOperatorEmpty() throws Exception {
		parseIndriQueryString("#prior()");
	}

	@Test(expected = Exception.class)
	public void parseUnknownBeliefOperator() throws Exception {
		parseIndriQueryString("#unknown");
	}

	@Test
	public void parseAnyFieldTermOperator() throws Exception {
		parseIndriQueryString("#any(fieldName)");
	}

	@Test
	public void parseAnyFieldTermOperatorColonSyntax() throws Exception {
		parseIndriQueryString("#any:fieldName");
	}

	@Test
	public void parseTermBeliefOperatorWithContextFields() throws Exception {
		parseIndriQueryString("#any(fieldName).(f1,f2,f3)");
	}

	@Test
	public void parseTermOperatorWithFieldConstraints() throws Exception {
		parseIndriQueryString("#any(fieldName).f1,f2,f3");
	}

	@Test
	public void parseTermOperatorWithFieldConstraintsAndContextFields()
			throws Exception {
		parseIndriQueryString("#any(fieldName).f1,f2,f3.(f4,f5)");
	}

	@Test
	public void parseCombineBeliefOperator() throws Exception {
		parseIndriQueryString("#combine(#any(fieldName))");
	}

	@Test
	public void parseOrBeliefOperator() throws Exception {
		parseIndriQueryString("#or(#any(fieldName))");
	}

	@Test
	public void parseSumBeliefOperator() throws Exception {
		parseIndriQueryString("#sum(#any(fieldName))");
	}

	@Test
	public void parseMaxBeliefOperator() throws Exception {
		parseIndriQueryString("#max(#any(fieldName))");
	}

	@Test
	public void parseCombineBeliefOperatorWithPassageExtentRestriction()
			throws Exception {
		parseIndriQueryString("#combine[passage10:5](#any(fieldName))");
	}

	@Test
	public void parseCombineBeliefOperatorWithFieldExtentRestriction()
			throws Exception {
		parseIndriQueryString("#combine[fieldName](#any(fieldName))");
	}

	@Test
	public void parseCombineBeliefOperatorWithTrickyFieldExtentRestriction()
			throws Exception {
		parseIndriQueryString("#combine[passage10](#any(fieldName))");
	}

	@Test
	public void parseCombineBeliefOperatorWithChildFieldExtentRestriction()
			throws Exception {
		parseIndriQueryString("#combine[.\\passage10](#any(fieldName))");
	}

	@Test
	public void parseCombineBeliefOperatorWithParentFieldExtentRestriction()
			throws Exception {
		parseIndriQueryString("#combine[./passage10](#any(fieldName))");
	}

	@Test
	public void parseCombineBeliefOperatorWithAncestorFieldExtentRestriction()
			throws Exception {
		parseIndriQueryString("#combine[.//passage10](#any(fieldName))");
	}

	@Test
	public void parseWeightBeliefOperator() throws Exception {
		parseIndriQueryString("#weight(1.0 #any(fieldName))");
	}

	@Test
	public void parseWeightedAndBeliefOperator() throws Exception {
		parseIndriQueryString("#wand(1.0 #any(fieldName))");
	}

	@Test
	public void parseWeightedSumBeliefOperator() throws Exception {
		parseIndriQueryString("#wsum(1.0 #any(fieldName))");
	}

	@Test
	public void parseWeightBeliefOperatorWithIntegerWeight() throws Exception {
		parseIndriQueryString("#weight(1 #any(fieldName))");
	}

	@Test
	public void parseWeightBeliefOperatorWithMultipleChildren() throws Exception {
		parseIndriQueryString("#weight(1 #any(f1) 2.0 #any(f2))");
	}

	@Test
	public void parseNotBeliefOperator() throws Exception {
		parseIndriQueryString("#not(#any(f1))");
	}

	@Test
	public void parseFilterRequiredBeliefOperator() throws Exception {
		parseIndriQueryString("#filreq(#any(f1) #any(f1))");
	}

	@Test
	public void parseScoreIfBeliefOperator() throws Exception {
		parseIndriQueryString("#scoreif(#any(f1) #any(f1))");
	}

	@Test
	public void parseFilterRejectBeliefOperator() throws Exception {
		parseIndriQueryString("#filrej(#any(f1) #any(f1))");
	}

	@Test
	public void parseScoreIfNotBeliefOperator() throws Exception {
		parseIndriQueryString("#scoreifnot(#any(f1) #any(f1))");
	}

	@Test
	public void parseOrderedWindowTermOperator() throws Exception {
		parseIndriQueryString("#od1(#any(f1))");
	}

	@Test
	public void parseOrderedWindowTermOperatorAbbreviatedSyntax()
			throws Exception {
		parseIndriQueryString("#1(#any(f1))");
	}

	@Test(expected = Exception.class)
	public void parseOrderedWindowTermOperatorAbbreviatedSyntaxBogusOperatorName()
			throws Exception {
		parseIndriQueryString("#harhar1(#any(f1))");
	}

	@Test
	public void parseUnorderedWindowTermOperator() throws Exception {
		parseIndriQueryString("#uw1(#any(f1))");
	}

	@Test
	public void parseSynonymTermOperator() throws Exception {
		parseIndriQueryString("#syn(#any(f1))");
	}

	@Test
	public void parseSynonymTermOperatorAngleSyntax() throws Exception {
		parseIndriQueryString("<#any(f1)>");
	}

	@Test
	public void parseSynonymTermOperatorBraceSyntax() throws Exception {
		parseIndriQueryString("{#any(f1)}");
	}

	@Test
	public void parseBinaryAndTermOperator() throws Exception {
		parseIndriQueryString("#band(#any(f1))");
	}

	@Test
	public void parseWeightedSynonymTermOperator() throws Exception {
		parseIndriQueryString("#wsyn(1 #any(f1) 2 #any(f2))");
	}

	@Test
	public void parseDateEqualsTermOperator() throws Exception {
		parseIndriQueryString("#dateequals(1-1-2009)");
	}

	@Test
	public void parseDateBeforeTermOperator() throws Exception {
		parseIndriQueryString("#datebefore(1-1-2009)");
	}

	@Test
	public void parseDateAfterTermOperator() throws Exception {
		parseIndriQueryString("#dateafter(1-1-2009)");
	}

	@Test
	public void parseDateBetweenTermOperator() throws Exception {
		parseIndriQueryString("#datebetween(1-1-2009 1-2-2009)");
	}

	@Test
	public void parseNumericEqualsTermOperator() throws Exception {
		parseIndriQueryString("#equals(f1 1)");
	}

	@Test
	public void parseNumericLessThanTermOperator() throws Exception {
		parseIndriQueryString("#less(f1 1)");
	}

	@Test
	public void parseNumericGreaterThanTermOperator() throws Exception {
		parseIndriQueryString("#greater(f1 1)");
	}

	@Test
	public void parseNumericBetweenTermOperator() throws Exception {
		parseIndriQueryString("#between(f1 1 2)");
	}

	@Test
	public void parseWildcardTermOperator() throws Exception {
		parseIndriQueryString("#wildcard(word)");
	}

	@Test
	public void parseWildcardTermOperatorStarSyntax() throws Exception {
		parseIndriQueryString("word*");
	}

	@Test
	public void parseTextTermOperator() throws Exception {
		parseIndriQueryString("word");
	}

	@Test
	public void parseTextTermOperatorNegativeFloat() throws Exception {
		parseIndriQueryString("-1.0");
	}

	@Test
	public void parseTextTermOperatorNegativeInteger() throws Exception {
		parseIndriQueryString("-1");
	}

	@Test
	public void parseTextTermOperatorPositiveFloat() throws Exception {
		parseIndriQueryString("1.0");
	}

	@Test
	public void parseTextTermOperatorPositiveInteger() throws Exception {
		parseIndriQueryString("1");
	}

	@Test
	public void parseBase64TextTermOperator() throws Exception {
		parseIndriQueryString("#base64(123456)");
	}

	@Test
	public void parseBase64QuoteTextTermOperator() throws Exception {
		parseIndriQueryString("#base64quote(123456)");
	}

	@Test
	public void parseBase64TextTermOperatorExoticChars() throws Exception {
		parseIndriQueryString("#base64(123+/=)");
	}

	@Test(expected = Exception.class)
	public void parseBase64TextTermOperatorMalformed1() throws Exception {
		parseIndriQueryString("#base64(123 123)");
	}

	@Test
	public void parseQuotedTextTermOperator() throws Exception {
		parseIndriQueryString("\"word\"");
	}

	@Test
	public void parseComplexQuery1() throws Exception {
		parseIndriQueryString("#combine(#syn(#1(high phosphate fertilizer) hpf"
				+ " #uw17(#syn(phosphate phosphorus) #syn(fertilizer soil)))"
				+ " #syn(boost increase raise augment affect effect multiple double triple high greater)"
				+ " #syn(yield output product produce crop crops))");
	}

	@Test
	public void parseComplexQuery2() throws Exception {
		parseIndriQueryString("#filreq(#band(#syn(#1(high phosphate fertilizer) hpf"
				+ " #uw17(#syn(phosphate phosphorus) #syn(fertilizer soil)))"
				+ " #syn(boost increase raise augment affect effect multiple double triple high greater)"
				+ " #syn(yield output product produce crop crops)) #combine(high phosphate fertilizer"
				+ " hpf phosphate phosphorus fertilizer soil boost increase raise augment affect effect"
				+ " multiple double triple high greater yield output product produce crop crops))");
	}

}

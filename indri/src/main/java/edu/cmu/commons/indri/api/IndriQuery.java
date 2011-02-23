package edu.cmu.commons.indri.api;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Parsed Indri Query representation. This model is supported by an Antlr3
 * grammar (ported from the Indri distribution's Antlr2 query grammar).
 * @see IndriQueryParser
 * @see IndriQueryLexer
 */
public class IndriQuery implements Serializable {
	private static final long serialVersionUID = 1L;

	public static interface BeliefOperator extends Serializable {}

	public static interface ExtentRestriction extends Serializable {}

	public static class PassageExtentRestriction implements ExtentRestriction {
		private static final long serialVersionUID = 1L;
		private int size;
		private int increment;

		public int getSize() {
			return size;
		}

		public void setSize(int size) {
			this.size = size;
		}

		public int getIncrement() {
			return increment;
		}

		public void setIncrement(int increment) {
			this.increment = increment;
		}
	}

	public static class FieldExtentRestriction implements ExtentRestriction {
		private static final long serialVersionUID = 1L;

		public static enum Relation {
			DEFAULT, CHILD, PARENT, ANCESTOR;
		}

		private String field;
		private Relation relation = Relation.DEFAULT;

		public String getField() {
			return field;
		}

		public void setField(String field) {
			this.field = field;
		}

		public Relation getRelation() {
			return relation;
		}

		public void setRelation(Relation relation) {
			this.relation = relation;
		}
	}

	public static interface ExtentRestrictedBeliefOperator extends BeliefOperator {
		public ExtentRestriction getExtentRestriction();

		public void setExtentRestriction(ExtentRestriction extentRestriction);
	}

	public static abstract class ExtentRestrictedBeliefOperatorImpl implements
			ExtentRestrictedBeliefOperator {
		private static final long serialVersionUID = 1L;
		private ExtentRestriction extentRestriction;

		public ExtentRestriction getExtentRestriction() {
			return extentRestriction;
		}

		public void setExtentRestriction(ExtentRestriction extentRestriction) {
			this.extentRestriction = extentRestriction;
		}
	}

	public static interface GroupBeliefOperator extends
			ExtentRestrictedBeliefOperator {
		public List<BeliefOperator> getBeliefOperators();

		public void setBeliefOperators(List<BeliefOperator> beliefOperators);

		public void add(BeliefOperator beliefOperator);
	}

	public static abstract class GroupBeliefOperatorImpl extends
			ExtentRestrictedBeliefOperatorImpl implements GroupBeliefOperator {
		private static final long serialVersionUID = 1L;
		private List<BeliefOperator> beliefOperators;

		@Override
		public List<BeliefOperator> getBeliefOperators() {
			if (beliefOperators == null) beliefOperators =
					new ArrayList<BeliefOperator>();
			return beliefOperators;
		}

		@Override
		public void setBeliefOperators(List<BeliefOperator> beliefOperators) {
			this.beliefOperators = beliefOperators;
		}

		@Override
		public void add(BeliefOperator beliefOperator) {
			beliefOperators.add(beliefOperator);
		}
	}

	public static class CombineBeliefOperator extends GroupBeliefOperatorImpl {
		private static final long serialVersionUID = 1L;
	}

	public static class OrBeliefOperator extends GroupBeliefOperatorImpl {
		private static final long serialVersionUID = 1L;
	}

	public static class SumBeliefOperator extends GroupBeliefOperatorImpl {
		private static final long serialVersionUID = 1L;
	}

	public static class MaxBeliefOperator extends GroupBeliefOperatorImpl {
		private static final long serialVersionUID = 1L;
	}

	public static interface WeightedGroupBeliefOperator extends
			GroupBeliefOperator {
		public List<Double> getWeights();

		public void setWeights(List<Double> weights);

		public void add(BeliefOperator beliefOperator, double weight);
	}

	public static abstract class WeightedGroupBeliefOperatorImpl extends
			GroupBeliefOperatorImpl implements WeightedGroupBeliefOperator {
		private static final long serialVersionUID = 1L;
		private List<Double> weights;

		@Override
		public List<Double> getWeights() {
			if (weights == null) weights = new ArrayList<Double>();
			return weights;
		}

		@Override
		public void setWeights(List<Double> weights) {
			this.weights = weights;
		}

		@Override
		public void add(BeliefOperator beliefOperator) {
			add(beliefOperator, 1.0);
		}

		@Override
		public void add(BeliefOperator beliefOperator, double weight) {
			super.add(beliefOperator);
			weights.add(weight);
		}
	}

	public static class WeightBeliefOperator extends
			WeightedGroupBeliefOperatorImpl {
		private static final long serialVersionUID = 1L;
	}

	public static class WeightedAndBeliefOperator extends
			WeightedGroupBeliefOperatorImpl {
		private static final long serialVersionUID = 1L;
	}

	public static class WeightedSumBeliefOperator extends
			WeightedGroupBeliefOperatorImpl {
		private static final long serialVersionUID = 1L;
	}

	public static class PriorBeliefOperator implements BeliefOperator {
		private static final long serialVersionUID = 1L;
		private String prior;

		public String getPrior() {
			return prior;
		}

		public void setPrior(String prior) {
			this.prior = prior;
		}
	}

	public static class NotBeliefOperator extends
			ExtentRestrictedBeliefOperatorImpl {
		private static final long serialVersionUID = 1L;
		private BeliefOperator negatedBeliefOperator;

		public BeliefOperator getNegatedBeliefOperator() {
			return negatedBeliefOperator;
		}

		public void setNegatedBeliefOperator(BeliefOperator negatedBeliefOperator) {
			this.negatedBeliefOperator = negatedBeliefOperator;
		}
	}

	public static abstract class FilterBeliefOperator implements BeliefOperator {
		private static final long serialVersionUID = 1L;

		private TermOperator filterTermOperator;
		private BeliefOperator rankBeliefOperator;

		public TermOperator getFilterTermOperator() {
			return filterTermOperator;
		}

		public void setFilterTermOperator(TermOperator filterTermOperator) {
			this.filterTermOperator = filterTermOperator;
		}

		public BeliefOperator getRankBeliefOperator() {
			return rankBeliefOperator;
		}

		public void setRankBeliefOperator(BeliefOperator rankBeliefOperator) {
			this.rankBeliefOperator = rankBeliefOperator;
		}
	}

	public static class FilterRequireBeliefOperator extends FilterBeliefOperator {
		private static final long serialVersionUID = 1L;
	}

	public static class FilterRejectBeliefOperator extends FilterBeliefOperator {
		private static final long serialVersionUID = 1L;
	}

	public static class TermBeliefOperator implements BeliefOperator {
		private static final long serialVersionUID = 1L;
		private TermOperator termOperator;
		private Set<String> contextFields;

		public TermOperator getTermOperator() {
			return termOperator;
		}

		public void setTermOperator(TermOperator termOperator) {
			this.termOperator = termOperator;
		}

		public Set<String> getContextFields() {
			if (contextFields == null) contextFields = new HashSet<String>();
			return contextFields;
		}

		public void setContextFields(Set<String> contextFields) {
			this.contextFields = contextFields;
		}
	}

	public static interface TermOperator extends Serializable {
		public Set<String> getFieldRestrictions();

		public void setFieldRestrictions(Set<String> fieldRestrictions);
	}

	public static abstract class TermOperatorImpl implements TermOperator {
		private static final long serialVersionUID = 1L;

		private Set<String> fieldRestrictions;

		public Set<String> getFieldRestrictions() {
			if (fieldRestrictions == null) fieldRestrictions = new HashSet<String>();
			return fieldRestrictions;
		}

		public void setFieldRestrictions(Set<String> fieldRestrictions) {
			this.fieldRestrictions = fieldRestrictions;
		}
	}

	public static interface GroupTermOperator extends TermOperator {
		public List<TermOperator> getTermOperators();

		public void setTermOperators(List<TermOperator> termOperators);
	}

	public static interface WeightedGroupTermOperator extends GroupTermOperator {
		public List<Double> getWeights();

		public void setWeights(List<Double> weights);
	}

	public static abstract class GroupTermOperatorImpl extends TermOperatorImpl
			implements GroupTermOperator {
		private static final long serialVersionUID = 1L;
		private List<TermOperator> termOperators;

		public List<TermOperator> getTermOperators() {
			if (termOperators == null) termOperators = new ArrayList<TermOperator>();
			return termOperators;
		}

		public void setTermOperators(List<TermOperator> termOperators) {
			this.termOperators = termOperators;
		}
	}

	public static abstract class WindowTermOperator extends GroupTermOperatorImpl {
		private static final long serialVersionUID = 1L;
		public static final int UNLIMITED_SIZE = 0;
		private int size = 1;

		public int getSize() {
			return size;
		}

		public void setSize(int size) {
			this.size = size;
		}
	}

	public static class OrderedWindowTermOperator extends WindowTermOperator {
		private static final long serialVersionUID = 1L;
	}

	public static class UnorderedWindowTermOperator extends WindowTermOperator {
		private static final long serialVersionUID = 1L;
	}

	public static class BinaryAndTermOperator extends GroupTermOperatorImpl {
		private static final long serialVersionUID = 1L;
	}

	public static class SynonymTermOperator extends GroupTermOperatorImpl {
		private static final long serialVersionUID = 1L;
	}

	public static class WeightedSynonymTermOperator extends SynonymTermOperator
			implements WeightedGroupTermOperator {
		private static final long serialVersionUID = 1L;
		private List<Double> weights;

		public List<Double> getWeights() {
			if (weights == null) weights = new ArrayList<Double>();
			return weights;
		}

		public void setWeights(List<Double> weights) {
			this.weights = weights;
		}
	}

	public static abstract class DateTermOperator extends TermOperatorImpl {
		private static final long serialVersionUID = 1L;
		private Date date;

		public Date getDate() {
			return date;
		}

		public void setDate(Date date) {
			this.date = date;
		}
	}

	public static class DateEqualsTermOperator extends DateTermOperator {
		private static final long serialVersionUID = 1L;
	}

	public static class DateBeforeTermOperator extends DateTermOperator {
		private static final long serialVersionUID = 1L;
	}

	public static class DateAfterTermOperator extends DateTermOperator {
		private static final long serialVersionUID = 1L;
	}

	public static class DateBetweenTermOperator extends DateTermOperator {
		private static final long serialVersionUID = 1L;
		private Date endDate;

		public Date getEndDate() {
			return endDate;
		}

		public void setEndDate(Date endDate) {
			this.endDate = endDate;
		}
	}

	public static abstract class NumericTermOperator extends TermOperatorImpl {
		private static final long serialVersionUID = 1L;
		private String field;
		private int value;

		public String getField() {
			return field;
		}

		public void setField(String field) {
			this.field = field;
		}

		public int getValue() {
			return value;
		}

		public void setValue(int value) {
			this.value = value;
		}
	}

	public static class NumericEqualsTermOperator extends NumericTermOperator {
		private static final long serialVersionUID = 1L;
	}

	public static class NumericLessThanTermOperator extends NumericTermOperator {
		private static final long serialVersionUID = 1L;
	}

	public static class NumericGreaterThanTermOperator extends
			NumericTermOperator {
		private static final long serialVersionUID = 1L;
	}

	public static class NumericBetweenTermOperator extends NumericTermOperator {
		private static final long serialVersionUID = 1L;
		private int highValue;

		public int getHighValue() {
			return highValue;
		}

		public void setHighValue(int highValue) {
			this.highValue = highValue;
		}
	}

	public static class AnyFieldTermOperator extends TermOperatorImpl {
		private static final long serialVersionUID = 1L;
		private String field;

		public String getField() {
			return field;
		}

		public void setField(String field) {
			this.field = field;
		}
	}

	public static class WildcardTermOperator extends TermOperatorImpl {
		private static final long serialVersionUID = 1L;
		private String prefix;

		public String getPrefix() {
			return prefix;
		}

		public void setPrefix(String prefix) {
			this.prefix = prefix;
		}
	}

	public static class TextTermOperator extends TermOperatorImpl {
		private static final long serialVersionUID = 1L;
		private String text;

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}
	}

	private List<BeliefOperator> beliefOperators;

	public List<BeliefOperator> getBeliefOperators() {
		if (beliefOperators == null) beliefOperators =
				new ArrayList<BeliefOperator>();
		return beliefOperators;
	}

	public void setBeliefOperators(List<BeliefOperator> beliefOperators) {
		this.beliefOperators = beliefOperators;
	}
}

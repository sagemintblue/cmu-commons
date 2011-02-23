grammar IndriQuery;

tokens {
  // belief operators
  NOT = '#not' ;
  PRIOR = '#prior' ;
  // group belief operators
  COMBINE = '#combine' ;
  OR = '#or' ;
  SUM = '#sum' ;
  MAX = '#max' ;
  // weighted group belief operators
  WEIGHT = '#weight' ;
  WAND = '#wand' ;
  WSUM = '#wsum' ;
  // filter belief operators
  FILREQ = '#filreq' ;
  FILREJ = '#filrej' ;
  SCOREIF = '#scoreif' ;
  SCOREIFNOT = '#scoreifnot' ;

  // term operators
  ANY = '#any' ;
  WCARD = '#wildcard' ;
  // group term operators
  BAND = '#band' ;
  SYN = '#syn' ;
  // weighted group term operators
  WSYN = '#wsyn' ;
  // window term operators
  OD = '#od' ;
  UW = '#uw' ;
  // numeric term operators
  DATEEQUALS = '#dateequals' ;
  DATEBEFORE = '#datebefore' ;
  DATEAFTER = '#dateafter' ;
  DATEBETWEEN = '#datebetween' ;
  EQUALS = '#equals' ;
  LESS = '#less' ;
  GREATER = '#greater' ;
  BETWEEN = '#between' ;
  
  // base64 coding operators
  BASE64 = '#base64' ;
  BASE64QUOTE = '#base64quote' ;
  
  // pseudo-tokens
  POS_INTEGER ;
  NEG_INTEGER ;
  POS_FLOAT ;
  NEG_FLOAT ;
}

/*
** Lexer customization
*/

@lexer::header {
package edu.cmu.commons.indri.api;
}

@lexer::members {
@Override public void reportError(RecognitionException e) {
  throw new IllegalArgumentException(e);
}
}

/*
** Parser customization
*/

@parser::header {
package edu.cmu.commons.indri.api;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;
import org.apache.commons.codec.binary.Base64;
}

@parser::members {
private Date parseDate(String year, String month, String day) {
  Calendar c = Calendar.getInstance();
  c.set(Calendar.YEAR, Integer.parseInt(year));
  if (Character.isDigit(month.charAt(0))) {
    // month is zero-based (Calendar.JANUARY == 0)
    c.set(Calendar.MONTH, Integer.parseInt(month) - 1);
  } else {
    int m = -1;
    month = month.toLowerCase();
    if (month.startsWith("ja")) m = Calendar.JANUARY;
    else if (month.startsWith("f")) m = Calendar.FEBRUARY;
    else if (month.startsWith("mar")) m = Calendar.MARCH;
    else if (month.startsWith("ap")) m = Calendar.APRIL;
    else if (month.startsWith("may")) m = Calendar.MAY;
    else if (month.startsWith("jun")) m = Calendar.JUNE;
    else if (month.startsWith("jul")) m = Calendar.JULY;
    else if (month.startsWith("au")) m = Calendar.AUGUST;
    else if (month.startsWith("s")) m = Calendar.SEPTEMBER;
    else if (month.startsWith("o")) m = Calendar.OCTOBER;
    else if (month.startsWith("n")) m = Calendar.NOVEMBER;
    else if (month.startsWith("d")) m = Calendar.DECEMBER;
    else throw new IllegalArgumentException("Invalid month name '" + month + "'");
    c.set(Calendar.MONTH, m);
  }
  c.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day));
  return c.getTime();
}

@Override public void reportError(RecognitionException e) {
  throw new IllegalArgumentException(e);
}
}

@parser::rulecatch {
catch (RecognitionException e) { throw e; }
catch (IllegalArgumentException e) {
  Throwable c = e.getCause();
  if (c != null && c instanceof RecognitionException)
    throw (RecognitionException) c;
  throw e;
}
}

/*
** Rule fragments
*/

fragment WS_CHAR : ' ' | '\t' | '\n' | '\r' | '\u000C' ;
fragment ALPHA_CHAR : 'a'..'z' | 'A'..'Z' ;
fragment DIGIT_CHAR : '0'..'9' ;
fragment ALNUM_CHAR : ALPHA_CHAR | DIGIT_CHAR ;
fragment NON_ALNUM_WORD_CHAR : '-' | '_' ;
fragment NON_DIGIT_WORD_CHAR : 'a'..'z' | 'A'..'Z' | '-' | '_' ;
fragment WORD_CHAR : 'a'..'z' | 'A'..'Z' | '0'..'9' | '-' | '_' ;
fragment HIGH_CHAR : '\u0080'..'\u00ff' ;
fragment TEXT_TERM : ( HIGH_CHAR | WORD_CHAR )+ ;
fragment POS_INTEGER : DIGIT_CHAR+ ;
fragment NEG_INTEGER: '-' POS_INTEGER ;
fragment POS_FLOAT : POS_INTEGER DOT POS_INTEGER ;
fragment NEG_FLOAT : '-' POS_FLOAT ;
fragment BASE64_CHAR : 'a'..'z' | 'A'..'Z' | '0'..'9' | '+' | '/' | '=' ;

/*
** Lexer rules
*/

STAR : '*' ;
LPAREN : '(' ;
RPAREN : ')' ;
LANGLE : '<' ;
RANGLE : '>' ;
LSQUARE : '[' ;
RSQUARE : ']' ;
LBRACE : '{' ;
RBRACE : '}' ;
QUOTE : '\'' ;
DBLQUOTE : '"' ;
DOT : '.' ;
COMMA : ',' ;
SLASH : '/' ;
BSLASH : '\\' ;
COLON : ':' ;
TERM
  : ( POS_INTEGER ( NON_DIGIT_WORD_CHAR | HIGH_CHAR ) ) => TEXT_TERM
  | ( NEG_FLOAT ) => NEG_FLOAT { $type = NEG_FLOAT; }
  | ( NEG_INTEGER ) => NEG_INTEGER { $type = NEG_INTEGER; }
  | ( POS_FLOAT ) => POS_FLOAT { $type = POS_FLOAT; }
  | ( POS_INTEGER ) => POS_INTEGER { $type = POS_INTEGER; }
  | TEXT_TERM
  ;
BASE64_TERM
  : ( BASE64
      LPAREN ( ' ' | '\t' )* ) BASE64_CHAR+
    ( ( ' ' | '\t' )* RPAREN )
  ;
BASE64QUOTE_TERM
  : ( BASE64QUOTE
      LPAREN ( ' ' | '\t' )* ) BASE64_CHAR+
    ( ( ' ' | '\t' )* RPAREN )
  ;
WS : WS_CHAR+ { $channel = HIDDEN; } ;

/*
** Parser Rules
*/

indriQuery returns [IndriQuery value]
@init {
  value = new IndriQuery();
}
  : ( beliefOperator { value.getBeliefOperators().add($beliefOperator.value); }
    )+
  ;

beliefOperator returns [IndriQuery.BeliefOperator value]
  : groupBeliefOperator { value = $groupBeliefOperator.value; }
  | weightedGroupBeliefOperator { value = $weightedGroupBeliefOperator.value; }
  | priorBeliefOperator { value = $priorBeliefOperator.value; }
  | notBeliefOperator { value = $notBeliefOperator.value; }
  | filterBeliefOperator { value = $filterBeliefOperator.value; }
  | termBeliefOperator { value = $termBeliefOperator.value; }
  ;

groupBeliefOperator returns [IndriQuery.GroupBeliefOperator value]
  : ( COMBINE { value = new IndriQuery.CombineBeliefOperator(); }
    | OR { value = new IndriQuery.OrBeliefOperator(); }
    | SUM { value = new IndriQuery.SumBeliefOperator(); }
    | MAX { value = new IndriQuery.MaxBeliefOperator(); }
    )
    ( extentRestriction { value.setExtentRestriction($extentRestriction.value); }
    )?
    beliefOperatorList {
      value.setBeliefOperators($beliefOperatorList.beliefOperators);
    }
  ;

weightedGroupBeliefOperator returns [IndriQuery.WeightedGroupBeliefOperator value]
  : ( WEIGHT { value = new IndriQuery.WeightBeliefOperator(); }
    | WAND { value = new IndriQuery.WeightedAndBeliefOperator(); }
    | WSUM { value = new IndriQuery.WeightedSumBeliefOperator(); }
    )
    ( extentRestriction { value.setExtentRestriction($extentRestriction.value); }
    )?
    weightedBeliefOperatorList {
      value.setBeliefOperators($weightedBeliefOperatorList.beliefOperators);
      value.setWeights($weightedBeliefOperatorList.weights);
    }
  ;

extentRestriction returns [IndriQuery.ExtentRestriction value]
  : LSQUARE
    ( ( TERM COLON ) => passageExtentRestriction { value = $passageExtentRestriction.value; }
    | fieldExtentRestriction { value = $fieldExtentRestriction.value; }
    )
    RSQUARE
  ;

passageExtentRestriction returns [IndriQuery.PassageExtentRestriction value]
  : { input.LT(1).getText().startsWith("passage") }?
    TERM {
      value = new IndriQuery.PassageExtentRestriction();
      value.setSize(Integer.parseInt($TERM.text.substring("passage".length())));
    }
    COLON
    POS_INTEGER { value.setIncrement(Integer.parseInt($POS_INTEGER.text)); }
  ;

fieldExtentRestriction returns [IndriQuery.FieldExtentRestriction value]
@init {
  value = new IndriQuery.FieldExtentRestriction(); 
}
  : ( DOT
      ( ( SLASH SLASH ) => SLASH SLASH {
          value.setRelation(IndriQuery.FieldExtentRestriction.Relation.ANCESTOR);
        }
      | SLASH { value.setRelation(IndriQuery.FieldExtentRestriction.Relation.PARENT); }
      | BSLASH { value.setRelation(IndriQuery.FieldExtentRestriction.Relation.CHILD); }
      )
    )?
    TERM { value.setField($TERM.text); }
  ;

beliefOperatorList returns [List<IndriQuery.BeliefOperator> beliefOperators]
@init {
  beliefOperators = new ArrayList<IndriQuery.BeliefOperator>();
}
  : LPAREN
    ( beliefOperator { beliefOperators.add($beliefOperator.value); } )+
    RPAREN
  ;

weightedBeliefOperatorList returns [List beliefOperators, List weights]
@init {
  retval.beliefOperators = new ArrayList();
  retval.weights = new ArrayList();
}
  : LPAREN
    ( weight { retval.weights.add($weight.value); }
      beliefOperator { retval.beliefOperators.add($beliefOperator.value); }
    )+
    RPAREN
  ;

weight returns [Double value]
  : ( ( POS_FLOAT ) => t=POS_FLOAT
    | t=POS_INTEGER
    ) { value = Double.parseDouble($t.text); }
  ;

priorBeliefOperator returns [IndriQuery.PriorBeliefOperator value]
  : PRIOR { value = new IndriQuery.PriorBeliefOperator(); }
    LPAREN
    TERM { value.setPrior($TERM.text); }
    RPAREN
  ;

notBeliefOperator returns [IndriQuery.NotBeliefOperator value]
  : NOT { value = new IndriQuery.NotBeliefOperator(); }
    ( extentRestriction { value.setExtentRestriction($extentRestriction.value); }
    )?
    LPAREN
    beliefOperator { value.setNegatedBeliefOperator($beliefOperator.value); }
    RPAREN
  ;

filterBeliefOperator returns [IndriQuery.FilterBeliefOperator value]
  : ( ( FILREJ | SCOREIFNOT ) { value = new IndriQuery.FilterRejectBeliefOperator(); }
    | ( FILREQ | SCOREIF ) { value = new IndriQuery.FilterRequireBeliefOperator(); }
    )
    LPAREN
    termOperator { value.setFilterTermOperator($termOperator.value); }
    beliefOperator { value.setRankBeliefOperator($beliefOperator.value); }
    RPAREN
  ;

termBeliefOperator returns [IndriQuery.TermBeliefOperator value]
  : termOperator {
      value = new IndriQuery.TermBeliefOperator();
      value.setTermOperator($termOperator.value);
    }
    ( DOT LPAREN
      fieldList { value.getContextFields().addAll($fieldList.value); }
      RPAREN
    )?
  ;

termOperator returns [IndriQuery.TermOperator value]
  : ( groupTermOperator { value = $groupTermOperator.value; }
    | weightedGroupTermOperator { value = $weightedGroupTermOperator.value; }
    | dateTermOperator { value = $dateTermOperator.value; }
    | numericTermOperator { value = $numericTermOperator.value; }
    | anyFieldTermOperator { value = $anyFieldTermOperator.value; }
    | ( WCARD | textTermOperator STAR ) => wildcardTermOperator { value = $wildcardTermOperator.value; }
    | textTermOperator {value = $textTermOperator.value; }
    )
    ( DOT
      fieldList { value.getFieldRestrictions().addAll($fieldList.value); }
    )?
  ;

fieldList returns [List<String> value]
  : t1=TERM {
      value = new ArrayList<String>();
      value.add($t1.text);
    }
    ( COMMA
      t2=TERM { value.add($t2.text); }
    )*
  ;

groupTermOperator returns [IndriQuery.GroupTermOperator value]
  : ( ( windowTermOperator { value = $windowTermOperator.value; }
      | SYN { value = new IndriQuery.SynonymTermOperator(); }
      | BAND { value = new IndriQuery.BinaryAndTermOperator(); }
      )
      LPAREN
      ( t1=termOperator { value.getTermOperators().add($t1.value); }
      )+
      RPAREN
    | LANGLE { value = new IndriQuery.SynonymTermOperator(); }
      ( t2=termOperator { value.getTermOperators().add($t2.value); }
      )+
      RANGLE
    | LBRACE { value = new IndriQuery.SynonymTermOperator(); }
      ( t3=termOperator { value.getTermOperators().add($t3.value); }
      )+
      RBRACE
    )
  ;

windowTermOperator returns [IndriQuery.WindowTermOperator value]
  : ( '#' { value = new IndriQuery.OrderedWindowTermOperator(); }
      i1=POS_INTEGER { value.setSize(Integer.parseInt($i1.text)); }
    )
  | ( OD { value = new IndriQuery.OrderedWindowTermOperator(); }
    | UW { value = new IndriQuery.UnorderedWindowTermOperator(); }
    )
    ( i2=POS_INTEGER { value.setSize(Integer.parseInt($i2.text)); }
    )?
  ;

weightedGroupTermOperator returns [IndriQuery.WeightedGroupTermOperator value]
  : ( WSYN { value = new IndriQuery.WeightedSynonymTermOperator(); }
    )
    LPAREN
    ( weight { value.getWeights().add($weight.value); }
      termOperator { value.getTermOperators().add($termOperator.value); }
    )+
    RPAREN
  ;

dateTermOperator returns [IndriQuery.DateTermOperator value]
  : ( DATEEQUALS { value = new IndriQuery.DateEqualsTermOperator(); }
    | DATEBEFORE { value = new IndriQuery.DateBeforeTermOperator(); }
    | DATEAFTER { value = new IndriQuery.DateAfterTermOperator(); }
    )
    LPAREN
    d1=date { value.setDate($d1.value); }
    RPAREN
  | DATEBETWEEN { value = new IndriQuery.DateBetweenTermOperator(); }
    LPAREN
    d2=date { value.setDate($d2.value); }
    d3=date { ((IndriQuery.DateBetweenTermOperator) value).setEndDate($d3.value); }
    RPAREN
  ;

date returns [Date value]
  : ( POS_INTEGER SLASH ) => slashDate { value = $slashDate.value; }
  | ( TERM POS_INTEGER | POS_INTEGER TERM ) => spaceDate { value = $spaceDate.value; }
  | dashDate { value = $dashDate.value; }
  ;

slashDate returns [Date value]
  : m=POS_INTEGER SLASH d=POS_INTEGER SLASH y=POS_INTEGER
    { value = parseDate($y.text, $m.text, $d.text); }
  ;

spaceDate returns [Date value]
  : ( ( POS_INTEGER ) => d=POS_INTEGER m=TERM y=POS_INTEGER
    | ( TERM ) => m=TERM d=POS_INTEGER y=POS_INTEGER
    ) { value = parseDate($y.text, $m.text, $d.text); }
  ;

dashDate returns [Date value]
  : TERM {
      String[] items = $TERM.text.split("-", 3);
      if (items.length != 3)
        throw new IllegalArgumentException("Invalid date string '"
          + $TERM.text + "'");
      value = parseDate(items[2], items[0], items[1]);
    }
  ;

numericTermOperator returns [IndriQuery.NumericTermOperator value]
  : ( EQUALS { value = new IndriQuery.NumericEqualsTermOperator(); }
    | LESS { value = new IndriQuery.NumericLessThanTermOperator(); }
    | GREATER { value = new IndriQuery.NumericGreaterThanTermOperator(); }
    )
    LPAREN
    TERM { value.setField($TERM.text); }
    i1=integer { value.setValue($i1.value); }
    RPAREN
  | BETWEEN { value = new IndriQuery.NumericBetweenTermOperator(); }
    LPAREN
    TERM { value.setField($TERM.text); }
    i2=integer { value.setValue($i2.value); }
    i3=integer { ((IndriQuery.NumericBetweenTermOperator) value).setHighValue($i3.value); }
    RPAREN
  ;

integer returns [Integer value]
  : ( t=POS_INTEGER
    | t=NEG_INTEGER
    ) { value = Integer.parseInt($t.text); }
  ;

anyFieldTermOperator returns [IndriQuery.AnyFieldTermOperator value]
  : ANY
    ( COLON t=TERM
    | LPAREN t=TERM RPAREN
    ) {
      value = new IndriQuery.AnyFieldTermOperator();
      value.setField($t.text);
    }
  ;

wildcardTermOperator returns [IndriQuery.WildcardTermOperator value]
  : ( WCARD LPAREN t=textTermOperator RPAREN
    | t=textTermOperator STAR
    ) {
      value = new IndriQuery.WildcardTermOperator();
      value.setPrefix($t.value.getText());
    }
  ;

textTermOperator returns [IndriQuery.TextTermOperator value]
@init {
  value = new IndriQuery.TextTermOperator();
  String base64 = null;
}
  : ( t=TERM
    | t=NEG_FLOAT
    | t=NEG_INTEGER
    | t=POS_FLOAT
    | t=POS_INTEGER
    ) { value.setText($t.text); }
  | ( t=BASE64QUOTE_TERM { base64 = $t.text.substring(13, $t.text.length()-1).trim(); }
    | t=BASE64_TERM { base64 = $t.text.substring(8, $t.text.length()-1).trim(); }
    )
    { value.setText(new String(Base64.decodeBase64(base64.getBytes()))); }
  | DBLQUOTE
    t2=textTermOperator { value.setText($t2.value.getText()); }
    DBLQUOTE
  ;

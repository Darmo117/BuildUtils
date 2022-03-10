// Generated from /home/damien/IdeaProjects/NaissanceE_mod/grammar/Calculator.g4 by ANTLR 4.9.2
package net.darmo_creations.build_utils.calculator.antlr4_parser;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.LexerATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class CalculatorLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.9.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		WS=1, LPAREN=2, RPAREN=3, COMMA=4, ASSIGN=5, PLUS=6, MINUS=7, MUL=8, DIV=9, 
		MOD=10, POWER=11, EQUAL=12, NEQUAL=13, GT=14, GE=15, LT=16, LE=17, NOT=18, 
		AND=19, OR=20, TRUE=21, FALSE=22, ID=23, NUMBER=24;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"WS", "LPAREN", "RPAREN", "COMMA", "ASSIGN", "PLUS", "MINUS", "MUL", 
			"DIV", "MOD", "POWER", "EQUAL", "NEQUAL", "GT", "GE", "LT", "LE", "NOT", 
			"AND", "OR", "TRUE", "FALSE", "ID", "NUMBER"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, "'('", "')'", "','", "':='", "'+'", "'-'", "'*'", "'/'", 
			"'%'", "'^'", "'='", "'!='", "'>'", "'>='", "'<'", "'<='", "'!'", "'&'", 
			"'|'", "'true'", "'false'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "WS", "LPAREN", "RPAREN", "COMMA", "ASSIGN", "PLUS", "MINUS", "MUL", 
			"DIV", "MOD", "POWER", "EQUAL", "NEQUAL", "GT", "GE", "LT", "LE", "NOT", 
			"AND", "OR", "TRUE", "FALSE", "ID", "NUMBER"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public CalculatorLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "Calculator.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\32\u0084\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\3\2\6\2\65\n\2\r\2\16\2\66\3\2\3\2\3\3\3\3\3\4\3\4\3\5\3\5\3\6\3"+
		"\6\3\6\3\7\3\7\3\b\3\b\3\t\3\t\3\n\3\n\3\13\3\13\3\f\3\f\3\r\3\r\3\16"+
		"\3\16\3\16\3\17\3\17\3\20\3\20\3\20\3\21\3\21\3\22\3\22\3\22\3\23\3\23"+
		"\3\24\3\24\3\25\3\25\3\26\3\26\3\26\3\26\3\26\3\27\3\27\3\27\3\27\3\27"+
		"\3\27\3\30\3\30\7\30r\n\30\f\30\16\30u\13\30\3\31\6\31x\n\31\r\31\16\31"+
		"y\3\31\5\31}\n\31\3\31\7\31\u0080\n\31\f\31\16\31\u0083\13\31\2\2\32\3"+
		"\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20\37"+
		"\21!\22#\23%\24\'\25)\26+\27-\30/\31\61\32\3\2\6\4\2\13\f\"\"\5\2C\\a"+
		"ac|\6\2\62;C\\aac|\3\2\62;\2\u0088\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2"+
		"\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3"+
		"\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2"+
		"\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2"+
		"\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\3\64\3\2\2\2\5:\3\2\2"+
		"\2\7<\3\2\2\2\t>\3\2\2\2\13@\3\2\2\2\rC\3\2\2\2\17E\3\2\2\2\21G\3\2\2"+
		"\2\23I\3\2\2\2\25K\3\2\2\2\27M\3\2\2\2\31O\3\2\2\2\33Q\3\2\2\2\35T\3\2"+
		"\2\2\37V\3\2\2\2!Y\3\2\2\2#[\3\2\2\2%^\3\2\2\2\'`\3\2\2\2)b\3\2\2\2+d"+
		"\3\2\2\2-i\3\2\2\2/o\3\2\2\2\61w\3\2\2\2\63\65\t\2\2\2\64\63\3\2\2\2\65"+
		"\66\3\2\2\2\66\64\3\2\2\2\66\67\3\2\2\2\678\3\2\2\289\b\2\2\29\4\3\2\2"+
		"\2:;\7*\2\2;\6\3\2\2\2<=\7+\2\2=\b\3\2\2\2>?\7.\2\2?\n\3\2\2\2@A\7<\2"+
		"\2AB\7?\2\2B\f\3\2\2\2CD\7-\2\2D\16\3\2\2\2EF\7/\2\2F\20\3\2\2\2GH\7,"+
		"\2\2H\22\3\2\2\2IJ\7\61\2\2J\24\3\2\2\2KL\7\'\2\2L\26\3\2\2\2MN\7`\2\2"+
		"N\30\3\2\2\2OP\7?\2\2P\32\3\2\2\2QR\7#\2\2RS\7?\2\2S\34\3\2\2\2TU\7@\2"+
		"\2U\36\3\2\2\2VW\7@\2\2WX\7?\2\2X \3\2\2\2YZ\7>\2\2Z\"\3\2\2\2[\\\7>\2"+
		"\2\\]\7?\2\2]$\3\2\2\2^_\7#\2\2_&\3\2\2\2`a\7(\2\2a(\3\2\2\2bc\7~\2\2"+
		"c*\3\2\2\2de\7v\2\2ef\7t\2\2fg\7w\2\2gh\7g\2\2h,\3\2\2\2ij\7h\2\2jk\7"+
		"c\2\2kl\7n\2\2lm\7u\2\2mn\7g\2\2n.\3\2\2\2os\t\3\2\2pr\t\4\2\2qp\3\2\2"+
		"\2ru\3\2\2\2sq\3\2\2\2st\3\2\2\2t\60\3\2\2\2us\3\2\2\2vx\t\5\2\2wv\3\2"+
		"\2\2xy\3\2\2\2yw\3\2\2\2yz\3\2\2\2z|\3\2\2\2{}\7\60\2\2|{\3\2\2\2|}\3"+
		"\2\2\2}\u0081\3\2\2\2~\u0080\t\5\2\2\177~\3\2\2\2\u0080\u0083\3\2\2\2"+
		"\u0081\177\3\2\2\2\u0081\u0082\3\2\2\2\u0082\62\3\2\2\2\u0083\u0081\3"+
		"\2\2\2\b\2\66sy|\u0081\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}

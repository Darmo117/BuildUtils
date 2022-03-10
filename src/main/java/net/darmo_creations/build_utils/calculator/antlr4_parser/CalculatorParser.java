// Generated from /home/damien/IdeaProjects/NaissanceE_mod/grammar/Calculator.g4 by ANTLR 4.9.2
package net.darmo_creations.build_utils.calculator.antlr4_parser;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.ParserATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.List;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class CalculatorParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.9.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		WS=1, LPAREN=2, RPAREN=3, COMMA=4, ASSIGN=5, PLUS=6, MINUS=7, MUL=8, DIV=9, 
		MOD=10, POWER=11, EQUAL=12, NEQUAL=13, GT=14, GE=15, LT=16, LE=17, NOT=18, 
		AND=19, OR=20, TRUE=21, FALSE=22, ID=23, NUMBER=24;
	public static final int
		RULE_start = 0, RULE_statement = 1, RULE_exp = 2;
	private static String[] makeRuleNames() {
		return new String[] {
			"start", "statement", "exp"
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

	@Override
	public String getGrammarFileName() { return "Calculator.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public CalculatorParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class StartContext extends ParserRuleContext {
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public TerminalNode EOF() { return getToken(CalculatorParser.EOF, 0); }
		public StartContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_start; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CalculatorListener ) ((CalculatorListener)listener).enterStart(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CalculatorListener ) ((CalculatorListener)listener).exitStart(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CalculatorVisitor ) return ((CalculatorVisitor<? extends T>)visitor).visitStart(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StartContext start() throws RecognitionException {
		StartContext _localctx = new StartContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_start);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(6);
			statement();
			setState(7);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StatementContext extends ParserRuleContext {
		public StatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statement; }
	 
		public StatementContext() { }
		public void copyFrom(StatementContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class ExpressionContext extends StatementContext {
		public ExpContext exp() {
			return getRuleContext(ExpContext.class,0);
		}
		public ExpressionContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CalculatorListener ) ((CalculatorListener)listener).enterExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CalculatorListener ) ((CalculatorListener)listener).exitExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CalculatorVisitor ) return ((CalculatorVisitor<? extends T>)visitor).visitExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class FunctionDefContext extends StatementContext {
		public Token fname;
		public ExpContext expr;
		public TerminalNode LPAREN() { return getToken(CalculatorParser.LPAREN, 0); }
		public List<TerminalNode> ID() { return getTokens(CalculatorParser.ID); }
		public TerminalNode ID(int i) {
			return getToken(CalculatorParser.ID, i);
		}
		public TerminalNode RPAREN() { return getToken(CalculatorParser.RPAREN, 0); }
		public TerminalNode ASSIGN() { return getToken(CalculatorParser.ASSIGN, 0); }
		public ExpContext exp() {
			return getRuleContext(ExpContext.class,0);
		}
		public List<TerminalNode> COMMA() { return getTokens(CalculatorParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(CalculatorParser.COMMA, i);
		}
		public FunctionDefContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CalculatorListener ) ((CalculatorListener)listener).enterFunctionDef(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CalculatorListener ) ((CalculatorListener)listener).exitFunctionDef(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CalculatorVisitor ) return ((CalculatorVisitor<? extends T>)visitor).visitFunctionDef(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class VariableDefContext extends StatementContext {
		public Token var;
		public ExpContext expr;
		public TerminalNode ASSIGN() { return getToken(CalculatorParser.ASSIGN, 0); }
		public TerminalNode ID() { return getToken(CalculatorParser.ID, 0); }
		public ExpContext exp() {
			return getRuleContext(ExpContext.class,0);
		}
		public VariableDefContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CalculatorListener ) ((CalculatorListener)listener).enterVariableDef(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CalculatorListener ) ((CalculatorListener)listener).exitVariableDef(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CalculatorVisitor ) return ((CalculatorVisitor<? extends T>)visitor).visitVariableDef(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StatementContext statement() throws RecognitionException {
		StatementContext _localctx = new StatementContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_statement);
		int _la;
		try {
			setState(26);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				_localctx = new ExpressionContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(9);
				exp(0);
				}
				break;
			case 2:
				_localctx = new VariableDefContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(10);
				((VariableDefContext)_localctx).var = match(ID);
				setState(11);
				match(ASSIGN);
				setState(12);
				((VariableDefContext)_localctx).expr = exp(0);
				}
				break;
			case 3:
				_localctx = new FunctionDefContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(13);
				((FunctionDefContext)_localctx).fname = match(ID);
				setState(14);
				match(LPAREN);
				setState(15);
				match(ID);
				setState(20);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(16);
					match(COMMA);
					setState(17);
					match(ID);
					}
					}
					setState(22);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(23);
				match(RPAREN);
				setState(24);
				match(ASSIGN);
				setState(25);
				((FunctionDefContext)_localctx).expr = exp(0);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExpContext extends ParserRuleContext {
		public ExpContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exp; }
	 
		public ExpContext() { }
		public void copyFrom(ExpContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class OperatorContext extends ExpContext {
		public ExpContext left;
		public Token operator;
		public ExpContext right;
		public List<ExpContext> exp() {
			return getRuleContexts(ExpContext.class);
		}
		public ExpContext exp(int i) {
			return getRuleContext(ExpContext.class,i);
		}
		public TerminalNode POWER() { return getToken(CalculatorParser.POWER, 0); }
		public TerminalNode MUL() { return getToken(CalculatorParser.MUL, 0); }
		public TerminalNode DIV() { return getToken(CalculatorParser.DIV, 0); }
		public TerminalNode MOD() { return getToken(CalculatorParser.MOD, 0); }
		public TerminalNode PLUS() { return getToken(CalculatorParser.PLUS, 0); }
		public TerminalNode MINUS() { return getToken(CalculatorParser.MINUS, 0); }
		public TerminalNode EQUAL() { return getToken(CalculatorParser.EQUAL, 0); }
		public TerminalNode NEQUAL() { return getToken(CalculatorParser.NEQUAL, 0); }
		public TerminalNode GT() { return getToken(CalculatorParser.GT, 0); }
		public TerminalNode GE() { return getToken(CalculatorParser.GE, 0); }
		public TerminalNode LT() { return getToken(CalculatorParser.LT, 0); }
		public TerminalNode LE() { return getToken(CalculatorParser.LE, 0); }
		public TerminalNode AND() { return getToken(CalculatorParser.AND, 0); }
		public TerminalNode OR() { return getToken(CalculatorParser.OR, 0); }
		public OperatorContext(ExpContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CalculatorListener ) ((CalculatorListener)listener).enterOperator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CalculatorListener ) ((CalculatorListener)listener).exitOperator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CalculatorVisitor ) return ((CalculatorVisitor<? extends T>)visitor).visitOperator(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class FunctionContext extends ExpContext {
		public Token name;
		public TerminalNode LPAREN() { return getToken(CalculatorParser.LPAREN, 0); }
		public List<ExpContext> exp() {
			return getRuleContexts(ExpContext.class);
		}
		public ExpContext exp(int i) {
			return getRuleContext(ExpContext.class,i);
		}
		public TerminalNode RPAREN() { return getToken(CalculatorParser.RPAREN, 0); }
		public TerminalNode ID() { return getToken(CalculatorParser.ID, 0); }
		public List<TerminalNode> COMMA() { return getTokens(CalculatorParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(CalculatorParser.COMMA, i);
		}
		public FunctionContext(ExpContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CalculatorListener ) ((CalculatorListener)listener).enterFunction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CalculatorListener ) ((CalculatorListener)listener).exitFunction(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CalculatorVisitor ) return ((CalculatorVisitor<? extends T>)visitor).visitFunction(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class VariableContext extends ExpContext {
		public TerminalNode ID() { return getToken(CalculatorParser.ID, 0); }
		public VariableContext(ExpContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CalculatorListener ) ((CalculatorListener)listener).enterVariable(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CalculatorListener ) ((CalculatorListener)listener).exitVariable(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CalculatorVisitor ) return ((CalculatorVisitor<? extends T>)visitor).visitVariable(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class NumberContext extends ExpContext {
		public TerminalNode NUMBER() { return getToken(CalculatorParser.NUMBER, 0); }
		public NumberContext(ExpContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CalculatorListener ) ((CalculatorListener)listener).enterNumber(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CalculatorListener ) ((CalculatorListener)listener).exitNumber(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CalculatorVisitor ) return ((CalculatorVisitor<? extends T>)visitor).visitNumber(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class UnaryOperatorContext extends ExpContext {
		public Token operator;
		public ExpContext operand;
		public ExpContext exp() {
			return getRuleContext(ExpContext.class,0);
		}
		public TerminalNode MINUS() { return getToken(CalculatorParser.MINUS, 0); }
		public TerminalNode PLUS() { return getToken(CalculatorParser.PLUS, 0); }
		public TerminalNode NOT() { return getToken(CalculatorParser.NOT, 0); }
		public UnaryOperatorContext(ExpContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CalculatorListener ) ((CalculatorListener)listener).enterUnaryOperator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CalculatorListener ) ((CalculatorListener)listener).exitUnaryOperator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CalculatorVisitor ) return ((CalculatorVisitor<? extends T>)visitor).visitUnaryOperator(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class BooleanContext extends ExpContext {
		public Token value;
		public TerminalNode TRUE() { return getToken(CalculatorParser.TRUE, 0); }
		public TerminalNode FALSE() { return getToken(CalculatorParser.FALSE, 0); }
		public BooleanContext(ExpContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CalculatorListener ) ((CalculatorListener)listener).enterBoolean(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CalculatorListener ) ((CalculatorListener)listener).exitBoolean(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CalculatorVisitor ) return ((CalculatorVisitor<? extends T>)visitor).visitBoolean(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ParenthesesContext extends ExpContext {
		public ExpContext expr;
		public TerminalNode LPAREN() { return getToken(CalculatorParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(CalculatorParser.RPAREN, 0); }
		public ExpContext exp() {
			return getRuleContext(ExpContext.class,0);
		}
		public ParenthesesContext(ExpContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CalculatorListener ) ((CalculatorListener)listener).enterParentheses(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CalculatorListener ) ((CalculatorListener)listener).exitParentheses(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CalculatorVisitor ) return ((CalculatorVisitor<? extends T>)visitor).visitParentheses(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpContext exp() throws RecognitionException {
		return exp(0);
	}

	private ExpContext exp(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExpContext _localctx = new ExpContext(_ctx, _parentState);
		ExpContext _prevctx = _localctx;
		int _startState = 4;
		enterRecursionRule(_localctx, 4, RULE_exp, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(50);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
			case 1:
				{
				_localctx = new ParenthesesContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(29);
				match(LPAREN);
				setState(30);
				((ParenthesesContext)_localctx).expr = exp(0);
				setState(31);
				match(RPAREN);
				}
				break;
			case 2:
				{
				_localctx = new UnaryOperatorContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(33);
				((UnaryOperatorContext)_localctx).operator = _input.LT(1);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << PLUS) | (1L << MINUS) | (1L << NOT))) != 0)) ) {
					((UnaryOperatorContext)_localctx).operator = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(34);
				((UnaryOperatorContext)_localctx).operand = exp(11);
				}
				break;
			case 3:
				{
				_localctx = new FunctionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(35);
				((FunctionContext)_localctx).name = match(ID);
				setState(36);
				match(LPAREN);
				setState(37);
				exp(0);
				setState(42);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(38);
					match(COMMA);
					setState(39);
					exp(0);
					}
					}
					setState(44);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(45);
				match(RPAREN);
				}
				break;
			case 4:
				{
				_localctx = new VariableContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(47);
				match(ID);
				}
				break;
			case 5:
				{
				_localctx = new NumberContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(48);
				match(NUMBER);
				}
				break;
			case 6:
				{
				_localctx = new BooleanContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(49);
				((BooleanContext)_localctx).value = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==TRUE || _la==FALSE) ) {
					((BooleanContext)_localctx).value = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(72);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
			while ( _alt!=2 && _alt!= ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(70);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
					case 1:
						{
						_localctx = new OperatorContext(new ExpContext(_parentctx, _parentState));
						((OperatorContext)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_exp);
						setState(52);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(53);
						((OperatorContext)_localctx).operator = match(POWER);
						setState(54);
						((OperatorContext)_localctx).right = exp(11);
						}
						break;
					case 2:
						{
						_localctx = new OperatorContext(new ExpContext(_parentctx, _parentState));
						((OperatorContext)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_exp);
						setState(55);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(56);
						((OperatorContext)_localctx).operator = _input.LT(1);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << MUL) | (1L << DIV) | (1L << MOD))) != 0)) ) {
							((OperatorContext)_localctx).operator = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(57);
						((OperatorContext)_localctx).right = exp(10);
						}
						break;
					case 3:
						{
						_localctx = new OperatorContext(new ExpContext(_parentctx, _parentState));
						((OperatorContext)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_exp);
						setState(58);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(59);
						((OperatorContext)_localctx).operator = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==PLUS || _la==MINUS) ) {
							((OperatorContext)_localctx).operator = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(60);
						((OperatorContext)_localctx).right = exp(9);
						}
						break;
					case 4:
						{
						_localctx = new OperatorContext(new ExpContext(_parentctx, _parentState));
						((OperatorContext)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_exp);
						setState(61);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(62);
						((OperatorContext)_localctx).operator = _input.LT(1);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << EQUAL) | (1L << NEQUAL) | (1L << GT) | (1L << GE) | (1L << LT) | (1L << LE))) != 0)) ) {
							((OperatorContext)_localctx).operator = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(63);
						((OperatorContext)_localctx).right = exp(8);
						}
						break;
					case 5:
						{
						_localctx = new OperatorContext(new ExpContext(_parentctx, _parentState));
						((OperatorContext)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_exp);
						setState(64);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(65);
						((OperatorContext)_localctx).operator = match(AND);
						setState(66);
						((OperatorContext)_localctx).right = exp(7);
						}
						break;
					case 6:
						{
						_localctx = new OperatorContext(new ExpContext(_parentctx, _parentState));
						((OperatorContext)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_exp);
						setState(67);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(68);
						((OperatorContext)_localctx).operator = match(OR);
						setState(69);
						((OperatorContext)_localctx).right = exp(6);
						}
						break;
					}
					} 
				}
				setState(74);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 2:
			return exp_sempred((ExpContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean exp_sempred(ExpContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 10);
		case 1:
			return precpred(_ctx, 9);
		case 2:
			return precpred(_ctx, 8);
		case 3:
			return precpred(_ctx, 7);
		case 4:
			return precpred(_ctx, 6);
		case 5:
			return precpred(_ctx, 5);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\32N\4\2\t\2\4\3\t"+
		"\3\4\4\t\4\3\2\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\7\3\25\n\3"+
		"\f\3\16\3\30\13\3\3\3\3\3\3\3\5\3\35\n\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3"+
		"\4\3\4\3\4\3\4\3\4\7\4+\n\4\f\4\16\4.\13\4\3\4\3\4\3\4\3\4\3\4\5\4\65"+
		"\n\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3"+
		"\4\3\4\7\4I\n\4\f\4\16\4L\13\4\3\4\2\3\6\5\2\4\6\2\7\4\2\b\t\24\24\3\2"+
		"\27\30\3\2\n\f\3\2\b\t\3\2\16\23\2Y\2\b\3\2\2\2\4\34\3\2\2\2\6\64\3\2"+
		"\2\2\b\t\5\4\3\2\t\n\7\2\2\3\n\3\3\2\2\2\13\35\5\6\4\2\f\r\7\31\2\2\r"+
		"\16\7\7\2\2\16\35\5\6\4\2\17\20\7\31\2\2\20\21\7\4\2\2\21\26\7\31\2\2"+
		"\22\23\7\6\2\2\23\25\7\31\2\2\24\22\3\2\2\2\25\30\3\2\2\2\26\24\3\2\2"+
		"\2\26\27\3\2\2\2\27\31\3\2\2\2\30\26\3\2\2\2\31\32\7\5\2\2\32\33\7\7\2"+
		"\2\33\35\5\6\4\2\34\13\3\2\2\2\34\f\3\2\2\2\34\17\3\2\2\2\35\5\3\2\2\2"+
		"\36\37\b\4\1\2\37 \7\4\2\2 !\5\6\4\2!\"\7\5\2\2\"\65\3\2\2\2#$\t\2\2\2"+
		"$\65\5\6\4\r%&\7\31\2\2&\'\7\4\2\2\',\5\6\4\2()\7\6\2\2)+\5\6\4\2*(\3"+
		"\2\2\2+.\3\2\2\2,*\3\2\2\2,-\3\2\2\2-/\3\2\2\2.,\3\2\2\2/\60\7\5\2\2\60"+
		"\65\3\2\2\2\61\65\7\31\2\2\62\65\7\32\2\2\63\65\t\3\2\2\64\36\3\2\2\2"+
		"\64#\3\2\2\2\64%\3\2\2\2\64\61\3\2\2\2\64\62\3\2\2\2\64\63\3\2\2\2\65"+
		"J\3\2\2\2\66\67\f\f\2\2\678\7\r\2\28I\5\6\4\r9:\f\13\2\2:;\t\4\2\2;I\5"+
		"\6\4\f<=\f\n\2\2=>\t\5\2\2>I\5\6\4\13?@\f\t\2\2@A\t\6\2\2AI\5\6\4\nBC"+
		"\f\b\2\2CD\7\25\2\2DI\5\6\4\tEF\f\7\2\2FG\7\26\2\2GI\5\6\4\bH\66\3\2\2"+
		"\2H9\3\2\2\2H<\3\2\2\2H?\3\2\2\2HB\3\2\2\2HE\3\2\2\2IL\3\2\2\2JH\3\2\2"+
		"\2JK\3\2\2\2K\7\3\2\2\2LJ\3\2\2\2\b\26\34,\64HJ";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}

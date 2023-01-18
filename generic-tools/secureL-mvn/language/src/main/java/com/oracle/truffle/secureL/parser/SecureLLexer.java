// Generated from language/src/main/java/com/oracle/truffle/secureL/parser/SecureL.g4 by ANTLR 4.9.2
package com.oracle.truffle.secureL.parser;



import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class SecureLLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.9.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, INT=12, DOUBLE=13, BOOLEAN=14, STRING=15, WS=16;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "T__8", 
			"T__9", "T__10", "DIGIT", "INT", "DOUBLE", "BOOLEAN", "STRING", "WS"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'('", "')'", "','", "'sInt'", "'sDouble'", "'sBool'", "'sString'", 
			"'int'", "'double'", "'bool'", "'sArray'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			"INT", "DOUBLE", "BOOLEAN", "STRING", "WS"
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


	public SecureLLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "SecureL.g4"; }

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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\22\u008b\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\3\2\3\2\3\3\3\3\3\4\3\4\3\5\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3\6"+
		"\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3"+
		"\t\3\t\3\t\3\t\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\13\3\13\3\13\3\13\3\13\3"+
		"\f\3\f\3\f\3\f\3\f\3\f\3\f\3\r\3\r\3\16\6\16a\n\16\r\16\16\16b\3\17\6"+
		"\17f\n\17\r\17\16\17g\3\17\3\17\6\17l\n\17\r\17\16\17m\3\20\3\20\3\20"+
		"\3\20\3\20\3\20\3\20\3\20\3\20\3\20\5\20z\n\20\3\21\3\21\7\21~\n\21\f"+
		"\21\16\21\u0081\13\21\3\21\3\21\3\22\6\22\u0086\n\22\r\22\16\22\u0087"+
		"\3\22\3\22\3\177\2\23\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27"+
		"\r\31\2\33\16\35\17\37\20!\21#\22\3\2\4\3\2\62;\5\2\13\f\16\17\"\"\2\u0090"+
		"\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2"+
		"\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2"+
		"\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\3%\3\2\2"+
		"\2\5\'\3\2\2\2\7)\3\2\2\2\t+\3\2\2\2\13\60\3\2\2\2\r8\3\2\2\2\17>\3\2"+
		"\2\2\21F\3\2\2\2\23J\3\2\2\2\25Q\3\2\2\2\27V\3\2\2\2\31]\3\2\2\2\33`\3"+
		"\2\2\2\35e\3\2\2\2\37y\3\2\2\2!{\3\2\2\2#\u0085\3\2\2\2%&\7*\2\2&\4\3"+
		"\2\2\2\'(\7+\2\2(\6\3\2\2\2)*\7.\2\2*\b\3\2\2\2+,\7u\2\2,-\7K\2\2-.\7"+
		"p\2\2./\7v\2\2/\n\3\2\2\2\60\61\7u\2\2\61\62\7F\2\2\62\63\7q\2\2\63\64"+
		"\7w\2\2\64\65\7d\2\2\65\66\7n\2\2\66\67\7g\2\2\67\f\3\2\2\289\7u\2\29"+
		":\7D\2\2:;\7q\2\2;<\7q\2\2<=\7n\2\2=\16\3\2\2\2>?\7u\2\2?@\7U\2\2@A\7"+
		"v\2\2AB\7t\2\2BC\7k\2\2CD\7p\2\2DE\7i\2\2E\20\3\2\2\2FG\7k\2\2GH\7p\2"+
		"\2HI\7v\2\2I\22\3\2\2\2JK\7f\2\2KL\7q\2\2LM\7w\2\2MN\7d\2\2NO\7n\2\2O"+
		"P\7g\2\2P\24\3\2\2\2QR\7d\2\2RS\7q\2\2ST\7q\2\2TU\7n\2\2U\26\3\2\2\2V"+
		"W\7u\2\2WX\7C\2\2XY\7t\2\2YZ\7t\2\2Z[\7c\2\2[\\\7{\2\2\\\30\3\2\2\2]^"+
		"\t\2\2\2^\32\3\2\2\2_a\5\31\r\2`_\3\2\2\2ab\3\2\2\2b`\3\2\2\2bc\3\2\2"+
		"\2c\34\3\2\2\2df\5\31\r\2ed\3\2\2\2fg\3\2\2\2ge\3\2\2\2gh\3\2\2\2hi\3"+
		"\2\2\2ik\7\60\2\2jl\5\31\r\2kj\3\2\2\2lm\3\2\2\2mk\3\2\2\2mn\3\2\2\2n"+
		"\36\3\2\2\2op\7v\2\2pq\7t\2\2qr\7w\2\2rz\7g\2\2st\7h\2\2tu\7c\2\2uv\7"+
		"n\2\2vw\7u\2\2wz\7g\2\2xz\4\62\63\2yo\3\2\2\2ys\3\2\2\2yx\3\2\2\2z \3"+
		"\2\2\2{\177\7$\2\2|~\13\2\2\2}|\3\2\2\2~\u0081\3\2\2\2\177\u0080\3\2\2"+
		"\2\177}\3\2\2\2\u0080\u0082\3\2\2\2\u0081\177\3\2\2\2\u0082\u0083\7$\2"+
		"\2\u0083\"\3\2\2\2\u0084\u0086\t\3\2\2\u0085\u0084\3\2\2\2\u0086\u0087"+
		"\3\2\2\2\u0087\u0085\3\2\2\2\u0087\u0088\3\2\2\2\u0088\u0089\3\2\2\2\u0089"+
		"\u008a\b\22\2\2\u008a$\3\2\2\2\t\2bgmy\177\u0087\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}
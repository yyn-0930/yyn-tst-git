package jp.co.maruzenshowa.malos.common.constant;

/**
 * マーク文字.
 * 
 * @author IBM Wei　kai
 * @version 1.0
 */
public enum Mark {
	NAN(""), ELLIPSIS("..."), DOUBLE_QUOTATION("\""), SINGLE_QUOTATION("'"), DIR_SEPARATOR("/"), DASH("-"), UNDERSCORE("_"),
	WHITE_SPACE(" "), EMPTY_JSON("{}"), PLUS("+"), PERCENT("%"), COLON(":"), SEMICOLON(";"), QUESTION("?"),
	COMMA(","), AND("&"), PARENTHESES_L("("), PARENTHESES_R(")"), BRACKET_L("["), BRACKET_R("]"), BRACE_L("{"),
	BRACE_R("}"), DOT("."), EQUAL("="), HASH("#"), HAT("^"), PIPE("|"), 
	RETURN("\r"), NEWLINE("\n"), LF("\n"), CRLF("\r\n");
	
	private final String val;
	
	/**
	 * コンストラクタ.
	 * 
	 * @param value マーク文字
	 */
	Mark(String value) {  
        this.val = value;
    }
	
	/**
	 * 記号を取得.
	 * 
	 * @return 記号
	 */
	public String getVal() {  
        return this.val;
    }
}

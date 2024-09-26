package jp.co.maruzenshowa.malos.common.constant;

/**
 * 引用符タイプ.
 * 
 * @author IBM Wei　kai
 * @version 1.0
 */
public enum QuoteType {
	/**
     * 引用符を「"」で設定する
     */
	DOUBLE_QUOTATION('"'),
	/**
     * 引用符を「'」で設定する
     */
	SINGLE_QUOTATION('\''),
	/**
     * 引用符をなしで設定する
     * ※区切り文字がある場合、引用符を「"」で設定する
     */
	NONE('"');
	
	private final char val;
	
	/**
	 * コンストラクタ.
	 * 
	 * @param value 引用符タイプ
	 */
	QuoteType(char value) {  
        this.val = value;
    }
	
	/**
	 * 引用符を取得.
	 * 
	 * @return 引用符
	 */
	public char getVal() {  
        return this.val;
    }
}

package jp.co.maruzenshowa.malos.common.constant;

/**
* HttpHeader.java <br>
* Copyright MARUZENSHOWA LIMITED 2024 <br>
* <br>
* フレームワーク共通 <br>
* HttpHeader定数定義クラス <br>
* <br>
* HttpHeaderの定数を定義する <br>
*/
public class HttpHeader {
    /**
    *   ETAG<br>
    */
	public static final String ETAG = "etag";
    /**
    *   ETAG（マッチ）<br>
    */
	public static final String IF_MATCH = "if-match";
    /**
    *   トレースID<br>
    */
	public static final String TRACEID = "X-Amzn-Trace-Id";
    /**
    *   会社コード<br>
    */
	public static final String MALOS_COMPCODE = "malos-compcode";
    /**
    *   部所コード<br>
    */
	public static final String MALOS_DEPTCODE = "malos-deptcode";
    /**
    *   部所コード<br>
    */
	public static final String MALOS_AUTH_USERID = "malos-auth-userid";
}

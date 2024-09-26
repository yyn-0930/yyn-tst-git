package jp.co.maruzenshowa.malos.common.context;


import lombok.Getter;
import lombok.Setter;

/**
* DynamicContext.java <br>
* Copyright MARUZENSHOWA LIMITED 2024 <br>
* <br>
* フレームワーク共通 <br>
* アプリケーション・動的なコンテキスト <br>
* <br>
*/
public class DynamicContext {
	
    /**
    *   eTag<br>
    */
	@Getter @Setter
	private String etag;
    
    /**
    *   会社コード<br>
    */
    @Getter @Setter
    private String compCode;
    
    /**
    *   部所コード<br>
    */
    @Getter @Setter
    private String deptCode;
    
    /**
    *   荷主コード<br>
    */
    @Getter @Setter
    private String shprCode;
    
    /**
    *   ユーザーID<br>
    */
    @Getter @Setter
    private String userId;

    /**
    *   動的コンテキスト情報を保持するThreadLoacalオブジェクト<br>
    */
	private static final ThreadLocal<DynamicContext> CONTEXT_HOLDER = new ThreadLocal<>();

	/**
	* メソッド名    ：　コンテキスト取得 <br>
	* メソッド説明  ：　動的コンテキスト情報を取得します。 <br>
	*  <br>
	* @return 動的コンテキスト
	*/
	public static DynamicContext getContext() {
		DynamicContext context = CONTEXT_HOLDER.get();
		if (context == null) {
			context = new DynamicContext();
			CONTEXT_HOLDER.set(context);
		}
		return context;
	}

	/**
	* メソッド名    ：　コンテキスト設定 <br>
	* メソッド説明  ：　動的コンテキスト情報を設定する。 <br>
	* 注意事項  ：TimeLimiterを利用する際に、DynamicContextを共有するメッソド、それ以外は呼び出せないです
	*  <br>
	* @param context 動的コンテキスト
	*/
	public static void setContext(DynamicContext context) {
		CONTEXT_HOLDER.set(context);
	}

	/**
	* メソッド名    ：　コンテキスト削除 <br>
	* メソッド説明  ：　動的コンテキスト情報を削除します。 <br>
	*  <br>
	*/
	public static void remove() {
		CONTEXT_HOLDER.remove();
	}
}


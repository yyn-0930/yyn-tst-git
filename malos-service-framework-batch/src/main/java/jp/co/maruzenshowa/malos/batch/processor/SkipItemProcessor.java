package jp.co.maruzenshowa.malos.batch.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.context.annotation.Scope;
import org.springframework.lang.Nullable;

/**
 * スキップの場合で使用するクラス.
 * 
 * チャンクモードでプロセッサーがない場合、スキップクラス.<br>
 * チャンクモードでプロセッサーがない場合、スキップクラスを提供する.
 * 
 * @author IBM wei kai
 * @version 1.0
 * 
 * @param <T> 範囲：Step.
 */
@Scope("step")
public class SkipItemProcessor<T> implements ItemProcessor<T, T> {

	/**
	 * チャンクモードでスキッププロセッサー.
	 */
	@Nullable
	@Override
	public T process(T item) throws Exception {
		return item;
	}
}
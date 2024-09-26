package jp.co.maruzenshowa.malos.batch.base;

import org.springframework.batch.item.ItemCountAware;

import lombok.Getter;

/**
 * 行番号取得ベースDTO.
 * 
 * @author  IBM Wei kai
 * @version 1.0
 */
public class LineNumBaseDto implements ItemCountAware {
	
	@Getter
	protected int lineNumber;
	
	@Override
	public void setItemCount(int count) {
		this.lineNumber = count;
	}
}

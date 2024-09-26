package jp.co.maruzenshowa.malos.common.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import org.apache.commons.lang3.StringUtils;


/**
 * 採番ユーティリティクラス.<br>
 * Integerをベースに最大は「3JUSWC7」まで採番する
 * 
 * @author IBM Wei　kai
 * @version 1.0
 */
public class SequneceUtils {

	/**
	 * コンストラクター
	 */
	private SequneceUtils() {}
	
	/**
	 * 最下位桁は10進数、それ以外は36進数で変換して返却する.
	 * 
	 * @param sequnece 新連番
	 * @param length 連番桁数
	 * @return 英字変換結果
	 */
	public static String sequneceToAlphanum(int sequnece, int length) {
		String temp = String.valueOf(sequnece);
		// 最下位桁
		String lastNum = temp.substring(temp.length() - 1);
		int otherNum = (sequnece - Integer.parseInt(lastNum)) / 10;
		return StringUtils.leftPad(Integer.toString(otherNum, 36).toUpperCase(Locale.ROOT), length - 1, "0") + lastNum;
	}
	
	/**
	 * 英数字は連番へ変換して返却する.
	 * 
	 * @param alphanumeric 英数字
	 * @return 連番
	 */
	public static Integer alphanumToSequnece(String alphanumeric) {
		// 最下位桁
		String lastNum = alphanumeric.substring(alphanumeric.length() - 1);
		String otherNum = alphanumeric.substring(0, alphanumeric.length() - 1);
		return Integer.parseInt(otherNum, 36) * 10 + Integer.parseInt(lastNum);
	}
	
	/**
	 * 採番番号ブロックを取得する.
	 * 
	 * @param sequnce 現在連番
	 * @param block ブロック(採番数)
	 * @param startNum 開始番号
	 * @param length 桁数
	 * @return 連番
	 */
	public static Iterator<Integer> getSequneceList(int sequnce, int block, int startNum, int length) {
		List<Integer> sequneceList = new ArrayList<>();
		int max = 10;
		for (int i = 0; i < length - 1; i++) {
			max = max * 36;
		}
		max = max - 1;
		int tempSequnce = sequnce;
		for (int i = 0; i < block; i++) {
			tempSequnce = tempSequnce + 1;
			if (tempSequnce > max) {
				tempSequnce = startNum;
			}
			sequneceList.add(tempSequnce);
		}
		return sequneceList.iterator();
	}
	
	/**
	 * 次の採番番号を取得する.
	 * 
	 * @param sequnce 現在連番
	 * @param startNum 開始番号
	 * @param length 桁数
	 * @return 連番
	 */
	public static Integer getNextSequnece(int sequnce, int startNum, int length) {
		int max = 10;
		for (int i = 0; i < length - 1; i++) {
			max = max * 36;
		}
		max = max - 1;
		int tempSequnce = sequnce;
		tempSequnce = tempSequnce + 1;
		if (tempSequnce > max) {
			tempSequnce = startNum;
		}
		return tempSequnce;
	}
}

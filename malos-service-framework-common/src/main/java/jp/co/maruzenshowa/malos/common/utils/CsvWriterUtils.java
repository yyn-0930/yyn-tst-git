package jp.co.maruzenshowa.malos.common.utils;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import jp.co.maruzenshowa.malos.common.annotation.csv.CsvCloumnIndex;
import jp.co.maruzenshowa.malos.common.annotation.csv.CsvDateFormat;
import jp.co.maruzenshowa.malos.common.annotation.csv.CsvFormat;
import jp.co.maruzenshowa.malos.common.annotation.csv.CsvHeaderName;
import jp.co.maruzenshowa.malos.common.constant.Mark;
import jp.co.maruzenshowa.malos.common.constant.MessageCode;
import jp.co.maruzenshowa.malos.common.constant.QuoteType;
import jp.co.maruzenshowa.malos.common.exception.SystemException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;



/**
 * CSVファイル作成ユーティリティ.<br>
 * オブジェクト配列からCSVファイルを作成する機能を提供する.
 * 
 * @author IBM Wei kai
 * @version 1.0
 */
public class CsvWriterUtils {

	/**
	 * コンストラクタ.
	 */
	private CsvWriterUtils() {}

	/**
	 * 検索結果オブジェクトリストからCSVファイルを作成する処理.<br>
	 * 文字列の囲み: ダブルクォーテーション(")
	 * 区切り: カンマ（,）
	 * 
	 * @param <T>      CSVデータタイプ
	 * @param filePath ファイルパス
	 * @param dataList データリスト
	 */
	public static <T> void writeCsv(final String filePath, final List<T> dataList) {
		File csvFile = new File(filePath);
		// 親フォルダの存在判定
		if (!csvFile.getParentFile().exists()) {
			csvFile.getParentFile().mkdirs();
		}
		boolean isExists = csvFile.exists();
		// ファイルが存在する場合、ヘッダー情報を再出力しない.
		
		try (OutputStream os = Files.newOutputStream(Paths.get(filePath))) {
			writeStream(os, dataList, isExists);
		} catch (IOException e) {
			throw new SystemException(MessageCode.A9007E.name(), e);
		}
	}
	
	/**
	 * 検索結果オブジェクトリストからCSVファイルを作成する処理.<br>
	 * 文字列の囲み: ダブルクォーテーション(")
	 * 区切り: カンマ（,）
	 * ヘッダーあり
	 * 
	 * @param <T>      CSVデータタイプ
	 * @param os       アウトストリーム
	 * @param dataList データ検索リスト
	 */
	public static <T> void writeStream(final OutputStream os, final List<T> dataList) {
		writeStream(os, dataList, false);
	}

	/**
	 * 検索結果オブジェクトリストからCSVファイルを作成する処理.<br>
	 * 区切り: カンマ（,）
	 * 
	 * @param <T>        CSVデータタイプ
	 * @param os         アウトストリーム
	 * @param dataList   データリスト
	 * @param appendFlag  既存ファイルへ追加出力フラグ（true:あり, false:なし）
	 */
	private static <T> void writeStream(final OutputStream os, final List<T> dataList, boolean appendFlag) {
		QuoteType quoteType = QuoteType.DOUBLE_QUOTATION;
		String delimiter = Mark.COMMA.name();
		boolean skipHeader = false;
		Class<?> clazz = dataList.get(0).getClass();
		if (clazz.getAnnotation(CsvFormat.class) != null) {
			quoteType = clazz.getAnnotation(CsvFormat.class).quoteType();
			delimiter = clazz.getAnnotation(CsvFormat.class).delimiter();
			skipHeader = clazz.getAnnotation(CsvFormat.class).skipHeader();
		}
		
		List<String> header = new ArrayList<>();
		List<List<Object>> content = new ArrayList<>();
		dataList.forEach(obj -> content.add(convertToRow(obj, getFieldsCount(clazz))));
		// ヘッダー出力判定
		if (!skipHeader && !appendFlag) {
			header = convertToHeader(clazz);
		}

		writeToCsv(os, header, content, quoteType, delimiter);
	}

	/**
	 * CSV作成処理.<br>
	 * 文字列の囲み: ダブルクォーテーション(")
	 * 区切り: カンマ（,）
	 * 
	 * @param os         アウトストリーム
	 * @param fileHeader ファイルヘッダー
	 * @param content    ファイル内容
	 * @param quoteType 引用符タイプ
	 * @param delimiter  区切り文字
	 */
	private static void writeToCsv(OutputStream os, final List<String> fileHeader, List<List<Object>> content, QuoteType quoteType,
			String delimiter) {
		QuoteMode quoteMode = QuoteType.NONE.equals(quoteType) ? QuoteMode.MINIMAL : QuoteMode.NON_NUMERIC;
		try (CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(os), CSVFormat.EXCEL.builder()
					.setNullString(Mark.NAN.name())
					.setQuoteMode(quoteMode)
					.setQuote(quoteType.getVal())
					.setDelimiter(delimiter)
					.build())) {
	
			// 新規作成の場合、CSVヘッダーを追加する
			if (!fileHeader.isEmpty()) {
				printer.printRecord(fileHeader);
			}
	
			for (List<Object> c : content) {
				printer.printRecord(c);
			}
			printer.flush();
		} catch (IOException e) {
			throw new SystemException(MessageCode.A9007E.name(), e);
		}
	}

	/**
	 * 検索結果オブジェクトより、ヘッダーを作成処理
	 * CsvCloumnIndex設定の順番より、CsvHeaderName設定の値はヘッダーを作成する
	 * 
	 * @param <T> CSVデータタイプ
	 * @param clazz オブジェクト対象クラス
	 * @return ファイルヘッダー
	 */
	public static <T> List<String> convertToHeader(Class<T> clazz) {
		Field[] fields = clazz.getDeclaredFields();
		List<String> emptyList = Arrays.asList(new String[fields.length]);
		List<String> recordList = new ArrayList<>(emptyList);
		for (Field field : fields) {
			if (field.getAnnotation(CsvCloumnIndex.class) == null) {
				continue;
			}
			int index = field.getAnnotation(CsvCloumnIndex.class).value() - 1;
			String headerName = field.getAnnotation(CsvHeaderName.class).value();
			recordList.set(index, headerName);
		}

		recordList.removeAll(Collections.singleton(null));
		return recordList;
	}

	/**
	 * 出力するオブジェクトの項目列数を取得する
	 * CsvCloumnIndex設定の項目より、列数を取得する
	 * 
	 * @param <T> CSVデータタイプ
	 * @param clazz オブジェクト対象クラス
	 * @return 列数
	 */
	private static <T> int getFieldsCount(Class<T> clazz) {
		Field[] fields = clazz.getDeclaredFields();
		int count = fields.length;
		for (Field field : fields) {
			if (field.getAnnotation(CsvCloumnIndex.class) == null) {
				count = count - 1;
			}
		}
		return count;
	}
	
	/**
	 * 検索結果オブジェクトより、データを作成処理
	 * CsvCloumnIndex設定の順番より、オブジェクトのデータを取得して、データリストを作成する
	 * 
	 * @param <T> CSVデータタイプ
	 * @param o オブジェクト対象
	 * @param len 列数
	 * @return 結果リスト
	 */
	public static <T> List<Object> convertToRow(T o, int len) {
		Field[] fields = o.getClass().getDeclaredFields();
		List<Object> emptyList = Arrays.asList((Object[]) new String[len]);
		List<Object> recordList = new ArrayList<>(emptyList);
		try {
			for (Field field : fields) {
				String fieldName = field.getName();
				fieldName = fieldName.substring(0, 1).toUpperCase(Locale.ROOT) + fieldName.substring(1);
				if (field.getAnnotation(CsvCloumnIndex.class) == null) {
					continue;
				}
				int index = field.getAnnotation(CsvCloumnIndex.class).value() - 1;
				Method getMethod = o.getClass().getMethod("get" + fieldName);
				Object value = getMethod.invoke(o);
				recordList.set(index, dataFormat(value, field));
			}
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | InvocationTargetException e) {
			throw new SystemException(MessageCode.A9008E.name(), e);
		}
		
		return recordList;
	}

	/**
	 * データフォーマット処理.<br>
	 * CsvDateFormatの指定フォーマットより、データフォーマット変換を行う.
	 * 
	 * @param obj   オブジェクト対象
	 * @param field フィールド
	 * @return 変換したデータ
	 */
	private static Object dataFormat(Object obj, Field field) {
		if (obj == null) {
			return "";
		}
		CsvDateFormat dateFormat = field.getAnnotation(CsvDateFormat.class);
		if (dateFormat != null && obj instanceof java.util.Date) {
		  return new SimpleDateFormat(dateFormat.pattern(), Locale.getDefault()).format(obj);
		}
		return obj;
	}

}

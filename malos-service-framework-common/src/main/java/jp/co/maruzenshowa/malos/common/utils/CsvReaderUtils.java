package jp.co.maruzenshowa.malos.common.utils;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import jp.co.maruzenshowa.malos.common.annotation.csv.CsvCloumnIndex;
import jp.co.maruzenshowa.malos.common.annotation.csv.CsvDateFormat;
import jp.co.maruzenshowa.malos.common.annotation.csv.CsvFormat;
import jp.co.maruzenshowa.malos.common.constant.Mark;
import jp.co.maruzenshowa.malos.common.constant.MessageCode;
import jp.co.maruzenshowa.malos.common.constant.QuoteType;
import jp.co.maruzenshowa.malos.common.exception.SystemException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.QuoteMode;



/**
 * CSVファイル読み込みユーティリティ.<br>
 * 指定したファイルまたはデータストリームからCSVファイルを読み込む機能を実現する.
 * 
 * @author  IBM Wei kai
 * @version 1.0
 */
public class CsvReaderUtils {

	/**
	 * コンストラクター
	 */
	private CsvReaderUtils() {}

	/**
	 * CSVストリームからデータを読込み、指定クラスに変換して結果リストを返す.<br>
	 * デフォルト：<br>
	 * 文字囲む: ダブルクォーテーション(")<br>
	 * 区切り: カンマ（,）<br>
	 * 
	 * @param <T> CSVデータタイプ
	 * @param isr ストリームリーダー
	 * @param skipLine スキップ行数
	 * @param clazz 変換後のデータクラス
	 * @return 変換後のCSVレコードリスト
	 */
	public static <T> List<T> readCsvStream(InputStreamReader isr, int skipLine, Class<T> clazz) {
		QuoteType quoteType = QuoteType.DOUBLE_QUOTATION;
		String delimiter = Mark.COMMA.name();
		if (clazz.getAnnotation(CsvFormat.class) != null) {
			quoteType = clazz.getAnnotation(CsvFormat.class).quoteType();
			delimiter = clazz.getAnnotation(CsvFormat.class).delimiter();
		}
		// データ読み込み
		var records = readCsvStream(isr, skipLine, quoteType, delimiter, true);
		// データ変換
		List<T> list = new ArrayList<>();
		records.forEach(r -> {
			var target = convertToObject(r, clazz);
			list.add(target);
		});

		return list;
	}

	/**
	 * CSVストリームからデータを読み込み、CSVレコードリストを返す.
	 * 
	 * @param isr	ストリームリーダー
	 * @param skipLine   スキップ行数
	 * @param quoteType 引用符タイプ
	 * @param delimiter 区切り文字
	 * @param ignrEmptyLine スキップ空白行
	 * @return CSVレコードリスト
	 */
	public static List<CSVRecord> readCsvStream(InputStreamReader isr, int skipLine, QuoteType quoteType,
			String delimiter, boolean ignrEmptyLine) {
		QuoteMode quoteMode = QuoteType.NONE.equals(quoteType) ? QuoteMode.MINIMAL : QuoteMode.ALL;
		try {
			var records = CSVFormat.RFC4180.builder()
					.setQuote(quoteType.getVal())
					.setQuoteMode(quoteMode)
					.setDelimiter(delimiter)
					.setIgnoreEmptyLines(ignrEmptyLine)
					.build().parse(isr).getRecords();

			// スキップ行数
			return records.subList(skipLine, records.size());
		} catch (IOException e) {
			throw new SystemException(MessageCode.A9003E.name(), e);
		}
	}

	/**
	 * CSVファイルを読み込み、CSVレコードリストを返す.<br>
	 * デフォルト：<br>
	 * 文字囲む: ダブルクォーテーション(")<br>
	 * 区切り: カンマ（,）<br>
	 * 
	 * @param <T> CSVデータタイプ
	 * @param csvFile ファイルパス
	 * @param skipLine スキップ行数
	 * @param clazz 変換後のデータクラス
	 * @return 変換後のCSVレコードリスト
	 */
	public static <T> List<T> readCsvFile(final String csvFile, int skipLine, Class<T> clazz) {
		Charset charset = StandardCharsets.UTF_8;
		if (clazz.getAnnotation(CsvFormat.class) != null) {
			charset = Charset.forName(clazz.getAnnotation(CsvFormat.class).charset());
		}
		try (DataInputStream in = new DataInputStream(Files.newInputStream(Paths.get(csvFile)));
				InputStreamReader isr = new InputStreamReader(in, charset);) {
			return readCsvStream(isr, skipLine, clazz);
		} catch (IOException e) {
			Map<String, String> param = new ConcurrentHashMap<>();
			param.put("filePath", csvFile);
			throw new SystemException(MessageCode.A9004E.name(), param, e);
		}
	}

	/**
	 * CSVレコードは対象オブジェクトに変換する
	 * 
	 * @param <T> CSVデータタイプ
	 * @param csvRecord CSVレコード
	 * @param clazz 対象クラス
	 * @return 対象オブジェクト
	 */
	public static <T> T convertToObject(CSVRecord csvRecord, Class<T> clazz) {
		try {
			T o = clazz.getDeclaredConstructor().newInstance();
			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields) {
				String fieldName = field.getName();
				if (field.getAnnotation(CsvCloumnIndex.class) == null) {
					continue;
				}
				int index = field.getAnnotation(CsvCloumnIndex.class).value() - 1;
				CsvDateFormat dateFormat = field.getAnnotation(CsvDateFormat.class);
				String value = csvRecord.get(index);
				fieldName = fieldName.substring(0, 1).toUpperCase(Locale.ROOT) + fieldName.substring(1);
				Method setMethod = clazz.getMethod("set" + fieldName, field.getType());
				if (field.getType().equals(Integer.class) || field.getType().equals(int.class)) {
					setMethod.invoke(o, Integer.valueOf(value));
				} else if (field.getType().equals(BigDecimal.class)) {
					setMethod.invoke(o, new BigDecimal(value));
				} else if (field.getType().equals(String.class)) {
					setMethod.invoke(o, value);
				} else if (field.getType().equals(Date.class) && dateFormat != null) {
					setMethod.invoke(o, DateUtils.toDate(value, dateFormat.pattern()));
				} else {
					throw new SystemException(MessageCode.A9005E.name());
				}
			}
			
			return o;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw new SystemException(MessageCode.A9006E.name(), e);
		}
	}
}

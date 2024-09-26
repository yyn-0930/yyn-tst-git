package jp.co.maruzenshowa.malos.common.utils;


import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jp.co.maruzenshowa.malos.common.base.MockWebServer;
import jp.co.maruzenshowa.malos.common.base.dto.ResponseDto;
import jp.co.maruzenshowa.malos.common.context.DynamicContext;
import jp.co.maruzenshowa.malos.common.dto.PaplesResultDto;
import jp.co.maruzenshowa.malos.common.dto.ResultDto;
import jp.co.maruzenshowa.malos.common.dto.UserInfoDto;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.model.Header;
import org.mockserver.model.Parameter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@SpringBootTest
@AutoConfigureMockMvc
class WebClientUtilsTest {

	@Value("${app.service-url.url}")
	private String baseUrl;

	@Value("${app.service-url.port}")
	private static int prot;

	private static final List<MockServerClient> MOCKLIST = new ArrayList<>();
	private static  MockWebServer mwc = new MockWebServer(8001);

	@BeforeAll
	@DisplayName("Start the mock server and mock clients for mocking the Gitea api.")
	public static void startMockServerClient() throws FileNotFoundException {
		try {
			List<Header> headersList1 = new ArrayList<>();
			Header header1 = new Header("MALoS-DeptCode","DEPT001");
			headersList1.add(header1);
			// MockAPI
			MOCKLIST.add(mwc.registerClientWithResponseJson(MockWebServer.Method.GET, "/test/api",
					headersList1,null, HttpStatus.OK, "mockApi/getapi_200.json"));

			List<Parameter> parameters = new ArrayList<>();
			parameters.add(new Parameter("id", "ID004"));
			MOCKLIST.add(mwc.registerClientWithResponseJson(MockWebServer.Method.GET, "/test/api",
					headersList1, parameters, HttpStatus.OK, "mockApi/getapi_200.json"));

			List<Header> headersList2 = new ArrayList<>();
			Header header2 = new Header("MALoS-DeptCode","DEPT002");
			headersList2.add(header2);
			MOCKLIST.add(mwc.registerClientWithResponseJson(MockWebServer.Method.GET, "/test/api",
					headersList2, null, HttpStatus.BAD_REQUEST, "mockApi/getapi_400.json"));
			List<Header> headers3 = new ArrayList<>();
			Header header3 = new Header("MALoS-DeptCode","DEPT003");
			headers3.add(header3);
			MOCKLIST.add(mwc.registerClientWithResponseJson(MockWebServer.Method.GET, "/test/api",
					headers3, null, HttpStatus.INTERNAL_SERVER_ERROR, "mockApi/getapi_500.json"));
			MOCKLIST.add(mwc.registerClientWithResponseJson(MockWebServer.Method.POST, "/test/api",
					null, null, HttpStatus.OK, "mockApi/postapi_200.json"));
			MOCKLIST.add(mwc.registerClientWithResponseJson(MockWebServer.Method.PUT, "/test/api",
					null, null, HttpStatus.OK, "mockApi/putapi_200.json"));
			MOCKLIST.add(mwc.registerClientWithResponseJson(MockWebServer.Method.PUT, "/test/api/204",
					null, null, HttpStatus.NO_CONTENT, "mockApi/putapi_204.json"));
			MOCKLIST.add(mwc.registerClientWithResponseJson(MockWebServer.Method.PATCH, "/test/api",
					null, null, HttpStatus.OK, "mockApi/patchapi_200.json"));
			MOCKLIST.add(mwc.registerClientWithResponseJson(MockWebServer.Method.DELETE, "/test/api",
					null, null, HttpStatus.OK, "mockApi/deleteapi_200.json"));
			MOCKLIST.add(mwc.registerClientWithResponseJson(MockWebServer.Method.DELETE, "/test/api/204",
					null, null, HttpStatus.NO_CONTENT, "mockApi/deleteapi_204.json"));

			// 外部API呼び出す方法
			// MockAPI
			MOCKLIST.add(mwc.registerClientWithResponseJson(MockWebServer.Method.GET, "/test/api2",
					headersList1, parameters, HttpStatus.OK, "mockApi/getExternalApi_200.json"));
			MOCKLIST.add(mwc.registerClientWithResponseJson(MockWebServer.Method.GET, "/test/api2",
					headersList1, null, HttpStatus.OK, "mockApi/getExternalApi_200.json"));
			MOCKLIST.add(mwc.registerClientWithResponseJson(MockWebServer.Method.GET, "/test/api2",
					headersList2, null, HttpStatus.BAD_REQUEST, "mockApi/getExternalApi_400.json"));
			headers3.add(header3);
			MOCKLIST.add(mwc.registerClientWithResponseJson(MockWebServer.Method.GET, "/test/api2",
					headers3, null, HttpStatus.INTERNAL_SERVER_ERROR, "mockApi/getExternalApi_500.json"));
			MOCKLIST.add(mwc.registerClientWithResponseJson(MockWebServer.Method.POST, "/test/api2",
					null,null,  HttpStatus.OK, "mockApi/postExternalApi_200.json"));
			MOCKLIST.add(mwc.registerClientWithResponseJson(MockWebServer.Method.PUT, "/test/api2",
					null,null,  HttpStatus.OK, "mockApi/putExternalApi_200.json"));
			MOCKLIST.add(mwc.registerClientWithResponseJson(MockWebServer.Method.PUT, "/test/api2/204",
					null, null, HttpStatus.NO_CONTENT, "mockApi/putExternalApi_204.json"));
			MOCKLIST.add(mwc.registerClientWithResponseJson(MockWebServer.Method.PATCH, "/test/api2",
					null, null, HttpStatus.OK, "mockApi/patchExternalApi_200.json"));
			MOCKLIST.add(mwc.registerClientWithResponseJson(MockWebServer.Method.DELETE, "/test/api2",
					null, null, HttpStatus.OK, "mockApi/deleteExternalApi_200.json"));
			MOCKLIST.add(mwc.registerClientWithResponseJson(MockWebServer.Method.DELETE, "/test/api2/204",
					null, null, HttpStatus.NO_CONTENT, "mockApi/deleteExternalApi_204.json"));

			// 外部API呼び出す方法(Paple MultiPart)
			// MockAPI
			// PDFファイル生成
			// ファイル返却フラグ ↓
			// 0 : 生成処理を実施し、取得のために必要なダウンロードキーを返却します。
			// 1: 生成したファイルを返却します。
			String returnFileFlagOne = "1";
			String returnFileFlagZero = "0";
			String key = "12345678912345678_12345678"; //ダウンロードキーを指定
			MOCKLIST.add(mwc.registerClientWithResponsePDF(MockWebServer.Method.POST, "/webapi/v1/pdf",
					null, null, HttpStatus.OK, Paths.get(ClassLoader.getSystemResource("mockApi/getPDf.pdf").toURI()).toString(), returnFileFlagOne));
			MOCKLIST.add(mwc.registerClientWithResponsePDF(MockWebServer.Method.POST, "/webapi/v1/pdf2",
					null, null, HttpStatus.OK, "mockApi/postPDF_200.json",returnFileFlagZero));
			// PDFファイル取得
			MOCKLIST.add(mwc.registerClientWithResponsePDF(MockWebServer.Method.GET, "/webapi/v1/pdf/" + key,
					null, null, HttpStatus.OK, Paths.get(ClassLoader.getSystemResource("mockApi/getPDf.pdf").toURI()).toString(), ""));
			// Excelファイル生成
			String key2 = "12345678912345678_12345678"; //ダウンロードキーを指定
			MOCKLIST.add(mwc.registerClientWithResponsePDF(MockWebServer.Method.POST, "/webapi/v1/excel",
					null, null, HttpStatus.OK, Paths.get(ClassLoader.getSystemResource("mockApi/getExcel.xlsx").toURI()).toString(), returnFileFlagOne));
			// Excelファイル取得
			MOCKLIST.add(mwc.registerClientWithResponsePDF(MockWebServer.Method.GET, "/webapi/v1/excel/" + key2,
					null, null, HttpStatus.OK, Paths.get(ClassLoader.getSystemResource("mockApi/getExcel.xlsx").toURI()).toString(), ""));
			// 帳票を印刷する
			MOCKLIST.add(mwc.registerClientWithResponsePDF(MockWebServer.Method.POST, "/webapi/v1/print",
					null, null, HttpStatus.OK, Paths.get(ClassLoader.getSystemResource("mockApi/getPDf.pdf").toURI()).toString(), returnFileFlagOne));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@AfterAll
	@DisplayName("Stop the mock server and mock clients for mocking the Gitea api.")
	public static void stopMockServerClients() {
		MOCKLIST.stream().forEach(mockPath -> {
			if (!mockPath.hasStopped()) {
				mockPath.close();
			}
		});
		mwc.getMockServer().stop();
	}

	/**
	 * テスト観点:<br>
	 * 内部API呼び出す方法
	 *
	 * テスト条件:<br>
	 * なし
	 *
	 * 予想結果:<br>
	 * 予想される戻り値と一致する
	 *
	 * @throws Exception 異常
	 */
	@Test
	void testInternalApiInterface() throws Exception {
		// Get方法（パラメータなし） → httpステータス:200
		DynamicContext.getContext().setDeptCode("DEPT001");
		WebClient webClient1 = InternalWebClientUtils.getWebClient(baseUrl, true);
		ResponseDto<ResultDto> response1 = InternalWebClientUtils.get(webClient1, "/test/api", ResultDto.class);

		// Get方法 → httpステータス:400
		DynamicContext.getContext().setDeptCode("DEPT002");
		WebClient webClient2 = InternalWebClientUtils.getWebClient(baseUrl, true);
		try {
			InternalWebClientUtils.get(webClient2, "/test/api", ResultDto.class);
		} catch (WebClientResponseException e) {
			e.printStackTrace();
		}

		// Get方法 → httpステータス:500
		DynamicContext.getContext().setDeptCode("DEPT003");
		WebClient webClient3 = InternalWebClientUtils.getWebClient(baseUrl, true);
		try {
			InternalWebClientUtils.get(webClient3, "/test/api", ResultDto.class);
		} catch (WebClientResponseException e) {
			e.printStackTrace();
		}

		// Get方法（パラメータあり） → httpステータス:200
		DynamicContext.getContext().setDeptCode("DEPT001");
		WebClient webClient4 = InternalWebClientUtils.getWebClient(baseUrl, true);
		Map<String, Object> param = new HashMap<>();
		param.put("ID", "ID002");
		List<String> nameList = new ArrayList<>();
		nameList.add("佐藤");
		nameList.add("田中");
		param.put("NAME", nameList);
		ResponseDto<ResultDto> response4 = InternalWebClientUtils.get(webClient4, "/test/api", ResultDto.class, param);

		// Post方法
		WebClient webClient5 = InternalWebClientUtils.getWebClient(baseUrl, false);
		ResultDto request5 = new ResultDto();
		request5.setId("ID002");
		request5.setName("田中");
		request5.setAge(58);
		ResponseDto<ResultDto> response5 = InternalWebClientUtils.post(webClient5, "/test/api", ResultDto.class, request5);

		// put方法 → httpステータス:200
		WebClient webClient6 = InternalWebClientUtils.getWebClient(baseUrl, false);
		ResultDto request6 = new ResultDto();
		request6.setId("ID003");
		request6.setName("山田");
		request6.setAge(64);
		ResponseDto<ResultDto> response6 = InternalWebClientUtils.put(webClient6, "/test/api", ResultDto.class, request6);

		// put方法→ httpステータス:204
		InternalWebClientUtils.put(webClient6, "/test/api/204", request6);

		// delete方法（パラメータあり） → httpステータス:200
		WebClient webClient7 = InternalWebClientUtils.getWebClient(baseUrl, false);
		ResponseDto<ResultDto> response7 = InternalWebClientUtils.delete(webClient7, "/test/api", ResultDto.class, param);

		// delete方法（パラメータなし） → httpステータス:200
		WebClient webClient8 = InternalWebClientUtils.getWebClient(baseUrl, false);
		ResponseDto<ResultDto> response8 = InternalWebClientUtils.delete(webClient8, "/test/api", ResultDto.class);

		// delete方法 → httpステータス:204
		InternalWebClientUtils.delete(webClient8, "/test/api/204");

		// pacth方法
		WebClient webClient9 = InternalWebClientUtils.getWebClient(baseUrl, false);
		ResultDto request9 = new ResultDto();
		request9.setId("ID003");
		request9.setAge(64);
		ResponseDto<ResultDto> response9 = InternalWebClientUtils.patch(webClient9, "/test/api", ResultDto.class, request9);
	}

	/**
	 * テスト観点:<br>
	 * 外部API呼び出す方法
	 *
	 * テスト条件:<br>
	 * なし
	 *
	 * 予想結果:<br>
	 * 予想される戻り値と一致する
	 *
	 * @throws Exception 異常
	 */
	@Test
	void testExternalApiInterface() throws Exception {
		// Get方法（パラメータなし） → httpステータス:200
		HttpHeaders httpHeaders1 = new HttpHeaders();
		httpHeaders1.set("Api-Key", "your_api_key");
		httpHeaders1.set("MALoS-DeptCode", "DEPT001");
		WebClient webClient1 = WebClient.create(baseUrl);
		UserInfoDto<?> response1 = ExternalWebClientUtils.get(webClient1, "/test/api2", UserInfoDto.class, httpHeaders1);

		// Get方法 → httpステータス:400
		HttpHeaders httpHeaders2 = new HttpHeaders();
		httpHeaders2.set("MALoS-DeptCode", "DEPT002");
		WebClient webClient2 = WebClient.create(baseUrl);
		try {
			ExternalWebClientUtils.get(webClient2, "/test/api2", UserInfoDto.class, httpHeaders2);
		} catch (WebClientResponseException e) {
			e.printStackTrace();
		}

		// Get方法 → httpステータス:500
		HttpHeaders httpHeaders3 = new HttpHeaders();
		httpHeaders3.set("MALoS-DeptCode", "DEPT003");
		WebClient webClient3 = WebClient.create(baseUrl);
		try {
			UserInfoDto<?> resultDto3 = ExternalWebClientUtils.get(webClient3, "/test/api2", UserInfoDto.class, httpHeaders3);
		} catch (WebClientResponseException e) {
			e.printStackTrace();
		}

		// Get方法（パラメータあり） → httpステータス:200
		WebClient webClient4 = WebClient.create(baseUrl);
		Map<String, Object> param = new HashMap<>();
		param.put("id", "ID004");
//		param.put("name", "山中");

		HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.set("MALoS-CompCode", "COMP001");
		httpHeaders.set("MALoS-DeptCode", "DEPT001");
//        httpHeaders.set("MALoS-ShprCode", "SHPR001");
//        httpHeaders.set("Authorization", "token");
		httpHeaders.set("Api-Key", "your_api_key");
//        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		try {
			UserInfoDto<?> resultDto4 = ExternalWebClientUtils.get(webClient4, "/test/api2", UserInfoDto.class, httpHeaders,param);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Post方法
		WebClient webClient5 = WebClient.create(baseUrl);
		HttpHeaders httpHeaders5 = new HttpHeaders();
		ResultDto request5 = new ResultDto();
		request5.setId("ID002");
		request5.setName("田中");
		request5.setAge(58);
		UserInfoDto<?> response5 = ExternalWebClientUtils.post(webClient5, "/test/api2", UserInfoDto.class, request5, httpHeaders5);

		// put方法 → httpステータス:200
		WebClient webClient6 = WebClient.create(baseUrl);
		HttpHeaders httpHeaders6 = new HttpHeaders();
		ResultDto request6 = new ResultDto();
		request6.setId("ID003");
		request6.setName("山田");
		request6.setAge(64);
		UserInfoDto<?> response6 = ExternalWebClientUtils.put(webClient6, "/test/api2", UserInfoDto.class, request6, httpHeaders6);

		// put方法 → httpステータス:204
		ExternalWebClientUtils.put(webClient6, "/test/api/204", request6, httpHeaders6);

		// delete方法（パラメータあり） → httpステータス:200
		WebClient webClient7 = WebClient.create(baseUrl);
		HttpHeaders httpHeaders7 = new HttpHeaders();
		UserInfoDto<?> response7 = ExternalWebClientUtils.delete(webClient7, "/test/api2", UserInfoDto.class, param, httpHeaders7);

		// delete方法（パラメータなし） → httpステータス:200
		WebClient webClient8 = WebClient.create(baseUrl);
		HttpHeaders httpHeaders8 = new HttpHeaders();
		UserInfoDto<?> response8 = ExternalWebClientUtils.delete(webClient8, "/test/api2", UserInfoDto.class, httpHeaders8);

		// delete方法 → httpステータス:204
		InternalWebClientUtils.delete(webClient8, "/test/api2/204");

		// pacth方法
		WebClient webClient9 = InternalWebClientUtils.getWebClient(baseUrl, false);
		ResultDto request9 = new ResultDto();
		request9.setId("ID003");
		request9.setAge(64);
		HttpHeaders httpHeaders9 = new HttpHeaders();
		UserInfoDto<?> response9 = ExternalWebClientUtils.patch(webClient9, "/test/api2", UserInfoDto.class, request9, httpHeaders9);
	}

	/**
	 * テスト観点:<br>
	 * 外部API呼び出す方法(MultiPart_Paples)
	 *
	 * テスト条件:<br>
	 * なし
	 *
	 * 予想結果:<br>
	 * 予想される戻り値と一致する
	 *
	 * @throws Exception 異常
	 */
	@Test
	void testMultiPartApiInterface() throws Exception {
		// Post方法(PDFファイルを生成する_ファイル返却フラグが「1（生成したファイルを返却）」の場合)
		// マルチパートデータの作成開始 リクエストパラメーターの部分
		String lineEnd = "\r\n"; // 改行文字
		StringBuilder builder = new StringBuilder();
		builder.append("--" + "***boundary***" + lineEnd);
		builder.append("Content-Disposition: form-data; name=\"parameter\"" + lineEnd);
		builder.append("Content-Type: application/json;" +
				"charset=\"UTF-8\"" + lineEnd + lineEnd);
		// JSON データ部分
		// ファイル返却フラグ = 1 ↓
		// 0: 生成処理を実施し、取得のために必要なダウンロードキーを返却します。
		// 1: 生成したファイルを返却します。
		String json = "{ \"frameFileName\": \"sampleFrame.frm\", " +
				"\"queryFileName\": \"sampleQuery.qry\", " +
				"\"outputFileName\": \"sampleOutputFile1.pdf\"" +
				"\"returnFileFlag\": \"1\"  }";
		builder.append(json + lineEnd + lineEnd);
		// データファイルの部分
		String dataFileName = "src/test/resources/mockApi/TEST_DATA1.txt";// データファイルの指定
		builder.append("--" + "***boundary***" + lineEnd);
		builder.append("Content-Disposition: form-data;" +
				"name=\"dataFile\";" + lineEnd + lineEnd);
		// データの書き込み開始
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream outputStream = new DataOutputStream(baos);
		outputStream.write(builder.toString().getBytes("UTF-8"));

		// ファイルを一定サイズ毎に読み書きする
		InputStream fileInputStream = Files.newInputStream(Paths.get(dataFileName));
		int bytesAvailable = fileInputStream.available();
		int bufferSize = Math.min(bytesAvailable, 1024 * 1024);
		byte[] buffer = new byte[bufferSize];
		int bytesRead = fileInputStream.read(buffer, 0, bufferSize);
		while (bytesRead > 0) {
			outputStream.write(buffer, 0, bufferSize);
			bytesAvailable = fileInputStream.available();
			bufferSize = Math.min(bytesAvailable, 1024 * 1024);
			bytesRead = fileInputStream.read(buffer, 0, bufferSize);
		}
		String lastStr = lineEnd + "--" + "***boundary***" + "--" + lineEnd;
		outputStream.write(lastStr.getBytes("UTF-8"));

		byte[] body = baos.toByteArray();
		// サーバーへの接続開始
		WebClient webClient = WebClient.create(baseUrl);
		HttpHeaders httpHeaders = new HttpHeaders();
		byte[] response = ExternalWebClientUtils.postMultiPart(webClient, "/webapi/v1/pdf", byte[].class, body, httpHeaders);

		try {
			DataOutputStream dataOutStream =
					new DataOutputStream(new BufferedOutputStream(
							//ファイル配置場所、ファイル名を指定
							Files.newOutputStream(Paths.get("src/test/resources/result/sampleOutput1.pdf"))));
			//データの読み書き
			dataOutStream.write(response);
			dataOutStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Get方法(PDFファイルを取得する)
		WebClient webClient2 = WebClient.create(baseUrl);
		String key = "12345678912345678_12345678"; //ダウンロードキーを指定
		Map<String, Object> param2 = new HashMap<>();
		param2.put("key", key);
		HttpHeaders httpHeaders2 = new HttpHeaders();
		byte[] dataInStream2 = ExternalWebClientUtils.getMultiPart(webClient2, "/webapi/v1/pdf/" + key, byte[].class, httpHeaders2);

		try {
			DataOutputStream dataOutStream2 =
					new DataOutputStream(new BufferedOutputStream(
							//ファイル配置場所、ファイル名を指定
							Files.newOutputStream(Paths.get("src/test/resources/result/sampleOutput2.pdf"))));
			//データの読み書き
			dataOutStream2.write(dataInStream2);
			dataOutStream2.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Post方法(Excelファイルを生成する)
		// マルチパートデータの作成開始 リクエストパラメーターの部分
		String lineEnd3 = "\r\n"; // 改行文字
		StringBuilder builder3 = new StringBuilder();
		builder3.append("--" + "***boundary***" + lineEnd3);
		builder3.append("Content-Disposition: form-data; name=\"parameter\"" + lineEnd3);
		builder3.append("Content-Type: application/json;" +
				"charset=\"UTF-8\"" + lineEnd3 + lineEnd3);
		// JSON データ部分
		// ファイル返却フラグ = 1 ↓
		// 0: 生成処理を実施し、取得のために必要なダウンロードキーを返却します。
		// 1: 生成したファイルを返却します。
		String json3 = "{ \"frameFileName\": \"sampleFrame.frm\", " +
				"\"queryFileName\": \"sampleQuery.qry\", " +
				"\"outputFileName\": \"sampleOutputFile1.xlsx\"" +
				"\"returnFileFlag\": \"1\"  }";
		builder3.append(json3 + lineEnd3 + lineEnd3);
		// データファイルの部分
		String dataFileName3 = "src/test/resources/mockApi/TEST_DATA2.txt";// データファイルの指定
		builder3.append("--" + "***boundary***" + lineEnd3);
		builder3.append("Content-Disposition: form-data;" +
				"name=\"dataFile\";" + lineEnd3 + lineEnd3);
		// データの書き込み開始
		ByteArrayOutputStream baos3 = new ByteArrayOutputStream();
		DataOutputStream outputStream3 = new DataOutputStream(baos);
		outputStream3.write(builder3.toString().getBytes("UTF-8"));

		// ファイルを一定サイズ毎に読み書きする
		InputStream fileInputStream3 = Files.newInputStream(Paths.get(dataFileName3));
		int bytesAvailable3 = fileInputStream3.available();
		int bufferSize3 = Math.min(bytesAvailable, 1024 * 1024);
		byte[] buffer3 = new byte[bufferSize3];
		int bytesRead3 = fileInputStream3.read(buffer3, 0, bufferSize3);
		while (bytesRead3 > 0) {
			outputStream3.write(buffer3, 0, bufferSize3);
			bytesAvailable3 = fileInputStream3.available();
			bufferSize3 = Math.min(bytesAvailable3, 1024 * 1024);
			bytesRead3 = fileInputStream3.read(buffer3, 0, bufferSize3);
		}
		String lastStr3 = lineEnd3 + "--" + "***boundary***" + "--" + lineEnd3;
		outputStream3.write(lastStr3.getBytes("UTF-8"));

		byte[] body3 = baos3.toByteArray();
		// サーバーへの接続開始
		WebClient webClient3 = WebClient.create(baseUrl);
		HttpHeaders httpHeaders3 = new HttpHeaders();
		byte[] response3 = ExternalWebClientUtils.postMultiPart(webClient3, "/webapi/v1/excel", byte[].class, body3, httpHeaders3);

		try {
			DataOutputStream dataOutStream3 =
					new DataOutputStream(new BufferedOutputStream(
							//ファイル配置場所、ファイル名を指定
							Files.newOutputStream(Paths.get("src/test/resources/result/sampleOutput1.xlsx"))));
//			new FileOutputStream("src/test/resources/result/sampleOutput1.xlsx")));
			//データの読み書き
			dataOutStream3.write(response3);
			dataOutStream3.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Get方法(PDFファイルを取得する)
		WebClient webClient4 = WebClient.create(baseUrl);
		String key4 = "12345678912345678_12345678"; //ダウンロードキーを指定
		Map<String, Object> param4 = new HashMap<>();
		param4.put("key", key4);
		HttpHeaders httpHeaders4 = new HttpHeaders();
		byte[] dataInStream4 = ExternalWebClientUtils.getMultiPart(webClient4, "/webapi/v1/excel/" + key4, byte[].class, httpHeaders4);

		try {
			DataOutputStream dataOutStream4 =
					new DataOutputStream(new BufferedOutputStream(

							//ファイル配置場所、ファイル名を指定
							Files.newOutputStream(Paths.get("src/test/resources/result/sampleOutput2.xlsx"))));
			//データの読み書き
			dataOutStream4.write(dataInStream4);
			dataOutStream4.close();
		} catch (Exception e) {
			e.printStackTrace();
		}


		// Post方法(帳票を印刷する方法)
		// マルチパートデータの作成開始 リクエストパラメーターの部分
		String lineEnd5 = "\r\n"; // 改行文字
		StringBuilder builder5 = new StringBuilder();
		builder5.append("--" + "***boundary***" + lineEnd5);
		builder5.append("Content-Disposition: form-data; name=\"parameter\"" + lineEnd5);
		builder5.append("Content-Type: application/json;" +
				"charset=\"UTF-8\"" + lineEnd5 + lineEnd5);
		// JSON データ部分
		String json5 = "{ \"frameFileName\": \"sampleFrame.frm\", " +
				"\"queryFileName\": \"sampleQuery.qry\" }";
		builder5.append(json5 + lineEnd5 + lineEnd5);
		// データファイルの部分
		String dataFileName5 = "src/test/resources/mockApi/TEST_DATA1.txt";// データファイルの指定
		builder5.append("--" + "***boundary***" + lineEnd5);
		builder5.append("Content-Disposition: form-data;" +
				"name=\"dataFile\";" + lineEnd5 + lineEnd5);
		// データの書き込み開始
		ByteArrayOutputStream baos5 = new ByteArrayOutputStream();
		DataOutputStream outputStream5 = new DataOutputStream(baos5);
		outputStream5.write(builder5.toString().getBytes("UTF-8"));

		// ファイルを一定サイズ毎に読み書きする
		InputStream fileInputStream5 = Files.newInputStream(Paths.get(dataFileName5));
		int bytesAvailable5 = fileInputStream5.available();
		int bufferSize5 = Math.min(bytesAvailable5, 1024 * 1024);
		byte[] buffer5 = new byte[bufferSize5];
		int bytesRead5 = fileInputStream5.read(buffer5, 0, bufferSize5);
		while (bytesRead5 > 0) {
			outputStream5.write(buffer5, 0, bufferSize5);
			bytesAvailable5 = fileInputStream5.available();
			bufferSize5 = Math.min(bytesAvailable5, 1024 * 1024);
			bytesRead5 = fileInputStream5.read(buffer5, 0, bufferSize5);
		}
		String lastStr5 = lineEnd5 + "--" + "***boundary***" + "--" + lineEnd5;
		outputStream5.write(lastStr5.getBytes("UTF-8"));

		byte[] body5 = baos.toByteArray();
		// サーバーへの接続開始
		WebClient webClient5 = WebClient.create(baseUrl);
		HttpHeaders httpHeaders5 = new HttpHeaders();
		try {
			ExternalWebClientUtils.postMultiPart(webClient5, "/webapi/v1/print", byte[].class, body5, httpHeaders5);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Post方法(PDFファイルを生成する_ファイル返却フラグが「0（ダウンロードキーを返却）」の場合)
		// マルチパートデータの作成開始 リクエストパラメーターの部分
		String lineEnd6 = "\r\n"; // 改行文字
		StringBuilder builder6 = new StringBuilder();
		builder6.append("--" + "***boundary***" + lineEnd6);
		builder6.append("Content-Disposition: form-data; name=\"parameter\"" + lineEnd6);
		builder6.append("Content-Type: application/json;" +
				"charset=\"UTF-8\"" + lineEnd6 + lineEnd6);
		// JSON データ部分
		// ファイル返却フラグ = 0 ↓
		// 0: 生成処理を実施し、取得のために必要なダウンロードキーを返却します。
		// 1: 生成したファイルを返却します。
		String json6 = "{ \"frameFileName\": \"sampleFrame.frm\", " +
				"\"queryFileName\": \"sampleQuery.qry\", " +
				"\"outputFileName\": \"sampleOutputFile1.pdf\"" +
				"\"returnFileFlag\": \"1\"  }";
		builder6.append(json6 + lineEnd6 + lineEnd6);
		// データファイルの部分
		String dataFileName6 = "src/test/resources/mockApi/TEST_DATA1.txt";// データファイルの指定
		builder6.append("--" + "***boundary***" + lineEnd6);
		builder6.append("Content-Disposition: form-data;" +
				"name=\"dataFile\";" + lineEnd6 + lineEnd);
		// データの書き込み開始
		ByteArrayOutputStream baos6 = new ByteArrayOutputStream();
		DataOutputStream outputStream6 = new DataOutputStream(baos6);
		outputStream6.write(builder6.toString().getBytes("UTF-8"));

		// ファイルを一定サイズ毎に読み書きする

		InputStream fileInputStream6 = Files.newInputStream(Paths.get(dataFileName6));
		int bytesAvailable6 = fileInputStream6.available();
		int bufferSize6 = Math.min(bytesAvailable6, 1024 * 1024);
		byte[] buffer6 = new byte[bufferSize6];
		int bytesRead6 = fileInputStream6.read(buffer6, 0, bufferSize6);
		while (bytesRead6 > 0) {
			outputStream6.write(buffer6, 0, bufferSize6);
			bytesAvailable6 = fileInputStream6.available();
			bufferSize6 = Math.min(bytesAvailable6, 1024 * 1024);
			bytesRead6 = fileInputStream6.read(buffer6, 0, bufferSize6);
		}
		String lastStr6 = lineEnd6 + "--" + "***boundary***" + "--" + lineEnd6;
		outputStream6.write(lastStr6.getBytes("UTF-8"));

		byte[] body6 = baos6.toByteArray();
		// サーバーへの接続開始
		WebClient webClient6 = WebClient.create(baseUrl);
		HttpHeaders httpHeaders6 = new HttpHeaders();
		PaplesResultDto response6 = ExternalWebClientUtils.postMultiPart(webClient6, "/webapi/v1/pdf2", PaplesResultDto.class, body6, httpHeaders6);
	}
}

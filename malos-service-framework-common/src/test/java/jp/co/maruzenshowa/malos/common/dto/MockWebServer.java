package jp.co.maruzenshowa.malos.common.dto;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.mockserver.client.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.Header;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.JsonBody;
import org.mockserver.model.Parameter;
import org.mockserver.model.RegexBody;
import org.springframework.http.HttpStatus;

/**
 * API Web Client モックサーバーのJunitテスト用のモックサーバー機能を提供します.
 * 
 * @author IBM 
 * @version 1.0
 */
public class MockWebServer {

	/** API Web Client mock server. */
	private ClientAndServer mockServer;

	private static final String MOCK_SERVER_HOST = "localhost";
	private static final int MOCK_SERVER_PORT = 8001;
	private static final List<Header> CUSTOM_HEADERS = Arrays
			.asList(new Header("Accept", "application/json, application/*+json"));
	private static final List<Header> CONTENT_TYPE = Arrays.asList(new Header("Content-Type", "application/json; charset=utf-8"));
	public static final String ZERO = "0"; 
	private final Function<String, String> funcForResourceFileToString = filename -> {
	   try (BufferedReader breader = Files.newBufferedReader(Paths.get(ClassLoader.getSystemResource(filename).toURI()))) {
	        return breader.lines().collect(Collectors.joining(System.lineSeparator()));
	   } catch (IOException | URISyntaxException exc) {
	        throw new RuntimeException(exc);
	   }
	};
	    
	private List<MockServerClient> mocksList = new ArrayList<>();

	public List<MockServerClient> getMocksList() {
		return this.mocksList;
	}

	public void setMocksList(List<MockServerClient> mocksList) {
		this.mocksList = mocksList;
	}

	/**
	 * モックサーバーを取得する.
	 * 
	 * @return mockServer モックサーバー
	 */
	public ClientAndServer getMockServer() {
		return mockServer;
	}

	/**
	 * モックサーバーを設定する.
	 * 
	 * @param mockServer モックサーバー
	 */
	public void setMockServer(ClientAndServer mockServer) {
		this.mockServer = mockServer;
	}

	public enum Method {
		GET, PUT, POST, DELETE, PATCH
	}

	public MockWebServer() {
		this.mockServer = ClientAndServer.startClientAndServer(MOCK_SERVER_PORT);
	}

	public MockWebServer(Integer port) {
		this.mockServer = ClientAndServer.startClientAndServer(port);
	}

	public MockServerClient registerClientWithResponseJson(final Method method, final String path,
			final String bodyFile) {
		return registerClientWithResponseJson(method, path, CUSTOM_HEADERS, null, HttpStatus.OK, bodyFile);
	}

	public MockServerClient registerClientWithResponseJson(final Method method, final String path,
			final List<Header> headers, final List<Parameter> params, final HttpStatus responseStatus, final String bodyFile) {
		final MockServerClient retObj = new MockServerClient(MOCK_SERVER_HOST, mockServer.getLocalPort());

		HttpRequest request = HttpRequest.request().withMethod(method.name()).withPath(path);
		if (headers != null) {
			request = request.withHeaders(headers);
		}
		if (params != null) {
			request.withQueryStringParameters(params);
		}
		HttpResponse response = HttpResponse.response().withStatusCode(responseStatus.value()).withHeaders(CONTENT_TYPE);
		if (bodyFile != null) {
			response.withBody(JsonBody.json(funcForResourceFileToString.apply(bodyFile)));
		}
		retObj.when(request).respond(response);
		return retObj;
	}

	public MockServerClient registerClientWithResponsePDF(final Method method, final String path,
			final List<Header> headers, final List<Parameter> params, final HttpStatus responseStatus, final String bodyFile,String returnFileFlag) throws IOException {
		final MockServerClient retObj = new MockServerClient(MOCK_SERVER_HOST, mockServer.getLocalPort());

		HttpRequest request = HttpRequest.request().withMethod(method.name()).withPath(path);
		if (headers != null) {
			request = request.withHeaders(headers);
		}
		if (params != null) {
			request.withQueryStringParameters(params);
		}
        HttpResponse response = new HttpResponse();
        if(bodyFile != null) {
            if(ZERO.equals(returnFileFlag)){
            	response.withStatusCode(responseStatus.value()).withHeaders(CONTENT_TYPE).withBody(JsonBody.json(funcForResourceFileToString.apply(bodyFile)));
            } else {
                File pdfFile = new File(bodyFile); 
                byte[] pdfBytes = Files.readAllBytes(pdfFile.toPath());  
            	response.withStatusCode(responseStatus.value())  
                .withHeader("Content-Type", "application/pdf")  
                .withBody(pdfBytes);  
            }
        }
		retObj.when(request).respond(response);
		return retObj;
	}
	
	public MockServerClient registerClientWithRequestRegex(final Method method, final String path,
			final String regexFile) {
		final MockServerClient retObj = new MockServerClient(MOCK_SERVER_HOST, mockServer.getLocalPort());
		retObj.when(HttpRequest.request().withHeaders(CUSTOM_HEADERS).withMethod(method.toString())
				.withBody(RegexBody.regex(funcForResourceFileToString.apply(regexFile))))
				.respond(HttpResponse.response().withStatusCode(200));
		return retObj;
	}
}

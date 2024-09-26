package jp.co.maruzenshowa.malos.backend.config;


import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;

@Configuration
class ApiDocsOperationCustomizer implements OperationCustomizer {

	@Override
	public Operation customize(Operation operation, HandlerMethod handlerMethod) {
		if (operation.getResponses().get("200") != null) {
		    final ApiResponse apiResponse = operation.getResponses().get("200"); 
			if (apiResponse.getContent() != null) {
			        apiResponse.getContent().keySet().forEach(mediaTypeKey -> {
					final MediaType mediaType = apiResponse.getContent().get(mediaTypeKey);
					mediaType.schema(this.customizeSchema(mediaType.getSchema()));
				});
			}
		}
		return operation;
	}

    /**
     * 標準なリスポンスデータ構造を返却する。
     *
     * @param objSchema アプリからのデータ構造
     * @return リスポンスデータ構造
     */
	private Schema<?> customizeSchema(final Schema<?> objSchema) {
		if (objSchema.get$ref() == null || !objSchema.get$ref().contains("ResponseDto")) {
			final Schema<?> wrapperSchema = new Schema<>();
			wrapperSchema.addProperty("data", objSchema);
			return wrapperSchema;
		} else {
			return new Schema<>();
		}
	}
}
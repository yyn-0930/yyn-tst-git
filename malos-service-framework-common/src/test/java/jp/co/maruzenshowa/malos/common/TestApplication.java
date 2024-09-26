package jp.co.maruzenshowa.malos.common;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.maruzen.mldx"})
@SuppressWarnings({"checkstyle:HideUtilityClassConstructor", "PMD.UseUtilityClass","PMD.TestClassWithoutTestCases"})
public class TestApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestApplication.class, args);
	}

}

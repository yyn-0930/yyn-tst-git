package jp.co.maruzenshowa.malos.batch.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.Getter;
import lombok.Setter;

/**
 * アプリケーションYamlの設定プロパティ.
 * 
 * @author  IBM Wei kai
 * @version 1.0
 */
@Configuration
@PropertySource(value = "classpath:application.yaml", factory = YamlPropertySourceFactory.class)
public class ApplicationYamlProperties {
	
	@Getter @Setter
	@Value("${spring.datasource.driver-class-name}")
	private String driverClassName;

	@Getter @Setter
	@Value("${spring.datasource.url}")
	private String url;

	@Getter @Setter
	@Value("${spring.datasource.username}")
	private String username;
	
	@Getter @Setter
	@Value("${spring.datasource.password}")
	private String password;
	
	@Getter @Setter
	@Value("${spring.jpa.scan-package}")
	private String scanPackage;
	
	@Getter @Setter
	@Value("${spring.jpa.show-sql:false}")
	private String showSql;

	@Getter @Setter
	@Value("${spring.jpa.properties.hibernate.format_sql:false}")
	private String formatSql;
}

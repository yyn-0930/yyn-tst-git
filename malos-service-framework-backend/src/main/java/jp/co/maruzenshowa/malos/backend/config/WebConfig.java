package jp.co.maruzenshowa.malos.backend.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jp.co.maruzenshowa.malos.backend.aop.ContextInterceptor;

/**
 * ウェブ設定クラス.<br>
 * 
 * @author IBM Wei kai
 * @version 1.0
 */
@Configuration
@EnableRetry
public class WebConfig implements WebMvcConfigurer {

	@Autowired
	private ContextInterceptor dynamicInterceptor;
	
	/**
	 * レジストリインターセプター
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(dynamicInterceptor);
	}
	
	/**
	 * タイムアウトトランザクションマネージャーBean
	 * 
	 * @param dataSource データソース
	 * @return タイムアウトトランザクションマネージャー
	 */
	@Bean
	@Primary
	JpaTransactionManager transactionManager(DataSource dataSource) {
		JpaTransactionManager manager = new TimeoutAwareTransactionManager(); 
		manager.setDataSource(dataSource);
		manager.setRollbackOnCommitFailure(true);
		return manager;
	}
}

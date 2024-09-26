package jp.co.maruzenshowa.malos.batch.config;

import jakarta.persistence.EntityManagerFactory;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.sql.DataSource;
import jp.co.maruzenshowa.malos.batch.common.ApplicationYamlProperties;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

/**
 * JPAのデータソースを定義するクラス.
 * 
 * @author IBM Wei kai
 * @version 1.0
 */
@Configuration
public class JpaDataSourceConfig {
	
	@Autowired
    private ApplicationYamlProperties applicationYamlProperties;
	
    /**
     * バッチデータソースBean.
     * 
     * @return バッチデータソース
     */
	@Bean
	DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(applicationYamlProperties.getDriverClassName());
		dataSource.setUrl(applicationYamlProperties.getUrl());
		dataSource.setUsername(applicationYamlProperties.getUsername());
		dataSource.setPassword(applicationYamlProperties.getPassword());

		return dataSource;
	}
	
    /**
     * トランザクションマネージャーBean.
     * 
     * @param entityManagerFactory エンティティマネージャ
     * @return トランザクションマネージャー
     */
	@Bean
	JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
		return new JpaTransactionManager(entityManagerFactory);
	}

    /**
     * エンティティマネージャBean.
     * 
     * @param dataSource データソース
     * @return エンティティマネージャ
     */
	@Bean
	EntityManagerFactory entityManagerFactory(DataSource dataSource) {
		LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
		factoryBean.setDataSource(dataSource);
		factoryBean.setPersistenceUnitName("postgres");
		factoryBean.setJpaVendorAdapter(getAdapter());
		factoryBean.setJpaPropertyMap(getPropertiesMap());
		factoryBean.setPackagesToScan(applicationYamlProperties.getScanPackage());
		factoryBean.afterPropertiesSet();
		return factoryBean.getObject();
	}
	
    /**
     * JPAアダプタBean.
     * 
     * @return JPAアダプタ
     */
	private HibernateJpaVendorAdapter getAdapter() {
		HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
		adapter.setShowSql(Boolean.parseBoolean(applicationYamlProperties.getShowSql())); 
		return adapter;
	}
	
    /**
     * hibernate設定初期化処理.
     * 
     * @return hibernate設定
     */
	private Map<String, Object> getPropertiesMap() {
	  Map<String, Object> properties = new ConcurrentHashMap<>();
	    properties.put("hibernate.format_sql", applicationYamlProperties.getFormatSql());
	    properties.put("hibernate.physical_naming_strategy", CamelCaseToUnderscoresNamingStrategy.class);
	    properties.put("hibernate.implicit_naming_strategy", SpringImplicitNamingStrategy.class);
	    return properties;
	}
}

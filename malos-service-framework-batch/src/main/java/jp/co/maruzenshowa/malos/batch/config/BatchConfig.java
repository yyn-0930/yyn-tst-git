package jp.co.maruzenshowa.malos.batch.config;

import org.springframework.batch.core.launch.support.ExitCodeMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import jp.co.maruzenshowa.malos.batch.common.ApplicationYamlProperties;
import jp.co.maruzenshowa.malos.batch.common.CustomExitCodeMapper;

/**
 * バッチ設定を定義するクラス.
 * 
 * @author IBM Wei kai
 * @version 1.0
 */
@Configuration
@Import(ApplicationYamlProperties.class)
public class BatchConfig {

    /**
     * バッチ終了コードのマッピングBean.
     * 
     * @return バッチ終了コードのマッピング
     */
	@Bean
	ExitCodeMapper exitCodeMapper() {
	    return new CustomExitCodeMapper();
	}

}

package jp.co.maruzenshowa.malos.backend.config;

import org.springframework.jdbc.datasource.ConnectionHolder;
import org.springframework.jdbc.datasource.JdbcTransactionObjectSupport;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.support.DefaultTransactionStatus;

/**
 * タイムアウトトランザクションマネージャーのクラス.<br>
 * コミット時点でタイムアウトチェックを行う<br>
 * 
 * @author IBM Wei kai
 * @version 1.0
 */
public class TimeoutAwareTransactionManager extends JpaTransactionManager {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doCommit(DefaultTransactionStatus status) {
		JdbcTransactionObjectSupport txObject = (JdbcTransactionObjectSupport) status.getTransaction();

		ConnectionHolder holder = txObject.getConnectionHolder();

		if (holder.hasTimeout()) {
			// トランザクションタイムアウトしていたらTransactionTimedOutExceptionがスローされる。
			holder.getTimeToLiveInMillis();
		}
		super.doCommit(status);
	}
}

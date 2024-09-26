package jp.co.maruzenshowa.malos.common.base;

import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jp.co.maruzenshowa.malos.common.context.DynamicContext;

/**
 *　部所コードベースサービス.<br>
 *　Filterを有効化機能を提供する
 * 
 * @author IBM Wei kai
 * @version 1.0
 */
@Service
public class AbstractBaseService {
	
	@Autowired
	protected EntityManager entityManager;
	
    /**
     * 部所コードFilter有効化処理.
     * 
     */
	public void openFilter() {
		// 部所コードがある場合、Filterを有効化するように設定する。
		if (DynamicContext.getContext().getDeptCode() != null) {
			Filter filter = entityManager.unwrap(Session.class).enableFilter("deptFilter");
			filter.setParameter("deptCode", DynamicContext.getContext().getDeptCode());
		}
	}
}

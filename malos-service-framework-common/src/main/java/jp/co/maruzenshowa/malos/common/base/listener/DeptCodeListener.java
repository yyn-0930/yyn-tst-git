package jp.co.maruzenshowa.malos.common.base.listener;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;
import jp.co.maruzenshowa.malos.common.base.AbstractBaseEntity;
import jp.co.maruzenshowa.malos.common.context.DynamicContext;

/**
 *　部所コード設定のリスナー.<br>
 * 更新・削除・作成する場合、エンティティに部所コードを自動で設定する.
 * 
 * @author IBM Wei kai
 * @version 1.0
 */
public class DeptCodeListener {
	
    /**
     * 部所コード設定処理.
     * 
     * @param entity 結果データ
     */
	@PreUpdate
    @PreRemove
    @PrePersist
    public void setDeptCode(AbstractBaseEntity entity) {
        final String deptCode = DynamicContext.getContext().getDeptCode();
        entity.setDeptCode(deptCode);
    }
}

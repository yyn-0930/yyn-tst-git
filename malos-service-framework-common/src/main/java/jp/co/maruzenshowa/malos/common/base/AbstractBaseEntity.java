package jp.co.maruzenshowa.malos.common.base;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jp.co.maruzenshowa.malos.common.base.listener.DeptCodeListener;
import lombok.NoArgsConstructor;

/**
 *　部所コードベースエンティティ.<br>
 *　検索する時、SQL条件に自動で部所コードを設定する
 * 更新・削除・作成する場合、エンティティに部所コードを自動で設定する.
 * 
 * @author IBM Wei kai
 * @version 1.0
 */
@MappedSuperclass
@NoArgsConstructor
@FilterDef(name = "deptFilter", parameters = {@ParamDef(name = "deptCode", type = String.class)})
@Filter(name = "deptFilter", condition = "dept_code = :deptCode")
@EntityListeners(DeptCodeListener.class)
public abstract class AbstractBaseEntity implements DeptCodeFilterable {

}

package jp.co.maruzenshowa.malos.common.base;

import java.io.Serializable;

import org.springframework.data.domain.Persistable;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PostPersist;
import jakarta.persistence.Transient;

/**
 *　JPAのsaveを直接登録するベースエンティティ.<br>
 *　
 * @param <T> Entityタイプ
 * @author IBM Wei kai
 * @version 1.0
 */
@MappedSuperclass
public abstract class AbstractPersistableEntity<T extends Serializable> implements Persistable<T> {

	@Transient
	private boolean isNewFlag = true;

	@Override
	public boolean isNew() {
		return isNewFlag;
	}

	public void setNew(boolean isNew) {
		this.isNewFlag = isNew;
	}
	
	/**
     * 新規レコード以外の場合、新規区分はfalseに設定する処理.
     * 
     */
	@PostLoad
    @PostPersist
    public void markNotNew() {
        this.isNewFlag = false;
    }
	
}

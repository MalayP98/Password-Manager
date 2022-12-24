package com.key.password_manager.key;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import com.key.password_manager.key.types.KeyType;
import com.key.password_manager.utils.BaseModel;

@MappedSuperclass
public abstract class AbstractKey extends BaseModel implements Key {

	private String key;

	@Column(updatable = false)
	private KeyType keyType;

	public AbstractKey() {
	}

	public AbstractKey(String key, KeyType keyType) {
		setKey(key);
		setKeyType(keyType);
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public void setKey(String key) {
		if (Objects.isNull(key) || key.equals("")) {
			throw new NullPointerException("Key is either null or empty.");
		}
		this.key = key;
	}

	@Override
	public KeyType type() {
		return keyType;
	}

	public void setKeyType(KeyType keyType) {
		if (Objects.isNull(keyType)) {
			throw new NullPointerException(
					"No type provided while key creation. Cannot create key!");
		}
		this.keyType = keyType;
	}

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public Key updateKey(String key) {
		setKey(key);
		return this;
	}
}

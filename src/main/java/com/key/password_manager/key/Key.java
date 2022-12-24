package com.key.password_manager.key;

import com.key.password_manager.key.types.KeyType;

public interface Key extends Cloneable {

	public String getKey();

	public void setKey(String key);

	public KeyType type();
}

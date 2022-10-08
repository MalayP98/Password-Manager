package com.key.password_manager.key;

import java.util.Objects;
import javax.persistence.MappedSuperclass;
import com.key.password_manager.utils.BaseModel;

@MappedSuperclass
public abstract class AbstarctKey extends BaseModel implements Key {

    private String key;

    public AbstarctKey() {

    }

    public AbstarctKey(String key) {
        setKey(key);
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
}

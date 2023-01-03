package com.key.password_manager.cache.cacheimpls;

public interface Cache<E> {

	public E save(E entity);

	public E find(Long id, Class<E> clazz);

	public boolean delete(E entity);

}

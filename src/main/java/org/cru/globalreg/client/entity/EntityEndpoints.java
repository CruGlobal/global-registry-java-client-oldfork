package org.cru.globalreg.client.entity;

import org.cru.globalreg.client.Filter;

import java.net.URL;
import java.util.UUID;

/**
 * Created by ryancarlson on 2/23/14.
 */
public interface EntityEndpoints
{
    <T> EntitySearchResponse<T> search(EntityClass<T> entityClass, String type, Filter... filters);
    <T> EntitySearchResponse<T> search(EntityClass<T> entityClass, String type, int page, Filter... filters);
    <T> T get(EntityClass<T> entityClass, UUID id, String type);
    <T> T create(EntityData<T> entityData, String type);
    <T> T update(EntityData<T> entityData, UUID id, String type);
    void delete(UUID id, String type);

	void initialize(URL url, String accessToken);
}

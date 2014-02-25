package org.cru.globalreg.client;

import org.cru.globalreg.client.impl.EntityClass;
import org.cru.globalreg.client.impl.EntityData;
import org.cru.globalreg.client.impl.EntitySearchResponse;

/**
 * Created by ryancarlson on 2/23/14.
 */
public interface EntityEndpoints
{
    <T> EntitySearchResponse<T> search(EntityClass<T> entityClass, String type, Filter... filters);
    <T> EntitySearchResponse<T> search(EntityClass<T> entityClass, String type, int page, Filter... filters);
    <T> T get(EntityClass<T> entityClass, Integer id, String type);
    <T> T create(EntityData<T> entityData, String type);
    <T> T update(EntityData<T> entityData, Integer id, String type);
    void delete(Integer id, String type);
}

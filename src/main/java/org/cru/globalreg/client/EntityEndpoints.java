package org.cru.globalreg.client;

import org.cru.globalreg.client.impl.EntitySearchResponse;

/**
 * Created by ryancarlson on 2/23/14.
 */
public interface EntityEndpoints
{
    <T> EntitySearchResponse<T> search(Class<T> entityType, String type, Filter... filters);
    <T> EntitySearchResponse<T> search(Class<T> entityType, String type, int page, Filter... filters);
    <T> T get(Class<T> entityType, Integer id, String type);
    <T> T create(T entity, String type);
    <T> T update(T entity, Integer id, String type);
    void delete(Integer id, String type);
}

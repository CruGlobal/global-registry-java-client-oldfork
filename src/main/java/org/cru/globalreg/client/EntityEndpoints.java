package org.cru.globalreg.client;

import org.codehaus.jackson.JsonNode;

/**
 * Created by ryancarlson on 2/23/14.
 */
public interface EntityEndpoints
{
    JsonNode get(String... criteria);
    void create(JsonNode entity);
    void update(JsonNode entity);
    void delete(JsonNode entity);
}

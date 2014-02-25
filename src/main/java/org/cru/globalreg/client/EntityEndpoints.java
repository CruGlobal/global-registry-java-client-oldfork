package org.cru.globalreg.client;

import org.codehaus.jackson.JsonNode;

/**
 * Created by ryancarlson on 2/23/14.
 */
public interface EntityEndpoints
{
    JsonNode get(Integer id, String type);
    JsonNode create(JsonNode entity, String type);
    JsonNode update(Integer id, JsonNode entity, String type);
    JsonNode delete(Integer id, String type);
}

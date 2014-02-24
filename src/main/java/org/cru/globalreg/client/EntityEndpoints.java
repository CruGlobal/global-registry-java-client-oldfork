package org.cru.globalreg.client;

import org.codehaus.jackson.JsonNode;

/**
 * Created by ryancarlson on 2/23/14.
 */
public interface EntityEndpoints
{
    JsonNode get(Integer id, String type, String accessToken);
    JsonNode create(JsonNode entity, String type, String accessToken);
    JsonNode update(Integer id, JsonNode entity, String type, String accessToken);
    JsonNode delete(Integer id, String type, String accessToken);
}

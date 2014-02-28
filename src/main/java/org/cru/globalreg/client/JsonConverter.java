package org.cru.globalreg.client;

import com.google.common.base.Throwables;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.cru.globalreg.jackson.GlobalRegistryApiNamingStrategy;

/**
 * Created by ryancarlson on 2/28/14.
 */
public class JsonConverter
{
    public static <T> T treeToValue(final JsonNode json, Class<T> type)
    {
        return treeToValue(json, type, null);
    }

    public static <T> T treeToValue(JsonNode json, Class<T> type, String... path)
    {
        try
        {
            if(path != null)
            {
                for(String pathElement : path)
                {
                    json = json.path(pathElement);
                }
            }
            return createObjectMapper().treeToValue(json, type);
        }
        catch(Exception checkedException)
        {
            Throwables.propagate(checkedException);
            return null; /*unreachable*/
        }
    }

    /**
     * This method prepares the data for a POST or PUT with two important steps
     *  1. It converts the POJO fields into underscore field names in the objectMapper.valueToTree call
     *  2. It wraps the data in an object called "entity" so meet the API requirements
     *    - this requirement may soon be going away :)
     * @param data
     * @param <T>
     * @return
     */
    private <T> JsonNode prepareData(T data, String[] path)
    {
        ObjectMapper objectMapper = createObjectMapper();

        JsonNode jsonData = objectMapper.valueToTree(data);

        final ObjectNode entity = objectMapper.createObjectNode();
        entity.put(entityType, jsonData);

        final ObjectNode root = objectMapper.createObjectNode();
        root.put("entity", entity);

        return root;
    }

    private static ObjectMapper createObjectMapper()
    {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(new GlobalRegistryApiNamingStrategy());
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        return objectMapper;
    }

}

package org.cru.globalreg.client.entitytypes;

import java.util.List;
import java.util.UUID;

/**
 * Created by ryancarlson on 2/25/14.
 */
public class EntityType
{
    private UUID id;
    private String name;
    private String fieldType;
    private UUID parentId;

    private List<EntityType> fields;

    public UUID getId()
    {
        return id;
    }

    public void setId(UUID id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getFieldType()
    {
        return fieldType;
    }

    public void setFieldType(String fieldType)
    {
        this.fieldType = fieldType;
    }

    public UUID getParentId()
    {
        return parentId;
    }

    public void setParentId(UUID parentId)
    {
        this.parentId = parentId;
    }

    public List<EntityType> getFields()
    {
        return fields;
    }

    public void setFields(List<EntityType> fields)
    {
        this.fields = fields;
    }
}

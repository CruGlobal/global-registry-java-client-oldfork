package org.cru.globalreg.client.entitytypes;

import java.util.List;

/**
 * Created by ryancarlson on 2/25/14.
 */
public class EntityType
{
    private Integer id;
    private String name;
    private String fieldType;
    private Integer parentId;

    private List<EntityType> fields;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
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

    public Integer getParentId()
    {
        return parentId;
    }

    public void setParentId(Integer parentId)
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

package org.cru.globalreg.client.entitytypes;

/**
 * Created by ryancarlson on 2/25/14.
 */
public class EntityType
{
    private String name;
    private String fieldType;
    private String parentId;

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

    public String getParentId()
    {
        return parentId;
    }

    public void setParentId(String parentId)
    {
        this.parentId = parentId;
    }
}

package org.cru.globalreg.client.entitytypes;

import java.util.List;

/**
 * Created by ryancarlson on 2/25/14.
 */
public interface EntityTypeEndpoints
{
    List<EntityType> getAll();
    EntityType create(EntityType newEntityType);
}

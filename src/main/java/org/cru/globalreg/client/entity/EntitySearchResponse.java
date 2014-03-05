package org.cru.globalreg.client.entity;

import java.util.List;

import com.google.common.collect.ForwardingList;
import com.google.common.collect.Lists;

/**
 * Created by ryancarlson on 2/25/14.
 */
public class EntitySearchResponse<T> extends ForwardingList<T>
{
    private final List<T> results = Lists.newArrayList();
    private MetaResults meta = new MetaResults();

    @Override
    protected List<T> delegate()
    {
        return results;
    }

    public MetaResults getMeta()
    {
        return meta;
    }

    public void setMeta(MetaResults meta)
    {
        this.meta = meta;
    }
}

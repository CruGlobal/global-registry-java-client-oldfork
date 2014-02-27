package org.cru.globalreg.client.entity;

import com.beust.jcommander.internal.Lists;
import com.google.common.collect.ForwardingList;

import java.util.List;

/**
 * Created by ryancarlson on 2/25/14.
 */
public class EntitySearchResponse<T> extends ForwardingList<T>
{
    List<T> results = Lists.newArrayList();
    MetaResults meta = new MetaResults();

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

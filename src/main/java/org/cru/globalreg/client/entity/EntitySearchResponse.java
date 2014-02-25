package org.cru.globalreg.client.entity;

import com.beust.jcommander.internal.Lists;

import java.util.List;

/**
 * Created by ryancarlson on 2/25/14.
 */
public class EntitySearchResponse<T>
{
    List<T> results = Lists.newArrayList();
    MetaResults meta = new MetaResults();

    public List<T> getResults()
    {
        return results;
    }

    public void setResults(List<T> results)
    {
        this.results = results;
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

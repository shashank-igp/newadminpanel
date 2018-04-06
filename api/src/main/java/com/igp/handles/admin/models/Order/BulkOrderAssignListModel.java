package com.igp.handles.admin.models.Order;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shanky on 5/4/18.
 */
public class BulkOrderAssignListModel {
    @JsonProperty("orderList")
    private List<BulkOrderAssignModel> bulkOrderAssignModelList=new ArrayList<>();

    public List<BulkOrderAssignModel> getBulkOrderAssignModelList()
    {
        return bulkOrderAssignModelList;
    }

    public void setBulkOrderAssignModelList(List<BulkOrderAssignModel> bulkOrderAssignModelList)
    {
        this.bulkOrderAssignModelList = bulkOrderAssignModelList;
    }
}

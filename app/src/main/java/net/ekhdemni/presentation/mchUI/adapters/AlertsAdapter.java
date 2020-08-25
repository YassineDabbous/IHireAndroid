package net.ekhdemni.presentation.mchUI.adapters;


import net.ekhdemni.presentation.base.BaseAdapter;
import net.ekhdemni.presentation.mchUI.adapters.viewholders.VHAlert;
import net.ekhdemni.model.models.Alert;

import java.util.List;

public class AlertsAdapter extends BaseAdapter<Alert, VHAlert> {
    public AlertsAdapter(List<Alert> itemList) {
        super(itemList, VHAlert.class);
    }
}


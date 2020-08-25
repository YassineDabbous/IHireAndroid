package net.ekhdemni.presentation.mchUI.adapters;

import java.util.List;

import net.ekhdemni.presentation.base.BaseAdapter;
import net.ekhdemni.presentation.mchUI.adapters.viewholders.RelationVH;
import tn.core.presentation.listeners.OnClickItemListener;
import net.ekhdemni.model.models.Relation;

/**
 * Created by X on 5/23/2018.
 */

public class RelationsAdapter extends BaseAdapter<Relation, RelationVH> {

    public RelationsAdapter(List<Relation> items, OnClickItemListener<Relation> mListener) {
        super(items, RelationVH.class, mListener);
    }


}
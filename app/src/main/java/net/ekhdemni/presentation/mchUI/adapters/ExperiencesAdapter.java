package net.ekhdemni.presentation.mchUI.adapters;

import net.ekhdemni.presentation.base.BaseAdapter;
import net.ekhdemni.presentation.mchUI.adapters.viewholders.ExperienceVH;
import tn.core.presentation.listeners.OnClickItemListener;
import net.ekhdemni.model.models.Experience;

import java.util.List;

/**
 * Created by X on 5/23/2018.
 */

public class ExperiencesAdapter extends BaseAdapter<Experience, ExperienceVH> {

    public ExperiencesAdapter(OnClickItemListener mListener, List<Experience> itemList) {
        super(itemList, ExperienceVH.class, mListener, false);
    }

}
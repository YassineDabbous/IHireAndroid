package net.ekhdemni.presentation.mchUI.adapters;

import java.util.List;

import net.ekhdemni.presentation.base.BaseAdapter;
import net.ekhdemni.presentation.mchUI.adapters.viewholders.UsersVH;
import tn.core.presentation.listeners.OnClickItemListener;
import net.ekhdemni.model.models.user.User;

public class UsersAdapter extends BaseAdapter<User, UsersVH> {

    public UsersAdapter(List<User> items, OnClickItemListener<User> listener) {
        super(items, UsersVH.class, listener);
    }



}

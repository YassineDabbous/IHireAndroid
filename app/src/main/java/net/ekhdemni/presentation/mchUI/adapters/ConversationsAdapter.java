package net.ekhdemni.presentation.mchUI.adapters;

import net.ekhdemni.presentation.base.BasePagedListAdapter;
import net.ekhdemni.presentation.mchUI.adapters.viewholders.ConversationViewHolder;
import tn.core.presentation.listeners.OnClickItemListener;
import net.ekhdemni.model.models.Conversation;
import androidx.paging.PagedList;
/**
 * Created by X on 5/23/2018.
 */

public class ConversationsAdapter extends BasePagedListAdapter<Conversation, ConversationViewHolder> {


    public ConversationsAdapter(PagedList<Conversation> itemList, OnClickItemListener<Conversation> mListener) {
        super(itemList, ConversationViewHolder.class, Conversation.DIFF_CALLBACK, mListener);
    }

}
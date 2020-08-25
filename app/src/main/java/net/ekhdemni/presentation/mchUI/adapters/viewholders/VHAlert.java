package net.ekhdemni.presentation.mchUI.adapters.viewholders;


import android.view.View;

import net.ekhdemni.R;
import tn.core.presentation.base.adapters.BaseViewHolder;
import net.ekhdemni.model.models.Alert;
import net.ekhdemni.utils.TextUtils;
import android.widget.TextView;

public class VHAlert extends BaseViewHolder<Alert> {
    public final View mView;
    public final TextView message, type, date;

    @Override
    public int getLayoutId() {
        return R.layout.item_alert;
    }

    public VHAlert(View view) {
        super(view);
        mView = view;
        message = mView.findViewById(R.id.message);
        type = mView.findViewById(R.id.type);
        date = mView.findViewById(R.id.date);
    }

    @Override
    public void bind(Alert item) {
        TextUtils.htmlToView(message, item.getTitle());
        TextUtils.htmlToViewNonClickable(type, item.getType());
        date.setText(item.getCreatedAt()+"");
    }
}
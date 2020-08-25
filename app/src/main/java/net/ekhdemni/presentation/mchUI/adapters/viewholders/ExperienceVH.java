package net.ekhdemni.presentation.mchUI.adapters.viewholders;

import android.view.View;
import android.widget.FrameLayout;

import net.ekhdemni.R;
import tn.core.presentation.base.adapters.BaseViewHolder;
import net.ekhdemni.model.models.Experience;
import android.widget.TextView;

public class ExperienceVH extends BaseViewHolder<Experience> {

    private static final int VIEW_TYPE_TOP = 0;
    private static final int VIEW_TYPE_MIDDLE = 1;
    private static final int VIEW_TYPE_BOTTOM = 2;

    View mView;
    TextView mItemTitle, mItemSubtitle, mItemDescription, itemDate;
    FrameLayout mItemLine;

    public ExperienceVH(View itemView) {
        super(itemView);
        mView = itemView;
        mItemTitle = itemView.findViewById(R.id.item_title);
        mItemSubtitle = itemView.findViewById(R.id.item_subtitle);
        itemDate = itemView.findViewById(R.id.item_date);
        mItemDescription = itemView.findViewById(R.id.item_description);
        mItemLine = (FrameLayout) itemView.findViewById(R.id.item_line);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_user_experience;
    }

    @Override
    public void bind(final Experience item) {
        mItemTitle.setText(item.title);
        mItemSubtitle.setText(item.company);
        mItemDescription.setText(item.description);
        itemDate.setText(item.date);
        mView.setOnClickListener(v -> {
            if (null != listener) {
                listener.onClick(item);
            }
        });
    }

    void setLine(int i){
        switch(getType(i)) {
            case VIEW_TYPE_TOP:
                // The top of the line has to be rounded
                mItemLine.setBackground(mItemLine.getContext().getResources().getDrawable(R.drawable.line_bg_top));
                break;
            case VIEW_TYPE_MIDDLE:
                // Only the color could be enough
                // but a drawable can be used to make the cap rounded also here
                mItemLine.setBackground(mItemLine.getContext().getResources().getDrawable(R.drawable.line_bg_middle));
                break;
            case VIEW_TYPE_BOTTOM:
                mItemLine.setBackground(mItemLine.getContext().getResources().getDrawable(R.drawable.line_bg_bottom));
                break;
        }
    }
    public int getType(int position) {
        if(position == 0)
            return VIEW_TYPE_TOP;
        else
        if(position == getAdapterPosition() - 1) {
            return VIEW_TYPE_BOTTOM;
        }
        return VIEW_TYPE_MIDDLE;
    }
}
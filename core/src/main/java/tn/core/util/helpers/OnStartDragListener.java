package tn.core.util.helpers;

/**
 * Created by X on 2/22/2018.
 */
import androidx.recyclerview.widget.RecyclerView;

public interface OnStartDragListener {

    /**
     * Called when a view is requesting a start of a drag.
     *
     * @param viewHolder The holder of the view to drag.
     */
    void onStartDrag(RecyclerView.ViewHolder viewHolder);

}
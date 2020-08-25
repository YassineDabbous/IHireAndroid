package net.ekhdemni.presentation.mchUI.adapters.viewholders;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import net.ekhdemni.R;
import net.ekhdemni.model.models.Category;
import android.widget.TextView;

public class CategoryViewHolder  extends RecyclerView.ViewHolder {
    public final View mView;
    public final TextView titleView, descView, counter;
    public ImageView follow;
    public Category mItem;


    int current = 0;
    int[] colors = {R.color.bg_screen1, R.color.bg_screen2, R.color.bg_screen3, R.color.bg_screen4,
            R.color.dot_dark_screen1 , R.color.dot_dark_screen2, R.color.dot_dark_screen3, R.color.dot_dark_screen4,
            R.color.blood_primary, R.color.grass_primary, R.color.sea_primary, R.color.candy_primary};

    public CategoryViewHolder(View view) {
        super(view);
        mView = view;
        titleView = (TextView) view.findViewById(R.id.title);
        counter = (TextView) view.findViewById(R.id.counter);
        descView = (TextView) view.findViewById(R.id.report);
        follow =  view.findViewById(R.id.follow);
        View colored = view.findViewById(R.id.colored);
        if (current>=colors.length) current = 0;
        colored.setBackgroundColor(mView.getResources().getColor(colors[current]));
        current++;
    }

    @Override
    public String toString() {
        return super.toString() + " '" + titleView.getText() + "'";
    }
}
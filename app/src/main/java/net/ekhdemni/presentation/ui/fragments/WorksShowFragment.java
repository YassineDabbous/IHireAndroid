package net.ekhdemni.presentation.ui.fragments;


import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import net.ekhdemni.R;
import net.ekhdemni.model.models.responses.LikeResponse;
import net.ekhdemni.presentation.ui.activities.WorksActivity;
import net.ekhdemni.presentation.base.MyActivity;
import tn.core.presentation.base.MyFragment;
import net.ekhdemni.model.models.Work;
import android.widget.TextView;
import net.ekhdemni.presentation.mchUI.vms.VMWork;
import tn.core.util.Const;
import net.ekhdemni.utils.ImageHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class WorksShowFragment extends MyFragment<VMWork> {

    TextView title, description, username, date, likes, views;
    ImageView user_photo;
    ImageView like;
    Work work;
    //////// For Slider ////////:
    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private String[] images;
    //private List<String> images = new ArrayList<>();
    int currentPage = 0;
    ///////////////////////////


    public WorksShowFragment() {}

    public static WorksShowFragment newInstance(int id) {
        WorksShowFragment fragment = new WorksShowFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Const.ID, id);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(VMWork.class);
        mViewModel.callErrors.observe(this, this::onError);
        mViewModel.loadStatus.observe(this, this::onStatusChanged);
        mViewModel.getLiveData().observe(this, this::onDataReceived);
        mViewModel.getLike().observe(this, this::handleLike);
        if(work==null){
            int id = getArgs().getInt(Const.ID);
            mViewModel.init(id);
        }
    }


    //@Override
    public void onDataReceived(Work data) {
        //super.onDataReceived(data);
        work = data;
        bind();
    }


    @Override
    public void clean() {
        super.clean();
        title = description = username = date = likes = views = null;
        user_photo = null;
        if(viewPager != null) viewPager.setAdapter(null);
        viewPager = null;
        myViewPagerAdapter = null;
        if(dotsLayout!=null) dotsLayout.removeAllViews();
        dotsLayout = null;
        dots = null;
        images = null;
        if (like!=null) like.setOnClickListener(null);
        like =null;
    }



    @Override
    public void onDetach() {
        super.onDetach();
        WorksActivity.isChildFragment = false;
        if(getActivity() instanceof WorksActivity)
            ((WorksActivity) getActivity()).setupToolbar();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_works_show, container, false);
    }

    @Override
    public void init() {
        super.init();
        View v = getView();
        title = v.findViewById(R.id.title);
        description = v.findViewById(R.id.description);
        user_photo = v.findViewById(R.id.poster_photo);
        username  = v.findViewById(R.id.uid);
        date = v.findViewById(R.id.date);
        like = v.findViewById(R.id.likesBtn);
        likes = v.findViewById(R.id.likes);
        views = v.findViewById(R.id.views);
        viewPager = (ViewPager) v.findViewById(R.id.view_pager);
        dotsLayout = (LinearLayout) v.findViewById(R.id.layoutDots);
    }

    public void setlikes(){
        likes.setText(work.getLikesCount()+"");
        int color;
        if(work.getLiked()){
            color = R.color.material_red;
            like.setImageResource(R.drawable.heart);
        }else{
            color = R.color.grey;
            like.setImageResource(R.drawable.heart_outline);
        }
        //ColorStateList csl = AppCompatResources.getColorStateList(getContext(), color);
        //ImageViewCompat.setImageTintList(like, csl);
        likes.setTextColor(getContext().getResources().getColor(color));
    }
    public void bind(){
        username.setText(work.getUname());
        date.setText(work.getTimeAgo());
        ImageHelper.load(user_photo, work.getUpicture());
        title.setText(work.getTitle());
        description.setText(work.getDescription());
        views.setText(work.getViewsCount()+"");
        setlikes();
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewModel.like(work.getId());
            }
        });

        String[] stockArr = new String[work.getImages().size()];
        images = work.getImages().toArray(stockArr);
        //images = new String[]{"http://cdn.ekhdemni.net/uploads/photos/11-1536082347.png","http://cdn.ekhdemni.net/uploads/photos/13-1536267929.png"};
        //
        //        // adding bottom dots
        addBottomDots(0);

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        autoSliding();
    }

    void autoSliding(){
        final Handler handler = new Handler();
        final Timer timer = new Timer();
        final Runnable Update = new Runnable() {
            public void run() {
                if(viewPager!=null && isInForegroundMode){
                    if (currentPage == work.getImages().size()-1) {
                        currentPage = 0;
                    }
                    MyActivity.log("☺ ViewPager setCurrentItem: "+(currentPage+1));
                    viewPager.setCurrentItem(currentPage++, true);
                }else{
                    MyActivity.log("☺ STOP TIMER !! ");
                    timer.cancel();
                }
            }
        };// This will create a new Thread
        timer .schedule(new TimerTask() { // task to be scheduled

            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);
    }



    public void handleLike(LikeResponse response){
        work.setLikesCount(response.getLikesCount());
        work.setLiked(response.getLiked());
        if (pd!=null)pd.dismiss();
        setlikes();
    }

    private void addBottomDots(int currentPage) {
        if(isInForegroundMode){
            dots = new TextView[images.length];

            int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
            int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);
            if(currentPage >= colorsInactive.length)
                currentPage = 0;

            dotsLayout.removeAllViews();
            for (int i = 0; i < dots.length; i++) {
                dots[i] = new TextView(getContext());
                dots[i].setText(Html.fromHtml("&#8226;"));
                dots[i].setTextSize(35);
                dots[i].setTextColor(colorsInactive[currentPage]);
                dotsLayout.addView(dots[i]);
            }

            if (dots.length > 0)
                dots[currentPage].setTextColor(colorsActive[currentPage]);
        }
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    //  viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            currentPage = position;
            addBottomDots(position);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };


    /**
     * View pager adapter
     */
    public class MyViewPagerAdapter extends PagerAdapter {
        //private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView view = new ImageView(getContext());
            ImageHelper.load(view, images[position]);
            view.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT));
            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

}

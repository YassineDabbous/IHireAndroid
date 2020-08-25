package net.ekhdemni.presentation.ui.fragments.menu


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import net.ekhdemni.R
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import net.ekhdemni.MyApplication
import net.ekhdemni.model.models.LeftMenuItem
import net.ekhdemni.model.models.MenuID
import net.ekhdemni.model.oldNet.Action
import net.ekhdemni.model.oldNet.Ekhdemni
import net.ekhdemni.presentation.adapters.DrawerRecyclerAdapterLeft
import net.ekhdemni.presentation.base.MyActivity
import net.ekhdemni.presentation.ui.activities.*
import net.ekhdemni.presentation.ui.activities.auth.LoginActivity
import net.ekhdemni.presentation.ui.activities.auth.YDUserManager
import net.ekhdemni.presentation.ui.fragments.IdeasFragment
import net.ekhdemni.presentation.ui.fragments.UsersShowFragment
import net.ekhdemni.presentation.vms.VMLeftDrawer
import net.ekhdemni.utils.ImageHelper
import net.ekhdemni.utils.Themes

import timber.log.Timber
import tn.core.presentation.base.BaseActivity

import tn.core.presentation.base.MyRecyclerFragment
import tn.core.presentation.listeners.OnClickItemListener

/**
 * Fragment handles the drawer menu.
 */
class DrawerFragmentLeft : MyRecyclerFragment<LeftMenuItem, VMLeftDrawer>() {


    private var mDrawerToggle: ActionBarDrawerToggle? = null

    // Drawer top menu fields.
    private var mDrawerLayout: DrawerLayout? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProviders.of(this).get(VMLeftDrawer::class.java)
        mViewModel.callErrors.observe(this, Observer<List<String>> { this.onError(it) })
        mViewModel.loadStatus.observe(this, Observer<Boolean> { this.onStatusChanged(it) })
        mViewModel.getLiveData().observe(this, Observer<List<LeftMenuItem>> { this.onDataReceived(it) })
    }



    override fun onDataReceived(data: List<LeftMenuItem>) {
        super.onDataReceived(data)
        BaseActivity.log(" Drawer data count:" + data.size)
        BaseActivity.log(" Drawer list count:" + lista.size)
        adapter.notifyDataSetChanged()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Timber.d("%s - onCreateView", this.javaClass.simpleName)
        val layout = inflater.inflate(R.layout.fragment_drawer_left, container, false)
        recyclerView = layout.findViewById(R.id.drawer_recycler)

        prepareDrawerRecycler()
        setHeader(layout.findViewById(R.id.h))

        mViewModel.menu()
        return layout
    }


    private fun prepareDrawerRecycler() {
        adapter = DrawerRecyclerAdapterLeft(lista, OnClickItemListener {
            closeDrawerMenu()

            if(YDUserManager.check())
                when(it.id){
                    MenuID.profile -> (activity as MyActivity).setFragment(UsersShowFragment.newInstance(0))
                    MenuID.ideas -> (activity as MyActivity).setFragment(IdeasFragment.newInstance())
                    MenuID.forums -> {
                        activity!!.startActivity(Intent(activity, ForumsActivity::class.java))
                    }
                    MenuID.users -> activity!!.startActivity(Intent(activity, UsersActivity::class.java))
                    MenuID.jobs -> activity!!.startActivity(Intent(activity, JobsActivity::class.java))
                    MenuID.works -> {
                        val i = Intent(getActivity(), WorksActivity::class.java);
                        i.putExtra("url" , Ekhdemni.works);
                        startActivity(i);
                    }
                }
            else{
                startActivity(Intent(getActivity(), LoginActivity::class.java))
            }

        })
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
    }

    /**
     * Base method for layout preparation. Also set a listener that will respond to events that occurred on the menu.
     *
     * @param drawerLayout   drawer layout, which will be managed.
     * @param toolbar        toolbar bundled with a side menu.
     */
    fun setUp(drawerLayout: DrawerLayout, toolbar: Toolbar) {
        mDrawerLayout = drawerLayout
        mDrawerToggle = object : ActionBarDrawerToggle(activity, drawerLayout, toolbar, R.string.password_toggle_content_description, R.string.password_toggle_content_description) {
            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
                activity!!.invalidateOptionsMenu()
            }

            override fun onDrawerClosed(drawerView: View) {
                super.onDrawerClosed(drawerView)
                activity!!.invalidateOptionsMenu()
            }

            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                super.onDrawerSlide(drawerView, slideOffset)
                //                toolbar.setAlpha(1 - slideOffset / 2);
            }
        }

        toolbar.setOnClickListener { toggleDrawerMenu() }

        mDrawerLayout!!.addDrawerListener(mDrawerToggle!!)
        mDrawerLayout!!.post { mDrawerToggle!!.syncState() }
    }


    /**
     * When the drawer menu is open, close it. Otherwise open it.
     */
    fun toggleDrawerMenu() {
        if (mDrawerLayout != null) {
            if (mDrawerLayout!!.isDrawerVisible(GravityCompat.START)) {
                mDrawerLayout!!.closeDrawer(GravityCompat.START, true)
            } else {
                mDrawerLayout!!.openDrawer(GravityCompat.START, true)
            }
        }
    }

    /**
     * When the drawer menu is open, close it.
     */
    fun closeDrawerMenu() {
        if (mDrawerLayout != null) {
            mDrawerLayout!!.closeDrawer(GravityCompat.START, true)
        }
    }


    override fun onDestroy() {
        mDrawerLayout!!.removeDrawerListener(mDrawerToggle!!)
        super.onDestroy()
    }


/*
*
    void setProfileBagde(){
        if(percent<100){
            MyActivity.log( "Complete ur profile plz!");
            BottomNavigationMenuView bottomNavigationMenuView = (BottomNavigationMenuView) navigation.getChildAt(0);
            View v = bottomNavigationMenuView.getChildAt(4);
            BottomNavigationItemView itemView = (BottomNavigationItemView) v;
            View badge = LayoutInflater.from(this).inflate(R.layout.badge_view, bottomNavigationMenuView, false);
            itemView.addView(badge);
        }
    }
* */




    fun setHeader(header:View){
        if (YDUserManager.auth() != null) {
            if (YDUserManager.auth().photo != null && YDUserManager.auth().name != null) {
                ImageHelper.load(header!!.findViewById<ImageView>(R.id.imageView), YDUserManager.auth().photo, 150, 150)
                header!!.findViewById<TextView>(R.id.nameView).setText(YDUserManager.auth().name)
            }
        }
        val logout = header!!.findViewById<ImageView>(R.id.logout)
        logout.visibility = View.VISIBLE
        logout.setOnClickListener {
            YDUserManager.logout()
            Action.deleteSubscriptionTags()
            MyActivity.log("go to login")
            startActivity(Intent(context!!, LoginActivity::class.java))
        }


        /*val addResource = header!!.findViewById<ImageView>(R.id.addResource)
        addResource.setOnClickListener {
            setFragment(NewResourceFragment())
            drawer.closeDrawer(GravityCompat.START, true)
        }*/

        val toggleNightMode = header!!.findViewById<ImageView>(R.id.toggleNightMode)
        //toggleNightMode.setChecked(MyApplication.nightMode);
        if (MyApplication.nightMode)
            toggleNightMode.setBackgroundDrawable(resources.getDrawable(R.drawable.weather_sunny))
        else
            toggleNightMode.setBackgroundDrawable(resources.getDrawable(R.drawable.brightness))
        toggleNightMode.setOnClickListener {
            MyApplication.nightMode = !MyApplication.nightMode
            if (MyApplication.nightMode)
                toggleNightMode.setBackgroundDrawable(resources.getDrawable(R.drawable.weather_sunny))
            else
                toggleNightMode.setBackgroundDrawable(resources.getDrawable(R.drawable.brightness))
            turnLight(MyApplication.nightMode)
        }
    }

    fun turnLight(o: Boolean) {
        MyApplication.nightMode = o
        Themes.saveTheme(if (o) Themes.DARK else Themes.DEFAULT, context!!)
        activity?.recreate()
    }

}

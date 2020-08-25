package net.ekhdemni.presentation.ui.fragments.menu


import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter

import net.ekhdemni.R
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.menu.MenuBuilder
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import net.ekhdemni.model.models.MenuID
import net.ekhdemni.model.oldNet.Ekhdemni
import net.ekhdemni.presentation.adapters.DrawerRecyclerAdapterLeft
import net.ekhdemni.presentation.adapters.DrawerRecyclerAdapterRight
import net.ekhdemni.presentation.base.MyActivity
import net.ekhdemni.presentation.ui.activities.*
import net.ekhdemni.presentation.ui.activities.auth.YDUserManager
import net.ekhdemni.presentation.ui.activities.settings.SettingsPrefActivity
import net.ekhdemni.presentation.ui.fragments.*
import net.ekhdemni.presentation.vms.VMLeftDrawer

import timber.log.Timber
import tn.core.presentation.base.BaseActivity
import tn.core.presentation.base.MyFragment

import tn.core.presentation.base.MyRecyclerFragment
import tn.core.presentation.listeners.OnClickItemListener
import tn.core.util.LocaleManager
import tn.core.util.WebUtils

/**
 * Fragment handles the drawer menu.
 */
class DrawerFragmentRight : MyRecyclerFragment<MenuItem, VMLeftDrawer>() {



    // Drawer top menu fields.
    private var mDrawerLayout: DrawerLayout? = null


    override fun onDataReceived(data: List<MenuItem>) {
        super.onDataReceived(data)
        BaseActivity.log(" Drawer data count:" + data.size)
        BaseActivity.log(" Drawer list count:" + lista.size)
        adapter.notifyDataSetChanged()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Timber.d("%s - onCreateView", this.javaClass.simpleName)
        val layout = inflater.inflate(R.layout.fragment_drawer_left, container, false)
        recyclerView = layout.findViewById(R.id.drawer_recycler)
        layout.findViewById<View>(R.id.h).visibility = View.GONE

        prepareDrawerRecycler()

        menu(context!!, R.menu.activity_main_drawer)
        return layout
    }


    @SuppressLint("RestrictedApi")
    fun menu(context: Context, menu_main: Int){
        val menu = MenuBuilder(context)
        MenuInflater(context).inflate(menu_main, menu)
        val temp = mutableListOf<MenuItem>()
        for (i in 0 until menu.size())
            temp.add(menu.getItem(i))

        onDataReceived(temp)
    }


    private fun prepareDrawerRecycler() {
        adapter = DrawerRecyclerAdapterRight(lista, OnClickItemListener {
            closeDrawerMenu()
            onNavigationItemSelected(it)

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
    fun setUp(drawerLayout: DrawerLayout) {
        mDrawerLayout = drawerLayout
    }


    /**
     * When the drawer menu is open, close it. Otherwise open it.
     */
    fun toggleDrawerMenu() {
        if (mDrawerLayout != null) {
            if (mDrawerLayout!!.isDrawerVisible(GravityCompat.END)) {
                mDrawerLayout!!.closeDrawer(GravityCompat.END, true)
                (activity as MainActivity).showLeftCase()
            } else {
                mDrawerLayout!!.openDrawer(GravityCompat.END, true)
            }
        }
    }

    /**
     * When the drawer menu is open, close it.
     */
    fun closeDrawerMenu() {
        if (mDrawerLayout != null) {
            mDrawerLayout!!.closeDrawer(GravityCompat.END, true)
        }
    }



    fun setFragment(fragment: MyFragment<*>) {
        (activity as MyActivity).setFragment(fragment)
    }


    fun onNavigationItemSelected(item: MenuItem) {
        // Handle navigation view item clicks here.
        val id = item.itemId
        if (id == R.id.all) {
            setFragment(FeedsFragment.newInstance(FeedsFragment.Filter.all, null))
        } else if (id == R.id.categories) {
            setFragment(TagsFragment())
        } else if (id == R.id.bookmarks) {
            setFragment(FeedsFragment.newInstance(FeedsFragment.Filter.bookmarks, null))
        } else if (id == R.id.unread) {
            setFragment(FeedsFragment.newInstance(FeedsFragment.Filter.unread, null))
        } else if (id == R.id.my_activities) {
            setFragment(FeedsFragment.newInstance(FeedsFragment.Filter.historic, null))
        } else if (id == R.id.apps) {
            setFragment(AppsFragment())
        } else if (id == R.id.countries) {
            setFragment(CountriesFragment.newInstance(0))
        } else if (id == R.id.alerts) {
            setFragment(AlertsFragment())
        } else if (id == R.id.languages) {
            val langsE = resources.getStringArray(R.array.languages_entries)
            val langsV = resources.getStringArray(R.array.languages_values)

            val builderSingle = AlertDialog.Builder(context!!)
            builderSingle.setIcon(R.drawable.ic_stat_onesignal_default)
            builderSingle.setTitle("Select a UserLanguage")

            val arrayAdapter = ArrayAdapter<String>(context!!, android.R.layout.select_dialog_singlechoice, langsE)

            builderSingle.setNegativeButton("Cancel") { dialog, which -> dialog.dismiss() }

            builderSingle.setAdapter(arrayAdapter) { dialog, which ->
                val lang = langsV[which]
                LocaleManager.setNewLocale(activity!!, lang, false, MainActivity::class.java)
            }
            builderSingle.show()


        } else if (id == R.id.settings) {
            startActivity(Intent(context!!, SettingsPrefActivity::class.java))
        } else if (id == R.id.help) {
            val i = Intent(context!!, BrowserActivity::class.java)
            i.putExtra(BrowserActivity.EL_URL, Ekhdemni.android_help)
            startActivity(i)
        } else if (id == R.id.facebook) {
            WebUtils.openLink(context!!, Ekhdemni.facebook)
        } else if (id == R.id.privacy) {
            WebUtils.openLink(context!!, Ekhdemni.privacy)
        } else if (id == R.id.share) {
            val sharingIntent = Intent("android.intent.action.SEND")
            sharingIntent.type = "text/plain"
            sharingIntent.putExtra("android.intent.extra.TEXT", resources.getString(R.string.app_name) + "\nhttps://play.google.com/store/apps/details?id=" + context?.packageName + "\n")
            val sharingfinalintent = Intent.createChooser(sharingIntent, "Share via")
            sharingfinalintent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            try {
                context?.startActivity(sharingfinalintent)
            } catch (e3: ActivityNotFoundException) {
                e3.printStackTrace()
            }

        } else if (id == R.id.rate) {
            val uri = Uri.parse("market://details?id=" + context?.packageName)
            val goToMarket = Intent(Intent.ACTION_VIEW, uri)
            // To count with Play market backstack, After pressing back button,
            // to taken back to our application, we need to add following flags to intent.
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
            try {
                startActivity(goToMarket)
            } catch (e: ActivityNotFoundException) {
                startActivity(Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + context?.packageName)))
            }

        }
        else if (id == R.id.resources) {
            if (YDUserManager.get(context!!, YDUserManager.COUNTRY_KEY) != null)
                setFragment(ResourcesFragment())
            else {
                setFragment(CountriesFragment.newInstance(0))
            }
        }
        else if (id == R.id.services) {
            if (YDUserManager.get(context!!, YDUserManager.COUNTRY_KEY) != null)
                setFragment(ServicesFragment())
            else {
                setFragment(CountriesFragment.newInstance(1))
            }

        }
    }
}

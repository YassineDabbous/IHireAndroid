package net.ekhdemni.presentation.ui.activities

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.drawerlayout.widget.DrawerLayout.DrawerListener
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import net.ekhdemni.R
import net.ekhdemni.model.configs.ConfigsResponse
import net.ekhdemni.model.feeds.db.MyDataBase
import net.ekhdemni.model.models.Country
import net.ekhdemni.model.models.responses.NewDataResponse
import net.ekhdemni.model.oldNet.Action
import net.ekhdemni.presentation.base.MyActivity
import net.ekhdemni.presentation.mchUI.notificationHeads.ChatHeadService
import net.ekhdemni.presentation.mchUI.notificationHeads.Utils
import net.ekhdemni.presentation.mchUI.vms.VMActivity
import net.ekhdemni.presentation.ui.activities.MainActivity
import net.ekhdemni.presentation.ui.activities.auth.LoginActivity
import net.ekhdemni.presentation.ui.activities.auth.YDUserManager
import net.ekhdemni.presentation.ui.fragments.*
import net.ekhdemni.presentation.ui.fragments.HomeFragment.Companion.newInstance
import net.ekhdemni.presentation.ui.fragments.menu.DrawerFragmentLeft
import net.ekhdemni.presentation.ui.fragments.menu.DrawerFragmentRight
import net.ekhdemni.utils.ImageHelper
import net.ekhdemni.utils.ProgressUtils
import net.ekhdemni.utils.Showcase.Companion.show
import timber.log.Timber
import tn.core.model.net.net.NetworkUtils
import tn.core.presentation.listeners.OnClickItemListener
import tn.core.util.WebUtils.openLink
import java.util.*

class MainActivity() : MyActivity(), OnRefreshListener {
    //LinearLayout disabler;
    var swipeRefresh: SwipeRefreshLayout? = null
    var toolbar: Toolbar? = null
    var pd: ProgressDialog? = null
    var searchView: SearchView? = null
    var convsCount: TextView? = null
    var notsCount: TextView? = null
    var reqsCount: TextView? = null
    var unseenCount = 0
    var notifsCount = 0
    var requestsCount = 0
    var vm: VMActivity? = null
    fun setViewModel() {
        vm = ViewModelProviders.of(this).get(VMActivity::class.java)
        vm!!.getLiveData().observe(
            this,
            Observer { data: ConfigsResponse ->
                this.onDataReceived (
                    data
                )
            }
        )
        vm!!.newData.observe(
            this,
            Observer { data: NewDataResponse ->
                this.onDataReceived(data)
            }
        )
        refreshSoul()
    }

    fun refreshSoul() {
        if (YDUserManager.check()) {
            vm!!.newData()
        }
        vm!!.configs()
        //drawerFragmentLeft.refreshHeader()
    }

    fun onDataReceived(data: NewDataResponse) {
        percent = data.percent
        unseenCount = data.conversations
        notifsCount = data.notifications
        requestsCount = data.requests
        DashboardFragment.jobsCount = data.jobs
        DashboardFragment.worksCount = data.works
        DashboardFragment.postsCount = data.posts
        DashboardFragment.usersCount = data.users
        setUnseen()
    }

    fun onDataReceived(data: ConfigsResponse) {
        val old = YDUserManager.configs()
        alert = data.alert
        Action.CACHE_TIME = data.cacheTime
        Action.CACHE_SIZE = data.cacheSize
        log("this time: " + data.time)
        log("last time: " + old.time)
        if (data.active && (data.time > old.time || data.strict)) {
            val image = data.image
            val url = data.url
            popup(image, url, data.strict)
        } else if (!data.active) {
        } else {
            data.time = old.time
        }
        YDUserManager.save(data)
        setBackground()
    }

    fun initViews() {
        //disabler = getView(R.id.disabler);
        pd = ProgressUtils.getProgressDialog(this)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        swipeRefresh = getView(R.id.swiperefresh)
        swipeRefresh?.setOnRefreshListener(this)
        swipeRefresh?.setDistanceToTriggerSync(20)
        toggleSwipe(false)
        val intent = intent
        if (intent != null) {
            val push = intent.getIntExtra("push", -1)
            if (push >= 0) {
                when (push) {
                    3 -> setFistFragment(RelationsFragment.newInstance())
                    99 -> setFistFragment(AlertsFragment())
                    else -> setFistFragment(NotificationsFragment())
                }
            }
        }
        //if(currentFragment==null)
        //    setFistFragment(new DashboardFragment());
        if (currentFragment == null) setFistFragment(newInstance())
        toolbar = getView<View>(R.id.toolbar) as Toolbar
        toolbar!!.title = ""
        toolbar!!.setTitleTextColor(Color.RED)
        //toolbar.inflateMenu(R.menu.menu);
        toolbar!!.setNavigationIcon(R.drawable.menu)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        convsCount = getView(R.id.convsCount)
        notsCount = getView(R.id.notifsCount)
        reqsCount = getView(R.id.reqsCount)
        socialListener()
        searchView =
            getView<View>(R.id.searchView) as SearchView
        searchView!!.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query == null || query.trim { it <= ' ' }.isEmpty()) {
                    resetSearch()
                    return false
                }
                if (currentFragment is FeedsFragment) {
                    val dba = MyDataBase.getInstance(applicationContext)
                    dba.openToRead()
                    val searchedArticles = dba.getPosts(query)
                    dba.close()
                    (currentFragment as FeedsFragment).refresh(searchedArticles)
                }
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                //Do some magic
                return false
            }
        })
        val searcher = toolbar!!.findViewById<View>(R.id.filter)
        searcher.setOnClickListener(View.OnClickListener {
            val f =
                supportFragmentManager.findFragmentById(R.id.content_main)
            if (f is FeedsFragment) {
                searchView!!.visibility = View.VISIBLE
                searchView!!.isIconified = false
            } else {
                JobsCreatorActivity.isSearch = true
                JobsCreatorActivity.searchFor = 0
                startActivity(
                    Intent(
                        applicationContext,
                        JobsCreatorActivity::class.java
                    )
                )
            }
        })
        searchView!!.setOnCloseListener(object :
            SearchView.OnCloseListener {
            override fun onClose(): Boolean {
                searchView!!.visibility = View.GONE
                return false
            }
        })
        val drawer =
            getView<View>(R.id.drawer_layout) as DrawerLayout
        val toggle =
            ActionBarDrawerToggle(
                this,
                drawer,
                toolbar,
                R.string.app_name,
                R.string.app_name
            )
        //drawer.addDrawerListener(toggle);
        drawer.addDrawerListener(object : DrawerListener {
            override fun onDrawerSlide(
                drawerView: View,
                slideOffset: Float
            ) {
            }

            override fun onDrawerOpened(drawerView: View) {}
            override fun onDrawerClosed(drawerView: View) {}
            override fun onDrawerStateChanged(newState: Int) {}
        })
        toggle.syncState()
        setDrawer()
        setViewModel()
    }

    var drawerFragmentRight: DrawerFragmentRight? = null
    var drawerFragmentLeft: DrawerFragmentLeft? = null
    fun setDrawer() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayShowHomeEnabled(true)
        } else {
            Timber.e(RuntimeException(), "GetSupportActionBar returned null.")
        }
        val dl = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawerFragmentLeft =
            supportFragmentManager.findFragmentById(R.id.left_navigation_drawer_fragment) as DrawerFragmentLeft?
        drawerFragmentLeft!!.setUp(dl, (toolbar)!!)
        drawerFragmentRight =
            supportFragmentManager.findFragmentById(R.id.right_navigation_drawer_fragment) as DrawerFragmentRight?
        drawerFragmentRight!!.setUp(dl)
        findViewById<View>(R.id.menuRight).setOnClickListener(
            { view: View? -> drawerFragmentRight!!.toggleDrawerMenu() })
        showToolbarCases()
    }

    private fun getNavButtonView(toolbar: Toolbar?): ImageButton? {
        for (i in 0 until toolbar!!.childCount) if (toolbar.getChildAt(i) is ImageButton) return toolbar.getChildAt(
            i
        ) as ImageButton
        return null
    }

    fun showRightCase() {
        if (YDUserManager.showcases().rightDrawer == 0) {
            val sc = YDUserManager.showcases()
            sc.rightDrawer = 1
            YDUserManager.save(sc)
            show(
                this,
                findViewById(R.id.menuRight),
                "الخدمات و المصادر",
                "تحتوي هذه القائمة على مجموعة من الخدمات مع كل المصادر التي تنشر المناظرات و فرص الشغل",
                OnClickItemListener { item: View? -> drawerFragmentRight!!.toggleDrawerMenu() }
            )
        }
    }

    fun showLeftCase() {
        if (YDUserManager.showcases().leftDrawer == 0) {
            val sc = YDUserManager.showcases()
            sc.leftDrawer = 1
            YDUserManager.save(sc)
            val drawerBtn: View? = getNavButtonView(toolbar)
            if (drawerBtn != null) show(
                this,
                drawerBtn,
                "الشبكة المهنية",
                "فضاء مهني يضم كل من المنتدى و المسخدمين و أعمالهم الحرفية و افكار المشاريع الصغرى و الوظائف التي تقوم الشركات بنشرها",
                OnClickItemListener { item: View? -> drawerFragmentLeft!!.toggleDrawerMenu() }
            )
        }
        //else
        //    Toast.makeText(this, "No Drawer Btn", Toast.LENGTH_SHORT).show();
    }

    fun showToolbarCases() {
        if ((YDUserManager.showcases().conversations == 0) || (YDUserManager.showcases().requests == 0) || (YDUserManager.showcases().notifications == 0)) {
            val sc = YDUserManager.showcases()
            sc.conversations = 1
            sc.requests = 1
            sc.notifications = 1
            YDUserManager.save(sc)
            show(
                this,
                findViewById(R.id.convs),
                getText(R.string.conversations).toString(),
                "تجد هنا جميع محادثاتك مع بقية الحرفيين",
                OnClickItemListener { item: View? ->
                    show(
                        this,
                        findViewById(R.id.notifs),
                        "التنبيهات",
                        "تصلك هنا جميع تنبيهات الإعجاب و التعليقات على أعمالك و مقالاتك",
                        OnClickItemListener { item2: View? ->
                            show(
                                this,
                                findViewById(R.id.reqs),
                                "طلبات المتابعة",
                                "طلبات المتابعة من بقية الأعضاء",
                                OnClickItemListener { item3: View? -> showRightCase() }
                            )
                        }
                    )
                }
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setBackground()
        followBroadcasts()
        initViews() //called on resume
    }

    /*public void disable(){
        if(pd!=null) pd.show();
        isUpdating = true;
        //disabler.setVisibility(View.VISIBLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        //enable();
    }

    public void enable(){
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        if (pd!=null) pd.cancel();
        //if (disabler!=null) disabler.setVisibility(View.GONE);
    }*/
    fun toggleSwipe(b: Boolean?) {
        if (swipeRefresh != null) swipeRefresh!!.isEnabled = (b)!!
    }

    fun showSwipe() {
        if (swipeRefresh != null) swipeRefresh!!.isRefreshing = true
    }

    fun hideSwipe() {
        if (swipeRefresh != null) swipeRefresh!!.isRefreshing = false
    }

    fun onCountrySelected(country: Country) {
        val old = YDUserManager.get(applicationContext, YDUserManager.COUNTRY_KEY)
        YDUserManager.save(
            applicationContext,
            YDUserManager.COUNTRY_KEY,
            country.id.toString() + ""
        )
        log("selected country: " + country.name + " #" + country.id)
        if (CountriesFragment.goTo == 0) {

            if (old != null && old != "${country.id}") {
                log("changing country cause db recreating")
                application.deleteDatabase(MyDataBase.DATABASE_NAME)
            }
            //ResourcesFragment.url = Ekhdemni.countries+"/"+country.id+"/resources";
            setFragment(ResourcesFragment.newInstance())
        } else {
            setFragment(ServicesFragment.newInstance())
        }
    }

    fun refresh() {
        if (!NetworkUtils.isOnline(applicationContext)) {
            Toast.makeText(
                baseContext,
                R.string.network_error,
                Toast.LENGTH_SHORT
            ).show()
        } else {
            if (currentFragment is FeedsFragment) (currentFragment as FeedsFragment).refresh(
                ArrayList()
            ) else currentFragment.getData()
            Handler().postDelayed(object : Runnable {
                override fun run() {
                    hideSwipe()
                }
            }, 1500)
        }
    }

    override fun onBackPressed() {
        val drawer = getView<DrawerLayout>(R.id.drawer_layout)
        if (drawer != null && drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else if (searchView != null && searchView!!.isShown) {
            searchView!!.visibility = View.GONE
        } else if (supportFragmentManager.backStackEntryCount != 0) {
            supportFragmentManager.popBackStackImmediate(
                null,
                FragmentManager.POP_BACK_STACK_INCLUSIVE
            )
        } else {
            val startMain =
                Intent(Intent.ACTION_MAIN)
            startMain.addCategory(Intent.CATEGORY_HOME)
            startMain.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(startMain)
            if (Utils.canDrawOverlays(
                    baseContext
                )
            ) {
                val it =
                    Intent(this, ChatHeadService::class.java)
                it.putExtra("img", "")
                it.putExtra("url", "")
                it.putExtra("msg", "")
                it.putExtra("nbr", 0)
                startService(it)
            } else {
                requestPermission(OVERLAY_PERMISSION_REQ_CODE_CHATHEAD)
            }

            //AlertUtils.toast(this, getString(R.string.slogon));


            // super.onBackPressed();
        }
    }

    fun socialListener() {
        val isAuth = YDUserManager.check()
        getView<View>(R.id.convs).setOnClickListener(object :
            View.OnClickListener {
            override fun onClick(view: View) {
                if (isAuth) {
                    unseenCount = 0
                    setUnseen()
                    startActivity(
                        Intent(
                            applicationContext,
                            ConversationsActivity::class.java
                        )
                    )
                } else {
                    Toast.makeText(
                        applicationContext,
                        getText(R.string.need_auth),
                        Toast.LENGTH_SHORT
                    ).show()
                    startActivity(
                        Intent(
                            applicationContext,
                            LoginActivity::class.java
                        )
                    )
                }
            }
        })
        getView<View>(R.id.notifs).setOnClickListener(object :
            View.OnClickListener {
            override fun onClick(view: View) {
                if (isAuth) {
                    toggleSwipe(true)
                    notifsCount = 0
                    setUnseen()
                    setFragment(NotificationsFragment())
                } else {
                    Toast.makeText(
                        applicationContext,
                        getText(R.string.need_auth),
                        Toast.LENGTH_SHORT
                    ).show()
                    startActivity(
                        Intent(
                            applicationContext,
                            LoginActivity::class.java
                        )
                    )
                }
            }
        })
        getView<View>(R.id.reqs).setOnClickListener(object :
            View.OnClickListener {
            override fun onClick(view: View) {
                if (isAuth) {
                    toggleSwipe(true)
                    requestsCount = 0
                    setUnseen()
                    setFragment(RelationsFragment.newInstance())
                } else {
                    Toast.makeText(
                        applicationContext,
                        getText(R.string.need_auth),
                        Toast.LENGTH_SHORT
                    ).show()
                    startActivity(
                        Intent(
                            applicationContext,
                            LoginActivity::class.java
                        )
                    )
                }
            }
        })
    }

    fun setUnseen() {
        Log.wtf(
            "ekhdemni.net",
            "conversations: $unseenCount notifications: $notifsCount requests: $requestsCount"
        )
        if (unseenCount > 0) {
            convsCount!!.text = unseenCount.toString() + ""
            convsCount!!.visibility = View.VISIBLE
        } else convsCount!!.visibility = View.GONE
        if (notifsCount > 0) {
            notsCount!!.text = notifsCount.toString() + ""
            notsCount!!.visibility = View.VISIBLE
        } else notsCount!!.visibility = View.GONE
        if (requestsCount > 0) {
            reqsCount!!.text = requestsCount.toString() + ""
            reqsCount!!.visibility = View.VISIBLE
        } else reqsCount!!.visibility = View.GONE
    }

    fun resetSearch() {
        //new ArrayList<Article>()
        val dba = MyDataBase.getInstance(applicationContext)
        dba.openToRead()
        val searchedArticles = dba.take("10", 0)
        dba.close()
        (currentFragment as FeedsFragment).refresh(searchedArticles)
    }

    var mp: MediaPlayer? = null
    override fun onRefresh() {
        mp = MediaPlayer.create(applicationContext, R.raw.swipe)
        mp?.start()
        //Toast.makeText(getBaseContext(), R.string.use_shaking_next_time, Toast.LENGTH_SHORT).show();
        refresh()
    }

    var alertadd: AlertDialog.Builder? = null
    fun popup(image: String?, url: String, strict: Boolean) {
        log("popup: $url")
        popupImage = image
        popupUrl = url
        if (popupImage != null) {
            alertadd = AlertDialog.Builder(this@MainActivity)
            alertadd!!.setCancelable(!strict)
            val factory =
                LayoutInflater.from(this@MainActivity)
            val view = factory.inflate(R.layout.popup, null)
            val banner =
                view.findViewById<ImageView>(R.id.imageview)
            ImageHelper.load(banner, popupImage)
            val currentUrl = popupUrl
            banner.setOnClickListener(object : View.OnClickListener {
                override fun onClick(view: View) {
                    openLink(applicationContext, (currentUrl)!!)
                }
            })
            alertadd!!.setView(view)
            alertadd!!.show()
            popupImage = null
            popupUrl = null
        }
    }

    var OVERLAY_PERMISSION_REQ_CODE_CHATHEAD = 1234
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        if (requestCode == OVERLAY_PERMISSION_REQ_CODE_CHATHEAD && resultCode == Activity.RESULT_OK) {
            val it =
                Intent(this, ChatHeadService::class.java)
            it.putExtra("img", "")
            it.putExtra("url", "")
            it.putExtra("msg", "")
            it.putExtra("nbr", 0)
            startService(it)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun requestPermission(requestCode: Int) {
        val intent =
            Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
        intent.data = Uri.parse("package:" + packageName)
        startActivityForResult(intent, requestCode)
    }

    companion object {
        @JvmField
        var alert: String? = null
        var percent = 100
        var popupImage: String? = null
        var popupUrl: String? = null
    }
}
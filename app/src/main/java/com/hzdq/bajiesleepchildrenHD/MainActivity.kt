package com.hzdq.bajiesleepchildrenHD


import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.hzdq.bajiesleepchildrenHD.databinding.ActivityMainBinding
import com.hzdq.bajiesleepchildrenHD.dataclass.*
import com.hzdq.bajiesleepchildrenHD.device.viewmodel.DeviceViewModel
import com.hzdq.bajiesleepchildrenHD.evaluate.viewmodel.EvaluateViewModel
import com.hzdq.bajiesleepchildrenHD.frontpagefragment.*
import com.hzdq.bajiesleepchildrenHD.home.viewmodel.FrontHomeViewModel
import com.hzdq.bajiesleepchildrenHD.home.viewmodel.HomeViewModel
import com.hzdq.bajiesleepchildrenHD.login.activity.LoginActivity
import com.hzdq.bajiesleepchildrenHD.randomvisit.viewmodel.RandomViewModel
import com.hzdq.bajiesleepchildrenHD.retrofit.OkhttpSingleton
import com.hzdq.bajiesleepchildrenHD.retrofit.RetrofitSingleton
import com.hzdq.bajiesleepchildrenHD.screen.viewmodel.ScreenViewModel
import com.hzdq.bajiesleepchildrenHD.setting.activity.SettingActivity
import com.hzdq.bajiesleepchildrenHD.user.viewmodel.UserViewModel
import com.hzdq.bajiesleepchildrenHD.utils.*
import com.hzdq.dowloadapk.DownLoadAPKDialog
import com.hzdq.dowloadapk.UpdateVersionDialog
import com.liulishuo.filedownloader.BaseDownloadTask
import com.liulishuo.filedownloader.FileDownloadListener
import com.liulishuo.filedownloader.FileDownloader
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.util.*


const val SETTING_REQUEST_CODE = 13
class MainActivity : AppCompatActivity(){
    private lateinit var binding: ActivityMainBinding
    private lateinit var screenViewModel: ScreenViewModel
    private lateinit var userViewModel: UserViewModel
    private lateinit var deviceViewModel: DeviceViewModel
    private lateinit var evaluateViewModel: EvaluateViewModel
    private lateinit var randomViewModel: RandomViewModel
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var frontHomeViewModel: FrontHomeViewModel
    private lateinit var mainViewModel :MainViewModel
    private lateinit var navController: NavController
    private var updateVersionDialog:UpdateVersionDialog? = null
    private var downLoadAPKDialog:DownLoadAPKDialog? = null
    private var tokenDialog:TokenDialog? = null
    private lateinit var retrofitSingleton: RetrofitSingleton
    private val fragmentList: MutableList<Fragment> = ArrayList()
    private lateinit var shp:Shp
    var homeFragment:Fragment = HomeFragment()
    var userFragment:Fragment = UserFragment()
    var deviceFragment:Fragment = DeviceFragment()
    var screeningFragment:Fragment = ScreeningFragment()
    var evaluateFragment:Fragment = EvaluateFragment()
    var randomVisitFragment:Fragment = RandomVisitFragment()
    var destinationMap:Map<Fragment,MotionLayout>? = null
    private var isExit: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
//        navController = findNavController(R.id.fragment)
        HideUI(this).hideSystemUI()
        ActivityCollector2.addActivity(this)
//        Shp(this).saveToSp("userbarpostion","1")
        shp = Shp(this)
        retrofitSingleton = RetrofitSingleton.getInstance(this)
        DataCleanManagerKotlin.cleanInternalCache(applicationContext)
        shp.saveToSp("frontuserkeyword","")
        getUserInfo(retrofitSingleton)
        getUserUid(retrofitSingleton,"")
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        homeViewModel.refreshHome.value = 1
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        screenViewModel = ViewModelProvider(this).get(ScreenViewModel::class.java)
        frontHomeViewModel = ViewModelProvider(this).get(FrontHomeViewModel::class.java)
        deviceViewModel = ViewModelProvider(this).get(DeviceViewModel::class.java)
        evaluateViewModel = ViewModelProvider(this).get(EvaluateViewModel::class.java)
        randomViewModel = ViewModelProvider(this).get(RandomViewModel::class.java)
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        userViewModel.userBarPosition.value = 1
        screenViewModel.screenBarPosition.value = 1
        if (savedInstanceState != null) {
            //?????????????????????????????????fragment???????????????tag?????????
            /*???????????????fragment  ??????????????????null*/
            homeFragment = supportFragmentManager.getFragment(savedInstanceState, "HomeFragment")!!
            userFragment = supportFragmentManager.getFragment(savedInstanceState, "UserFragment")!!
            deviceFragment = supportFragmentManager.getFragment(savedInstanceState, "DeviceFragment")!!
            screeningFragment = supportFragmentManager.getFragment(savedInstanceState, "ScreeningFragment") !!
            evaluateFragment = supportFragmentManager.getFragment(savedInstanceState, "EvaluateFragment") !!
            randomVisitFragment = supportFragmentManager.getFragment(savedInstanceState, "RandomVisitFragment") !!
            addToList(homeFragment)
            addToList(userFragment)
            addToList(deviceFragment)
            addToList(screeningFragment)
            addToList(evaluateFragment)
            addToList(randomVisitFragment)
        } else {
            initFragment()
        }



        getCheckSoftWare()
//        testCheckVersion()

//        ????????????map??????,??????key ????????????value
         destinationMap = mapOf(
            homeFragment  to binding.homeIcon.homeMotion,
            userFragment  to binding.userIcon.userMotion,
            deviceFragment  to binding.deviceIcon.deviceMotion,
            screeningFragment to binding.screeningIcon.screeningMotion,
            evaluateFragment  to binding.evaluateIcon.evaluateMotion,
            randomVisitFragment  to binding.randomVisitIcon.randomVisitMotion
        )
//        val destinationMap = mapOf(
//            R.id.homeFragment  to binding.homeIcon.homeMotion,
//            R.id.userFragment  to binding.userIcon.userMotion,
//            R.id.deviceFragment  to binding.deviceIcon.deviceMotion,
//            R.id.screeningFragment to binding.screeningIcon.screeningMotion,
//            R.id.evaluateFragment  to binding.evaluateIcon.evaluateMotion,
//            R.id.randomVisitFragment  to binding.randomVisitIcon.randomVisitMotion
//        )
//        //?????????????????? ??????????????????
//        destinationMap.forEach { map->
//            map.value.setOnClickListener {
//                navController.navigate(map.key)
//            }
//        }
//        navController.addOnDestinationChangedListener { controller, destination, arguments ->
//            navController.popBackStack() //?????????????????????????????? ?????????????????????????????????????????????????????????
//            destinationMap.values.forEach {
//                it.progress = 0f
//            }// ???????????? layout ?????????????????????
//            //?????????destination.id  ???????????????fragment???id  ???fragment id??????destinationMap??????key????????????????????????????????????motionlayout ?????????transitionToEnd?????????????????????
//            destinationMap[destination.id]?.transitionToEnd() //?????????????????????
//        }

        userViewModel.keyword.observe(this, androidx.lifecycle.Observer {
            if (!it.equals("")){
                userIconCompleteClick()

                if (userFragment == null){

                    userFragment = UserFragment()
                    addFragment(userFragment)

                }

                showFragment(userFragment)
            }
        })

        binding.homeIcon.homeMotion.setOnClickListener {
            homeViewModel.refreshHome.value = 1
            homeIconCompleteClick()
            if (homeFragment == null) {

                    homeFragment = HomeFragment()
                    addFragment(homeFragment)

            }
            showFragment(homeFragment)
        }

        binding.userIcon.userMotion.setOnClickListener {
            userIconCompleteClick()

            if (userFragment == null){

                    userFragment = UserFragment()
                    addFragment(userFragment)

            }

            showFragment(userFragment)
        }

        binding.deviceIcon.deviceMotion.setOnClickListener {
            deviceIconCompleteClick()
            if (deviceFragment == null) {

                    deviceFragment = DeviceFragment()
                    addFragment(deviceFragment)

            }
            showFragment(deviceFragment)


        }

        binding.screeningIcon.screeningMotion.setOnClickListener {
            screenIconCompleteClick()


                if (screeningFragment == null) {
                    screeningFragment = ScreeningFragment()
                    addFragment(screeningFragment)
                }

                showFragment(screeningFragment)


        }

        binding.evaluateIcon.evaluateMotion.setOnClickListener {
            evaluateIconCompleteClick()

                if (evaluateFragment == null) {
                    evaluateFragment = ScreeningFragment()
                    addFragment(evaluateFragment)
                }

                showFragment(evaluateFragment)


        }

        binding.randomVisitIcon.randomVisitMotion.setOnClickListener {
            randomIconCompleteClick()


               if (randomVisitFragment == null) {
                   randomVisitFragment = RandomVisitFragment()
               }
               addFragment(randomVisitFragment)
               showFragment(randomVisitFragment)


        }


        binding.setUp.setOnClickListener {
            startActivityForResult(Intent(this,SettingActivity::class.java),SETTING_REQUEST_CODE)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            SETTING_REQUEST_CODE -> {
                screenViewModel.refreshFrontScreen.value = 1

            }
        }
    }


    override fun onStop() {
        super.onStop()
        Shp(this).saveToSp("openActivity","open")
    }

    /**
     * ????????????????????????
     */
    private fun randomIconCompleteClick() {
//        binding.homeIcon.homeMotion.transitionToStart()
//        binding.userIcon.userMotion.transitionToStart()
//        binding.deviceIcon.deviceMotion.transitionToStart()
//        binding.screeningIcon.screeningMotion.transitionToStart()
//        binding.evaluateIcon.evaluateMotion.transitionToStart()
//        binding.randomVisitIcon.randomVisitMotion.transitionToEnd()

        destinationMap?.values?.forEach {
            it.progress = 0f
        }// ???????????? layout ?????????????????????
        binding.randomVisitIcon.randomVisitMotion.transitionToEnd()
    }

    /**
     * ????????????????????????
     */
    private fun evaluateIconCompleteClick() {
        destinationMap?.values?.forEach {
            it.progress = 0f
        }// ???????????? layout ?????????????????????
        binding.evaluateIcon.evaluateMotion.transitionToEnd()


    }

    /**
     * ????????????????????????
     */
    private fun screenIconCompleteClick() {
        destinationMap?.values?.forEach {
            it.progress = 0f
        }// ???????????? layout ?????????????????????
        binding.screeningIcon.screeningMotion.transitionToEnd()

    }

    /**
     * ????????????????????????
     */
    private fun deviceIconCompleteClick() {
        destinationMap?.values?.forEach {
            it.progress = 0f
        }// ???????????? layout ?????????????????????
        binding.deviceIcon.deviceMotion.transitionToEnd()

    }

    /**
     * ????????????????????????
     */
    private fun userIconCompleteClick() {
        destinationMap?.values?.forEach {
            it.progress = 0f
        }// ???????????? layout ?????????????????????
        binding.userIcon.userMotion.transitionToEnd()

    }

    /**
     * ????????????????????????
     */
    private fun homeIconCompleteClick() {
        destinationMap?.values?.forEach {
                it.progress = 0f
            }// ???????????? layout ?????????????????????
        binding.homeIcon.homeMotion.transitionToEnd()

    }

    private fun initFragment() {
        /* ????????????home  fragment*/
        homeIconCompleteClick()
        homeFragment = HomeFragment()
        addFragment(homeFragment as HomeFragment)
        showFragment(homeFragment as HomeFragment)

        addFragment(userFragment as UserFragment)
        addFragment(deviceFragment as DeviceFragment)
        addFragment(screeningFragment as ScreeningFragment)
        addFragment(evaluateFragment as EvaluateFragment)
        addFragment(randomVisitFragment as RandomVisitFragment)

    }

    private fun addToList(fragment: Fragment?) {
        if (fragment != null) {
            fragmentList.add(fragment)
        }
    }

    /*??????fragment*/
    private fun showFragment(fragment: Fragment) {
        for (frag in fragmentList) {
            if (frag !== fragment) {
                /*???????????????fragment*/
                supportFragmentManager.beginTransaction().hide(frag).commit()
            }
        }
        supportFragmentManager.beginTransaction().show(fragment).commit()
    }
    /*??????fragment*/
    private fun addFragment(fragment: Fragment) {

        /*?????????fragment????????????????????????  ?????????????????????  ?????????*/
        if (!fragment.isAdded) {
            supportFragmentManager.beginTransaction().add(R.id.content_layout, fragment).commit()
            /*????????? fragmentList*/fragmentList.add(fragment)
        }
    }

    override fun onDestroy() {
        ActivityCollector2.removeActivity(this)
        DataCleanManagerKotlin.cleanCustomCache(getExternalFilesDir(null)?.getPath().toString() + "BarcodeBitmap")
        super.onDestroy()

        Shp(this).saveToSp("userbarpostion","1")
    }


    override fun onSaveInstanceState(outState: Bundle) {

        /*fragment???????????? ??????*/
        if (homeFragment != null) {
            supportFragmentManager.putFragment(outState!!, "HomeFragment", homeFragment)
        }
        if (userFragment != null) {
            supportFragmentManager.putFragment(outState!!, "UserFragment", userFragment)
        }
        if (deviceFragment != null) {
            supportFragmentManager.putFragment(outState!!, "DeviceFragment", deviceFragment)
        }
        if (screeningFragment != null) {
            supportFragmentManager.putFragment(outState!!, "ScreeningFragment", screeningFragment)
        }
        if (evaluateFragment != null) {
            supportFragmentManager.putFragment(outState!!, "EvaluateFragment", evaluateFragment)
        }
        if (randomVisitFragment != null) {
            supportFragmentManager.putFragment(outState!!, "RandomVisitFragment", randomVisitFragment)
        }
        super.onSaveInstanceState(outState)
    }


    override fun onBackPressed() {

        exitBy2Click()
    }

    /**
     * ???????????????app
     */
    fun exitBy2Click() {
        val handler = Handler()
        if ((!isExit)) {
            isExit = true
            Toast.makeText(this, "??????????????????APP", Toast.LENGTH_LONG).show()
            handler.postDelayed({ isExit = false }, 1000 * 2) //x?????????????????????
        } else {

            Shp(this).saveToSp("userbarpostion","1")
            finish()


        }
    }


    /**
     * ????????????????????????
     * @param filePath
     * @return
     */
    private fun fileIsExists(filePath: String): Boolean {
        try {
            val f = File(filePath)
            if (!f.exists()) {
                return false
            }
        } catch (e: Exception) {
            return false
        }
        return true
    }


    /**
     * ??????apk
     */
    private fun installApk(fileSavePath: String) {
        val file = File(fileSavePath)

        val intent = Intent(Intent.ACTION_VIEW)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val data: Uri
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { //????????????????????????7.0
            // ??????FileProvider????????????content?????????Uri
            data =
                FileProvider.getUriForFile(this, "$packageName.fileProvider", file)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // ?????????????????????????????????
        } else {
            data = Uri.fromFile(file)
        }
        intent.setDataAndType(data, "application/vnd.android.package-archive")
        startActivity(intent)
    }


    /**
     * ?????????????????????
     * @param url
     * @param path
     */
    private fun DownLoadFile(url: String, path: String) {
        FileDownloader.getImpl().create(url).setPath(path)
            .setListener(object : FileDownloadListener() {
                override fun pending(task: BaseDownloadTask, soFarBytes: Int, totalBytes: Int) {

                }
                override fun progress(task: BaseDownloadTask, soFarBytes: Int, totalBytes: Int) {

                    mainViewModel.soFarBytes.value = soFarBytes.toFloat()
                    mainViewModel.totalBytes.value = totalBytes.toFloat()
                    mainViewModel.setPercent()
                }
                override fun completed(task: BaseDownloadTask) {

                    if (fileIsExists(path)){
                        installApk(path)
                        ActivityCollector2.finishAll()
                    }

                }

                override fun paused(task: BaseDownloadTask, soFarBytes: Int, totalBytes: Int) {

                }
                override fun error(task: BaseDownloadTask, e: Throwable) {

                    ToastUtils.showTextToast(this@MainActivity, "????????????")
                }

                override fun warn(task: BaseDownloadTask) {

                }
            }).start()
    }


    /**
     * ????????????
     */
    fun updateVersion(url: String){
        if (updateVersionDialog == null){
            updateVersionDialog = UpdateVersionDialog(mainViewModel,this,this,object : UpdateVersionDialog.ConfirmAction{
                override fun onRightClick() {
                    DownLoadFile(url,"${cacheDir}/childrenOSA.apk")
                    if (downLoadAPKDialog == null){

                        downLoadAPKDialog = DownLoadAPKDialog(mainViewModel,this@MainActivity,this@MainActivity)
                        downLoadAPKDialog?.show()
                        //???show????????????????????????????????????
                        downLoadAPKDialog?.setCanceledOnTouchOutside(false)
                    }else {
                        downLoadAPKDialog?.show()
                        //???show????????????????????????????????????
                        downLoadAPKDialog?.setCanceledOnTouchOutside(false)
                    }

                }

            })
            updateVersionDialog?.show()
            //???show????????????????????????????????????
            updateVersionDialog?.setCanceledOnTouchOutside(false)
        }else {
            updateVersionDialog?.show()
            //???show????????????????????????????????????
            updateVersionDialog?.setCanceledOnTouchOutside(false)
        }
    }


    /**
     * ????????????
     */
    fun getCheckSoftWare(){
        val request = Request.Builder()
            .url(OkhttpSingleton.BASE_URL+"/v2/software?type=1&subtype=2")
            //                .addHeader("token", "30267f97bb1aeb1e2ddca1cda79d92b5")
            //                .addHeader("uid", "8")
            .addHeader("token", shp.getToken())
            .addHeader("uid", shp.getUid())
            .addHeader("User-Agent", shp.getUserAgent())
            .get()
            .build()
        //3.???request?????????call
        val call = OkhttpSingleton.ok()?.newCall(request)

        //4.??????call
//        ????????????
//        Response response = call.execute();

        //????????????
        call?.enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                runOnUiThread { ToastUtils.showTextToast2(this@MainActivity, "????????????????????????????????????") }
            }

            @Throws(IOException::class)
            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                val res = response.body()!!.string()

                Log.d("checkVersion", "onResponse: $res")

                //????????????
                val dataClassSoftInfo = DataClassSoftInfo()
                val dataSoftInfo = DataSoftInfo()


                try {
                    val jsonObject = JSONObject(res)
                    //???????????????
                    //???????????????
                    val code = jsonObject.optInt("code")
                    val data = jsonObject.optJSONObject("data")
                    val msg = jsonObject.optString("msg")


                    //???????????????

                    //???????????????
                    dataClassSoftInfo.code = code
                    dataClassSoftInfo.msg = msg
                    if (data != null){
                        val id =  data.optInt("id")
                        val version =  data.optString("version")
                        val type =  data.optInt("type")
                        val subtype =  data.optInt("subtype")
                        val content =  data.optString("content")
                        val url =  data.optString("url")
                        val status =  data.optInt("status")
                        dataSoftInfo.id = id
                        dataSoftInfo.version = version
                        dataSoftInfo.type = type
                        dataSoftInfo.subtype = subtype
                        dataSoftInfo.content = content
                        dataSoftInfo.url = url
                        dataSoftInfo.status = status

                        dataClassSoftInfo.data = dataSoftInfo

                    }
                }catch (e: JSONException){
                    e.printStackTrace()
                }

                runOnUiThread{

                    if (dataClassSoftInfo.code == 0){
                        if (!dataSoftInfo.version.equals(getVerName())){
                            updateVersion(dataSoftInfo.url)
                        }


                        mainViewModel.contentVersion.value = dataSoftInfo.content
                        mainViewModel.contentVersion.value = dataSoftInfo.content
                        mainViewModel.currentVersion.value = getVerName()
                        mainViewModel.nextVersion.value = dataSoftInfo.version
                    }else if ( dataClassSoftInfo.code == 10010 ||  dataClassSoftInfo.code == 10004) {
                        if (tokenDialog == null) {
                            tokenDialog = TokenDialog(this@MainActivity, object : TokenDialog.ConfirmAction {
                                override fun onRightClick() {
                                    shp.saveToSp("token", "")
                                    shp.saveToSp("uid", "")

                                    startActivity(
                                        Intent(this@MainActivity,
                                            LoginActivity::class.java)
                                    )
                                    ActivityCollector2.finishAll()
                                }

                            })
                            tokenDialog!!.show()
                            tokenDialog?.setCanceledOnTouchOutside(false)
                        } else {
                            tokenDialog!!.show()
                            tokenDialog?.setCanceledOnTouchOutside(false)
                        }
                    }

                }
            }
        })

//        retrofitSingleton.api().getSoftware(1,2).enqueue(object :Callback<DataClassSoftInfo>{
//            override fun onResponse(
//                call: Call<DataClassSoftInfo>,
//                response: Response<DataClassSoftInfo>
//            ) {
//
//               if (response.body()?.code ==0){
//                   if (response.body()?.data?.version.equals(getVerName())){
//                       updateVersion(response.body()?.data?.url!!)
//                   }
//
//                   mainViewModel.contentVersion.value = response.body()?.data?.content
//                   mainViewModel.contentVersion.value = response.body()?.data?.content
//                   mainViewModel.currentVersion.value = getVerName()
//                   mainViewModel.nextVersion.value = response.body()?.data?.version
//               }
//            }
//
//            override fun onFailure(call: Call<DataClassSoftInfo>, t: Throwable) {
//                ToastUtils.showTextToast(this@MainActivity,"????????????????????????????????????")
//            }
//
//        })
    }


    fun testCheckVersion(){
        updateVersion("https://bajiesleep.com/download/bajiesleep.apk")

        mainViewModel.contentVersion.value = "????????????????????????????????????????????????????????????????????????????????????????????????" +
                "?????????????????????????????????????????????????????????????????????????????????????????????KDJ???KDJ??????????????????" +
                "???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????" +
                "???????????????????????????????????????????????????????????????????????????????????????????????????????????????KDJ???KDJ???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????KDJ???KDJ??????????????????????????????"

//        mainViewModel.contentVersion.value = "????????????bug???"
        mainViewModel.currentVersion.value = getVerName()
        mainViewModel.nextVersion.value = "1.0.1"
    }

    fun getVerName(): String? {
        var verName: String? = ""
        try {
            verName = packageManager.getPackageInfo(
                "com.hzdq.bajiesleepchildrenHD", 0
            ).versionName
        } catch (e: PackageManager.NameNotFoundException) {
        }
        return verName
    }

    fun getUserInfo(retrofit:RetrofitSingleton){
        retrofit.api().getUserInfo("/v1/auser/${shp.getUid()}").enqueue(object :Callback<DataClassUserInfo>{
            override fun onResponse(
                call: Call<DataClassUserInfo>,
                response: Response<DataClassUserInfo>
            ) {

                if (response.body()?.code == 0){

                    response.body()?.data?.hospitalid?.let {
                        shp.saveToSpInt("hospitalid",
                            it
                        )
                    }

                }
//

            }

            override fun onFailure(call: Call<DataClassUserInfo>, t: Throwable) {
                ToastUtils.showTextToast(this@MainActivity,"??????????????????????????????????????????")
            }

        })
    }


    fun getUserUid(retrofit: RetrofitSingleton, kewords:String){


        retrofit.api().getFrontUserList(kewords,shp.getHospitalId()!!,1,1,0).enqueue(object :
            Callback<DataClassFrontUser> {
            override fun onResponse(
                call: Call<DataClassFrontUser>,
                response: Response<DataClassFrontUser>
            ) {
                if (response.body()?.code == 0){

                    if (response.body()?.data?.data?.size!! > 0){
                        response.body()?.data?.data?.get(0)
                            ?.let { shp.saveToSpInt("treatrecordpatientid", it.uid) }
                    }




                }

            }

            override fun onFailure(call: Call<DataClassFrontUser>, t: Throwable) {
                ToastUtils.showTextToast(this@MainActivity,"????????????uid??????????????????")
            }

        })
    }
}
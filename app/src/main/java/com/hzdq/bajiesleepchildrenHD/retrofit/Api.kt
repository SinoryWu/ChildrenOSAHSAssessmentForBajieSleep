package com.hzdq.bajiesleepchildrenHD.retrofit


import com.hzdq.bajiesleepchildrenHD.dataclass.*
import okhttp3.RequestBody
import okhttp3.Response
import retrofit2.Call

import retrofit2.http.*

interface Api {
    @GET("/v2/login/index")
    fun getPasswordLogin(@Query("username") username:String,@Query("password") password:String,@Query("reg_id") reg_id:String):Call<PasswordDateClass>


    @POST("/v1/login/check")
    fun postMessageLogin(@Body messageBody: MessageBody):Call<MessageDateClass>

    @GET("/v2/login/check")
    fun postMessageLogin1(@Body requestBody: RequestBody):Call<MessageDateClass>
    @POST("/v2/Jms")
    fun postSendMessage(@Body sendMessageBody: SendMessageBody):Call<SendMessageDataClass>

    @GET("/v2/report")
    fun getFrontHomeSleepReport(@Query("limit") limit:Int,@Query("hospitalid") hospitalid:Int):Call<DataClassFrontHome>
    @GET("/v2/report")
    fun getHomeSleepReport(@Query("limit") limit:Int,
                           @Query("hospitalid") hospitalid:Int,
                           @Query("keywords") keywords:String,
                           @Query("startTime") startTime:String,
                           @Query("endTime") endTime:String,@Query("page") page:Int):Call<DataClassHomeReportSleep>
    @GET("/v2/User/index")
    fun getFrontUserList(@Query("keywords") keywords:String,
                         @Query("hospitalid") hospitalid:Int,
                         @Query("limit") limit:Int,
                         @Query("page") page:Int,
                         @Query("simple") simple:Int):Call<DataClassFrontUser>


    @GET("/v2/User/index")
    fun getFrontFollowUpList(@Query("keywords") keywords:String,
                         @Query("hospitalid") hospitalid:Int,
                         @Query("limit") limit:Int,
                         @Query("page") page:Int,
                             @Query("simple") simple:Int,
                             @Query("follow") follow:Int):Call<DataClassFrontFollowUp>
    @GET("/v2/device")
    fun getFrontDeviceList(@Query("limit") limit:Int,
                           @Query("hospitalid") hospitalid:Int,
                           @Query("keywords") keywords:String,
                           @Query("type") type: Int,
                           @Query("page")page: Int,@Query("devStatus") devStatus:Int):Call<DataClassFrontDevice>

    @POST("/v2/accessmenTaskList")
    fun postFrontScreenList(@Body frontScreenBody: FrontScreenBody):Call<DataClassFrontScreen>


    @POST("/v2/accessmentResult")
    fun postOSASubmitResult(@Body osaResultBody: OSAResultBody):Call<DataClassQuestionResult>


    @POST("/v2/accessmentResult")
    fun postPSQSubmitResult(@Body psqResultBody: PSQResultBody):Call<DataClassQuestionResult>

    @GET("/v2/accessmenTaskInfo")
    fun getTaskInfoResult(@Query("task_id")task_id:Int,@Query("hospital_id")hospital_id:Int):Call<DataClassTaskInfo>

    @POST("/v2/getMyAssList")
    fun postQuestionResultList(@Body questionResultListBody: QuestionResultListBody):Call<DataClassQuestionResultList>

    @POST("/v2/accessmenTaskSave")
    fun postNewTask(@Body newTaskBody: NewTaskBody):Call<DataClassNewScreenTask>

    @GET
    fun getDeviceInfo(@Url url:String): Call<DataClassDeviceInfo>


    @GET
    fun getUserInfo(@Url url:String): Call<DataClassUserInfo>
    @GET("/v2/calculate")
    fun getFrontHomeCalculate(@Query("hospitalid") hospitalid:Int): Call<DataClassFrontHomeCalculate>

    @GET("/v2/user/create")
    fun getAddUser(
        @Query("truename")truename:String,
        @Query("sex")sex:String,
        @Query("age")age:String,
        @Query("height")height:String,
        @Query("weight")weight:String,
        @Query("mobile")mobile:String,
        @Query("hospitalid")hospitalid:String,
        @Query("mzh")mzh:String,
        @Query("id_card")id_card:String,
        @Query("address")address:String,
        @Query("needfollow")needfollow:String
    ):Call<DataclassAddUser>


    @GET("/v2/device")
    fun getHomeBindDeviceDeviceList(@Query("limit") limit:Int,
                           @Query("hospitalid") hospitalid:Int,
                           @Query("keywords") keywords:String,
                           @Query("page")page: Int,@Query("type") type:Int):Call<DataClassHomeBindDeviceDevice>



    @GET("/v2/User/index")
    fun getHomeBindDeviceUserList(@Query("limit") limit:Int,
                                    @Query("hospitalid") hospitalid:Int,
                                    @Query("keywords") keywords:String,
                                    @Query("page")page: Int,
                                  @Query("simple") simple:Int):Call<DataClassHomeBindDeviceUser>


    @POST
    fun postDeviceBindUser(@Url url: String,@Body deviceBindUserBody: DeviceBindUserBody):Call<DataClassDeviceBindUser>


    //??????????????????
    @PUT
    fun putEditDevice(@Url url: String,@Query("status")status:Int,@Query("hospitalid")hospitalid:Int):Call<DataClassEditDevice>


    //????????????
    @POST("/v1/device/endDev")
    fun postEndDevice(@Query("hospitalid")hospitalid:Int,@Query("sn")sn:String):Call<DataClassEditDevice>

    //???????????????????????????
    @GET
    fun getRecoverDeviceInfo(@Url url: String):Call<DataClassRecoverDeviceInfo>

    /**
     * ??????????????????
     */
//    @POST("/v2/user/update")
//    @Headers("Content-Type: application/json;charset=UTF-8")
//    fun postUpdateUser(@Body updateUserBody: UpdateUserBody):Call<DataClassUpdateUser>

    @POST("/v2/user/update")
    fun postUpdateUser(@Body requestBody: RequestBody):okhttp3.Call
    /**
     * ??????????????????
     */
    @POST("/v2/user/status")
    fun postDeleteUser(@Body deleteUserBody: DeleteUserBody):Call<DataClassDeleteUser>

    @GET("/v2/getMyAssDetail")
    fun getPsqResultDetail(@Query("id")id:Int):Call<DataclassResultPSQDetail>


    /**
     * OSA????????????
     */
    @GET("/v2/getMyAssDetail")
    fun getOsaResultDetail(@Query("id")id:Int):Call<DataClassResultOSADetail>


    /**
     * ????????????
     */
    @PUT
    fun putDeviceEdit(@Url url: String,@Body editDeviceBody: EditDeviceBody):Call<DataClassEditDevice>


    /**
     * ????????????
     */
    @POST("/v2/estimateSave")
    fun postAddEvaluate(@Body addEvaluateBody: AddEvaluateBody):Call<DataClassAddEvaluate>

    /**
     * ????????????
     */
    @GET("/v2/estimate")
    fun getEvaluateRecord(@Query("hospitalid") hospitalid:Int,@Query("patient_id")patient_id:Int,@Query("page")page:Int):Call<DataclassEvaluateRecord>

    /**
     * ??????????????????
     */
    @GET("/v2/treat")
    fun getTreat(@Query("type")type:Int,@Query("hospitalid")hospitalid:Int,@Query("patient_id")patient_id:Int,@Query("page")page:Int):Call<DataClassTreatRecord>


    /**
     * ??????????????????
     */
    @GET("/v2/treatInfo")
    fun getTreatDetail(@Query("id")id:Int):Call<DataClassTreatmentDetail>

    /**
     * ????????????????????????
     */
    @GET("/v2/followup")
    fun getFollowUpList(@Query("patient_id")patient_id:Int,@Query("hospitalid")hospitalid:Int,@Query("page")page:Int):Call<DataClassFollowUpList>

    /**
     * ??????????????????
     */
    @GET("/v2/followupInfo")
    fun getFollowUpInfo(@Query("id")id:Int):Call<DataClassFollowUpDetail>


    /**
     * ??????????????????
     */
    @GET("/v2/followup")
    fun getFollowUp(@Query("patient_id")patient_id:Int,@Query("hospitalid")hospitalid:Int,@Query("page")page:Int):Call<DataClassFollowUp>


    /**
     * ??????????????????????????????
     */
    @POST("/v2/getMyAssList")
    fun postUserQuestionResultList(@Body questionResultListBody: QuestionResultListBody):Call<DataClassUserQuestionResultList>


    /**
     * ??????????????????
     */
    @GET("/v2/treat")
    fun getUserReportList(@Query("type")type:Int,@Query("hospitalid")hospitalid:Int,@Query("patient_id")patient_id:Int,@Query("page")page:Int,@Query("subtype")subtype:Int):Call<DataClassReportList>


    /**
     * ????????????????????????
     */
    @GET("/v2/User/index")
    fun getHomeUserSearchList(@Query("limit") limit:Int,
                                  @Query("hospitalid") hospitalid:Int,
                                  @Query("keywords") keywords:String,
                                  @Query("page")page: Int,
                              @Query("simple") simple:Int):Call<DataClassHomeUserSearch>


    /**
     * ??????????????????
     */
    @GET("/v2/accessmentList")
    fun getMyQuestionList(@Query("hospitalid")hospitalid:Int,@Query("type")type:String):Call<DataClassMyQuestionList>

    /**
     * ??????????????????
     */
    @GET("/v2/accessmentInfo")
    fun getMyQuestionInfo(@Query("type")type:Int):Call<DataClassQuestionInfo>


    /**
     * ??????????????????
     */
    @GET("/v2/accessmentList")
    fun getQuestionMarketList(@Query("hospitalid")hospitalid:Int,@Query("type")type:String):Call<DataClassQuestionMarketList>

    /**
     * ????????????
     */
    @POST("/v2/logout")
    fun postLogOut():Call<DataClassLogout>

    @GET("/v2/accessmenTaskInfo")
    fun getTaskInfoRegister(@Query("register")register:Int,@Query("hospital_id")hospital_id:Int):Call<DataClassTaskInfo>


    @GET("/v2/software")
    fun getSoftware(@Query("type")type:Int,@Query("subtype")subtype:Int):Call<DataClassSoftInfo>

}
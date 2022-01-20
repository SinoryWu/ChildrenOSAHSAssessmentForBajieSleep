package com.hzdq.bajiesleepchildrenHD.dataclass

import com.google.gson.annotations.SerializedName

data class AddEvaluateBody (
    @SerializedName("hospitalid")
    var hospitalid: Int = 0, // 0
    @SerializedName("patient_id")
    var patient_id: Int = 0, // 添加成功
    @SerializedName("id")
    var id: Int = 0, //
    @SerializedName("sleep")
    var sleep: Int = 0,
    @SerializedName("pih")
    var pih: Int = 0,
    @SerializedName("dm")
    var dm: Int = 0,
    @SerializedName("mode")
    var mode: Int = 0,
    @SerializedName("birth")
    var birth: Int = 0,
    @SerializedName("pomr")
    var pomr: Int = 0,
    @SerializedName("cmv")
    var cmv: Int = 0,
    @SerializedName("fhs")
    var fhs: String = "",
    @SerializedName("rhinitis")
    var rhinitis: Int = 0,
    @SerializedName("ekc")
    var ekc: Int = 0,
    @SerializedName("asthma")
    var asthma: Int = 0,
    @SerializedName("ihb")
    var ihb: Int = 0,
    @SerializedName("eczema")
    var eczema: Int = 0,
    @SerializedName("urticaria")
    var urticaria: Int = 0,
    @SerializedName("thyroid")
    var thyroid: Int = 0,
    @SerializedName("fat")
    var fat: Int = 0,
    @SerializedName("tnb")
    var tnb: Int = 0,
    @SerializedName("tonsils")
    var tonsils: Int = 0,
    @SerializedName("mdd")
    var mdd: Int = 0,
    @SerializedName("adenoid")
    var adenoid: Int = 0,
    @SerializedName("weight")
    var weight: String = "",
    @SerializedName("height")
    var height: String = "",
    @SerializedName("bmi")
    var bmi: String = "",
    @SerializedName("dns")
    var dns: Int = 0,
    @SerializedName("hypertrophy")
    var hypertrophy: Int = 0,
    @SerializedName("polyp")
    var polyp: Int = 0,
    @SerializedName("face")
    var face: Int = 0,
    @SerializedName("occlusion")
    var occlusion: Int = 0,
    @SerializedName("crossbite")
    var crossbite: Int = 0,
    @SerializedName("brodskyAdenoid")
    var brodskyAdenoid: Int = 0,
    @SerializedName("neck")
    var neck: String = "",
    @SerializedName("brodskyTonsils")
    var brodskyTonsils: Int = 0,
    @SerializedName("npc")
    var npc: String = "",
    @SerializedName("oahi")
    var oahi: String = "",
    @SerializedName("noise")
    var noise: Int = 0,
    @SerializedName("lamp")
    var lamp: Int = 0,
    @SerializedName("crowd")
    var crowd: Int = 0,
    @SerializedName("smoke")
    var smoke: Int = 0,
    @SerializedName("assessment")
    var assessment: String ="",
    @SerializedName("osas")
    var osas: Int = 0,
    @SerializedName("opinion")
    var opinion: String = "",
    @SerializedName("treat")
    var treat: Int = 0,
    @SerializedName("treatment")
    var treatment: String = ""




)
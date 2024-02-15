package com.rizadwi.snapsift.datasource.service.dto.response

import com.google.gson.annotations.SerializedName
import com.rizadwi.snapsift.model.Source

data class SourceResponse (
    @SerializedName("status")
    val status:String,

    @SerializedName("sources")
    val sources: List<Source>
)
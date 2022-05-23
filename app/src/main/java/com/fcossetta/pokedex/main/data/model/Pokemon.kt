package com.fcossetta.pokedex.main.data.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import java.io.Serializable


@JsonClass(generateAdapter = true)
@Parcelize
data class Pokemon(
    @Json(name = "name")
    val name: String? = null,
    @Json(name = "order")
    val order: Int? = null,
    @Json(name = "height")
    val height: Int? = null,
    @Json(name = "id")
    val id: Int? = null,
    @Json(name = "weight")
    val weight: Int? = null,
    @Json(name = "stats")
    val stats: List<StatInfo>? = null,
    @Json(name = "sprites")
    val sprites: Sprite? = null,
    @Json(name = "types")
    val types: List<Type>? = null,

    ) : Serializable, Parcelable

@JsonClass(generateAdapter = true)
@Parcelize
data class Stat(
    @Json(name = "name")
    val name: String? = null,
    @Json(name = "url")
    val url: String? = null,
) : Parcelable

@JsonClass(generateAdapter = true)
@Parcelize
data class StatInfo(
    @Json(name = "base_stat")
    val baseStats: Int? = null,
    @Json(name = "effort")
    val effort: Int? = null,
    @Json(name = "stat")
    val stat: Stat? = null,
) : Parcelable

@JsonClass(generateAdapter = true)
@Parcelize
data class Sprite(
    @Json(name = "front_default")
    val front_default: String? = null,
) : Parcelable


@JsonClass(generateAdapter = true)
@Parcelize
data class Type(
    @Json(name = "slot")
    val slot: String? = null,
    @Json(name = "type")
    val typeDetail: TypeDetail? = null,
) : Parcelable

@JsonClass(generateAdapter = true)
@Parcelize
data class TypeDetail(
    @Json(name = "name")
    val name: String? = null,
    @Json(name = "url")
    val url: String? = null,
) : Parcelable

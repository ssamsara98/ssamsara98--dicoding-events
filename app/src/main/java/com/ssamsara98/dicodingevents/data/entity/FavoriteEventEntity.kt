package com.ssamsara98.dicodingevents.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_event_entity")
class FavoriteEventEntity(
    @field:ColumnInfo
    @field:PrimaryKey
    val id: Int,

    @field:ColumnInfo("name")
    val name: String,

    @field:ColumnInfo("summary")
    val summary: String,

    @field:ColumnInfo("description")
    val description: String,

    @field:ColumnInfo("image_logo")
    val imageLogo: String,

    @field:ColumnInfo("media_cover")
    val mediaCover: String,

    @field:ColumnInfo("category")
    val category: String,

    @field:ColumnInfo("owner_name")
    val ownerName: String,

    @field:ColumnInfo("city_name")
    val cityName: String,

    @field:ColumnInfo("quota")
    val quota: Int,

    @field:ColumnInfo("registrants")
    val registrants: Int,

    @field:ColumnInfo("begin_time")
    val beginTime: String,

    @field:ColumnInfo("end_time")
    val endTime: String,

    @field:ColumnInfo("link")
    val link: String
)
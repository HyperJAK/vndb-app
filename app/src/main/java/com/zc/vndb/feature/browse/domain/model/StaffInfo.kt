package com.zc.vndb.feature.browse.domain.model

import com.zc.vndb.common.domain.model.ExternalLink

/** A VNDB staff member (writer, artist, voice actor…), for the header of a staff-filtered Browse. */
data class StaffInfo(
    val id: String,
    val name: String,
    /** The name in its original script, when it differs from [name]. */
    val originalName: String?,
    /** Other names the person has worked under, without [name] and [originalName]. */
    val aliases: List<String>,
    /** Language code VNDB files the person under, such as `ja`. */
    val language: String?,
    val gender: StaffGender?,
    /** VNDB BBCode. */
    val description: String?,
    val links: List<ExternalLink>,
)

enum class StaffGender { Male, Female }

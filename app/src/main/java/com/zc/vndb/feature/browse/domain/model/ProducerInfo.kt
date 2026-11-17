package com.zc.vndb.feature.browse.domain.model

/** A VNDB producer, for the header of a producer-filtered Browse. */
data class ProducerInfo(
    val id: String,
    val name: String,
    /** The name in its original script, when it differs from [name]. */
    val originalName: String?,
    val aliases: List<String>,
    val type: ProducerType?,
    /** Language code VNDB files the producer under, such as `ja`. */
    val language: String?,
    /** VNDB BBCode. */
    val description: String?,
)

enum class ProducerType { Company, Individual, AmateurGroup }

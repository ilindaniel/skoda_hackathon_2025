package org.example.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PositionDTO(
    @SerialName("role_id")
    val id: String,
    @SerialName("name")
    val description: String
)

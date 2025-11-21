package org.example.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EmployeeDTO(
    @SerialName("personal_number")
    val personalNumber: String,
    @SerialName("user_name")
    val username: String,
    @SerialName("profession")
    val profession: String,
    @SerialName("planned_position_id")
    val plannedPositionId: String,
    @SerialName("planned_position")
    val plannedPosition: String
)

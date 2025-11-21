package org.example.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EmployeeDTO(
    @SerialName("personal_number")
    val personalNumber: String,
    @SerialName("profession")
    val profession: String,
    @SerialName("user_name")
    val username: String,
    @SerialName("planned_profession_id")
    val plannedPositionId: String,
    @SerialName("planned_profession")
    val plannedPosition: String
)

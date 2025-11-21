package org.example.dto

import kotlinx.serialization.Serializable

@Serializable
data class EmployeeDTO(
    val personalNumber: String,
    val profession: String,
    val username: String,
    val plannedPositionId: String,
    val plannedPosition: String
)

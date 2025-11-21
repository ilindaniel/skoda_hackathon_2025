package org.example.dto

import kotlinx.serialization.Serializable

@Serializable
data class ProfileDTO(
    val employeeDTO: EmployeeDTO,
    val skills: List<String>,
    val qualifications: List<String>
)

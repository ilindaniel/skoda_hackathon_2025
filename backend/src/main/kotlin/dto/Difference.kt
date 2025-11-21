package org.example.dto

import kotlinx.serialization.Serializable

@Serializable
data class Difference(
    val employee_qualifications: List<String>,
    val missing_qualifications: List<String>,
    val employee_skills: List<String>,
    val missing_skills: List<String>,
)

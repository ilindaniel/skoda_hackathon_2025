package org.example.dao

import org.jetbrains.exposed.sql.Table

object CourseSkill : Table("course_skill") {
    val courseId = varchar("course_id", 20)
    val skillName = varchar("skill_name", 100).nullable()
}

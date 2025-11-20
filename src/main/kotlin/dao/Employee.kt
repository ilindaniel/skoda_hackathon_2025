package org.example.dao

import org.jetbrains.exposed.sql.Table

object Employee : Table("employee") {
    val personalNumber = varchar("personal_number", 50)
    val profession = varchar("profession", 50)

    override val primaryKey = PrimaryKey(personalNumber)
}

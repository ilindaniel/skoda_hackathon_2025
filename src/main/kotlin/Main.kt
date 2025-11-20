package org.example

import org.example.dao.CourseHistory
import org.example.dao.Employee
import org.example.dao.Repository
import org.example.parser.xlsxParser
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun main() {
    Database.connect(
        url = "jdbc:postgresql://localhost:5432/mydb",
        driver = "org.postgresql.Driver",
        user = System.getenv("DB_USER"),
        password = System.getenv("DB_PASSWORD")
    )

    transaction {
        SchemaUtils.create(Employee, CourseHistory)
    }

    val employees = xlsxParser("data/ERP_SK1.Start_month - SE.xlsx")
    Repository.insertEmployees(employees)

    val coursesHistory = xlsxParser("data/RE_VZD_STA_007.xlsx")
    Repository.insertCourses(coursesHistory)
}

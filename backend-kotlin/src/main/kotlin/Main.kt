package org.example

import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import org.example.dao.CourseHistory
import org.example.dao.Employee
import org.example.dao.Position
import org.example.dao.Repository
import org.example.dao.Skill
import org.example.parser.xlsxParser
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

fun main() {
    Database.connect(
        url = "jdbc:postgresql://localhost:5432/mydb",
        driver = "org.postgresql.Driver",
        user = System.getenv("DB_USER"),
        password = System.getenv("DB_PASSWORD")
    )
    initializeDatabase()

    embeddedServer(Netty, host = "127.0.0.1", port = 8000) {
        install(ContentNegotiation) {
            json()
        }
        routing {
            get ("/api/employees") {
                val employees = Repository.getAllEmployees()
                call.respond(employees)
            }

            get ("/api/roles") {
                val positions = Repository.getAllPositions()
                call.respond(positions)
            }
        }
    }.start(wait = true)
}

fun initializeDatabase() {
    transaction {
        SchemaUtils.create(Employee, CourseHistory, Skill, Position)

        if (Employee.selectAll().empty()) {
            val employees = xlsxParser("data/ERP_SK1.Start_month - SE.xlsx")
            Repository.insertEmployees(employees)
            Repository.insertPositions(employees)
        }

        if (CourseHistory.selectAll().empty()) {
            val coursesHistory = xlsxParser("data/RE_VZD_STA_007.xlsx")
            Repository.insertCourses(coursesHistory)
        }

        if (Skill.selectAll().empty()) {
            val skillsMapping = xlsxParser(
                path = "data/Skill_mapping.xlsx",
                sheetIndex = 7
            )
            Repository.insertSkills(skillsMapping)
        }

//        if (Qualification.selectAll().empty()) {
//            val requiredQualifications = xlsxParser("data/ZPE_KOM_KVAL.xlsx")
//            Repository.insertRequiredQualifications(requiredQualifications)
//        }
    }
}

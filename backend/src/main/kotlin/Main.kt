package org.example

import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import org.example.dao.Course
import org.example.dao.CourseHistory
import org.example.dao.CourseSkill
import org.example.dao.Employee
import org.example.dao.Position
import org.example.dao.CurrentPosition
import org.example.dao.Qualification
import org.example.dao.Repository
import org.example.dao.Skill
import org.example.dto.Difference
import org.example.parser.xlsxParser
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import tsvParser

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
            get ("/health") {
                call.respond(HttpStatusCode.OK)
            }
            get ("/api/employees") {
                val employees = Repository.getAllEmployees()
                call.respond(employees)
            }

            get ("/api/roles") {
                val positions = Repository.getAllPositions()
                call.respond(positions)
            }

            get ("/api/profile") {
                val personalNumber = call.request.queryParameters["personal_number"]
                    ?: return@get call.respondText("Missing personal_number", status = HttpStatusCode.BadRequest)
                val profile = Repository.getProfile(personalNumber)
                call.respond(profile)
            }

            get ("/api/gaps") {
                val params = call.request.queryParameters
                val personalNumber = params["personal_number"] ?: return@get call.respondText("Missing personal_number", status = HttpStatusCode.BadRequest)
                val roleId = params["role_id"]

                val someoneWithThatRole = Repository.getEmployeeWithRoleId(roleId!!)
                    ?: return@get call.respondText("Not found", status = HttpStatusCode.NotFound)
                val destinationProfile = Repository.getProfile(someoneWithThatRole)
                val profile = Repository.getProfile(personalNumber)
                val skills = profile.skills
                val destinationSkills = destinationProfile.skills
                val skillsDifference = destinationSkills.minus(skills)
                val qualifications = profile.qualifications
                val destinationQualifications = destinationProfile.qualifications
                val qualificationDifference = destinationQualifications.minus(qualifications)

                val diff = Difference(
                    employee_qualifications = qualifications,
                    missing_qualifications = qualificationDifference,
                    employee_skills = skills,
                    missing_skills = skillsDifference
                )
                call.respond(diff)
            }
        }
    }.start(wait = true)
}

fun initializeDatabase() {
    transaction {
        SchemaUtils.create(Employee, CourseSkill, Course, CourseHistory, Skill, Position, CurrentPosition, Qualification)

        if (Employee.selectAll().empty()) {
            val employees = xlsxParser("data/ERP_SK1.Start_month - SE.xlsx")
            Repository.insertEmployees(employees)
            Repository.insertPositions(employees)
        }

        if (CurrentPosition.selectAll().empty()) {
            val positions = tsvParser("data/RE_RHRHAZ00_P_S.txt")
            Repository.insertCurrentPositions(positions)
        }

        if (Skill.selectAll().empty()) {
            val skills = xlsxParser(
                path = "data/Skill_mapping.xlsx",
                sheetIndex = 6
            )
            Repository.insertSkills(skills)
        }

        if (Course.selectAll().empty()) {
            val mapping = xlsxParser(
                path = "data/Skill_mapping.xlsx",
                sheetIndex = 7
            )
            Repository.insertCourses(mapping)
        }

        if (CourseSkill.selectAll().empty()) {
            val mapping = xlsxParser(
                path = "data/Skill_mapping.xlsx",
                sheetIndex = 7
            )
            Repository.mapSkillCourse(mapping)
        }

        if (CourseHistory.selectAll().empty()) {
            val coursesHistory = xlsxParser("data/RE_VZD_STA_007.xlsx")
            Repository.insertCourseHistories(coursesHistory)
        }

        if (Qualification.selectAll().empty()) {
            val qualifications = xlsxParser("data/RE_RHRHAZ00.xlsx")
            Repository.insertQualifications(qualifications)
        }
    }
}

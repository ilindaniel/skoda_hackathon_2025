package org.example.dao

import org.example.dto.EmployeeDTO
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

object Repository {
    fun insertEmployees(rows: List<Map<String, String?>>) {
        transaction {
            Employee.batchInsert(rows) { row ->
                this[Employee.personalNumber] = row["persstat_start_month.personal_number"]!!.trimStart('0')
                this[Employee.profession] = row["persstat_start_month.profession"]!!
                this[Employee.username] = row["persstat_start_month.user_name"] ?: throw IllegalArgumentException("row: $row")
                this[Employee.plannedPositionId] = row["persstat_start_month.planned_position_id"]!!
                this[Employee.plannedPosition] = row["persstat_start_month.planned_position"]!!
            }
        }
    }

    fun getAllEmployees(): List<EmployeeDTO> = transaction {
        Employee.selectAll().map { row ->
            EmployeeDTO(
                personalNumber = row[Employee.personalNumber],
                profession = row[Employee.profession],
                username = row[Employee.username],
                plannedPositionId = row[Employee.plannedPositionId],
                plannedPosition = row[Employee.plannedPosition]
            )
        }
    }


    fun insertCourses(rows: List<Map<String, String?>>) {
        transaction {
            CourseHistory.batchInsert(rows) { row ->
                this[CourseHistory.personalNumber] = row["ID účastníka"]!!
                this[CourseHistory.objectId] = row["IDOBJ"]!!.removePrefix("E ")
            }
        }
    }

    fun insertSkills(rows: List<Map<String, String?>>) {
        transaction {
            Skill.batchInsert(rows) { row ->
                this[Skill.objectId] = row["ID objektu"]!!
                this[Skill.name] = row["Skill v EN"]
            }
        }
    }
}

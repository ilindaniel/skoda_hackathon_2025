package org.example.dao

import org.example.dto.EmployeeDTO
import org.example.dto.PositionDTO
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
                username = row[Employee.username],
                profession = row[Employee.profession],
                plannedPositionId = row[Employee.plannedPositionId],
                plannedPosition = row[Employee.plannedPosition]
            )
        }
    }

    fun insertPositions(rows: List<Map<String, String?>>) {
        val uniquePositions = rows
            .mapNotNull { row ->
                val id = row["persstat_start_month.planned_position_id"]
                val pos = row["persstat_start_month.planned_position"]
                if (id != null && pos != null) id to pos else null
            }
            .distinctBy { it.first } // ensures unique position_id

        transaction {
            Position.batchInsert(uniquePositions) { (id, pos) ->
                this[Position.id] = id
                this[Position.description] = pos
            }
        }
    }

    fun getAllPositions(): List<PositionDTO> = transaction {
        Position.selectAll()
            .map { row ->
                PositionDTO(
                    id = row[Position.id],
                    description = row[Position.description]
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

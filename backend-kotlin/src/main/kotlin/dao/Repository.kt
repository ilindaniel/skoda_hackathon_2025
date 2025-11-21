package org.example.dao

import org.example.dto.EmployeeDTO
import org.example.dto.PositionDTO
import org.example.dto.ProfileDTO
import org.jetbrains.exposed.sql.JoinType
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
                this[Employee.plannedProfession] = row["persstat_start_month.planned_profession"]!!
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
                plannedPosition = row[Employee.plannedPosition],
                plannedProfession = row[Employee.plannedProfession]
            )
        }
    }

    private fun getEmployee(personalNumber: String): EmployeeDTO? = transaction {
        Employee.selectAll().where { Employee.personalNumber eq personalNumber }
            .map { row ->
                EmployeeDTO(
                    personalNumber = row[Employee.personalNumber],
                    username = row[Employee.username],
                    profession = row[Employee.profession],
                    plannedPositionId = row[Employee.plannedPositionId],
                    plannedPosition = row[Employee.plannedPosition],
                    plannedProfession = row[Employee.plannedProfession],
                )
            }
            .singleOrNull()
    }

    private fun getEmployeeSkills(personalNumber: String): List<String> {
        return transaction {
            CourseHistory
                .join(CourseSkill, JoinType.INNER, additionalConstraint = {
                    CourseHistory.courseId eq CourseSkill.courseId
                })
                .join(Skill, JoinType.INNER, additionalConstraint = {
                    CourseSkill.skillName eq Skill.name
                })
                .select(Skill.name)
                .where { CourseHistory.personalNumber eq personalNumber }
                .mapNotNull { it[Skill.name] }
                .distinct()
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

    fun getProfile(personalNumber: String) =
        ProfileDTO(
            skills = getEmployeeSkills(personalNumber),
            qualifications = getQualifications(personalNumber),
            employeeDTO = getEmployee(personalNumber)!!
        )

//    fun insertQualifications(rows: List<Map<String, String?>>) = transaction {
//        transaction {
//            Qualification.batchInsert(rows) { row ->
//                this[Qualification.id] = row["ID kvalifikace"]!!
//                this[Qualification.description] = row["Kvalifikace"]!!
//                this[Qualification.positionId] = row["Číslo FM"]!!
//            }
//        }
//    }
    fun insertQualifications(rows: List<Map<String, String?>>) = transaction {
        transaction {
            Qualification.batchInsert(rows) { row ->
                this[Qualification.description] = row["Název Q"]!!
                this[Qualification.personalNumber] = row["ID P"]!!
            }
        }
    }

    private fun getQualifications(personalNumber: String): List<String> = transaction {
        Qualification
            .selectAll().where { Qualification.personalNumber eq personalNumber }
            .map { it[Qualification.description] }
    }

    fun insertCourseHistories(rows: List<Map<String, String?>>) {
        transaction {
            CourseHistory.batchInsert(rows) { row ->
                this[CourseHistory.personalNumber] = row["ID účastníka"]!!
                this[CourseHistory.courseId] = row["Typ akce"]!!
            }
        }
    }

    fun insertCourses(rows: List<Map<String, String?>>) {
        transaction {
            Course.batchInsert(rows, ignore = true) { row ->
                this[Course.id] = row["ID objektu"]!!
                this[Course.name] = row["Označení objektu"]!!
            }
        }
    }


    fun insertSkills(rows: List<Map<String, String?>>) {
        transaction {
            Skill.batchInsert(rows, ignore = true) { row ->
                this[Skill.id] = row["Skill ID"]!!
                this[Skill.name] = row["Skill (EN)"]
            }
        }
    }

    fun mapSkillCourse(rows: List<Map<String, String?>>) {
        transaction {
            CourseSkill.batchInsert(rows) { row ->
                this[CourseSkill.courseId] = row["ID objektu"]!!
                this[CourseSkill.skillName] = row["Skill v EN"]
            }
        }
    }

    fun insertCurrentPositions(positions: List<Pair<String, String>>) {
        transaction {
            CurrentPosition.batchInsert(positions) { position ->
                this[CurrentPosition.personalNumber] = position.first
                this[CurrentPosition.positionId] = position.second
            }
        }
    }
}

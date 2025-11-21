package org.example.dao

import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.transactions.transaction

object Repository {
    fun insertEmployees(rows: List<Map<String, String?>>) {
        transaction {
            Employee.batchInsert(rows) { row ->
                this[Employee.personalNumber] = row["persstat_start_month.personal_number"]!!.trimStart('0')
                this[Employee.profession] = row["persstat_start_month.profession"]!!
            }
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

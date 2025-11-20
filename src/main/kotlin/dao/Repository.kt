package org.example.dao

import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.transactions.transaction

object Repository {
    fun insertEmployees(rows: List<Map<String, String?>>) {
        transaction {
            Employee.batchInsert(rows) { row ->
                this[Employee.personalNumber] = row["persstat_start_month.personal_number"]!!.trimStart('0')
            }
        }
    }
}

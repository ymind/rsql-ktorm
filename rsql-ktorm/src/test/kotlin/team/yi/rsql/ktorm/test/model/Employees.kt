package team.yi.rsql.ktorm.test.model

import java.time.LocalDateTime
import org.ktorm.entity.Entity
import org.ktorm.schema.*

@Suppress("unused")
open class Employees(alias: String?) : Table<Employee>("t_employee", alias) {
    companion object : Employees(null)

    override fun aliased(alias: String) = Employees(alias)

    val id = int("id").primaryKey().bindTo { it.id }
    val name = varchar("name").bindTo { it.name }
    val job = varchar("job").bindTo { it.job }
    val managerId = int("manager_id").bindTo { it.manager?.id }
    val hireDate = datetime("hire_date").bindTo { it.hireDate }
    val salary = long("salary").bindTo { it.salary }
    val departmentId = int("department_id").references(Departments) { it.department }
    val department = departmentId.referenceTable as Departments
}

interface Employee : Entity<Employee> {
    companion object : Entity.Factory<Employee>()

    var id: Int?
    var name: String
    var job: String
    var manager: Employee?
    var hireDate: LocalDateTime?
    var salary: Long
    var department: Department
}

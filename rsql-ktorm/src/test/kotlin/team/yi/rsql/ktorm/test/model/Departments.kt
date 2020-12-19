package team.yi.rsql.ktorm.test.model

import java.io.Serializable
import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

@Suppress("unused")
open class Departments(alias: String?) : Table<Department>("t_department", alias) {
    companion object : Departments(null)

    override fun aliased(alias: String) = Departments(alias)

    val id = int("id").primaryKey().bindTo { it.id }
    val name = varchar("name").bindTo { it.name }
    val location = varchar("location").transform({ LocationWrapper(it) }, { it.underlying }).bindTo { it.location }
    val mixedCase = varchar("mixedCase").bindTo { it.mixedCase }
}

interface Department : Entity<Department> {
    companion object : Entity.Factory<Department>()

    val id: Int
    var name: String
    var location: LocationWrapper
    var mixedCase: String?
}

data class LocationWrapper(val underlying: String = "") : Serializable

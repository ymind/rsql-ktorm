package team.yi.rsql.ktorm.converter

import org.ktorm.schema.BaseTable
import java.util.*

class UuidValueConverter : FieldValueConverter {
    override fun convert(arguments: Collection<String>, sourceTable: BaseTable<*>): List<Any?> {
        return arguments.map {
            UUID.fromString(it)
        }
    }
}

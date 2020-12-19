package team.yi.rsql.ktorm.converter

import org.ktorm.schema.BaseTable
import java.time.Instant

class InstantValueConverter : FieldValueConverter {
    override fun convert(arguments: Collection<String>, sourceTable: BaseTable<*>): List<Any?> {
        return arguments.map {
            Instant.parse(it)
        }
    }
}

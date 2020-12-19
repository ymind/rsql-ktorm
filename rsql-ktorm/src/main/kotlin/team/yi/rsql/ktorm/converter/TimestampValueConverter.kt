package team.yi.rsql.ktorm.converter

import org.ktorm.schema.BaseTable
import java.sql.Timestamp

class TimestampValueConverter : FieldValueConverter {
    override fun convert(arguments: Collection<String>, sourceTable: BaseTable<*>): List<Any?> {
        return arguments.map {
            Timestamp.valueOf(it)
        }
    }
}

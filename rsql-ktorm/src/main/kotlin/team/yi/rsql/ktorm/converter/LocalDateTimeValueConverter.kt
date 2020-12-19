package team.yi.rsql.ktorm.converter

import java.time.LocalDateTime
import java.time.ZoneId
import org.ktorm.schema.BaseTable
import team.yi.rsql.ktorm.util.DateUtil

class LocalDateTimeValueConverter : FieldValueConverter {
    override fun convert(arguments: Collection<String>, sourceTable: BaseTable<*>): List<Any?> {
        return arguments.map {
            DateUtil.parse(it)?.let { date ->
                LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault())
            }
        }
    }
}

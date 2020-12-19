package team.yi.rsql.ktorm.converter

import org.ktorm.schema.BaseTable
import java.time.Year

class YearValueConverter : FieldValueConverter {
    override fun convert(arguments: Collection<String>, sourceTable: BaseTable<*>): List<Any?> {
        return arguments.map {
            Year.parse(it)
        }
    }
}

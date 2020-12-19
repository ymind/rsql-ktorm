package team.yi.rsql.ktorm.converter

import org.ktorm.schema.BaseTable
import team.yi.rsql.ktorm.util.DateUtil

class DateValueConverter : FieldValueConverter {
    override fun convert(arguments: Collection<String>, sourceTable: BaseTable<*>): List<Any?> {
        return arguments.map {
            DateUtil.parse(it)
        }
    }
}

package team.yi.rsql.ktorm.converter

import org.ktorm.schema.BaseTable
import team.yi.rsql.ktorm.util.BooleanUtil

class BooleanValueConverter : FieldValueConverter {
    override fun convert(arguments: Collection<String>, sourceTable: BaseTable<*>): List<Any?> {
        return arguments.map {
            BooleanUtil.parse(it, false)
        }
    }
}

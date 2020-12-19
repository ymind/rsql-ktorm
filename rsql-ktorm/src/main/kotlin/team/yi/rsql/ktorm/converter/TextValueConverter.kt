package team.yi.rsql.ktorm.converter

import org.ktorm.schema.BaseTable

open class TextValueConverter : FieldValueConverter {
    override fun convert(arguments: Collection<String>, sourceTable: BaseTable<*>): List<Any?> {
        return arguments.toList()
    }
}

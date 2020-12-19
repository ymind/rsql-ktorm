package team.yi.rsql.ktorm.converter

import org.ktorm.schema.BaseTable

interface FieldValueConverter {
    fun convert(arguments: Collection<String>, sourceTable: BaseTable<*>): List<Any?>
}

package team.yi.rsql.ktorm.converter

import org.ktorm.schema.BaseTable

abstract class NumberValueConverter<N : Number> : FieldValueConverter {
    override fun convert(arguments: Collection<String>, sourceTable: BaseTable<*>): List<Any?> {
        return arguments.map {
            toNumber(it)
        }
    }

    protected abstract fun toNumber(s: String): N?
}

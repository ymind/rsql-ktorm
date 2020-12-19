package team.yi.rsql.ktorm.converter

import org.ktorm.schema.BaseTable
import java.util.*

open class BytesValueConverter : FieldValueConverter {
    override fun convert(arguments: Collection<String>, sourceTable: BaseTable<*>): List<Any?> {
        return arguments.map {
            Base64.getDecoder().decode(it)
        }
    }
}

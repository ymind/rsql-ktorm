package team.yi.rsql.ktorm.converter

import org.ktorm.schema.BaseTable

class EnumValueConverter<C : Enum<C>>(private val enumClass: Class<C>) : FieldValueConverter {
    private val valueOf = enumClass.getDeclaredMethod("valueOf", String::class.java)

    override fun convert(arguments: Collection<String>, sourceTable: BaseTable<*>): List<Any?> {
        return arguments.map {
            enumClass.cast(valueOf(null, it))
        }
    }
}

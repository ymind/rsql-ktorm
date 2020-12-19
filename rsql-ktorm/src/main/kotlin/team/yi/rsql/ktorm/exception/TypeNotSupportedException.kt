package team.yi.rsql.ktorm.exception

import org.ktorm.schema.SqlType

@Suppress("unused")
class TypeNotSupportedException(
    message: String,
    val fieldSelector: String?,
    val sqlType: SqlType<Any>,
) : RsqlException(message) {
    companion object {
        private const val serialVersionUID = 5993677606796379281L
    }
}

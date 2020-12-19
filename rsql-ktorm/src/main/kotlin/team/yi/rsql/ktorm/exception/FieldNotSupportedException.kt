package team.yi.rsql.ktorm.exception

@Suppress("unused")
class FieldNotSupportedException(
    message: String,
    val fieldSelector: String?,
) : RsqlException(message) {
    companion object {
        private const val serialVersionUID = 5993677606796379281L
    }
}

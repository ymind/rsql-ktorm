package team.yi.rsql.ktorm.exception

@Suppress("unused")
open class RsqlException : RuntimeException {
    constructor(cause: Throwable) : super(cause)
    constructor(message: String, cause: Throwable) : super(message, cause)
    constructor(message: String) : super(message)

    companion object {
        private const val serialVersionUID = -6636177131093201908L
    }
}

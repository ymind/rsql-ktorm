package team.yi.rsql.ktorm

@Suppress("MemberVisibilityCanBePrivate", "unused")
class RsqlOperator(
    val symbols: Array<String>,
    val multiValue: Boolean,
) {
    val symbol: String = symbols[0]

    override fun hashCode(): Int = symbols.contentHashCode()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true

        return (other as? RsqlOperator)?.symbols?.intersect(arrayListOf(*symbols))?.isNotEmpty() ?: false
    }
}

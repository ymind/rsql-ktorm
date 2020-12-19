package team.yi.rsql.ktorm.adapter

import cz.jirutka.rsql.parser.ast.ComparisonNode
import cz.jirutka.rsql.parser.ast.ComparisonOperator
import org.ktorm.dsl.like
import org.ktorm.dsl.notLike
import org.ktorm.expression.FunctionExpression
import org.ktorm.expression.ScalarExpression
import org.ktorm.schema.ColumnDeclaring
import org.ktorm.schema.VarcharSqlType
import team.yi.rsql.ktorm.RsqlOperator

open class LikeOperatorAdapter : AbstractOperatorAdapter(
    "LIKE",
    "=like="
) {
    protected open val notEq: Boolean = false
    protected open val ignoreCase: Boolean = false

    override fun getExpression(
        rsqlOperator: RsqlOperator,
        left: ColumnDeclaring<Any>,
        right: List<Any?>,
        node: ComparisonNode
    ): ScalarExpression<Boolean>? {
        if (right.size > 1) throw RuntimeException(name)

        val value = right.firstOrNull()?.toString() ?: throw RuntimeException(name)

        return like(left, ensure(value))
    }

    protected open fun ensure(value: String): String = value

    private fun like(left: ColumnDeclaring<Any>, value: String): ScalarExpression<Boolean> {
        if (value.isBlank()) throw RuntimeException("like")

        if (ignoreCase) {
            val function = FunctionExpression(
                functionName = "UPPER",
                arguments = listOf(left.asExpression()),
                sqlType = VarcharSqlType
            )

            return if (notEq) function.notLike(value.toUpperCase()) else function.like(value.toUpperCase())
        }

        return if (notEq) left.notLike(value) else left.like(value)
    }
}

@Suppress("SpellCheckingInspection")
class LikeIgnorecaseOperatorAdapter : LikeOperatorAdapter() {
    override val name: String = "LIKE_IGNORECASE"
    override val comparisonOperator: ComparisonOperator = ComparisonOperator(
        "=ilike=",
        "=likeignorecase=",
        "=likeIgnoreCase=",
        "=likeIgnorecase="
    )

    override val notEq: Boolean = false
    override val ignoreCase: Boolean = true
}

class NotLikeOperatorAdapter : LikeOperatorAdapter() {
    override val name: String = "NOT_LIKE"

    @Suppress("SpellCheckingInspection")
    override val comparisonOperator: ComparisonOperator = ComparisonOperator(
        "=notlike=",
        "=notLike="
    )

    override val notEq: Boolean = true
    override val ignoreCase: Boolean = false
}

@Suppress("SpellCheckingInspection")
class NotLikeIgnorecaseOperatorAdapter : LikeOperatorAdapter() {
    override val name: String = "NOT_LIKE_IGNORECASE"
    override val comparisonOperator: ComparisonOperator = ComparisonOperator(
        "=notilike=",
        "=notlikeignorecase=",
        "=notLikeIgnoreCase=",
        "=notLikeIgnorecase="
    )

    override val notEq: Boolean = true
    override val ignoreCase: Boolean = true
}

class StartsWithOperatorAdapter : LikeOperatorAdapter() {
    override val name: String = "STARTS_WITH"
    override val comparisonOperator: ComparisonOperator = ComparisonOperator(
        "=sw=",
        "=startswith=",
        "=startsWith="
    )

    override val notEq: Boolean = false
    override val ignoreCase: Boolean = false

    override fun ensure(value: String): String = "$value%"
}

@Suppress("SpellCheckingInspection")
class StartsWithIgnorecaseOperatorAdapter : LikeOperatorAdapter() {
    override val name: String = "STARTS_WITH_IGNORECASE"
    override val comparisonOperator: ComparisonOperator = ComparisonOperator(
        "=isw=",
        "=startswithignorecase=",
        "=startsWithIgnoreCase=",
        "=startsWithIgnorecase="
    )

    override val notEq: Boolean = false
    override val ignoreCase: Boolean = true

    override fun ensure(value: String): String = "$value%"
}

class NotStartsWithOperatorAdapter : LikeOperatorAdapter() {
    override val name: String = "NOT_STARTS_WITH"

    @Suppress("SpellCheckingInspection")
    override val comparisonOperator: ComparisonOperator = ComparisonOperator(
        "=notsw=",
        "=notstartswith=",
        "=notStartsWith="
    )

    override val notEq: Boolean = true
    override val ignoreCase: Boolean = false

    override fun ensure(value: String): String = "$value%"
}

@Suppress("SpellCheckingInspection")
class NotStartsWithIgnorecaseOperatorAdapter : LikeOperatorAdapter() {
    override val name: String = "NOT_STARTS_WITH_IGNORECASE"
    override val comparisonOperator: ComparisonOperator = ComparisonOperator(
        "=inotsw=",
        "=notstartswithignorecase=",
        "=notStartsWithIgnoreCase=",
        "=notStartsWithIgnorecase="
    )

    override val notEq: Boolean = true
    override val ignoreCase: Boolean = true

    override fun ensure(value: String): String = "$value%"
}

class EndsWithOperatorAdapter : LikeOperatorAdapter() {
    override val name: String = "ENDS_WITH"
    override val comparisonOperator: ComparisonOperator = ComparisonOperator(
        "=ew=",
        "=endswith=",
        "=endsWith="
    )

    override val notEq: Boolean = false
    override val ignoreCase: Boolean = false

    override fun ensure(value: String): String = "%$value"
}

@Suppress("SpellCheckingInspection")
class EndsWithIgnorecaseOperatorAdapter : LikeOperatorAdapter() {
    override val name: String = "ENDS_WITH_IGNORECASE"
    override val comparisonOperator: ComparisonOperator = ComparisonOperator(
        "=iew=",
        "=endswithignorecase=",
        "=endsWithIgnoreCase=",
        "=endsWithIgnorecase="
    )

    override val notEq: Boolean = false
    override val ignoreCase: Boolean = true

    override fun ensure(value: String): String = "%$value"
}

class NotEndsWithOperatorAdapter : LikeOperatorAdapter() {
    override val name: String = "NOT_ENDS_WITH"

    @Suppress("SpellCheckingInspection")
    override val comparisonOperator: ComparisonOperator = ComparisonOperator(
        "=notew=",
        "=notendswith=",
        "=notEndsWith="
    )

    override val notEq: Boolean = true
    override val ignoreCase: Boolean = false

    override fun ensure(value: String): String = "%$value"
}

@Suppress("SpellCheckingInspection")
class NotEndsWithIgnorecaseOperatorAdapter : LikeOperatorAdapter() {
    override val name: String = "NOT_ENDS_WITH_IGNORECASE"
    override val comparisonOperator: ComparisonOperator = ComparisonOperator(
        "=inotew=",
        "=notendswithignorecase=",
        "=notEndsWithIgnoreCase=",
        "=notEndsWithIgnorecase="
    )

    override val notEq: Boolean = true
    override val ignoreCase: Boolean = true

    override fun ensure(value: String): String = "%$value"
}

class ContainsOperatorAdapter : LikeOperatorAdapter() {
    override val name: String = "CONTAINS"
    override val comparisonOperator: ComparisonOperator = ComparisonOperator(
        "=con=",
        "=contains="
    )

    override val notEq: Boolean = false
    override val ignoreCase: Boolean = false

    override fun ensure(value: String): String = "%$value%"
}

@Suppress("SpellCheckingInspection")
class ContainsIgnorecaseOperatorAdapter : LikeOperatorAdapter() {
    override val name: String = "CONTAINS_IGNORECASE"
    override val comparisonOperator: ComparisonOperator = ComparisonOperator(
        "=icon=",
        "=containsignorecase=",
        "=containsIgnoreCase=",
        "=containsIgnorecase="
    )

    override val notEq: Boolean = false
    override val ignoreCase: Boolean = true

    override fun ensure(value: String): String = "%$value%"
}

class NotContainsOperatorAdapter : LikeOperatorAdapter() {
    override val name: String = "NOT_CONTAINS"

    @Suppress("SpellCheckingInspection")
    override val comparisonOperator: ComparisonOperator = ComparisonOperator(
        "=notcon=",
        "=notcontains=",
        "=notContains="
    )

    override val notEq: Boolean = true
    override val ignoreCase: Boolean = false

    override fun ensure(value: String): String = "%$value%"
}

@Suppress("SpellCheckingInspection")
class NotContainsIgnorecaseOperatorAdapter : LikeOperatorAdapter() {
    override val name: String = "NOT_CONTAINS_IGNORECASE"
    override val comparisonOperator: ComparisonOperator = ComparisonOperator(
        "=inotcon=",
        "=notContainsignorecase=",
        "=notContainsIgnoreCase=",
        "=notContainsIgnorecase="
    )

    override val notEq: Boolean = true
    override val ignoreCase: Boolean = true

    override fun ensure(value: String): String = "%$value%"
}

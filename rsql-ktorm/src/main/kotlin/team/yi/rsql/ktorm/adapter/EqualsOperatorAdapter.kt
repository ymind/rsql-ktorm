package team.yi.rsql.ktorm.adapter

import cz.jirutka.rsql.parser.ast.ComparisonNode
import cz.jirutka.rsql.parser.ast.ComparisonOperator
import org.ktorm.dsl.eq
import org.ktorm.dsl.notEq
import org.ktorm.expression.FunctionExpression
import org.ktorm.expression.ScalarExpression
import org.ktorm.schema.ColumnDeclaring
import org.ktorm.schema.VarcharSqlType
import team.yi.rsql.ktorm.RsqlOperator

class EqualsOperatorAdapter : AbstractOperatorAdapter(
    "EQUALS",
    "=eq=",
    "=="
) {
    override fun getExpression(
        rsqlOperator: RsqlOperator,
        left: ColumnDeclaring<Any>,
        right: List<Any?>,
        node: ComparisonNode
    ): ScalarExpression<Boolean>? {
        if (right.size > 1) throw RuntimeException(name)

        return left.eq(right.firstOrNull() ?: return null)
    }
}

@Suppress("SpellCheckingInspection")
class NotEqualsOperatorAdapter : AbstractOperatorAdapter(
    "NOT_EQUALS",
    "=ne=",
    "=noteq=",
    "=notEq=",
    "!="
) {
    override fun getExpression(
        rsqlOperator: RsqlOperator,
        left: ColumnDeclaring<Any>,
        right: List<Any?>,
        node: ComparisonNode
    ): ScalarExpression<Boolean>? {
        if (right.size > 1) throw RuntimeException(name)

        return left.notEq(right.firstOrNull() ?: return null)
    }
}

@Suppress("SpellCheckingInspection")
open class EqualsIgnoreCaseOperatorAdapter : AbstractOperatorAdapter(
    "EQUALS_IGNORECASE",
    "=ieq=",
    "=equalsignorecase=",
    "=equalsIgnoreCase=",
    "=equalsIgnorecase="
) {
    protected open val notEq: Boolean = false

    override fun getExpression(
        rsqlOperator: RsqlOperator,
        left: ColumnDeclaring<Any>,
        right: List<Any?>,
        node: ComparisonNode
    ): ScalarExpression<Boolean>? {
        if (right.size > 1) throw RuntimeException(name)

        val function = FunctionExpression(
            functionName = "UPPER",
            arguments = listOf(left.asExpression()),
            sqlType = VarcharSqlType
        )
        val value = right.firstOrNull()?.toString()?.toUpperCase() ?: throw RuntimeException("equalsIgnoreCase")

        return if (notEq) function.notEq(value) else function.eq(value)
    }
}

class NotEqualsIgnoreCaseOperatorAdapter : EqualsIgnoreCaseOperatorAdapter() {
    @Suppress("SpellCheckingInspection")
    override val name: String = "NOT_EQUALS_IGNORECASE"

    @Suppress("SpellCheckingInspection")
    override val comparisonOperator: ComparisonOperator = ComparisonOperator(
        "=ine=",
        "=notequalsignorecase=",
        "=notEqualsIgnoreCase=",
        "=notEqualsIgnorecase="
    )

    override val notEq: Boolean = true
}

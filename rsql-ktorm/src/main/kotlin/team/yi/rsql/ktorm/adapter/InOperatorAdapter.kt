package team.yi.rsql.ktorm.adapter

import cz.jirutka.rsql.parser.ast.ComparisonNode
import cz.jirutka.rsql.parser.ast.ComparisonOperator
import org.ktorm.expression.InListExpression
import org.ktorm.expression.ScalarExpression
import org.ktorm.schema.ColumnDeclaring
import team.yi.rsql.ktorm.RsqlOperator

open class InOperatorAdapter : AbstractOperatorAdapter(
    "IN",
    "=in=",
    multiValue = true,
) {
    protected open val notInList: Boolean = false

    override fun getExpression(
        rsqlOperator: RsqlOperator,
        left: ColumnDeclaring<Any>,
        right: List<Any?>,
        node: ComparisonNode
    ): ScalarExpression<Boolean> {
        if (right.isEmpty()) throw RuntimeException("in")

        val values = right.map { left.wrapArgument(it) }.distinctBy { it.value }

        return InListExpression(left = left.asExpression(), values = values, notInList = notInList)
    }
}

class NotInOperatorAdapter : InOperatorAdapter() {
    override val name: String = "NOT_IN"

    @Suppress("SpellCheckingInspection")
    override val comparisonOperator: ComparisonOperator = ComparisonOperator(
        "=notin=",
        "=notIn=",
        "=out="
    )

    override val notInList: Boolean = true
}

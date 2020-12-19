package team.yi.rsql.ktorm.adapter

import cz.jirutka.rsql.parser.ast.ComparisonNode
import cz.jirutka.rsql.parser.ast.ComparisonOperator
import org.ktorm.expression.BetweenExpression
import org.ktorm.expression.ScalarExpression
import org.ktorm.schema.ColumnDeclaring
import team.yi.rsql.ktorm.RsqlOperator

open class BetweenOperatorAdapter : AbstractOperatorAdapter(
    "BETWEEN",
    "=between=",
    multiValue = true,
) {
    protected open val notBetween: Boolean = false

    @Suppress("UNCHECKED_CAST")
    override fun getExpression(
        rsqlOperator: RsqlOperator,
        left: ColumnDeclaring<Any>,
        right: List<Any?>,
        node: ComparisonNode
    ): ScalarExpression<Boolean> {
        if (right.size != 2) throw RuntimeException("between")

        val column = left as ColumnDeclaring<Comparable<Any>>
        val lower = right[0] as Comparable<Any>
        val upper = right[1] as Comparable<Any>

        return BetweenExpression(
            expression = column.asExpression(),
            lower = column.wrapArgument(lower),
            upper = column.wrapArgument(upper),
            notBetween = notBetween
        ).asExpression()
    }
}

open class NotBetweenOperatorAdapter : BetweenOperatorAdapter() {
    override val name: String = "NOT_BETWEEN"

    @Suppress("SpellCheckingInspection")
    override val comparisonOperator: ComparisonOperator = ComparisonOperator(
        "=notbetween=",
        "=notBetween=",
    )

    override val notBetween: Boolean = true
}

package team.yi.rsql.ktorm.adapter

import cz.jirutka.rsql.parser.ast.ComparisonNode
import cz.jirutka.rsql.parser.ast.ComparisonOperator
import org.ktorm.expression.ScalarExpression
import org.ktorm.schema.ColumnDeclaring
import team.yi.rsql.ktorm.RsqlOperator

interface OperatorAdapter {
    val name: String
    val comparisonOperator: ComparisonOperator

    fun supports(node: ComparisonNode, rsqlOperator: RsqlOperator): Boolean

    fun getExpression(
        rsqlOperator: RsqlOperator,
        left: ColumnDeclaring<Any>,
        right: List<Any?>,
        node: ComparisonNode,
    ): ScalarExpression<Boolean>?
}

abstract class AbstractOperatorAdapter(
    override val name: String,
    private vararg val symbols: String,
    private val multiValue: Boolean = false,
) : OperatorAdapter {
    override val comparisonOperator: ComparisonOperator
        get() = ComparisonOperator(symbols, multiValue)

    override fun supports(node: ComparisonNode, rsqlOperator: RsqlOperator): Boolean {
        return comparisonOperator.symbols.any { it == rsqlOperator.symbol }
    }
}

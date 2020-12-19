package team.yi.rsql.ktorm

import cz.jirutka.rsql.parser.ast.*
import org.ktorm.expression.BinaryExpression
import org.ktorm.expression.BinaryExpressionType
import org.ktorm.expression.ScalarExpression
import org.ktorm.schema.BooleanSqlType
import org.ktorm.schema.ColumnDeclaring

internal class RsqlBuilderVisitor(
    private val rsqlBuilder: RsqlBuilder,
) : RSQLVisitor<ColumnDeclaring<Boolean>, ScalarExpression<*>> {
    override fun visit(node: AndNode, param: ScalarExpression<*>?): ColumnDeclaring<Boolean> {
        return logicalExpression(node, param, true)
    }

    override fun visit(node: OrNode, param: ScalarExpression<*>?): ColumnDeclaring<Boolean> {
        return logicalExpression(node, param, false)
    }

    override fun visit(node: ComparisonNode, param: ScalarExpression<*>?): ColumnDeclaring<Boolean>? {
        val rsqlOperator = RsqlOperator(node.operator.symbols, node.operator.isMultiValue)

        return rsqlBuilder.getExpression(node, rsqlOperator)
    }

    private fun logicalExpression(node: LogicalNode, param: ScalarExpression<*>?, isAndOperation: Boolean): ColumnDeclaring<Boolean> {
        val children = node.children.toMutableList()
        val firstNode = children.removeAt(0)
        var predicate = firstNode.accept(this, param) as ColumnDeclaring<Boolean>

        for (subNode in children) {
            val subPredicate = subNode.accept(this, param) as ScalarExpression<*>

            predicate = combineLogicalExpression(isAndOperation, predicate, subPredicate)
        }

        return predicate
    }

    private fun combineLogicalExpression(isAndOperation: Boolean, left: ColumnDeclaring<Boolean>, right: ScalarExpression<*>): BinaryExpression<Boolean> {
        val binaryExpressionType = if (isAndOperation) BinaryExpressionType.AND else BinaryExpressionType.OR

        return BinaryExpression(binaryExpressionType, left.asExpression(), right, BooleanSqlType)
    }
}

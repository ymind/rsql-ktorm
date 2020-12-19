package team.yi.rsql.ktorm.adapter

import cz.jirutka.rsql.parser.ast.ComparisonNode
import org.ktorm.dsl.*
import org.ktorm.expression.ScalarExpression
import org.ktorm.schema.ColumnDeclaring
import team.yi.rsql.ktorm.RsqlOperator

class IsNullOperatorAdapter : AbstractOperatorAdapter("IS_NULL", "=null=", "=isnull=", "=isNull=") {
    override fun getExpression(
        rsqlOperator: RsqlOperator,
        left: ColumnDeclaring<Any>,
        right: List<Any?>,
        node: ComparisonNode
    ): ScalarExpression<Boolean> {
        if (right.size > 1) throw RuntimeException(name)

        return left.isNull()
    }
}

@Suppress("SpellCheckingInspection")
class IsNotNullOperatorAdapter : AbstractOperatorAdapter(
    "IS_NOT_NULL",
    "=notnull=",
    "=notNull=",
    "=notisnull=",
    "=notIsNull=",
    "=isNotNull="
) {
    override fun getExpression(
        rsqlOperator: RsqlOperator,
        left: ColumnDeclaring<Any>,
        right: List<Any?>,
        node: ComparisonNode
    ): ScalarExpression<Boolean> {
        if (right.size > 1) throw RuntimeException(name)

        return left.isNotNull()
    }
}

open class IsEmptyOperatorAdapter : AbstractOperatorAdapter(
    "IS_EMPTY",
    "=empty=",
    "=isempty=",
    "=isEmpty=",
) {
    override fun getExpression(
        rsqlOperator: RsqlOperator,
        left: ColumnDeclaring<Any>,
        right: List<Any?>,
        node: ComparisonNode
    ): ScalarExpression<Boolean> {
        if (right.size != 1) throw RuntimeException(name)

        return left.eq("")
    }
}

@Suppress("SpellCheckingInspection")
class IsNotEmptyOperatorAdapter : AbstractOperatorAdapter(
    "IS_NOT_EMPTY",
    "=notempty=",
    "=notEmpty=",
    "=notisempty=",
    "=notIsEmpty=",
) {
    override fun getExpression(
        rsqlOperator: RsqlOperator,
        left: ColumnDeclaring<Any>,
        right: List<Any?>,
        node: ComparisonNode
    ): ScalarExpression<Boolean> {
        if (right.size != 1) throw RuntimeException(name)

        return left.notEq("")
    }
}

@Suppress("SpellCheckingInspection")
open class IsNullOrEmptyOperatorAdapter : AbstractOperatorAdapter(
    "IS_NULL_OR_EMPTY",
    "=nullorempty=",
    "=isnullorempty=",
    "=isNullOrEmpty="
) {
    override fun getExpression(
        rsqlOperator: RsqlOperator,
        left: ColumnDeclaring<Any>,
        right: List<Any?>,
        node: ComparisonNode
    ): ScalarExpression<Boolean> {
        if (right.size != 1) throw RuntimeException(name)

        return left.isNull().or(left.eq(""))
    }
}

@Suppress("SpellCheckingInspection")
class NotIsNullOrEmptyOperatorAdapter : AbstractOperatorAdapter(
    "NOT_IS_NULL_OR_EMPTY",
    "=notnullorempty=",
    "=notisnullorempty=",
    "=notIsNullOrEmpty=",
) {
    override fun getExpression(
        rsqlOperator: RsqlOperator,
        left: ColumnDeclaring<Any>,
        right: List<Any?>,
        node: ComparisonNode
    ): ScalarExpression<Boolean> {
        if (right.size != 1) throw RuntimeException(name)

        return left.isNotNull().and(left.notEq(""))
    }
}

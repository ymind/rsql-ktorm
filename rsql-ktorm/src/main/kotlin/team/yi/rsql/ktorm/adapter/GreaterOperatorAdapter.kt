package team.yi.rsql.ktorm.adapter

import cz.jirutka.rsql.parser.ast.ComparisonNode
import cz.jirutka.rsql.parser.ast.ComparisonOperator
import org.ktorm.dsl.greater
import org.ktorm.dsl.greaterEq
import org.ktorm.dsl.less
import org.ktorm.dsl.lessEq
import org.ktorm.expression.ScalarExpression
import org.ktorm.schema.ColumnDeclaring
import team.yi.rsql.ktorm.RsqlOperator

open class GreaterOperatorAdapter : AbstractOperatorAdapter(
    "GREATER",
    "=gt=",
    ">",
    "=greater=",
) {
    @Suppress("UNCHECKED_CAST")
    override fun getExpression(
        rsqlOperator: RsqlOperator,
        left: ColumnDeclaring<Any>,
        right: List<Any?>,
        node: ComparisonNode
    ): ScalarExpression<Boolean> {
        if (right.size != 1) throw RuntimeException(name)

        val column = left as ColumnDeclaring<Comparable<Any>>
        val value = right[0] as Comparable<Any>

        return column.greater(value)
    }
}

@Suppress("SpellCheckingInspection")
open class GreaterOrEqualsOperatorAdapter : AbstractOperatorAdapter(
    "GREATER_OR_EQUALS",
    "=gte=",
    "=ge=",
    ">=",
    "=greaterorequals=",
    "=greaterOrEquals=",
) {
    @Suppress("UNCHECKED_CAST")
    override fun getExpression(
        rsqlOperator: RsqlOperator,
        left: ColumnDeclaring<Any>,
        right: List<Any?>,
        node: ComparisonNode
    ): ScalarExpression<Boolean> {
        if (right.size != 1) throw RuntimeException(name)

        val column = left as ColumnDeclaring<Comparable<Any>>
        val value = right[0] as Comparable<Any>

        return column.greaterEq(value)
    }
}

open class NotGreaterOperatorAdapter : LessOrEqualsOperatorAdapter() {
    override val name: String = "NOT_GREATER"

    @Suppress("SpellCheckingInspection")
    override val comparisonOperator: ComparisonOperator = ComparisonOperator(
        "=notgt=",
        "=notgreater=",
        "=notGreater=",
    )
}

open class NotGreaterOrEqualsOperatorAdapter : LessOperatorAdapter() {
    override val name: String = "NOT_GREATER_OR_EQUALS"

    @Suppress("SpellCheckingInspection")
    override val comparisonOperator: ComparisonOperator = ComparisonOperator(
        "=notgte=",
        "=notge=",
        "=notgreaterorequals=",
        "=notGreaterOrEquals=",
    )
}

open class LessOperatorAdapter : AbstractOperatorAdapter(
    "LESS",
    "=lt=",
    "<",
    "=less=",
) {
    @Suppress("UNCHECKED_CAST")
    override fun getExpression(
        rsqlOperator: RsqlOperator,
        left: ColumnDeclaring<Any>,
        right: List<Any?>,
        node: ComparisonNode
    ): ScalarExpression<Boolean>? {
        if (right.size != 1) throw RuntimeException(name)

        val column = left as ColumnDeclaring<Comparable<Any>>
        val value = right[0] as Comparable<Any>

        return column.less(value)
    }
}

@Suppress("SpellCheckingInspection")
open class LessOrEqualsOperatorAdapter : AbstractOperatorAdapter(
    "LESS_OR_EQUALS",
    "=lte=",
    "=le=",
    "<=",
    "=lessorequals=",
    "=lessOrEquals=",
) {
    @Suppress("UNCHECKED_CAST")
    override fun getExpression(
        rsqlOperator: RsqlOperator,
        left: ColumnDeclaring<Any>,
        right: List<Any?>,
        node: ComparisonNode
    ): ScalarExpression<Boolean>? {
        if (right.size != 1) throw RuntimeException(name)

        val column = left as ColumnDeclaring<Comparable<Any>>
        val value = right[0] as Comparable<Any>

        return column.lessEq(value)
    }
}

open class NotLessOperatorAdapter : GreaterOrEqualsOperatorAdapter() {
    override val name: String = "NOT_LESS"

    @Suppress("SpellCheckingInspection")
    override val comparisonOperator: ComparisonOperator = ComparisonOperator(
        "=notlt=",
        "=notless=",
        "=notLess=",
    )
}

open class NotLessOrEqualsOperatorAdapter : GreaterOperatorAdapter() {
    override val name: String = "NOT_LESS_OR_EQUALS"

    @Suppress("SpellCheckingInspection")
    override val comparisonOperator: ComparisonOperator = ComparisonOperator(
        "=notlte=",
        "=notle=",
        "=notlessorequals=",
        "=notLessOrEquals=",
    )
}

open class BeforeOperatorAdapter : LessOperatorAdapter() {
    override val name: String = "BEFORE"
    override val comparisonOperator: ComparisonOperator = ComparisonOperator(
        "=before=",
    )
}

open class NotBeforeOperatorAdapter : GreaterOrEqualsOperatorAdapter() {
    override val name: String = "NOT_BEFORE"

    @Suppress("SpellCheckingInspection")
    override val comparisonOperator: ComparisonOperator = ComparisonOperator(
        "=notbefore=",
        "=notBefore=",
    )
}

open class AfterOperatorAdapter : GreaterOperatorAdapter() {
    override val name: String = "AFTER"
    override val comparisonOperator: ComparisonOperator = ComparisonOperator(
        "=after=",
    )
}

open class NotAfterOperatorAdapter : LessOrEqualsOperatorAdapter() {
    override val name: String = "NOT_AFTER"

    @Suppress("SpellCheckingInspection")
    override val comparisonOperator: ComparisonOperator = ComparisonOperator(
        "=notafter=",
        "=notAfter=",
    )
}

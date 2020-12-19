package team.yi.rsql.ktorm

import cz.jirutka.rsql.parser.ast.ComparisonNode
import java.util.*
import org.ktorm.dsl.Query
import org.ktorm.dsl.QuerySource
import org.ktorm.dsl.select
import org.ktorm.expression.ScalarExpression
import org.ktorm.schema.*
import team.yi.rsql.ktorm.adapter.OperatorAdapter
import team.yi.rsql.ktorm.converter.*
import team.yi.rsql.ktorm.exception.FieldNotSupportedException
import team.yi.rsql.ktorm.exception.TypeNotSupportedException

class RsqlBuilder internal constructor(
    internal val operatorAdapters: List<OperatorAdapter>,
    private val valueConverters: Map<SqlType<out Any>, FieldValueConverter>,
    querySource: QuerySource,
    vararg columns: ColumnDeclaring<*>,
) {
    private val sourceTable = querySource.sourceTable
    private val selectedColumns = columns

    internal val query: Query = querySource.select(*selectedColumns)

    fun getExpression(node: ComparisonNode, rsqlOperator: RsqlOperator): ScalarExpression<Boolean>? {
        val left = getLeft(node)
        val right = getRight(node, left)
        val operatorAdapter = getAdapter(node, rsqlOperator)

        return operatorAdapter.getExpression(rsqlOperator, left, right, node)
    }

    private fun getLeft(node: ComparisonNode): ColumnDeclaring<Any> {
        return getColumn(node)
    }

    private fun getRight(node: ComparisonNode, left: ColumnDeclaring<Any>): List<Any?> {
        val converter: FieldValueConverter? = if (valueConverters.containsKey(left.sqlType)) {
            valueConverters[left.sqlType]
        } else {
            try {
                val underlyingTypeField = left.sqlType.javaClass.getDeclaredField("underlyingType")
                underlyingTypeField.isAccessible = true

                val underlyingType = underlyingTypeField.get(left.sqlType) as SqlType<*>

                valueConverters.filter { it.key.typeCode == underlyingType.typeCode }.map { it.value }.firstOrNull()
            } catch (e: Exception) {
                throw FieldNotSupportedException("The field is not recognized: ${node.selector}.", node.selector)
            }
        }

        val valueConverter = converter ?: throw TypeNotSupportedException(
            "The field value converter could not be found: ${node.selector}.",
            node.selector,
            left.sqlType,
        )

        return valueConverter.convert(node.arguments, sourceTable)
    }

    private fun getAdapter(node: ComparisonNode, rsqlOperator: RsqlOperator): OperatorAdapter {
        return operatorAdapters.first { it.supports(node, rsqlOperator) }
    }

    @Suppress("UNCHECKED_CAST")
    private fun getColumn(node: ComparisonNode): ColumnDeclaring<Any> {
        val fieldPaths = Stack<ColumnDeclaring<*>>()
        val selectors = LinkedList<String>()
        selectors.addAll(node.selector.split('.'))

        var table = sourceTable

        while (selectors.isNotEmpty()) {
            val selector = selectors.pop()
            val column = table.columns.firstOrNull {
                if (it.name == selector) return@firstOrNull true

                val binding = it.binding ?: return@firstOrNull false

                if (binding is ReferenceBinding) {
                    if (binding.onProperty.name == selector) {
                        return@firstOrNull true
                    }
                }

                return@firstOrNull false
            } ?: break

            fieldPaths.add(column)

            table = column.referenceTable ?: column.table
        }

        return fieldPaths.pop() as ColumnDeclaring<Any>
    }
}

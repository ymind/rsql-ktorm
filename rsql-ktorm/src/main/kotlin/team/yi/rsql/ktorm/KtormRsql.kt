package team.yi.rsql.ktorm

import cz.jirutka.rsql.parser.RSQLParser
import org.ktorm.dsl.Query
import org.ktorm.dsl.QuerySource
import org.ktorm.schema.ColumnDeclaring
import org.ktorm.schema.SqlType
import team.yi.rsql.ktorm.adapter.OperatorAdapter
import team.yi.rsql.ktorm.converter.FieldValueConverter
import team.yi.rsql.ktorm.util.RsqlUtil

class KtormRsql private constructor(
    private val rsqlBuilder: RsqlBuilder,
) {
    val query: Query = rsqlBuilder.query

    fun buildPredicate(rsql: String): ColumnDeclaring<Boolean> {
        val operators = rsqlBuilder.operatorAdapters.map { it.comparisonOperator }.toSet()
        val rootNode = RSQLParser(operators).parse(rsql)

        return rootNode.accept(RsqlBuilderVisitor(rsqlBuilder))
    }

    companion object {
        fun builder(): Builder {
            return Builder()
        }
    }

    @Suppress("unused")
    class Builder internal constructor() {
        private lateinit var querySource: QuerySource
        private var columns: Array<out ColumnDeclaring<*>>? = null

        private var operatorAdapters: List<OperatorAdapter> = RsqlUtil.defaultOperatorAdapters
        private var valueConverters: Map<SqlType<out Any>, FieldValueConverter> = RsqlUtil.defaultValueConverters

        fun build(): KtormRsql {
            val rsqlDslBuilder = RsqlBuilder(operatorAdapters, valueConverters, querySource, *columns.orEmpty())

            return KtormRsql(rsqlDslBuilder)
        }

        fun adapter(vararg operatorAdapter: OperatorAdapter): Builder {
            this.operatorAdapters += operatorAdapter

            return this
        }

        fun valueConverters(valueConverters: Map<SqlType<out Any>, FieldValueConverter>): Builder {
            this.valueConverters += valueConverters

            return this
        }

        fun from(querySource: QuerySource): Builder {
            this.querySource = querySource

            return this
        }

        fun select(vararg columns: ColumnDeclaring<*>): Builder {
            this.columns = columns

            return this
        }
    }
}

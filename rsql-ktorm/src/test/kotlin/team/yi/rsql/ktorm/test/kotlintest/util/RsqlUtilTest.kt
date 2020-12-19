package team.yi.rsql.ktorm.test.kotlintest.util

import org.junit.jupiter.api.Test
import team.yi.rsql.ktorm.util.RsqlUtil

class RsqlUtilTest {
    @Test
    fun printOperators() {
        println("| Operator | Syntax |")
        println("| -------- | ------ |")

        for (adapter in RsqlUtil.defaultOperatorAdapters) {
            val symbols = adapter.comparisonOperator.symbols.joinToString("` `", "`", "`")

            println("| ${adapter.name} | $symbols |")
        }
    }
}

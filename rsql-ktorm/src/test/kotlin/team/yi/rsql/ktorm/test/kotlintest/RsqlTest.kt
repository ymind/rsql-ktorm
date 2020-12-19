package team.yi.rsql.ktorm.test.kotlintest

import cn.hutool.json.JSONUtil
import mu.KotlinLogging
import org.junit.jupiter.api.Test
import org.ktorm.dsl.*
import team.yi.rsql.ktorm.KtormRsql
import team.yi.rsql.ktorm.test.BaseTest
import team.yi.rsql.ktorm.test.model.Employees

class RsqlTest : BaseTest() {
    private val log = KotlinLogging.logger {}

    @Test
    fun test() {
        val rsql =
            """
            name!=bob 
            and (name==alice or id=in=(2,3,4,5)) 
            or manager_id>0 
            and hire_date=before='2099-12-31 12:38:59' 
            or department.location=notnull=1
            """.trimIndent()
                .replace("\n", "")

        val ktormRsql = KtormRsql.builder()
            .from(
                database.from(Employees)
                    .leftJoin(Employees.department, on = Employees.department.id.eq(Employees.departmentId))
            )
            .select(
                Employees.id,
                Employees.name,
                Employees.departmentId,
                Employees.department.name,
                Employees.hireDate,
            )
            .build()

        // build ktorm predicate
        val predicate = ktormRsql.buildPredicate(rsql)

        // ktorm query
        val q = ktormRsql.query
            .whereWithConditions {
                // inject rsql predicate
                it += predicate

                // more predicates
                it += Employees.job.isNotNull()
                it += Employees.department.id.greater(0)
            }
            .orderBy(
                Employees.departmentId.desc(),
                Employees.name.asc(),
                Employees.department.location.desc(),
            )

        // print sql or fetch results
        log.info { q.sql }

        q.forEach {
            log.info {
                val item = Employees.createEntity(it)

                JSONUtil.toJsonStr(item)
            }
        }
    }
}

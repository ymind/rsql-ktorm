package team.yi.rsql.ktorm.sqlite.test.kotlintest

import cn.hutool.json.JSONUtil
import java.sql.Connection
import java.sql.DriverManager
import mu.KotlinLogging
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.ktorm.database.Database
import org.ktorm.dsl.*
import org.ktorm.logging.ConsoleLogger
import org.ktorm.logging.LogLevel
import team.yi.rsql.ktorm.KtormRsql
import team.yi.rsql.ktorm.sqlite.useSqlite
import team.yi.rsql.ktorm.test.BaseTest
import team.yi.rsql.ktorm.test.model.Employees

class SqliteRsqlTest : BaseTest() {
    private val log = KotlinLogging.logger {}

    lateinit var connection: Connection

    @BeforeAll
    override fun init() {
        connection = DriverManager.getConnection("jdbc:sqlite::memory:")

        database = Database.connect(logger = ConsoleLogger(LogLevel.TRACE)) {
            object : Connection by connection {
                override fun close() {
                    // do nothing...
                }
            }
        }

        execSqlScript("init-sqlite-data.sql")
    }

    override fun destroy() {
        super.destroy()

        connection.close()
    }

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
            .useSqlite()
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
            .limit(0, 10)

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

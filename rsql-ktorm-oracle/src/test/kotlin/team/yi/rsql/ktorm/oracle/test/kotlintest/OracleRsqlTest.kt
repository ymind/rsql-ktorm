package team.yi.rsql.ktorm.oracle.test.kotlintest

import cn.hutool.json.JSONUtil
import mu.KotlinLogging
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.ktorm.database.Database
import org.ktorm.dsl.*
import org.ktorm.logging.ConsoleLogger
import org.ktorm.logging.LogLevel
import org.testcontainers.containers.OracleContainer
import team.yi.rsql.ktorm.KtormRsql
import team.yi.rsql.ktorm.oracle.useOracle
import team.yi.rsql.ktorm.test.BaseTest
import team.yi.rsql.ktorm.test.model.Employees

class OracleRsqlTest : BaseTest() {
    private val log = KotlinLogging.logger {}

    companion object {
        private const val dockerImageName = "zerda/oracle-database:11.2.0.2-xe"

        val dbContainer: OracleContainer = OracleContainer(dockerImageName)
            .withCreateContainerCmdModifier { cmd ->
                cmd.hostConfig?.withShmSize(1 * 1024 * 1024 * 1024)
            }
    }

    @BeforeAll
    override fun init() {
        dbContainer.start()

        database = Database.connect(
            url = dbContainer.jdbcUrl,
            driver = dbContainer.driverClassName,
            user = dbContainer.username,
            password = dbContainer.password,
            logger = ConsoleLogger(threshold = LogLevel.TRACE),
            alwaysQuoteIdentifiers = true
        )

        execSqlScript("init-oracle-data.sql")
    }

    override fun destroy() {
        dbContainer.stop()

        execSqlScript("drop-oracle-data.sql")
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
            .useOracle()
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

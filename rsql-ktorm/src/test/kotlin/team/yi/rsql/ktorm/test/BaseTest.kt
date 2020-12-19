package team.yi.rsql.ktorm.test

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.ktorm.database.Database
import org.ktorm.database.use
import org.ktorm.logging.ConsoleLogger
import org.ktorm.logging.LogLevel

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
open class BaseTest {
    lateinit var database: Database

    @BeforeAll
    open fun init() {
        database = Database.connect(
            url = "jdbc:h2:mem:ktorm;DB_CLOSE_DELAY=-1",
            driver = "org.h2.Driver",
            logger = ConsoleLogger(threshold = LogLevel.TRACE),
            alwaysQuoteIdentifiers = true
        )

        execSqlScript("init-data.sql")
    }

    @AfterAll
    open fun destroy() {
        execSqlScript("drop-data.sql")
    }

    protected fun execSqlScript(filename: String) {
        database.useConnection { conn ->
            conn.createStatement()
                .use { statement ->
                    javaClass.classLoader
                        ?.getResourceAsStream(filename)
                        ?.bufferedReader()
                        ?.use { reader ->
                            for (sql in reader.readText().split(';')) {
                                if (sql.any { it.isLetterOrDigit() }) {
                                    statement.executeUpdate(sql)
                                }
                            }
                        }
                }
        }
    }
}

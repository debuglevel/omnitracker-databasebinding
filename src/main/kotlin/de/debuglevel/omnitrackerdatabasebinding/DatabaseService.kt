package de.debuglevel.omnitrackerdatabasebinding

import io.micronaut.context.annotation.Property
import mu.KotlinLogging
import java.sql.Connection
import java.sql.DriverManager
import javax.inject.Singleton

@Singleton
class DatabaseService(
    @Property(name = "database.connectionstring") val connectionString: String
) {
    private val logger = KotlinLogging.logger {}

    init {
        // MSSQL driver need to be loaded explicitly
        logger.debug { "Loading MSSQL driver..." }
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver")
    }

    fun getConnection(): Connection {
        logger.trace { "Getting connection..." }
        val connection = DriverManager.getConnection(connectionString)
        logger.trace { "Got connection: $connection" }
        return connection
    }
}
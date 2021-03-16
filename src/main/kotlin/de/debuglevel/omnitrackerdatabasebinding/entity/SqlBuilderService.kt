package de.debuglevel.omnitrackerdatabasebinding.entity

import mu.KotlinLogging
import java.sql.Connection
import java.sql.DriverManager
import javax.inject.Singleton

@Singleton
class SqlBuilderService {
    private val logger = KotlinLogging.logger {}

    fun getDatabaseType(connection: Connection): DatabaseType {
        logger.trace { "Getting database type..." }
        val clazz = DriverManager.getDriver(connection.metaData.url)::class.java
        logger.trace { "Driver is '${clazz.name}'" }

        val databaseType = when {
            clazz.name.contains("sqlserver") -> DatabaseType.MSSQL
            clazz.name.contains("ucanaccess") -> DatabaseType.Access
            clazz.name.contains("postgresql") -> DatabaseType.PostgreSQL
            else -> DatabaseType.Unknown
        }
        logger.trace { "Got database type: $databaseType" }
        return databaseType
    }

    fun getLeftDelimit(databaseType: DatabaseType): String {
        return when (databaseType) {
            DatabaseType.MSSQL -> "["
            DatabaseType.Access -> "["
            DatabaseType.PostgreSQL -> "\""
            else -> "`"
        }
    }

    fun getRightDelimit(databaseType: DatabaseType): String {
        return when (databaseType) {
            DatabaseType.MSSQL -> "]"
            DatabaseType.Access -> "]"
            DatabaseType.PostgreSQL -> "\""
            else -> "`"
        }
    }

    fun buildSelectAllQuery(
        databaseType: DatabaseType,
        table: String,
        columns: Map<String, ColumnType>
    ): String {
        logger.trace { "Building select-all-query for table $table..." }

        val leftDelimit = getLeftDelimit(databaseType)
        val rightDelimit = getRightDelimit(databaseType)

        val columnList = columns.keys.joinToString(",") { "$leftDelimit$it$rightDelimit" }
        val query = "SELECT $columnList FROM $leftDelimit$table$rightDelimit"
        logger.trace { "Built select-all-query for table $table: $query" }
        return query
    }

    fun buildSelectOneQuery(
        databaseType: DatabaseType,
        table: String,
        columns: Map<String, ColumnType>,
        idColumn: String,
        id: Int
    ): String {
        return buildSelectOneQuery(databaseType, table, columns, idColumn, id.toString())
    }

    fun buildSelectOneQuery(
        databaseType: DatabaseType,
        table: String,
        columns: Map<String, ColumnType>,
        idColumn: String,
        id: String
    ): String {
        logger.trace { "Building select-one-query for table $table..." }
        val selectAllQuery = buildSelectAllQuery(databaseType, table, columns)

        val leftDelimit = getLeftDelimit(databaseType)
        val rightDelimit = getRightDelimit(databaseType)

        val query = "$selectAllQuery WHERE $leftDelimit$idColumn$rightDelimit=$id"
        logger.trace { "Built select-one-query for table $table: $query" }
        return query
    }
}

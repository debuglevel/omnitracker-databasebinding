package de.debuglevel.omnitrackerdatabasebinding.entity

import mu.KotlinLogging
import javax.inject.Singleton

@Singleton
class SqlBuilderService {
    private val logger = KotlinLogging.logger {}

    fun buildSelectAllQuery(table: String, columns: Map<String, ColumnType>): String {
        logger.trace { "Building getAllQuery for table $table..." }
        val columnList = columns.keys.joinToString(",") { "[$it]" }
        val query = "SELECT $columnList FROM [$table]"
        logger.trace { "Built getAllQuery for table $table: $query" }
        return query
    }

    fun buildSelectOneQuery(table: String, columns: Map<String, ColumnType>, idColumn: String, id: String): String {
        logger.trace { "Building select-one-query for table $table..." }
        val selectAllQuery = buildSelectAllQuery(table, columns)
        val query = "$selectAllQuery WHERE [$idColumn]=$id"
        logger.trace { "Built select-one-query for table $table: $query" }
        return query
    }
}

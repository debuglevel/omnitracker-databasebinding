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
}

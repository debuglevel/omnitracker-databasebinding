package de.debuglevel.omnitrackerdatabasebinding.entity

import de.debuglevel.omnitrackerdatabasebinding.DatabaseService
import mu.KotlinLogging
import java.sql.ResultSet
import javax.inject.Inject

abstract class EntityService<T : Entity>(
    private val databaseService: DatabaseService
) {
    private val logger = KotlinLogging.logger {}

    /**
     * Human-readable name of the entity
     */
    protected abstract val name: String

    /**
     * Table in the database
     */
    protected abstract val table: String

    /**
     * Columns in the table
     * Note: ColumnType is unused right now and unclear whether it can be used.
     */
    protected abstract val columns: Map<String, ColumnType>

    @Inject
    lateinit var sqlBuilderService: SqlBuilderService

    private val cache = Cache<T>()

    protected val getAllQuery: String
        get() {
            logger.trace { "Building getAllQuery for $name..." }
            val query = sqlBuilderService.buildSelectAllQuery(table, columns)
            logger.trace { "Built getAllQuery for $name: $query" }
            return query
        }

    /**
     * Gets all entities, clears the cache and puts all entities into the cache.
     */
    fun getAll(): Map<Int, T> {
        logger.trace { "Getting ${name}s..." }

        val entities = databaseService.getConnection().use { connection ->
            val sqlStatement = connection.createStatement()
            val resultSet = sqlStatement.executeQuery(getAllQuery)

            val entities = hashMapOf<Int, T>()

            while (resultSet.next()) {
                val entity = build(resultSet)
                entities[entity.id] = entity
            }

            entities
        }

        cache.clearCache()
        cache.putCache(entities)

        logger.trace { "Got ${entities.size} ${name}s" }
        return entities
    }

    /**
     * Clears the cache.
     */
    fun clearCache() {
        cache.clearCache()
    }

    /**
     * Gets an entity by its id.
     * Tries to get it from the the cache first. On cache miss, retrieve it from the database and put it into the cache.
     */
    fun get(id: Int): T {
        logger.trace { "Getting $name id=$id..." }

        val entity = try {
            cache.getFromCache(id)
        } catch (e: Cache.CacheMissedException) {
            val entity = getFromDatabase(id)
            cache.putCache(entity)
            entity
        }

        logger.trace { "Got ${name}: $entity" }
        return entity
    }

    /**
     * Gets an entity from the database by its id.
     */
    private fun getFromDatabase(id: Int): T {
        logger.trace { "Getting $name id=$id from database..." }

        val entity = databaseService.getConnection().use { connection ->
            val sqlStatement = connection.createStatement()
            val resultSet = sqlStatement.executeQuery("$getAllQuery WHERE id=$id")

            val available = resultSet.next()
            when {
                available -> build(resultSet)
                else -> throw EntityNotFoundException(id)
            }
        }

        logger.trace { "Got $name id=$id from database..." }
        return entity
    }

    /**
     * Builds an entity object from the ResultSet.
     */
    protected abstract fun build(resultSet: ResultSet): T

    class EntityNotFoundException(id: Int) : Exception("Entity id='$id' was not found.")
}
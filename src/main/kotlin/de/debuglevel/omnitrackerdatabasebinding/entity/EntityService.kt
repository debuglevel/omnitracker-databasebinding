package de.debuglevel.omnitrackerdatabasebinding.entity

import de.debuglevel.omnitrackerdatabasebinding.DatabaseService
import mu.KotlinLogging
import java.sql.ResultSet

abstract class EntityService<T : Entity>(
    private val databaseService: DatabaseService
) {
    private val logger = KotlinLogging.logger {}

    /**
     * Human-readable name of the entity
     */
    protected abstract val name: String
    private val cache: MutableMap<Int, T> = mutableMapOf<Int, T>()

    /**
     * Table in the database
     */
    protected abstract val table: String

    /**
     * Columns in the table with their type to map to
     */
    protected abstract val columns: Map<String, ColumnType>

    protected val getAllQuery: String
        get() {
            logger.trace { "Building getAllQuery for $name..." }
            val columnList = columns.keys
                .map { "[$it]" }
                .joinToString(",")
            val query = "SELECT $columnList FROM [$table]"
            logger.trace { "Built getAllQuery for $name: $query" }
            return query
        }

    /**
     * Puts an entity into the cache.
     */
    private fun putCache(entity: T) {
        logger.trace { "Putting id=${entity.id} into $name cache..." }
        cache[entity.id] = entity
        logger.trace { "Put id=${entity.id} into $name cache (size: ${cache.size}" }
    }

    /**
     * Puts (or updates) all entities into the cache.
     */
    private fun putCache(entities: Map<Int, T>) {
        val beforeCacheSize = cache.size
        logger.trace { "Putting ${entities.size} ${name}s into $name cache (size: $beforeCacheSize..." }
        cache.putAll(entities)
        val afterCacheSize = cache.size
        val differenceSize = afterCacheSize - beforeCacheSize
        logger.trace { "Put $differenceSize ${name}s $name cache (size: $afterCacheSize" }
    }

    /**
     * Gets an entity from the cache by its id.
     */
    private fun getFromCache(id: Int): T {
        logger.trace { "Getting id=${id} from $name cache..." }

        val entity = cache.getOrElse(id) {
            logger.trace { "Missed id=${id} in $name cache" }
            throw CacheMissedException(id)
        }
        logger.trace { "Got id=${id} from $name cache" }

        return entity
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

        clearCache()
        putCache(entities)

        logger.trace { "Got ${entities.size} ${name}s" }
        return entities
    }

    /**
     * Clears the cache.
     */
    fun clearCache() {
        cache.clear()
    }

    /**
     * Gets an entity by its id.
     * Tries to get it from the the cache first. On cache miss, retrieve it from the database and put it into the cache.
     */
    fun get(id: Int): T {
        logger.trace { "Getting $name id=$id..." }

        val entity = try {
            getFromCache(id)
        } catch (e: CacheMissedException) {
            val entity = getFromDatabase(id)
            putCache(entity)
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
    class CacheMissedException(id: Int) : Exception("Entity id='$id' not present in cache.")
}
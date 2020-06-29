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
    protected abstract val query: String
    private val cache: MutableMap<Int, T> = mutableMapOf<Int, T>()

    /**
     * Puts an entity into the cache
     */
    private fun putCache(entity: T) {
        logger.trace { "Putting id=${entity.id} into $name cache..." }
        cache[entity.id] = entity
        logger.trace { "Put id=${entity.id} into $name cache (size: ${cache.size}" }
    }

    /**
     * Puts (or updates) all entities into the cache
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
     * Gets an entity from the cache by its id. Returns null if not present.
     */
    private fun getFromCache(id: Int): T? {
        logger.trace { "Getting id=${id} from $name cache..." }
        val entity = cache[id]
        if (entity != null) {
            logger.trace { "Got id=${id} from $name cache" }
        } else {
            logger.trace { "Missed id=${id} in $name cache" }
        }

        return entity
    }

    /**
     * Gets all entities and updates the cache.
     */
    fun getAll(): Map<Int, T> {
        logger.debug { "Getting ${name}s..." }

        val entities = databaseService.getConnection().use { connection ->
            val sqlStatement = connection.createStatement()
            val resultSet = sqlStatement.executeQuery(query)

            val entities = hashMapOf<Int, T>()

            while (resultSet.next()) {
                val entity = build(resultSet)
                entities[entity.id] = entity
            }

            entities
        }

        // TODO: could be useful to clear cache as we just retrieved all entities
        putCache(entities)

        logger.debug { "Got ${entities.size} ${name}s" }
        return entities
    }

    /**
     * Gets an entity by its id.
     * Tries to get it from the the cache first. On cache miss, retrieve it from the database and put it into the cache.
     */
    fun get(id: Int): T? {
        logger.debug { "Getting $name id=$id..." }

        val cachedEntity = getFromCache(id)
        val entity = if (cachedEntity != null) {
            cachedEntity
        } else {
            val entity = getFromDatabase(id)
            if (entity != null) {
                putCache(entity)
            }
            entity
        }

        logger.debug { "Got ${name}: $entity" }
        return entity
    }

    /**
     * Gets an entity from the database by its id. Returns null if it does not exist.
     */
    private fun getFromDatabase(id: Int): T? {
        logger.debug { "Getting $name id=$id from database..." }

        val entity = databaseService.getConnection().use { connection ->
            val sqlStatement = connection.createStatement()
            val resultSet = sqlStatement.executeQuery("$query WHERE id=$id")

            val available = resultSet.next()
            when {
                available -> build(resultSet)
                else -> null
            }
        }

        logger.debug { "Got $name id=$id from database..." }
        return entity
    }

    /**
     * Builds an entity object from the ResultSet.
     */
    protected abstract fun build(resultSet: ResultSet): T
}
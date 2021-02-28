package de.debuglevel.omnitrackerdatabasebinding.entity

import mu.KotlinLogging

class Cache<T : Entity> {
    private val logger = KotlinLogging.logger {}

    private val cache: MutableMap<Int, T> = mutableMapOf<Int, T>()

    /**
     * Puts an entity into the cache.
     */
    fun putCache(entity: T) {
        logger.trace { "Putting id=${entity.id} into cache..." }
        cache[entity.id] = entity
        logger.trace { "Put id=${entity.id} into cache (size: ${cache.size}" }
    }

    /**
     * Puts (or updates) all entities into the cache.
     */
    fun putCache(entities: Map<Int, T>) {
        val beforeCacheSize = cache.size
        logger.trace { "Putting ${entities.size} items into cache (size: $beforeCacheSize..." }
        cache.putAll(entities)
        val afterCacheSize = cache.size
        val differenceSize = afterCacheSize - beforeCacheSize
        logger.trace { "Put $differenceSize items into cache (size: $afterCacheSize" }
    }

    /**
     * Gets an entity from the cache by its id.
     */
    fun getFromCache(id: Int): T {
        logger.trace { "Getting id=${id} from cache..." }

        val entity = cache.getOrElse(id) {
            logger.trace { "Missed id=${id} in cache" }
            throw CacheMissedException(id)
        }
        logger.trace { "Got id=${id} from cache" }

        return entity
    }

    /**
     * Clears the cache.
     */
    fun clearCache() {
        cache.clear()
    }

    class CacheMissedException(id: Int) : Exception("Entity id='$id' not present in cache.")
}
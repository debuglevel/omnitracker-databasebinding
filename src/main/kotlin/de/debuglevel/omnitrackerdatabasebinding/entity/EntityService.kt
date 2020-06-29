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

        logger.debug { "Got ${entities.size} ${name}s" }
        return entities
    }

    fun get(id: Int): T? {
        logger.debug { "Getting $name id=$id..." }

        val entity = databaseService.getConnection().use { connection ->
            val sqlStatement = connection.createStatement()
            val resultSet = sqlStatement.executeQuery("$query WHERE id=$id")

            val available = resultSet.next()
            when {
                available -> build(resultSet)
                else -> null
            }
        }

        logger.debug { "Got ${name}: $entity" }
        return entity
    }

    protected abstract fun build(resultSet: ResultSet): T
}
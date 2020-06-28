package de.debuglevel.omnitrackerdatabasebinding.script

import de.debuglevel.omnitrackerdatabasebinding.DatabaseService
import de.debuglevel.omnitrackerdatabasebinding.folder.FolderService
import mu.KotlinLogging
import java.sql.ResultSet
import javax.inject.Singleton

@Singleton
class ScriptsService(
    private val databaseService: DatabaseService,
    private val folderService: FolderService
) {
    private val logger = KotlinLogging.logger {}

    private val query = "SELECT id, folder, type, name, script FROM [Scripts]"

    fun getAll(): Map<Int, Script> {
        databaseService.getConnection().use { connection ->
            val sqlStatement = connection.createStatement()
            val resultSet = sqlStatement.executeQuery(query)

            val scripts = hashMapOf<Int, Script>()

            while (resultSet.next()) {
                val script = build(resultSet)
                scripts[script.id] = script
            }

            return scripts
        }
    }

    fun get(id: Int): Script? {
        logger.debug { "Getting script id=$id..." }
        val layout = databaseService.getConnection().use { connection ->
            val sqlStatement = connection.createStatement()
            val resultSet = sqlStatement.executeQuery("$query WHERE id=$id")

            val available = resultSet.next()
            when {
                available -> build(resultSet)
                else -> null
            }
        }

        logger.debug { "Got script: $layout" }
        return layout
    }

    private fun build(resultSet: ResultSet): Script {
        logger.debug { "Building script for ResultSet $resultSet..." }

        val id = resultSet.getInt("id")
        val folderId = resultSet.getInt("folder")
        val type = resultSet.getInt("type")
        val name = resultSet.getString("name").trimEnd()
        val content = resultSet.getString("script")

        val folder = folderService.get(folderId)

        val script = Script(
            id,
            name,
            content,
            type,
            folderId,
            folder
            //lazy { folderService.fetchFolders() }
        )

        logger.debug { "Built script: $script" }
        return script
    }
}
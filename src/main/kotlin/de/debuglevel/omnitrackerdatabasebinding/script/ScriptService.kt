package de.debuglevel.omnitrackerdatabasebinding.script

import de.debuglevel.omnitrackerdatabasebinding.DatabaseService
import de.debuglevel.omnitrackerdatabasebinding.entity.EntityService
import de.debuglevel.omnitrackerdatabasebinding.folder.FolderService
import mu.KotlinLogging
import java.sql.ResultSet
import javax.inject.Singleton

@Singleton
class ScriptService(
    databaseService: DatabaseService,
    private val folderService: FolderService
) : EntityService<Script>(databaseService) {
    private val logger = KotlinLogging.logger {}

    override val name = "Script"
    override val getAllQuery = "SELECT " +
            "id, " +
            "folder, " +
            "type, " +
            "name, " +
            "script " +
            " FROM [Scripts]"

    override fun build(resultSet: ResultSet): Script {
        logger.trace { "Building script for ResultSet $resultSet..." }

        val id = resultSet.getInt("id")
        val folderId = resultSet.getInt("folder")
        val type = resultSet.getInt("type")
        val name = resultSet.getString("name").trimEnd()
        val content = resultSet.getString("script")

        val folder = try {
            folderService.get(folderId)
        } catch (e: EntityNotFoundException) {
            null
        }

        val script = Script(
            id,
            name,
            content,
            type,
            folderId,
            folder
            //lazy { folderService.fetchFolders() }
        )

        logger.trace { "Built script: $script" }
        return script
    }
}
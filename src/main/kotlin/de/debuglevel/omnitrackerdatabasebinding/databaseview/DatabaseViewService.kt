package de.debuglevel.omnitrackerdatabasebinding.databaseview

import de.debuglevel.omnitrackerdatabasebinding.DatabaseService
import de.debuglevel.omnitrackerdatabasebinding.entity.EntityService
import de.debuglevel.omnitrackerdatabasebinding.folder.FolderService
import mu.KotlinLogging
import java.sql.ResultSet
import javax.inject.Singleton

@Singleton
class DatabaseViewService(
    databaseService: DatabaseService,
    private val folderService: FolderService
) : EntityService<DatabaseView>(databaseService) {
    private val logger = KotlinLogging.logger {}

    override val name = "Database View"
    override val query = "SELECT [id]\n" +
            "      ,[folder]\n" +
            "      ,[view_name_db]\n" +
            "      ,[type]\n" +
            "      ,[assoc_field]\n" +
            "      ,[include_subfolders]\n" +
            "  FROM [DbViewDef]"

    override fun build(resultSet: ResultSet): DatabaseView {
        logger.trace { "Building DatabaseView for ResultSet $resultSet..." }

        val id = resultSet.getInt("id")
        val folderId = resultSet.getInt("folder")
        val viewName = resultSet.getString("view_name_db")
        //val type = resultSet.getInt("type")
        //val associatedField = resultSet.getInt("assoc_field")
        val isIncludingSubfolders = resultSet.getBoolean("include_subfolders")

        val folder = folderService.get(folderId)

        val databaseView = DatabaseView(
            id,
            viewName,
            folderId,
            folder,
            isIncludingSubfolders
        )

        logger.trace { "Built DatabaseView: $databaseView" }
        return databaseView
    }
}
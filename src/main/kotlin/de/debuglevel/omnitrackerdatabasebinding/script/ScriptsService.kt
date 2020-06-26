package de.debuglevel.omnitrackerdatabasebinding.script

import de.debuglevel.omnitrackerdatabasebinding.DatabaseService
import mu.KotlinLogging
import java.sql.ResultSet
import javax.inject.Singleton

@Singleton
class ScriptsService(
    private val databaseService: DatabaseService
    //private val folderService: FolderService
) {
    private val logger = KotlinLogging.logger {}

    private val scriptsQuery = "SELECT id, folder, type, name, script FROM [Scripts]"

    fun getScripts(): Map<Int, Script> {
        databaseService.getConnection().use { connection ->
            val sqlStatement = connection.createStatement()
            val resultSet = sqlStatement.executeQuery(scriptsQuery)

            val scripts = hashMapOf<Int, Script>()

            while (resultSet.next()) {
                val script = buildScript(resultSet)
                scripts[script.id] = script
            }

            return scripts
        }
    }

    private fun buildScript(resultSet: ResultSet): Script {
        val id = resultSet.getInt("id")
        val folderId = resultSet.getInt("folder")
        val type = resultSet.getInt("type")
        val name = resultSet.getString("name").trimEnd()
        val content = resultSet.getString("script")

        val script = Script(
            id,
            name,
            content,
            type,
            folderId
            //lazy { folderService.fetchFolders() }
        )
        return script
    }
}
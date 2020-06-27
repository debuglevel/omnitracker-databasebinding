package de.debuglevel.omnitrackerdatabasebinding.folder

import de.debuglevel.omnitrackerdatabasebinding.DatabaseService
import mu.KotlinLogging
import java.sql.ResultSet
import javax.inject.Singleton

@Singleton
class FolderService(
    private val databaseService: DatabaseService
    //private val fieldService: FieldService,
    //private val stringTranslationService: StringTranslationService
) {
    private val logger = KotlinLogging.logger {}

    private val folderQuery = "SELECT id, name, parent, term_singular, term_plural, alias FROM [ProblemArea]"

    fun getFolders(): Map<Int, Folder> {
        databaseService.getConnection().use { connection ->
            val sqlStatement = connection.createStatement()
            val resultSet =
                sqlStatement.executeQuery(folderQuery)

            val folders = hashMapOf<Int, Folder>()

            while (resultSet.next()) {
                val folder = buildFolder(resultSet)
                folders[folder.id] = folder
            }

            return folders
        }
    }

    fun getFolder(id: Int): Folder {
        return getFolders().getValue(id)
    }

    private fun buildFolder(resultSet: ResultSet): Folder {
        val id = resultSet.getInt("id")
        // 'name' and some other columns are CHAR instead of VARCHAR and have to be trimmed therefore.
        val name = resultSet.getString("name").trimEnd()
        val parentFolderId = resultSet.getInt("parent")
        val singularTerm = resultSet.getString("term_singular")?.trimEnd()
        val pluralTerm = resultSet.getString("term_plural")?.trimEnd()
        val alias = resultSet.getString("alias")?.trimEnd()

        val folder = Folder(
            id,
            name,
            alias,
            singularTerm,
            pluralTerm,
            parentFolderId
            //                    lazy { folders },
            //                    lazy { fieldService.fetchFields(folders) },
            //                    lazy { stringTranslationService.fetchStringTranslations() }
        )
        return folder
    }
}
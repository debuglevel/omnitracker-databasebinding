package de.debuglevel.omnitrackerdatabasebinding.folder

import de.debuglevel.omnitrackerdatabasebinding.DatabaseService
import mu.KotlinLogging
import javax.inject.Singleton

@Singleton
class FolderService(
    private val databaseService: DatabaseService
    //private val fieldService: FieldService,
    //private val stringTranslationService: StringTranslationService
) {
    private val logger = KotlinLogging.logger {}

    fun fetchFolders(): Map<Int, Folder> {
        databaseService.getConnection().use { connection ->
            val sqlStatement = connection.createStatement()
            val resultSet =
                sqlStatement.executeQuery("SELECT id, name, parent, term_singular, term_plural, alias FROM [ProblemArea]")

            val folders = hashMapOf<Int, Folder>()

            while (resultSet.next()) {
                val id = resultSet.getInt("id")
                // 'name' and some other columns are CHAR instead of VARCHAR and have to be trimed therefore.
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

                folders[folder.id] = folder
            }

            return folders
        }
    }
}
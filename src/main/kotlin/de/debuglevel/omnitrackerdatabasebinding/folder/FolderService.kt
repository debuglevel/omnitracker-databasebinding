package de.debuglevel.omnitrackerdatabasebinding.folder

import de.debuglevel.omnitrackerdatabasebinding.DatabaseService
import de.debuglevel.omnitrackerdatabasebinding.stringtranslation.StringTranslationLanguage
import de.debuglevel.omnitrackerdatabasebinding.stringtranslation.StringTranslationService
import de.debuglevel.omnitrackerdatabasebinding.stringtranslation.StringTranslationType
import mu.KotlinLogging
import java.sql.ResultSet
import javax.inject.Singleton

@Singleton
class FolderService(
    private val databaseService: DatabaseService,
    //private val fieldService: FieldService,
    private val stringTranslationService: StringTranslationService
) {
    private val logger = KotlinLogging.logger {}

    private val folderQuery = "SELECT id, name, parent, term_singular, term_plural, alias FROM [ProblemArea]"

    fun getFolders(): Map<Int, Folder> {
        logger.debug { "Getting folders..." }
        val folders = databaseService.getConnection().use { connection ->
            val sqlStatement = connection.createStatement()
            val resultSet = sqlStatement.executeQuery(folderQuery)

            val folders = hashMapOf<Int, Folder>()

            while (resultSet.next()) {
                val folder = buildFolder(resultSet)
                folders[folder.id] = folder
            }

            folders
        }

        logger.debug { "Got ${folders.size} folders" }
        return folders
    }

    fun getFolder(id: Int): Folder? {
        logger.debug { "Getting folder id=$id..." }
        val folder = databaseService.getConnection().use { connection ->
            val sqlStatement = connection.createStatement()
            val resultSet = sqlStatement.executeQuery("$folderQuery WHERE id=$id")

            val available = resultSet.next()
            when {
                available -> buildFolder(resultSet)
                else -> null
            }
        }

        logger.debug { "Got folder: $folder" }
        return folder
    }

    private fun getFolderName(folderId: Int, language: StringTranslationLanguage): String {
        logger.trace { "Getting folder name for folderId=$folderId ..." }

        val stringTranslations = stringTranslationService.getStringTranslations(folderId)

        val stringTranslation = stringTranslations
            .singleOrNull { it.language == language && it.type == StringTranslationType.FolderName }

        val folderName = stringTranslation?.text ?: "<UNDEFINED>"

        logger.trace { "Got folder name for folderId=$folderId: $folderName" }
        return folderName
    }

    private fun getDescription(folderId: Int, language: StringTranslationLanguage): String? {
        logger.trace { "Getting description for folderId=$folderId ..." }

        val stringTranslations = stringTranslationService.getStringTranslations(folderId)

        val stringTranslation = stringTranslations
            .singleOrNull { it.language == language && it.type == StringTranslationType.FolderDescription }

        val description = stringTranslation?.text

        logger.trace { "Getting description for folderId=$folderId: $description" }
        return description
    }

    private fun buildPath(folderId: Int, parentFolder: Folder?): String {
        logger.trace { "Building path for folderId=$folderId ..." }

        val folderName = getFolderName(folderId, StringTranslationLanguage.German)

        val path = when (parentFolder) {
            null -> "\\$folderName"
            else -> "${parentFolder.path}\\$folderName"
        }

        logger.trace { "Built path for folderId=$folderId: $path" }
        return path
    }

    private fun buildFolder(resultSet: ResultSet): Folder {
        logger.trace { "Building folder for ResultSet $resultSet" }

        val id = resultSet.getInt("id")
        // 'name' and some other columns are CHAR instead of VARCHAR and have to be trimmed therefore.
        val name = resultSet.getString("name").trimEnd()
        val parentFolderId = resultSet.getInt("parent")
        val singularTerm = resultSet.getString("term_singular")?.trimEnd()
        val pluralTerm = resultSet.getString("term_plural")?.trimEnd()
        val alias = resultSet.getString("alias")?.trimEnd()

        val parentFolder = getFolder(parentFolderId)
        val path = buildPath(id, parentFolder)
        val description = getDescription(id, StringTranslationLanguage.German)

        val folder = Folder(
            id,
            name,
            alias,
            singularTerm,
            pluralTerm,
            parentFolderId,
            parentFolder,
            path,
            description
            //                    lazy { folders },
            //                    lazy { fieldService.fetchFields(folders) },
            //                    lazy { stringTranslationService.fetchStringTranslations() }
        )

        logger.trace { "Built folder: $folder" }
        return folder
    }
}
package de.debuglevel.omnitrackerdatabasebinding

import de.debuglevel.omnitrackerdatabasebinding.models.Field
import de.debuglevel.omnitrackerdatabasebinding.models.Folder
import de.debuglevel.omnitrackerdatabasebinding.models.Script
import de.debuglevel.omnitrackerdatabasebinding.models.StringTranslation
import mu.KotlinLogging
import java.sql.DriverManager

class OmnitrackerDatabase {
    private val logger = KotlinLogging.logger {}

    init {
        logger.debug { "Initialize OMNITRACKER DatabaseBinding..." }
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver")
    }

    val fields: Map<Int, Field> by lazy {
        logger.debug("Lazy initialize fields...")
        fetchFields()
    }

    private fun fetchFields(): Map<Int, Field> {
        DriverManager.getConnection(Configuration.databaseConnectionString).use { connection ->
            val sqlStatement = connection.createStatement()
            val resultSet = sqlStatement.executeQuery("SELECT id, area, label, remark, type, alias, subtype, max_size, refobj_key FROM [UserFieldDef]")

            val fields = hashMapOf<Int, Field>()

            while (resultSet.next()) {
                val id = resultSet.getInt("id")
                val folderId = resultSet.getInt("area")
                val label = resultSet.getString("label")
                val remark = resultSet.getString("remark")
                val type = resultSet.getInt("type")
                val alias = resultSet.getString("alias")
                val subtype = resultSet.getInt("subtype")
                val maxSize = resultSet.getInt("max_size")
                val referenceFolderId = resultSet.getInt("refobj_key")

                val field = Field(
                        id,
                        folderId,
                        label,
                        remark,
                        type,
                        alias,
                        subtype,
                        maxSize,
                        referenceFolderId,
                        lazy { folders },
                        lazy { stringTranslations }
                )

                fields[id] = field
            }

            return fields
        }
    }

    val stringTranslations: Map<Int, StringTranslation> by lazy {
        logger.debug("Lazy initialize stringTranslations...")
        fetchStringTranslations()
    }

    private fun fetchStringTranslations(): Map<Int, StringTranslation> {
        DriverManager.getConnection(Configuration.databaseConnectionString).use { connection ->
            val sqlStatement = connection.createStatement()
            val resultSet = sqlStatement.executeQuery("SELECT id, str_guid, type, ref, field, folder, langcode, txt, untranslated FROM [StringTranslations]")

            val stringTranslations = hashMapOf<Int, StringTranslation>()

            while (resultSet.next()) {
                val id = resultSet.getInt("id")
                val guid = resultSet.getString("str_guid")
                val type = resultSet.getInt("type")
                val fieldId = resultSet.getInt("ref")
                val folderId = resultSet.getInt("folder")
                val langCode = resultSet.getString("langcode")
                val text = resultSet.getString("txt") ?: null
                val untranslated = resultSet.getBoolean("untranslated")

                val stringTranslation = StringTranslation(
                        id,
                        guid,
                        folderId,
                        lazy { folders },
                        langCode,
                        text,
                        untranslated,
                        type,
                        lazy { fields },
                        fieldId
                )

                stringTranslations[id] = stringTranslation
            }

            return stringTranslations
        }
    }

    val scripts: List<Script> by lazy {
        logger.debug("Lazy initialize scripts...")
        fetchScripts()
    }

    private fun fetchScripts(): List<Script> {
        DriverManager.getConnection(Configuration.databaseConnectionString).use { connection ->
            val sqlStatement = connection.createStatement()
            val resultSet = sqlStatement.executeQuery("SELECT id, folder, type, name, script FROM [Scripts]")

            val scripts = mutableListOf<Script>()

            while (resultSet.next()) {
                val id = resultSet.getInt("id")
                val folderId = resultSet.getInt("folder")
                val type = resultSet.getInt("type")
                val name = resultSet.getString("name")
                val content = resultSet.getString("script")

                val script = Script(
                        id,
                        folderId,
                        lazy { folders },
                        type,
                        name,
                        content)

                scripts.add(script)
            }

            return scripts
        }
    }

    val folders: Map<Int, Folder> by lazy {
        logger.debug("Lazy initialize folders...")
        fetchFolders()
    }

    private fun fetchFolders(): Map<Int, Folder> {
        DriverManager.getConnection(Configuration.databaseConnectionString).use { connection ->
            val sqlStatement = connection.createStatement()
            val resultSet = sqlStatement.executeQuery("SELECT id, name, parent, term_singular, term_plural, alias FROM [ProblemArea]")

            val folders = hashMapOf<Int, Folder>()

            while (resultSet.next()) {
                val id = resultSet.getInt("id")
                val name = resultSet.getString("name")
                val parentId = resultSet.getInt("parent")
                val singularTerm = resultSet.getString("term_singular")
                val pluralTerm = resultSet.getString("term_plural")
                val alias = resultSet.getString("alias")

                val folder = Folder(
                        id,
                        name,
                        parentId,
                        singularTerm,
                        pluralTerm,
                        alias,
                        lazy { folders }
                )

                folders[folder.id] = folder
            }

            return folders
        }
    }
}
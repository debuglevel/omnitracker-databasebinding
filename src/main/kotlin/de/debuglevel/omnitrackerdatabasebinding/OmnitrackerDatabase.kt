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
        logger.debug { "Initializing OMNITRACKER DatabaseBinding..." }

        // MSSQL driver need to be loaded explicitly
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver")
    }

    val fields: Map<Int, Field> by lazy {
        logger.debug("Lazy initializing fields...")
        fetchFields()
    }

    val stringTranslations: Map<Int, StringTranslation> by lazy {
        logger.debug("Lazy initializing stringTranslations...")
        fetchStringTranslations()
    }

    val scripts: Map<Int, Script> by lazy {
        logger.debug("Lazy initializing scripts...")
        fetchScripts()
    }

    val folders: Map<Int, Folder> by lazy {
        logger.debug("Lazy initializing folders...")
        fetchFolders()
    }

    private fun fetchFields(): Map<Int, Field> {
        DriverManager.getConnection(Configuration.databaseConnectionString).use { connection ->
            val sqlStatement = connection.createStatement()
            val resultSet = sqlStatement.executeQuery("SELECT id, area, label, remark, type, alias, subtype, max_size, refobj_key FROM [UserFieldDef]")

            val fields = hashMapOf<Int, Field>()

            while (resultSet.next()) {
                val id = resultSet.getInt("id")
                val folderId = resultSet.getInt("area")
                // some text fields are CHAR() instead of VARCHAR() and have spaces at the end therefore, which have to be removed.
                val label = resultSet.getString("label").trimEnd()
                val remark = resultSet.getString("remark")?.trimEnd()
                val typeId = resultSet.getInt("type")
                val alias = resultSet.getString("alias")?.trimEnd()
                val subtype = resultSet.getInt("subtype")
                val maxSize = resultSet.getInt("max_size")
                val referenceFolderId = resultSet.getInt("refobj_key")

                val field = Field(
                        id,
                        alias,
                        label,
                        remark,
                        maxSize,
                        typeId,
                        subtype,
                        folderId,
                        referenceFolderId,
                        lazy { folders },
                        lazy { stringTranslations }
                )

                fields[id] = field
            }

            return fields
        }
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
                val languageCode = resultSet.getString("langcode")
                val text = resultSet.getString("txt") ?: null
                val untranslated = resultSet.getBoolean("untranslated")

                val stringTranslation = StringTranslation(
                        id,
                        guid,
                        languageCode,
                        text,
                        untranslated,
                        type,
                        fieldId,
                        folderId,
                        lazy { fields },
                        lazy { folders }
                )

                stringTranslations[id] = stringTranslation
            }

            return stringTranslations
        }
    }

    private fun fetchScripts(): Map<Int, Script> {
        DriverManager.getConnection(Configuration.databaseConnectionString).use { connection ->
            val sqlStatement = connection.createStatement()
            val resultSet = sqlStatement.executeQuery("SELECT id, folder, type, name, script FROM [Scripts]")

            val scripts = hashMapOf<Int, Script>()

            while (resultSet.next()) {
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
                        folderId,
                        lazy { folders })

                scripts[id] = script
            }

            return scripts
        }
    }

    private fun fetchFolders(): Map<Int, Folder> {
        DriverManager.getConnection(Configuration.databaseConnectionString).use { connection ->
            val sqlStatement = connection.createStatement()
            val resultSet = sqlStatement.executeQuery("SELECT id, name, parent, term_singular, term_plural, alias FROM [ProblemArea]")

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
                        parentFolderId,
                        lazy { folders },
                        lazy { fields }
                )

                folders[folder.id] = folder
            }

            return folders
        }
    }
}
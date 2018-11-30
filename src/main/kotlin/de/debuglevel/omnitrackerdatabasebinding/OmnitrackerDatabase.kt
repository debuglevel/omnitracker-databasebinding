package de.debuglevel.omnitrackerdatabasebinding

import com.natpryce.konfig.*
import de.debuglevel.omnitrackerdatabasebinding.models.Field
import de.debuglevel.omnitrackerdatabasebinding.models.Folder
import de.debuglevel.omnitrackerdatabasebinding.models.Script
import de.debuglevel.omnitrackerdatabasebinding.models.StringTranslation
import java.io.File
import java.sql.DriverManager

class OmnitrackerDatabase {
    val db_connection_string by stringType

    val config =
            ConfigurationProperties.systemProperties() overriding
            EnvironmentVariables() overriding
            ConfigurationProperties.fromOptionalFile(File("configuration.properties"))

    init {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver")
    }

    fun getFields(): List<Field> {
        val folders = getFolders().associateBy { it.id }

        DriverManager.getConnection(config[db_connection_string]).use { connection ->
            val sqlStatement = connection.createStatement()
            val resultSet = sqlStatement.executeQuery("SELECT id, area, label, remark, type, alias, subtype, max_size, refobj_key FROM [UserFieldDef]")

            val fields = mutableListOf<Field>()

            while (resultSet.next()) {
                val id = resultSet.getInt("id")
                val folderId = resultSet.getInt("area")
                val folder = folders.getValue(folderId)
                val label = resultSet.getString("label")
                val remark = resultSet.getString("remark")
                val type = resultSet.getInt("type")
                val alias = resultSet.getString("alias")
                val subtype = resultSet.getInt("subtype")
                val maxSize = resultSet.getInt("max_size")
                val referenceFolderId = resultSet.getInt("refobj_key")

                val field = Field(
                        id,
                        folder,
                        label,
                        remark,
                        type,
                        alias,
                        subtype,
                        maxSize,
                        referenceFolderId,
                        folders
                        )

                fields.add(field)
            }

            return fields
        }
    }

    fun getStringTranslations(): List<StringTranslation> {
        val folders = getFolders().associateBy { it.id }
        val fields = getFields().associateBy { it.id }

        DriverManager.getConnection(config[db_connection_string]).use { connection ->
            val sqlStatement = connection.createStatement()
            val resultSet = sqlStatement.executeQuery("SELECT id, str_guid, type, ref, field, folder, langcode, txt, untranslated FROM [StringTranslations]")

            val stringTranslations = mutableListOf<StringTranslation>()

            while (resultSet.next()) {
                val id = resultSet.getInt("id")
                val guid = resultSet.getString("str_guid")
                val type = resultSet.getInt("type")
                val ref = resultSet.getInt("ref")
                //val field = resultSet.getInt("field")
                val field = fields[ref]
                val folderId = resultSet.getInt("folder")
                val folder = folders[folderId]
                val langCode = resultSet.getString("langcode")
                val text = resultSet.getString("txt") ?: null
                val untranslated = resultSet.getBoolean("untranslated")

                val stringTranslation = StringTranslation(
                        id,
                        guid,
                        folder,
                        langCode,
                        text,
                        untranslated,
                        type,
                        field
                )

                stringTranslations.add(stringTranslation)
            }

            return stringTranslations
        }
    }

    fun getScripts(): List<Script> {
        val folders = getFolders().associateBy { it.id }

        DriverManager.getConnection(config[db_connection_string]).use { connection ->
            val sqlStatement = connection.createStatement()
            val resultSet = sqlStatement.executeQuery("SELECT id, folder, type, name, script FROM [Scripts]")

            val scripts = mutableListOf<Script>()

            while (resultSet.next()) {
                val id = resultSet.getInt("id")
                val folderId = resultSet.getInt("folder")
                val folder = folders[folderId]
                val type = resultSet.getInt("type")
                val name = resultSet.getString("name")
                val content = resultSet.getString("script")

                val script = Script(
                        id,
                        folder,
                        type,
                        name,
                        content)

                scripts.add(script)
            }

            return scripts
        }
    }

    fun getFolders(): List<Folder> {
        DriverManager.getConnection(config[db_connection_string]).use { connection ->
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
                        folders
                )

                folders[folder.id] = folder
            }

            // populate fields

            return folders.map { it.value }
        }
    }
}
package de.debuglevel.omnitrackerdatabasebinding.stringtranslation

import de.debuglevel.omnitrackerdatabasebinding.DatabaseService
import de.debuglevel.omnitrackerdatabasebinding.entity.ColumnType
import de.debuglevel.omnitrackerdatabasebinding.entity.SqlBuilderService
import mu.KotlinLogging
import java.sql.Connection
import java.sql.ResultSet
import javax.inject.Singleton

@Singleton
class StringTranslationService(
    private val databaseService: DatabaseService,
    private val sqlBuilderService: SqlBuilderService
    //private val folderService: FolderService,
    //private val fieldService: FieldService
) {
    private val logger = KotlinLogging.logger {}

    val columns = mapOf(
        "id" to ColumnType.Integer,
        "str_guid" to ColumnType.String,
        "type" to ColumnType.Integer,
        "ref" to ColumnType.Integer,
        "field" to ColumnType.Integer,
        "folder" to ColumnType.Integer,
        "langcode" to ColumnType.String,
        "txt" to ColumnType.String,
        "untranslated" to ColumnType.Boolean
    )

    private fun getStringTranslations(
        short: Boolean,
        connection: Connection,
        folderId: Int?
    ): MutableList<StringTranslation> {
        logger.trace { "Getting stringTranslations (short=$short, folderId=$folderId)..." }

        val table = if (short) "StringTransShort" else "StringTranslations"
        val databaseType = sqlBuilderService.getDatabaseType(connection)
        val stringTranslationQuery = when (folderId) {
            null -> sqlBuilderService.buildSelectAllQuery(databaseType, table, columns)
            else -> sqlBuilderService.buildSelectOneQuery(databaseType, table, columns, "folder", folderId)
        }
        val sqlStatement = connection.createStatement()
        val resultSet = sqlStatement.executeQuery(stringTranslationQuery)

        val stringTranslations = mutableListOf<StringTranslation>()

        while (resultSet.next()) {
            val stringTranslation = build(resultSet, short)
            stringTranslations.add(stringTranslation)
        }

        logger.trace { "Got ${stringTranslations.size} stringTranslations (short=$short, folderId=$folderId)" }
        return stringTranslations
    }

//    fun getStringTranslation(id: Int): StringTranslation
//    {
//        return getStringTranslations().getValue(id)
//    }

    private fun build(
        resultSet: ResultSet,
        short: Boolean
    ): StringTranslation {
        //logger.debug { "Building stringTranslation for ResultSet $resultSet ..." }

        val id = resultSet.getInt("id")
        val guid = resultSet.getString("str_guid")
        val type = resultSet.getInt("type")
        val fieldId = resultSet.getInt("ref")
        val folderId = resultSet.getInt("folder")
        val languageCode = resultSet.getString("langcode")
        val textRaw = resultSet.getString("txt") ?: null
        val text = when {
            short -> textRaw?.trimEnd()
            else -> textRaw
        }
        val isUntranslated = resultSet.getBoolean("untranslated")

        val stringTranslation = StringTranslation(
            id,
            guid,
            languageCode,
            text,
            isUntranslated,
            short,
            type,
            fieldId,
            folderId
            //                lazy { fields },
            //                lazy { folders }
        )

        //logger.debug { "Built stringTranslation: $stringTranslation" }
        return stringTranslation
    }

    private fun fetchStringTranslations(folderId: Int? = null): List<StringTranslation> {
        logger.trace { "Fetching stringTranslations (folderId=$folderId)..." }

        val stringTranslations = databaseService.getConnection().use { connection ->
            getStringTranslations(true, connection, folderId)
                .plus(getStringTranslations(false, connection, folderId))
        }

        logger.trace { "Fetched ${stringTranslations.size} stringTranslations (folderId=$folderId)" }
        return stringTranslations
    }

    private var cachedStringTranslations: List<StringTranslation>? = null

    fun getStringTranslations(folderId: Int): List<StringTranslation> {
        logger.trace { "Getting stringTranslations (folderId=$folderId)..." }

        if (cachedStringTranslations == null) {
            cachedStringTranslations = fetchStringTranslations()
        }

        val stringTranslations = cachedStringTranslations!!.filter { it.folderId == folderId }

        logger.trace { "Got ${stringTranslations.size} stringTranslations (folderId=$folderId)" }
        return stringTranslations
    }
}
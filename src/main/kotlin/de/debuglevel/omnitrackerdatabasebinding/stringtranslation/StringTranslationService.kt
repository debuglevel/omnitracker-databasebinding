package de.debuglevel.omnitrackerdatabasebinding.stringtranslation

import de.debuglevel.omnitrackerdatabasebinding.DatabaseService
import mu.KotlinLogging
import java.sql.Connection
import java.sql.ResultSet
import javax.inject.Singleton

@Singleton
class StringTranslationService(
    private val databaseService: DatabaseService
    //private val folderService: FolderService,
    //private val fieldService: FieldService
) {
    private val logger = KotlinLogging.logger {}

    private fun getStringTranslations(
        short: Boolean,
        connection: Connection,
        folderId: Int?
    ): MutableList<StringTranslation> {
        logger.debug { "Getting stringTranslations (short=$short, folderId=$folderId)..." }

        val table = if (short) "StringTransShort" else "StringTranslations"
        val sqlStatement = connection.createStatement()
        val stringTranslationQuery =
            if (folderId == null) {
                "SELECT id, str_guid, type, ref, field, folder, langcode, txt, untranslated FROM [$table]"
            } else {
                "SELECT id, str_guid, type, ref, field, folder, langcode, txt, untranslated FROM [$table] WHERE folder=$folderId"
            }
        val resultSet = sqlStatement.executeQuery(stringTranslationQuery)

        val stringTranslations = mutableListOf<StringTranslation>()

        while (resultSet.next()) {
            val stringTranslation = build(resultSet, short)
            stringTranslations.add(stringTranslation)
        }

        logger.debug { "Got ${stringTranslations.size} stringTranslations (short=$short, folderId=$folderId)" }
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
        logger.debug { "Fetching stringTranslations (folderId=$folderId)..." }

        val stringTranslations = databaseService.getConnection().use { connection ->
            getStringTranslations(true, connection, folderId)
                .plus(getStringTranslations(false, connection, folderId))
        }

        logger.debug { "Fetched ${stringTranslations.size} stringTranslations (folderId=$folderId)" }
        return stringTranslations
    }

    private var cachedStringTranslations: List<StringTranslation>? = null

    fun getStringTranslations(folderId: Int): List<StringTranslation> {
        logger.debug { "Getting stringTranslations (folderId=$folderId)..." }

        if (cachedStringTranslations == null) {
            cachedStringTranslations = fetchStringTranslations()
        }

        val stringTranslations = cachedStringTranslations!!.filter { it.folderId == folderId }

        logger.debug { "Got ${stringTranslations.size} stringTranslations (folderId=$folderId)" }
        return stringTranslations
    }
}
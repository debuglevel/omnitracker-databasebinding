package de.debuglevel.omnitrackerdatabasebinding.field

import de.debuglevel.omnitrackerdatabasebinding.DatabaseService
import mu.KotlinLogging
import java.sql.ResultSet
import javax.inject.Singleton

@Singleton
class FieldService(
    private val databaseService: DatabaseService
    //private val stringTranslationService: StringTranslationService
) {
    private val logger = KotlinLogging.logger {}

    private val fieldQuery =
        "SELECT id, area, label, remark, type, alias, subtype, max_size, refobj_key FROM [UserFieldDef]"

    fun getFields(/*folders: Map<Int, Folder>*/): Map<Int, Field> {
        databaseService.getConnection().use { connection ->
            val sqlStatement = connection.createStatement()
            val resultSet =
                sqlStatement.executeQuery(fieldQuery)

            val fields = hashMapOf<Int, Field>()

            while (resultSet.next()) {
                val field = buildField(resultSet)
                fields[field.id] = field
            }

            return fields
        }
    }

    fun getField(id: Int): Field {
        return getFields().getValue(id)
    }

    private fun buildField(resultSet: ResultSet): Field {
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
            referenceFolderId
            //                    lazy { folders },
            //                    lazy { stringTranslationService.fetchStringTranslations(folders, fields) }
        )
        return field
    }
}
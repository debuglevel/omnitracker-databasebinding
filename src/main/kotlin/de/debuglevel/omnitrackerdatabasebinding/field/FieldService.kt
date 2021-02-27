package de.debuglevel.omnitrackerdatabasebinding.field

import de.debuglevel.omnitrackerdatabasebinding.DatabaseService
import de.debuglevel.omnitrackerdatabasebinding.entity.EntityService
import mu.KotlinLogging
import java.sql.ResultSet
import javax.inject.Singleton

@Singleton
class FieldService(
    databaseService: DatabaseService
    //private val stringTranslationService: StringTranslationService
) : EntityService<Field>(databaseService) {
    private val logger = KotlinLogging.logger {}

    override val name = "Field"
    override val getAllQuery =
        "SELECT " +
                "id, " +
                "area, " +
                "label, " +
                "remark, " +
                "type, " +
                "alias, " +
                "subtype, " +
                "max_size, " +
                "indexDB, " +
                "refobj_key" +
                " FROM [UserFieldDef]"

    override fun build(resultSet: ResultSet): Field {
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
        val isDatabaseIndexed = resultSet.getBoolean("indexDB")

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
            isDatabaseIndexed
            //                    lazy { folders },
            //                    lazy { stringTranslationService.fetchStringTranslations(folders, fields) }
        )
        return field
    }
}
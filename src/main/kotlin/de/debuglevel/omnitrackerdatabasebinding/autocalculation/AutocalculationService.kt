package de.debuglevel.omnitrackerdatabasebinding.autocalculation

import de.debuglevel.omnitrackerdatabasebinding.DatabaseService
import de.debuglevel.omnitrackerdatabasebinding.entity.ColumnType
import de.debuglevel.omnitrackerdatabasebinding.entity.EntityService
import de.debuglevel.omnitrackerdatabasebinding.field.FieldService
import de.debuglevel.omnitrackerdatabasebinding.folder.FolderService
import de.debuglevel.omnitrackerdatabasebinding.script.ScriptService
import mu.KotlinLogging
import java.sql.ResultSet
import javax.inject.Singleton

@Singleton
class AutocalculationService(
    private val databaseService: DatabaseService,
    private val folderService: FolderService,
    private val scriptService: ScriptService,
    private val fieldService: FieldService
) : EntityService<Autocalculation>(databaseService) {
    private val logger = KotlinLogging.logger {}

    override val name = "AutoCalculation"
    override val table = "SummaryDef"
    override val columns = mapOf(
        "id" to ColumnType.Integer,
        "name" to ColumnType.String,
        "type" to ColumnType.Integer,
        "folder" to ColumnType.Integer,
        "ref_field" to ColumnType.Integer,
        "script" to ColumnType.Integer,
        "mod_obj_flags" to ColumnType.Integer,
        "recalc_after_move" to ColumnType.Integer,
        "store_zero" to ColumnType.Integer
    )

    override fun build(resultSet: ResultSet): Autocalculation {
        logger.trace { "Building Autocalculation for ResultSet $resultSet..." }

        val id = resultSet.getInt("id")
        val name = resultSet.getString("name")
        val typeId = resultSet.getInt("type")
        val folderId = resultSet.getInt("folder")
        val fieldId = resultSet.getInt("ref_field")
        val scriptId = resultSet.getInt("script")
        val modObjFlags = resultSet.getInt("mod_obj_flags")
        val recalcAfterMove = resultSet.getBoolean("recalc_after_move")
        val storeZero = resultSet.getBoolean("store_zero")

        val folder = folderService.get(folderId)

        val layout = Autocalculation(
            id,
            name,
            typeId,
            folderId,
            folder,
            fieldId,
            scriptId,
            modObjFlags,
            recalcAfterMove,
            storeZero
        )

        logger.trace { "Built kpi: $layout" }
        return layout
    }
}
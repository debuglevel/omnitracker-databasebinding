package de.debuglevel.omnitrackerdatabasebinding.kpi

import de.debuglevel.omnitrackerdatabasebinding.DatabaseService
import de.debuglevel.omnitrackerdatabasebinding.entity.EntityService
import de.debuglevel.omnitrackerdatabasebinding.folder.FolderService
import mu.KotlinLogging
import java.sql.ResultSet
import javax.inject.Singleton

@Singleton
class KpiService(
    private val databaseService: DatabaseService,
    private val folderService: FolderService
) : EntityService<Kpi>(databaseService) {
    private val logger = KotlinLogging.logger {}

    override val name = "KPI"
    override val query =
        "SELECT " +
                "id, " +
                "folder, " +
                "name, " +
                "alias, " +
                "measure_method, " +
                "recursive, " +
                "group_field, " +
                "script, " +
                "max_history, " +
                "trend_count, " +
                "trend_units, " +
                "trend_reference, " +
                "active, " +
                "kpi_event, " +
                "aggregate_period, " +
                "aggregate_unit, " +
                "aggregation_type, " +
                "bpmn_kpi_unit " +
                " FROM [KpiDef]"

    override fun build(resultSet: ResultSet): Kpi {
        logger.trace { "Building kpi for ResultSet $resultSet..." }

        val id = resultSet.getInt("id")
        val folderId = resultSet.getInt("folder")
        val name = resultSet.getString("name")
        val alias = resultSet.getString("alias")
        val measureMethod = resultSet.getInt("measure_method")
        val recursive = resultSet.getBoolean("recursive")
        val groupFieldId = resultSet.getInt("group_field")
        val scriptId = resultSet.getInt("script")
        val maxHistory = resultSet.getInt("max_history")
        val trendCount = resultSet.getInt("trend_count")
        val trendUnits = resultSet.getInt("trend_units")
        val trendReference = resultSet.getInt("trend_reference")
        val active = resultSet.getBoolean("active")
        val kpiEvent = resultSet.getInt("kpi_event")
        val aggregatePeriod = resultSet.getInt("aggregate_period")
        val aggregateUnit = resultSet.getInt("aggregate_unit")
        val aggregationType = resultSet.getInt("aggregation_type")
        val bpmnKpiUnit = resultSet.getInt("bpmn_kpi_unit")

        val folder = folderService.get(folderId)

        val layout = Kpi(
            id,
            folderId,
            folder,
            name,
            alias,
            measureMethod,
            recursive,
            groupFieldId,
            scriptId,
            maxHistory,
            trendCount,
            trendUnits,
            trendReference,
            active,
            kpiEvent,
            aggregatePeriod,
            aggregateUnit,
            aggregationType,
            bpmnKpiUnit
        )

        logger.trace { "Built kpi: $layout" }
        return layout
    }
}
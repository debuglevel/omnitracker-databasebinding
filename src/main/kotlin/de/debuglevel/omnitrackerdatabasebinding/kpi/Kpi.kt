package de.debuglevel.omnitrackerdatabasebinding.kpi

import de.debuglevel.omnitrackerdatabasebinding.entity.Entity
import de.debuglevel.omnitrackerdatabasebinding.folder.Folder

data class Kpi(
    override val id: Int,
    val folderId: Int,
    val folder: Folder,
    val name: String,
    val alias: String,
    val measureMethod: Int,
    val recursive: Boolean,
    val groupFieldId: Int,
    val scriptId: Int,
    val maxHistory: Int,
    val trendCount: Int,
    val trendUnits: Int,
    val trendReference: Int,
    val active: Boolean,
    val kpiEvent: Int,
    val aggregatePeriod: Int,
    val aggregateUnit: Int,
    val aggregationType: Int,
    val bpmnKpiUnit: Int
) : Entity {
    override fun hashCode() = this.id

    override fun toString(): String {
        return "Kpi(" +
                "id=$id, " +
                "folderId=$folderId, " +
                "name='$name', " +
                "alias='$alias', " +
                "measureMethod=$measureMethod, " +
                "recursive=$recursive, " +
                "groupFieldId=$groupFieldId, " +
                "scriptId=$scriptId, " +
                "maxHistory=$maxHistory, " +
                "trendCount=$trendCount, " +
                "trendUnits=$trendUnits, " +
                "trendReference=$trendReference, " +
                "active=$active, " +
                "kpiEvent=$kpiEvent, " +
                "aggregatePeriod=$aggregatePeriod, " +
                "aggregateUnit=$aggregateUnit, " +
                "aggregationType=$aggregationType, " +
                "bpmnKpiUnit=$bpmnKpiUnit" +
                ")"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Kpi

        if (id != other.id) return false
        if (folderId != other.folderId) return false
        if (name != other.name) return false
        if (alias != other.alias) return false
        if (measureMethod != other.measureMethod) return false
        if (recursive != other.recursive) return false
        if (groupFieldId != other.groupFieldId) return false
        if (scriptId != other.scriptId) return false
        if (maxHistory != other.maxHistory) return false
        if (trendCount != other.trendCount) return false
        if (trendUnits != other.trendUnits) return false
        if (trendReference != other.trendReference) return false
        if (active != other.active) return false
        if (kpiEvent != other.kpiEvent) return false
        if (aggregatePeriod != other.aggregatePeriod) return false
        if (aggregateUnit != other.aggregateUnit) return false
        if (aggregationType != other.aggregationType) return false
        if (bpmnKpiUnit != other.bpmnKpiUnit) return false

        return true
    }
}
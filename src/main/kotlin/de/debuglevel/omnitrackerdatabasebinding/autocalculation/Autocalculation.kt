package de.debuglevel.omnitrackerdatabasebinding.autocalculation

import de.debuglevel.omnitrackerdatabasebinding.entity.Entity
import de.debuglevel.omnitrackerdatabasebinding.folder.Folder

data class Autocalculation(
    override val id: Int,
    val name: String,
    val typeId: Int,
    val folderId: Int,
    val folder: Folder,
    val fieldId: Int,
    val scriptId: Int,
    val modObjFlags: Int,
    val recalcAfterMove: Boolean,
    val storeZero: Boolean
) : Entity {
    override fun hashCode() = this.id

    override fun toString(): String {
        return "Autocalculation(" +
                "id=$id, " +
                "name='$name', " +
                "typeId=$typeId, " +
                "folderId=$folderId, " +
                "fieldId=$fieldId, " +
                "scriptId=$scriptId, " +
                "modObjFlags=$modObjFlags, " +
                "recalcAfterMove=$recalcAfterMove, " +
                "storeZero=$storeZero" +
                ")"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Autocalculation

        if (id != other.id) return false
        if (name != other.name) return false
        if (typeId != other.typeId) return false
        if (folderId != other.folderId) return false
        if (fieldId != other.fieldId) return false
        if (scriptId != other.scriptId) return false
        if (modObjFlags != other.modObjFlags) return false
        if (recalcAfterMove != other.recalcAfterMove) return false
        if (storeZero != other.storeZero) return false

        return true
    }


}
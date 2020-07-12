package de.debuglevel.omnitrackerdatabasebinding.databaseview

import de.debuglevel.omnitrackerdatabasebinding.entity.Entity
import de.debuglevel.omnitrackerdatabasebinding.folder.Folder

data class DatabaseView(
    override val id: Int,
    val viewName: String,
    val folderId: Int,
    val folder: Folder?,
    val isIncludingSubfolders: Boolean
) : Entity {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DatabaseView

        if (id != other.id) return false
        if (viewName != other.viewName) return false
        if (folderId != other.folderId) return false
        if (isIncludingSubfolders != other.isIncludingSubfolders) return false

        return true
    }

    override fun hashCode(): Int {
        return id
    }

    override fun toString(): String {
        return "DatabaseView(" +
                "id=$id, " +
                "viewName='$viewName', " +
                "folderId=$folderId, " +
                "isIncludingSubfolders=$isIncludingSubfolders" +
                ")"
    }
}
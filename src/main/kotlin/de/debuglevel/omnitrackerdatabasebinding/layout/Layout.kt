package de.debuglevel.omnitrackerdatabasebinding.layout

import de.debuglevel.omnitrackerdatabasebinding.entity.Entity
import de.debuglevel.omnitrackerdatabasebinding.folder.Folder
import java.util.*

data class Layout(
    override val id: Int,
    val name: String,
    val crystalreportsVersion: Int,
    val reportDataBase64: String,
    val mailmergeSql: String?,
    val isCrystalreportsReplaceMdb: Boolean,
    val crystalreportsStaticDatabaseConnection: String?,
    val typeId: Int,
    val outputTypeId: Int,
    val mailmergeDoctypeId: Int,
    val mailmergeFiletypeId: Int,
    val folderId: Int,
    val folder: Folder
    //private val folderMap: Lazy<Map<Int, Folder>>
) : Entity {
//    val folder: Folder?
//        get() = folderMap.value[folderId]

    val type: LayoutType?
        get() = LayoutType.values().firstOrNull { it.id == typeId }

    val mailmergeFiletype: MailmergeFiletype?
        get() = MailmergeFiletype.values().firstOrNull { it.id == mailmergeFiletypeId }

    val outputType: LayoutOutputType?
        get() = LayoutOutputType.values().firstOrNull { it.id == outputTypeId }

    val reportData: ByteArray
        get() = Base64.getMimeDecoder().decode(reportDataBase64)

    override fun hashCode() = this.id

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Layout

        if (id != other.id) return false
        if (name != other.name) return false
        if (crystalreportsVersion != other.crystalreportsVersion) return false
        if (mailmergeSql != other.mailmergeSql) return false
        if (isCrystalreportsReplaceMdb != other.isCrystalreportsReplaceMdb) return false
        if (crystalreportsStaticDatabaseConnection != other.crystalreportsStaticDatabaseConnection) return false
        if (typeId != other.typeId) return false
        if (outputTypeId != other.outputTypeId) return false
        if (mailmergeDoctypeId != other.mailmergeDoctypeId) return false
        if (mailmergeFiletypeId != other.mailmergeFiletypeId) return false
        if (folderId != other.folderId) return false

        return true
    }

    override fun toString(): String {
        return "Layout(" +
                "id=$id, " +
                "name='$name', " +
                "crystalreportsVersion=$crystalreportsVersion, " +
                "mailmergeSql='$mailmergeSql', " +
                "crReplaceMdb=$isCrystalreportsReplaceMdb, " +
                "crStaticDbConn='$crystalreportsStaticDatabaseConnection', " +
                "typeId=$typeId, " +
                "outputTypeId=$outputTypeId, " +
                "mailmergeDoctypeId=$mailmergeDoctypeId, " +
                "mailmergeFiletypeId=$mailmergeFiletypeId, " +
                "folderId=$folderId" +
                ")"
    }
}
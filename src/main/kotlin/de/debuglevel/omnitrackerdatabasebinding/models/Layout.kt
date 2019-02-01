package de.debuglevel.omnitrackerdatabasebinding.models

import java.util.*

data class Layout(val id: Int,
                  val name: String,
                  val version: Int,
                  val reportDataBase64: String,
                  val mailmergeSql: String?,
                  val crReplaceMdb: Int,
                  val crStaticDbConn: String?,
                  private val typeId: Int,
                  private val outputType: Int,
                  private val mailmergeDoctypeId: Int,
                  private val mailmergeFiletypeId: Int,
                  private val folderId: Int,
                  private val folderMap: Lazy<Map<Int, Folder>>
) {
    val folder: Folder?
        get() = folderMap.value[folderId]

    val type: LayoutType?
        get() = LayoutType.values().firstOrNull { it.id == typeId }

    val mailmergeFiletype: MailmergeFiletype?
        get() = MailmergeFiletype.values().firstOrNull { it.id == mailmergeFiletypeId }

    val reportData: ByteArray
        get() = Base64.getDecoder().decode(reportDataBase64)

    override fun hashCode() = this.id

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Layout

        if (id != other.id) return false
        if (name != other.name) return false
        if (version != other.version) return false
        if (mailmergeSql != other.mailmergeSql) return false
        if (crReplaceMdb != other.crReplaceMdb) return false
        if (crStaticDbConn != other.crStaticDbConn) return false
        if (typeId != other.typeId) return false
        if (outputType != other.outputType) return false
        if (mailmergeDoctypeId != other.mailmergeDoctypeId) return false
        if (mailmergeFiletypeId != other.mailmergeFiletypeId) return false
        if (folderId != other.folderId) return false

        return true
    }

    override fun toString(): String {
        return "Layout(" +
                "id=$id, " +
                "name='$name', " +
                "version=$version, " +
                "mailmergeSql='$mailmergeSql', " +
                "crReplaceMdb=$crReplaceMdb, " +
                "crStaticDbConn='$crStaticDbConn', " +
                "typeId=$typeId, " +
                "outputType=$outputType, " +
                "mailmergeDoctypeId=$mailmergeDoctypeId, " +
                "mailmergeFiletypeId=$mailmergeFiletypeId, " +
                "folderId=$folderId" +
                ")"
    }
}
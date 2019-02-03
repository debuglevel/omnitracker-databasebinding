package de.debuglevel.omnitrackerdatabasebinding.models

data class Script(
    val id: Int,
    val name: String,
    val content: String?,
    private val typeId: Int,
    private val folderId: Int,
    private val folderMap: Lazy<Map<Int, Folder>>
) {
    val folder: Folder?
        get() = folderMap.value[folderId]

    val type: ScriptType?
        get() = ScriptType.values().firstOrNull { it.id == typeId }

    override fun hashCode() = this.id

    override fun equals(other: Any?): Boolean {
        return when {
            other == null -> false
            this === other -> true
            other is Script -> {
                val o = other
                this.id == o.id &&
                        this.folderId == o.folderId &&
                        this.typeId == o.typeId &&
                        this.name == o.name &&
                        this.content == o.content
            }
            else -> false
        }
    }

    override fun toString(): String {
        return "Script(" +
                "id=$id," +
                "folder=${folder?.alias}," +
                "typeId=$typeId," +
                "name='$name'," +
//                "content=$content" +
                ")"
    }
}
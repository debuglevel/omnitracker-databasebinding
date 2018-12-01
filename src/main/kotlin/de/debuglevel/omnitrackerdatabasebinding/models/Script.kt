package de.debuglevel.omnitrackerdatabasebinding.models

data class Script(val id: Int,
                  private val folderId: Int,
                  private val folderMap: Lazy<Map<Int, Folder>>,
        //val folder: Folder?,
                  val type: Int,
                  val name: String,
                  val content: String? /* database column "script" */
) {
    val folder: Folder get() = folderMap.value.getValue(folderId)

    override fun toString() = "$id (${folder.alias}): $name"

    override fun hashCode() = this.id
}
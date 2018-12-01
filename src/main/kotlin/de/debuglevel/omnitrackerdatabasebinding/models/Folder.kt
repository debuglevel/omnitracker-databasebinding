package de.debuglevel.omnitrackerdatabasebinding.models

data class Folder(val id: Int,
                  val name: String,
                  val alias: String? = null,
                  val singularTerm: String? = null,
                  val pluralTerm: String? = null,
                  private val parentFolderId: Number,
                  private val folderMap: Lazy<Map<Int, Folder>>,
                  private val fieldMap: Lazy<Map<Int, Field>>
) {
    val fields: Map<Int, Field>
        get() = fieldMap.value.filter { it.value.folder == this }

    val parentFolder: Folder?
        get() = folderMap.value[this.parentFolderId]

    override fun hashCode() = this.id

    override fun equals(other: Any?): Boolean {
        return when {
            other == null -> false
            this === other -> true
            other is Folder -> {
                val o = other
                this.id == o.id &&
                        this.name == o.name &&
                        this.parentFolderId == o.parentFolderId &&
                        this.singularTerm == o.singularTerm &&
                        this.pluralTerm == o.pluralTerm &&
                        this.alias == o.alias
            }
            else -> false
        }
    }

    override fun toString(): String {
        return "Folder(" +
                "id=$id," +
                "name='$name'," +
                "parentFolder=${parentFolder?.alias}," +
                "alias=$alias" +
                ")"
    }
}
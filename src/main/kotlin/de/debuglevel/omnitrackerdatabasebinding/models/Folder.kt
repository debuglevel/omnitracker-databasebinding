package de.debuglevel.omnitrackerdatabasebinding.models

data class Folder(val id: Int,
                  val name: String,
                  private val parentId: Number,
                  val singularTerm: String? = null,
                  val pluralTerm: String? = null,
                  val alias: String? = null,
                  private val folderMap: Lazy<HashMap<Int, Folder>>
)
{
    val fields = listOf<Field>()

    val parentFolder: Folder?
        get() = folderMap.value[this.parentId]

    override fun toString() = "$id: $name ($alias) [Parent: ${parentFolder?.alias}]"

    override fun hashCode() = this.id

    override fun equals(other: Any?): Boolean {
        return when {
            other == null -> false
            this === other -> true
            other is Folder -> {
                val o = other
                this.id == o.id &&
                        this.name == o.name &&
                        this.parentId == o.parentId &&
                        this.singularTerm == o.singularTerm &&
                        this.pluralTerm == o.pluralTerm &&
                        this.alias == o.alias
            }
            else -> false
        }
    }
}
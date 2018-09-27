package de.debuglevel.omnitrackerdatabasebinding.models

import kotlin.jvm.internal.Intrinsics

data class Folder(val id: Int,
                  val name: String,
                  val parentId: Number,
                  val singularTerm: String? = null,
                  val pluralTerm: String? = null,
                  val alias: String? = null,
                  val folderMap: HashMap<Int, Folder>
)
{
    val fields = listOf<Field>()

    val parentFolder: Folder?
        get() = folderMap[this.parentId]

    override fun toString() = "$id: $name ($alias)"

    override fun hashCode() = this.id

    override fun equals(other: Any?): Boolean {
        return when {
            other == null -> false
            this === other -> true
            other is Folder -> {
                val o = other as Folder
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
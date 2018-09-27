package de.debuglevel.omnitrackerdatabasebinding.models

data class Script(val id: Int,
                  val folder: Folder?,
                  val type: Int,
                  val name: String,
                  val content: String? /* database column "script" */
)
{
    override fun toString() = "$id: $name"

    override fun hashCode() = this.id
}
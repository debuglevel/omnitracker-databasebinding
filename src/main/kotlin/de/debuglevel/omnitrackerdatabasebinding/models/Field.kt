package de.debuglevel.omnitrackerdatabasebinding.models

data class Field(val id: Int,
                 val folder: Folder,
                 //val folderId: Int, /* database column "area" */
                 val label: String,
                 val remark: String?,
                 val typeId: Int, /* 252 = ref to obj, 254 = list of ref obj */
                 val alias: String?,
                 val subtype: Int,
                 val maxSize: Int,
                 val referenceFolderId: Int?, /* database column "refobj_key" */
                 val folderMap: Map<Int, Folder>
                 )
{
    val type: FieldType?
    get() {
        return FieldType.values().firstOrNull { it.id == typeId }
    }

    val referenceFolder: Folder?
        get() = folderMap[this.referenceFolderId]

    override fun toString(): String {
        var s = "$id: $label ($alias)"
        if (referenceFolder != null) {
            s += " -> ${referenceFolder?.name}"
        }

        return s
    }

    override fun hashCode() = this.id
}
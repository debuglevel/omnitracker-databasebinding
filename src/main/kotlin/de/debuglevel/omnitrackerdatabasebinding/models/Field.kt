package de.debuglevel.omnitrackerdatabasebinding.models

data class Field(val id: Int,
        //val folder: Folder,
                 private val folderId: Int, /* database column "area" */
                 val label: String,
                 val remark: String?,
                 private val typeId: Int, /* 252 = ref to obj, 254 = list of ref obj */
                 val alias: String?,
                 val subtype: Int,
                 val maxSize: Int,
                 private val referenceFolderId: Int?, /* database column "refobj_key" */
                 private val folderMap: Lazy<Map<Int, Folder>>,
                 private val stringTranslationMap: Lazy<Map<Int, StringTranslation>>
) {
    val type: FieldType?
        get() {
            return FieldType.values().firstOrNull { it.id == typeId }
        }

    val folder: Folder get() = folderMap.value.getValue(folderId)

    /**
     * If this field is a ReferenceTo
     */
    val referenceFolder: Folder?
        get() = folderMap.value[this.referenceFolderId]

    override fun toString(): String {
        var s = "$id: $label ($alias) [${folder.alias}]"
        if (referenceFolder != null) {
            s += " -> ${referenceFolder?.name}"
        }

        return s
    }

    override fun hashCode() = this.id
}
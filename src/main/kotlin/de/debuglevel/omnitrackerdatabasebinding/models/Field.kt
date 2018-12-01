package de.debuglevel.omnitrackerdatabasebinding.models

data class Field(val id: Int,
                 val alias: String?,
                 val label: String,
                 val remark: String?,
                 val maxSize: Int,
                 private val typeId: Int,
                 val subtypeId: Int,
                 private val folderId: Int,
                 private val referenceFolderId: Int?,
                 private val folderMap: Lazy<Map<Int, Folder>>,
                 private val stringTranslationMap: Lazy<Map<Int, StringTranslation>>
) {
    val type: FieldType?
        get() = FieldType.values().firstOrNull { it.id == typeId }

    val folder: Folder
        get() = folderMap.value.getValue(folderId)

    /**
     * If this field is a ReferenceTo
     */
    val referenceFolder: Folder?
        get() = folderMap.value[this.referenceFolderId]

    override fun hashCode() = this.id

    override fun equals(other: Any?): Boolean {
        return when {
            other == null -> false
            this === other -> true
            other is Field -> {
                val o = other
                this.id == o.id &&
                        this.folderId == o.folderId &&
                        this.label == o.label &&
                        this.remark == o.remark &&
                        this.typeId == o.typeId &&
                        this.maxSize == o.maxSize
            }
            else -> false
        }
    }

    override fun toString(): String {
        return "Field(" +
                "id=$id," +
                "folder=${folder.alias}," +
                "label='$label'," +
                "type=${type?.name}," +
                "alias=$alias," +
                "referenceFolder=${referenceFolder?.alias}" +
                ")"
    }
}
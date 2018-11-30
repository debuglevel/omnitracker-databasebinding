package de.debuglevel.omnitrackerdatabasebinding.models

data class StringTranslation(val id: Int,
                             val guid: String,
        //val folder: Folder?,
                             private val folderId: Int?,
                             private val folderMap: Lazy<Map<Int, Folder>>,
                             val languageCode: String,
                             val text: String?,
                             val untranslated: Boolean,
                             private val typeId: Int,
                             private val fieldMap: Lazy<Map<Int, Field>>,
                             val fieldId: Int?
        //val refId: Int
) {
    val folder: Folder? get() = folderMap.value[folderId]
    val field: Field? get() = fieldMap.value[fieldId]

    val type: StringTranslationType?
        get() = StringTranslationType.values().firstOrNull { it.id == typeId }

    override fun toString() = "$id (${field?.alias}): '$text'"

    override fun hashCode() = this.id
}
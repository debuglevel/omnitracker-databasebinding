package de.debuglevel.omnitrackerdatabasebinding.models

data class StringTranslation(val id: Int,
                             val guid: String,
                             val languageCode: String,
                             val text: String?,
                             val untranslated: Boolean,
                             private val typeId: Int,
                             val fieldId: Int?,
                             private val folderId: Int?,
                             private val fieldMap: Lazy<Map<Int, Field>>,
                             private val folderMap: Lazy<Map<Int, Folder>>
) {
    val folder: Folder?
        get() = folderMap.value[folderId]

    val field: Field?
        get() = fieldMap.value[fieldId]

    val type: StringTranslationType?
        get() = StringTranslationType.values().firstOrNull { it.id == typeId }

    override fun hashCode() = this.id

    override fun equals(other: Any?): Boolean {
        return when {
            other == null -> false
            this === other -> true
            other is StringTranslation -> {
                val o = other
                this.id == o.id &&
                        this.guid == o.guid &&
                        this.folderId == o.folderId &&
                        this.languageCode == o.languageCode &&
                        this.text == o.text &&
                        this.untranslated == o.untranslated &&
                        this.typeId == o.typeId &&
                        this.fieldId == o.fieldId
            }
            else -> false
        }
    }

    override fun toString(): String {
        return "StringTranslation(" +
                "id=$id," +
                "guid='$guid'," +
                "folder=${folder?.alias}," +
                "languageCode='$languageCode'," +
                "text=$text," +
                "untranslated=$untranslated," +
                "type=${type?.name}," +
                "field=${field?.alias}" +
                ")"
    }
}
package de.debuglevel.omnitrackerdatabasebinding.stringtranslation

data class StringTranslation(
    val id: Int,
    val guid: String,
    val languageCode: String,
    val text: String?,
    val isUntranslated: Boolean,
    val short: Boolean, // set if table is "StringTransShort"
    val typeId: Int,
    val fieldId: Int?,
    val folderId: Int?
    //private val fieldMap: Lazy<Map<Int, Field>>,
    //private val folderMap: Lazy<Map<Int, Folder>>
) {
//    val folder: Folder?
//        get() = folderMap.value[folderId]
//
//    val field: Field?
//        get() = fieldMap.value[fieldId]

    val type: StringTranslationType?
        get() = StringTranslationType.values().firstOrNull { it.id == typeId }

    val language: StringTranslationLanguage?
        get() = StringTranslationLanguage.values().firstOrNull { it.languageCode == languageCode }

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
                        this.isUntranslated == o.isUntranslated &&
                        this.short == o.short &&
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
                "typeId='$typeId'," +
                "folderId=${folderId}," +
                //"folder=${folder?.alias}," +
                "languageCode='$languageCode'," +
                "text=$text," +
                "untranslated=$isUntranslated," +
                "short=$short," +
                "type=${type?.name}," +
                "fieldId=${fieldId}" +
                //"field=${field?.alias}" +
                ")"
    }
}
package de.debuglevel.omnitrackerdatabasebinding.field

import de.debuglevel.omnitrackerdatabasebinding.entity.Entity

data class Field(
    override val id: Int,
    val alias: String?,
    val label: String,
    val remark: String?,
    val maxSize: Int,
    private val typeId: Int,
    private val subtypeId: Int,
    val folderId: Int,
    val referenceFolderId: Int?,
    val isIndexed: Boolean
    //private val folderMap: Lazy<Map<Int, Folder>>,
    //private val stringTranslationList: Lazy<List<StringTranslation>>
) : Entity {
    val type: FieldType?
        get() = FieldType.values().firstOrNull { it.id == typeId }

//    val folder: Folder
//        get() = folderMap.value.getValue(folderId)

//    private val stringTranslations: List<StringTranslation> by lazy {
//        stringTranslationList.value.filter { it.field == this }
//    }

//    fun getName(language: StringTranslationLanguage) = stringTranslations
//        .singleOrNull { it.language == language && it.type == StringTranslationType.FieldName }
//
//    fun getComment(language: StringTranslationLanguage) = stringTranslations
//        .singleOrNull { it.language == language && it.type == StringTranslationType.FieldComment }
//
//    fun getDescription(language: StringTranslationLanguage) = stringTranslations
//        .singleOrNull { it.language == language && it.type == StringTranslationType.FieldDescription }

    /**
     * If this field is a ReferenceTo
     */
//    val referenceFolder: Folder?
//        get() = folderMap.value[this.referenceFolderId]

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
                //"folder=${folder.alias}," +
                "folderId=${folderId}," +
                "label='$label'," +
                "typeId=$typeId," +
                "subtypeId=$subtypeId," +
                "type=${type?.name}," +
                "alias=$alias," +
                "referenceFolderId=${referenceFolderId}" +
                //"referenceFolder=${referenceFolder?.alias}" +
                ")"
    }
}
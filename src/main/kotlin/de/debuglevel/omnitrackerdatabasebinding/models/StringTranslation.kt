package de.debuglevel.omnitrackerdatabasebinding.models

data class StringTranslation(val id: Int,
                             val guid: String,
                             val folder: Folder?,
                             val languageCode: String,
                             val text: String?,
                             val untranslated: Boolean,
                             val typeId: Int,
                             val field: Field?
        //val refId: Int
) {
    val type: StringTranslationType?
        get() = StringTranslationType.values().firstOrNull { it.id == typeId }

    override fun toString() = "$id (${field?.alias}): '$text'"

    override fun hashCode() = this.id
}
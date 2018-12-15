package de.debuglevel.omnitrackerdatabasebinding.models

enum class StringTranslationType(val id: Int) {
    FolderDescription(140),
    FieldDescription(141),
    FieldComment(108),
    FieldName(109),
    FolderName(112) // occurs only in "StringTransShort" table
}

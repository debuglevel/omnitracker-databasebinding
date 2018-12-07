package de.debuglevel.omnitrackerdatabasebinding.models

enum class StringTranslationType(val id: Int) {
    Description(141),
    Comment(108),
    FieldName(109),
    Folder(112) // occurs only in "StringTransShort" table
}

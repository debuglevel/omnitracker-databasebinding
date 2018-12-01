package de.debuglevel.omnitrackerdatabasebinding.models

/**
 * 252 = reference to object
 * 254 = list of reference to object
 */
enum class FieldType(val id: Int) {
    ObjectReference(252),
    ObjectReferenceList(254)
}

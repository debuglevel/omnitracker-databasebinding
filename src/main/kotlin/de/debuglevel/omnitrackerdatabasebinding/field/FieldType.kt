package de.debuglevel.omnitrackerdatabasebinding.field

/**
 * 252 = reference to object
 * 254 = list of reference to object
 */
enum class FieldType(val id: Int) {
    ObjectReference(252),
    ObjectReferenceList(254),
    DateTime(8),
    UserOrGroupReference(250),
    Comments(52),
    Workflow(53),
    probably_ShortText(10),
    probably_LongText(12),
    Dropdown(51),
    probably_UniqueNumber(50),
    probably_LongOrInteger(3),
    probably_Floatingpoint2(6),
    Boolean(1),
    Attachments(55),
    probably_Schedule(54),
    Currency(5),
}

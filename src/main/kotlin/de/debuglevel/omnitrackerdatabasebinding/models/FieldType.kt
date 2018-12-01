package de.debuglevel.omnitrackerdatabasebinding.models

/**
 * 252 = reference to object
 * 254 = list of reference to object
 */
enum class FieldType(val id: Int) {
    ObjectReference(252),
    ObjectReferenceList(254),
    probably_DateTime(8),
    probably_UserOrGroupReference(250),
    probably_Comments(52),
    probably_Workflow(53),
    probably_ShortText(10),
    probably_LongText(12),
    probably_Dropdown(51),
    probably_Number(50),
    probably_Floatingpoint1(3),
    probably_Floatingpoint2(6),
    probably_Boolean(1),
    probably_Attachments(55),
    probably_Schedule(55),
    probably_Currency(5),
}

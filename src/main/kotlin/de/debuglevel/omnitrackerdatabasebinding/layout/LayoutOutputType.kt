package de.debuglevel.omnitrackerdatabasebinding.layout

enum class LayoutOutputType(
    val id: Int,
    val fileExtension: String
) {
    CrystalReports(0, "rpt"),
    Word(1, "dot"),
}

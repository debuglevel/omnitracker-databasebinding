package de.debuglevel.omnitrackerdatabasebinding

fun main(args: Array<String>) {
    val omnitrackerDatabase = OmnitrackerDatabase()
    omnitrackerDatabase.fields.map { it.value }.sortedBy { it.id }.forEach { println(it) }
    omnitrackerDatabase.stringTranslations.map { it.value }.sortedBy { it.id }.forEach { println(it) }
    omnitrackerDatabase.folders.map { it.value }.sortedBy { it.id }.forEach { println(it) }
    omnitrackerDatabase.scripts.map { it.value }.sortedBy { it.id }.forEach { println(it) }
}
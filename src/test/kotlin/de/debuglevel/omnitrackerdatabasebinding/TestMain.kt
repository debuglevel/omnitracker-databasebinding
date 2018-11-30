package de.debuglevel.omnitrackerdatabasebinding

fun main(args : Array<String>) {
    //OmnitrackerDatabase().getFields().forEach { println(it) }
    OmnitrackerDatabase().getStringTranslations().filter { it.type != null }.forEach { println(it) }
    //OmnitrackerDatabase().getFolders().sortedBy { it.id }.forEach { println(it) }
}
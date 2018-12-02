package de.debuglevel.omnitrackerdatabasebinding

import de.debuglevel.omnitrackerdatabasebinding.models.StringTranslationLanguage

fun main(args: Array<String>) {
    val omnitrackerDatabase = OmnitrackerDatabase()
    //omnitrackerDatabase.fields.map { it.value }.sortedBy { it.id }.forEach { println(it) }
    //omnitrackerDatabase.fields.filter { it.value.type==null }.map { it.value }.sortedBy { it.id }.forEach { println(it) }
    //omnitrackerDatabase.stringTranslations.map { it.value }.filter { it.type == null }.sortedBy { it.typeId }.forEach { println(it) }
    //omnitrackerDatabase.folders.map { it.value }.sortedBy { it.id }.forEach { println(it) }
    //omnitrackerDatabase.scripts.map { it.value }.sortedBy { it.typeId }.forEach { println(it) }

    omnitrackerDatabase.folders
            .map { it.value }
            .sortedBy { it.name }
            .flatMap { it.fields.values }
            .forEach { println("${it.folder.name}: ${it.getName(StringTranslationLanguage.German)}: ${it.getComment(StringTranslationLanguage.German)}") }
}
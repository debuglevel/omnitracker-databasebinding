package de.debuglevel.omnitrackerdatabasebinding

import io.micronaut.runtime.Micronaut

fun main(args: Array<String>) {
    val context = Micronaut.build().start()
    val omnitrackerDatabase = context.getBean(OmnitrackerDatabase::class.java)

//    println("Folders:")
//    omnitrackerDatabase.folders.map { it.value }.sortedBy { it.id }.forEach { println("\t$it") }

//    println("Fields:")
//    omnitrackerDatabase.fields.map { it.value }.sortedBy { it.id }.forEach { println("\t$it") }
//
//    //println("StringTranslations:")
//    //omnitrackerDatabase.stringTranslations.map { it }.sortedBy { it.id }.forEach { println("\t$it") }
//
//    println("Scripts:")
//    omnitrackerDatabase.scripts.map { it.value }.sortedBy { it.id }.forEach { println("\t$it") }
//
    println("Layouts:")
    omnitrackerDatabase.layouts.map { it.value }.sortedBy { it.id }.forEach { println("\t$it") }
//
//    println("WSC Profiles:")
//    omnitrackerDatabase.webServiceConsumerProfiles.map { it.value }.sortedBy { it.id }.forEach { println("\t$it") }
//
//    println("WSC Call Profiles:")
//    omnitrackerDatabase.webServiceConsumerCallProfiles.map { it.value }.sortedBy { it.id }.forEach { println("\t$it") }

    //val omnitrackerDatabase = OmnitrackerDatabase()
    //omnitrackerDatabase.fields.map { it.value }.sortedBy { it.id }.forEach { println(it) }
    //omnitrackerDatabase.fields.filter { it.value.type==null }.map { it.value }.sortedBy { it.id }.forEach { println(it) }
    //omnitrackerDatabase.stringTranslations.map { it }.filter { it.short == false }.sortedByDescending { it.short }.forEach { println(it) }
    //omnitrackerDatabase.folders.map { it.value }.sortedBy { it.path }.forEach { println("${it.path} -- ${it.getDescription(StringTranslationLanguage.German)?.text}") }
    //omnitrackerDatabase.scripts.map { it.value }.sortedBy { it.typeId }.forEach { println(it) }


//    omnitrackerDatabase.folders
//            .map { it.value }
//            .sortedBy { it.name }
//            .flatMap { it.fields.values }
//            .forEach { println("${it.folder.name}: ${it.getName(StringTranslationLanguage.German)}: ${it.getComment(StringTranslationLanguage.German)}") }

//    omnitrackerDatabase.webServiceConsumerCallProfiles
//        .map { it.value }
//        .sortedBy { it.id }
//        .filter { it.status != WebServiceConsumerProfileStatus.Valid }
//        .forEach { println(it) }
}
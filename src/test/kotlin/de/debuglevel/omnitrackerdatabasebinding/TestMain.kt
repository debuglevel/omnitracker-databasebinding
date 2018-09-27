package de.debuglevel.omnitrackerdatabasebinding

fun main(args : Array<String>) {
    OmnitrackerDatabase().getFields().forEach { println(it) }
}
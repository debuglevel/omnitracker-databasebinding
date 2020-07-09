package de.debuglevel.omnitrackerdatabasebinding

import de.debuglevel.omnitrackerdatabasebinding.field.Field
import de.debuglevel.omnitrackerdatabasebinding.field.FieldService
import de.debuglevel.omnitrackerdatabasebinding.folder.Folder
import de.debuglevel.omnitrackerdatabasebinding.folder.FolderService
import de.debuglevel.omnitrackerdatabasebinding.layout.Layout
import de.debuglevel.omnitrackerdatabasebinding.layout.LayoutService
import de.debuglevel.omnitrackerdatabasebinding.script.Script
import de.debuglevel.omnitrackerdatabasebinding.script.ScriptService
import de.debuglevel.omnitrackerdatabasebinding.stringtranslation.StringTranslationService
import de.debuglevel.omnitrackerdatabasebinding.webserviceconsumercallprofile.WebServiceConsumerCallProfile
import de.debuglevel.omnitrackerdatabasebinding.webserviceconsumercallprofile.WebServiceConsumerCallProfileService
import de.debuglevel.omnitrackerdatabasebinding.webserviceconsumerprofile.WebServiceConsumerProfile
import de.debuglevel.omnitrackerdatabasebinding.webserviceconsumerprofile.WebServiceConsumerProfileService
import mu.KotlinLogging
import javax.inject.Singleton

@Singleton
class OmnitrackerDatabase(
    private val databaseService: DatabaseService,
    private val fieldService: FieldService,
    private val folderService: FolderService,
    private val layoutService: LayoutService,
    private val scriptService: ScriptService,
    private val stringTranslationService: StringTranslationService,
    private val webServiceConsumerCallProfileService: WebServiceConsumerCallProfileService,
    private val webServiceConsumerProfileService: WebServiceConsumerProfileService
) {
    private val logger = KotlinLogging.logger {}

    init {
        logger.debug { "Initializing OMNITRACKER DatabaseBinding..." }

        // MSSQL driver need to be loaded explicitly
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver")
    }

    /**
     * Not really necessary. Connects for a first time, which may take a while for AccessDB (eases performance measurement).
     */
    fun connect() {
        databaseService.getConnection()
    }

    val fields: Map<Int, Field> by lazy {
        logger.debug("Lazy initializing fields...")
        fieldService.getAll()
    }

//    val stringTranslations: List<StringTranslation> by lazy {
//        logger.debug("Lazy initializing stringTranslations...")
//        stringTranslationService.getStringTranslations()
//    }

    val scripts: Map<Int, Script> by lazy {
        logger.debug("Lazy initializing scripts...")
        scriptService.getAll()
    }

    val folders: Map<Int, Folder> by lazy {
        logger.debug("Lazy initializing folders...")
        folderService.getAll()
    }

    val layouts: Map<Int, Layout> by lazy {
        logger.debug("Lazy initializing layouts...")
        layoutService.getAll()
    }

    val webServiceConsumerProfiles: Map<Int, WebServiceConsumerProfile> by lazy {
        logger.debug("Lazy initializing Web Service Consumer Profiles...")
        webServiceConsumerProfileService.getAll()
    }

    val webServiceConsumerCallProfiles: Map<Int, WebServiceConsumerCallProfile> by lazy {
        logger.debug("Lazy initializing Web Service Consumer Call Profiles...")
        webServiceConsumerCallProfileService.getAll()
    }
}
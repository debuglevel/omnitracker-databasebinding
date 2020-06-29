package de.debuglevel.omnitrackerdatabasebinding

import de.debuglevel.omnitrackerdatabasebinding.field.Field
import de.debuglevel.omnitrackerdatabasebinding.field.FieldService
import de.debuglevel.omnitrackerdatabasebinding.folder.Folder
import de.debuglevel.omnitrackerdatabasebinding.folder.FolderService
import de.debuglevel.omnitrackerdatabasebinding.layout.Layout
import de.debuglevel.omnitrackerdatabasebinding.layout.LayoutService
import de.debuglevel.omnitrackerdatabasebinding.script.Script
import de.debuglevel.omnitrackerdatabasebinding.script.ScriptsService
import de.debuglevel.omnitrackerdatabasebinding.stringtranslation.StringTranslationService
import de.debuglevel.omnitrackerdatabasebinding.webservice.WebServiceConsumerCallProfile
import de.debuglevel.omnitrackerdatabasebinding.webservice.WebServiceConsumerCallProfileService
import de.debuglevel.omnitrackerdatabasebinding.webservice.WebServiceConsumerProfile
import de.debuglevel.omnitrackerdatabasebinding.webservice.WebServiceConsumerProfileService
import mu.KotlinLogging
import javax.inject.Singleton

@Singleton
class OmnitrackerDatabase(
    private val fieldService: FieldService,
    private val folderService: FolderService,
    private val layoutService: LayoutService,
    private val scriptsService: ScriptsService,
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

    val fields: Map<Int, Field> by lazy {
        logger.debug("Lazy initializing fields...")
        fieldService.getFields()
    }

//    val stringTranslations: List<StringTranslation> by lazy {
//        logger.debug("Lazy initializing stringTranslations...")
//        stringTranslationService.getStringTranslations()
//    }

    val scripts: Map<Int, Script> by lazy {
        logger.debug("Lazy initializing scripts...")
        scriptsService.getAll()
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
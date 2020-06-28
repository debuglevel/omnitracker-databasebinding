package de.debuglevel.omnitrackerdatabasebinding.layout

import de.debuglevel.omnitrackerdatabasebinding.DatabaseService
import de.debuglevel.omnitrackerdatabasebinding.folder.FolderService
import mu.KotlinLogging
import java.sql.ResultSet
import java.util.*
import javax.inject.Singleton

@Singleton
class LayoutService(
    private val databaseService: DatabaseService,
    private val folderService: FolderService
) {
    private val logger = KotlinLogging.logger {}

    private val query =
        "SELECT id, name, folder, report_data, type, version, output_type, mailmerge_doctype, mailmerge_sql, mailmerge_filetype, cr_replace_mdb, cr_static_db_conn FROM [Layout]"

    fun getAll(): Map<Int, Layout> {
        logger.debug { "Getting layouts..." }

        val layouts = databaseService.getConnection().use { connection ->
            val sqlStatement = connection.createStatement()
            val resultSet = sqlStatement.executeQuery(query)

            val layouts = hashMapOf<Int, Layout>()

            while (resultSet.next()) {
                val layout = build(resultSet)
                layouts[layout.id] = layout
            }

            layouts
        }

        logger.debug { "Got ${layouts.size} layouts" }
        return layouts
    }

    fun get(id: Int): Layout? {
        logger.debug { "Getting layout id=$id..." }
        val layout = databaseService.getConnection().use { connection ->
            val sqlStatement = connection.createStatement()
            val resultSet = sqlStatement.executeQuery("$query WHERE id=$id")

            val available = resultSet.next()
            when {
                available -> build(resultSet)
                else -> null
            }
        }

        logger.debug { "Got layout: $layout" }
        return layout
    }

    private fun build(resultSet: ResultSet): Layout {
        logger.debug { "Building layout for ResultSet $resultSet..." }

        val id = resultSet.getInt("id")
        val name = resultSet.getString("name")
        val folderId = resultSet.getInt("folder")
        val reportDataBase64 = resultSet.getString("report_data")
        val typeId = resultSet.getInt("type")
        val version = resultSet.getInt("version")
        val outputTypeId = resultSet.getInt("output_type")
        val mailmergeDoctype = resultSet.getInt("mailmerge_doctype")
        val mailmergeSql = resultSet.getString("mailmerge_sql")
        val mailmergeFiletype = resultSet.getInt("mailmerge_filetype")
        val crReplaceMdb = resultSet.getInt("cr_replace_mdb")
        val crStaticDbConn = resultSet.getString("cr_static_db_conn")

        val folder = folderService.get(folderId)

        val layout = Layout(
            id,
            name,
            version,
            reportDataBase64,
            mailmergeSql,
            crReplaceMdb,
            crStaticDbConn,
            typeId,
            outputTypeId,
            mailmergeDoctype,
            mailmergeFiletype,
            folderId,
            folder!!
            //lazy { folderService.fetchFolders() }
        )

        logger.debug { "Built layout: $layout" }
        return layout
    }

    fun updateLayoutReportData(layout: Layout, reportData: ByteArray) {
        logger.debug("Updating report data for layout $layout...")

        logger.trace("Converting byte data into Base64...")
        val reportDataBase64 = Base64.getMimeEncoder().encodeToString(reportData)

        databaseService.getConnection().use { connection ->
            val preparedStatement = connection.prepareStatement("UPDATE [Layout] SET report_data = ? WHERE id = ?")
            preparedStatement.setString(1, reportDataBase64)
            preparedStatement.setInt(2, layout.id)

            logger.trace("Executing update...")
            val rowCount = preparedStatement.executeUpdate()
            logger.trace("Count of affected rows is $rowCount")

            if (rowCount == 1) {
                return
            } else {
                logger.error("Count of affected rows was not 1 but $rowCount")
                throw Exception("Row count on update was $rowCount although it should have been 1.")
            }
        }
    }
}
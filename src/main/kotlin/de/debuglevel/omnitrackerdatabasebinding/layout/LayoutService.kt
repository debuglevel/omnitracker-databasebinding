package de.debuglevel.omnitrackerdatabasebinding.layout

import de.debuglevel.omnitrackerdatabasebinding.DatabaseService
import mu.KotlinLogging
import java.util.*
import javax.inject.Singleton

@Singleton
class LayoutService(
    private val databaseService: DatabaseService
    //private val folderService: FolderService
) {
    private val logger = KotlinLogging.logger {}

    fun fetchLayouts(): Map<Int, Layout> {
        databaseService.getConnection().use { connection ->
            val sqlStatement = connection.createStatement()
            val resultSet =
                sqlStatement.executeQuery("SELECT id, name, folder, report_data, type, version, output_type, mailmerge_doctype, mailmerge_sql, mailmerge_filetype, cr_replace_mdb, cr_static_db_conn FROM [Layout]")

            val layouts = hashMapOf<Int, Layout>()

            while (resultSet.next()) {
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
                    folderId
                    //lazy { folderService.fetchFolders() }
                )

                layouts[id] = layout
            }

            return layouts
        }
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
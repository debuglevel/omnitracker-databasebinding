package de.debuglevel.omnitrackerdatabasebinding.webservice

data class WebServiceConsumerCallProfile(
    val id: Int,
    val name: String,
    val alias: String?,
    val profileVersion: Int,
    private val profileStatus: Int,
    val folderId: Int,
    val webServiceConsumerProfileId: Int
    //private val folderMap: Lazy<Map<Int, Folder>>,
    //private val webServiceConsumerProfileMap: Lazy<Map<Int, WebServiceConsumerProfile>>
) {
//    val folder: Folder?
//        get() = folderMap.value[folderId]
//
//    val webServiceConsumerProfile: WebServiceConsumerProfile?
//        get() = webServiceConsumerProfileMap.value[webServiceConsumerProfileId]

    val status: WebServiceConsumerProfileStatus?
        get() = WebServiceConsumerProfileStatus.values().firstOrNull { it.id == profileStatus }

    override fun hashCode() = this.id

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as WebServiceConsumerCallProfile

        if (id != other.id) return false
        if (name != other.name) return false
        if (alias != other.alias) return false
        if (profileVersion != other.profileVersion) return false
        if (profileStatus != other.profileStatus) return false
        if (folderId != other.folderId) return false
        //if (webServiceConsumerProfileId != other.webServiceConsumerProfileId) return false
        //if (folderMap != other.folderMap) return false
        //if (webServiceConsumerProfileMap != other.webServiceConsumerProfileMap) return false

        return true
    }

    override fun toString(): String {
        return "WebServiceConsumerCallProfile(" +
                "id=$id, " +
                "name='$name', " +
                "alias='$alias', " +
                "profileVersion=$profileVersion, " +
                "profileStatus=$profileStatus, " +
                "status=$status, " +
                "folderId=$folderId, " +
                "webServiceConsumerProfileId=$webServiceConsumerProfileId" +
                ")"
    }
}
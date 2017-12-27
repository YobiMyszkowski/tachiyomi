package eu.kanade.tachiyomi.source

import android.content.Context
import eu.kanade.tachiyomi.extension.ExtensionManager
import eu.kanade.tachiyomi.source.online.HttpSource
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

open class SourceManager(private val context: Context,
                         extensionManager: ExtensionManager = Injekt.get()) {

    private val sourcesMap = mutableMapOf<Long, Source>()

    init {
        createInternalSources().forEach { registerSource(it) }
        extensionManager.init(this)
    }

    open fun get(sourceKey: Long): Source? {
        return sourcesMap[sourceKey]
    }

    fun getOnlineSources() = sourcesMap.values.filterIsInstance<HttpSource>()

    fun getCatalogueSources() = sourcesMap.values.filterIsInstance<CatalogueSource>()

    internal fun registerSource(source: Source, overwrite: Boolean = false) {
        if (overwrite || !sourcesMap.containsKey(source.id)) {
            sourcesMap.put(source.id, source)
        }
    }

    internal fun unregisterSource(source: Source) {
        sourcesMap.remove(source.id)
    }

    private fun createInternalSources(): List<Source> = listOf(LocalSource(context))
}
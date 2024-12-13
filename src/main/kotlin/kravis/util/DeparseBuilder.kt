package kravis.util

import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.api.Infer
import org.jetbrains.kotlinx.dataframe.api.toColumn
import org.jetbrains.kotlinx.dataframe.api.toDataFrame

// todo javadoc example needed
/** Create a data-frame from a list of objects */
fun <T> Iterable<T>.deparseRecords(mapping: (T) -> DataFrameRow) = DataFrame.fromRecords(this, mapping)

internal typealias DeparseFormula<T> = T.(T) -> Any?

inline fun <reified T> Iterable<T>.deparseRecords(vararg mapping: Pair<String, DeparseFormula<T>>): DataFrame<*> {
    //    val revMapping = mapping.toMap().entries.associateBy({ it.value }) { it.key }
    val mappings = mapOf<String, Any?>().toMutableMap().apply { putAll(mapping) }

    val function = { record: T ->
        mapping.toMap().map { (name, deparse) -> name to deparse(record, record) }.toMap() as DataFrameRow
    }
    return DataFrame.fromRecords(this, function)
}


infix fun <T> String.with(that: DeparseFormula<T>) = Pair(this, that)

typealias DataFrameRow = Map<String, Any?>

/** Create a data-frame from a list of objects */
fun <T> DataFrame.Companion.fromRecords(records: Iterable<T>, mapping: (T) -> DataFrameRow): DataFrame<*> {
    val rowData = records.map { mapping(it) }
    val columnNames = mapping(records.first()).keys

    val columnData = columnNames.map { it to emptyList<Any?>().toMutableList() }.toMap()

    for(record in rowData) {
        columnData.forEach { colName, colData -> colData.add(record[colName]) }
    }

    val map = columnData.map { (name, data) -> data.toColumn(name, Infer.Type) }
    return map.toDataFrame()
}



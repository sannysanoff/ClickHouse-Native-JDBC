package com.github.housepower.jdbc.wrapper

import com.github.housepower.jdbc.ClickHouseResultSetMetaData
import com.github.housepower.jdbc.statement.ClickHouseStatement
import java.io.InputStream
import java.io.Reader
import java.math.BigDecimal
import java.net.URL
import java.sql.*
import java.sql.Array
import java.sql.Date
import java.util.*

abstract class SQLResultSet {

    open operator fun next(): Boolean {
        throw SQLFeatureNotSupportedException()
    }


    open fun close() {
        throw SQLFeatureNotSupportedException()
    }


    open fun wasNull(): Boolean {
        throw SQLFeatureNotSupportedException()
    }


    open fun getString(columnIndex: Int): String? {
        throw SQLFeatureNotSupportedException()
    }


    fun getBoolean(columnIndex: Int): Boolean {
        throw SQLFeatureNotSupportedException()
    }


    open fun getByte(columnIndex: Int): Byte {
        throw SQLFeatureNotSupportedException()
    }


    open fun getShort(columnIndex: Int): Short {
        throw SQLFeatureNotSupportedException()
    }


    open fun getInt(columnIndex: Int): Int {
        throw SQLFeatureNotSupportedException()
    }


    open fun getLong(columnIndex: Int): Long {
        throw SQLFeatureNotSupportedException()
    }


    open fun getFloat(columnIndex: Int): Float {
        throw SQLFeatureNotSupportedException()
    }


    open fun getDouble(columnIndex: Int): Double {
        throw SQLFeatureNotSupportedException()
    }


    fun getBigDecimal(columnIndex: Int, scale: Int): BigDecimal {
        throw SQLFeatureNotSupportedException()
    }


    fun getBytes(columnIndex: Int): ByteArray {
        throw SQLFeatureNotSupportedException()
    }


    open fun getDate(columnIndex: Int): Date? {
        throw SQLFeatureNotSupportedException()
    }


    fun getTime(columnIndex: Int): Time {
        throw SQLFeatureNotSupportedException()
    }


    open fun getTimestamp(columnIndex: Int): Timestamp? {
        throw SQLFeatureNotSupportedException()
    }


    fun getAsciiStream(columnIndex: Int): InputStream {
        throw SQLFeatureNotSupportedException()
    }


    fun getUnicodeStream(columnIndex: Int): InputStream {
        throw SQLFeatureNotSupportedException()
    }


    fun getBinaryStream(columnIndex: Int): InputStream {
        throw SQLFeatureNotSupportedException()
    }


    open fun getString(columnLabel: String?): String? {
        throw SQLFeatureNotSupportedException()
    }


    fun getBoolean(columnLabel: String?): Boolean {
        throw SQLFeatureNotSupportedException()
    }


    open fun getByte(columnLabel: String?): Byte {
        throw SQLFeatureNotSupportedException()
    }


    open fun getShort(columnLabel: String?): Short {
        throw SQLFeatureNotSupportedException()
    }


    open fun getInt(columnLabel: String?): Int {
        throw SQLFeatureNotSupportedException()
    }


    open fun getLong(columnLabel: String?): Long {
        throw SQLFeatureNotSupportedException()
    }


    open fun getFloat(columnLabel: String?): Float {
        throw SQLFeatureNotSupportedException()
    }


    open fun getDouble(columnLabel: String?): Double {
        throw SQLFeatureNotSupportedException()
    }


    fun getBigDecimal(columnLabel: String?, scale: Int): BigDecimal {
        throw SQLFeatureNotSupportedException()
    }


    fun getBytes(columnLabel: String?): ByteArray {
        throw SQLFeatureNotSupportedException()
    }


    open fun getDate(columnLabel: String?): Date? {
        throw SQLFeatureNotSupportedException()
    }


    fun getTime(columnLabel: String?): Time {
        throw SQLFeatureNotSupportedException()
    }


    open fun getTimestamp(columnLabel: String?): Timestamp? {
        throw SQLFeatureNotSupportedException()
    }


    fun getAsciiStream(columnLabel: String?): InputStream {
        throw SQLFeatureNotSupportedException()
    }


    fun getUnicodeStream(columnLabel: String?): InputStream {
        throw SQLFeatureNotSupportedException()
    }


    fun getBinaryStream(columnLabel: String?): InputStream {
        throw SQLFeatureNotSupportedException()
    }


    val warnings: SQLWarning
        get() {
            throw SQLFeatureNotSupportedException()
        }


    fun clearWarnings() {
        throw SQLFeatureNotSupportedException()
    }


    val cursorName: String
        get() {
            throw SQLFeatureNotSupportedException()
        }

    open fun getMetaData(): ClickHouseResultSetMetaData {
        throw SQLException("No metadata here")
    }


    open fun getObject(columnIndex: Int): Any? {
        throw SQLFeatureNotSupportedException()
    }


    open fun getObject(columnLabel: String?): Any? {
        throw SQLFeatureNotSupportedException()
    }


    open fun findColumn(columnLabel: String?): Int {
        throw SQLFeatureNotSupportedException()
    }


    fun getCharacterStream(columnIndex: Int): Reader {
        throw SQLFeatureNotSupportedException()
    }


    fun getCharacterStream(columnLabel: String?): Reader {
        throw SQLFeatureNotSupportedException()
    }


    open fun getBigDecimal(columnIndex: Int): BigDecimal? {
        throw SQLFeatureNotSupportedException()
    }


    open fun getBigDecimal(columnLabel: String?): BigDecimal? {
        throw SQLFeatureNotSupportedException()
    }


    val isBeforeFirst: Boolean
        get() {
            throw SQLFeatureNotSupportedException()
        }


    val isAfterLast: Boolean
        get() {
            throw SQLFeatureNotSupportedException()
        }


    val isFirst: Boolean
        get() {
            throw SQLFeatureNotSupportedException()
        }


    val isLast: Boolean
        get() {
            throw SQLFeatureNotSupportedException()
        }


    fun beforeFirst() {
        throw SQLFeatureNotSupportedException()
    }


    fun afterLast() {
        throw SQLFeatureNotSupportedException()
    }


    fun first(): Boolean {
        throw SQLFeatureNotSupportedException()
    }


    fun last(): Boolean {
        throw SQLFeatureNotSupportedException()
    }


    val row: Int
        get() {
            throw SQLFeatureNotSupportedException()
        }


    fun absolute(row: Int): Boolean {
        throw SQLFeatureNotSupportedException()
    }


    fun relative(rows: Int): Boolean {
        throw SQLFeatureNotSupportedException()
    }


    fun previous(): Boolean {
        throw SQLFeatureNotSupportedException()
    }


    @set:Throws(SQLException::class)
    var fetchDirection: Int
        get() {
            throw SQLFeatureNotSupportedException()
        }
        set(direction) {
            throw SQLFeatureNotSupportedException()
        }


    @set:Throws(SQLException::class)
    var fetchSize: Int
        get() {
            throw SQLFeatureNotSupportedException()
        }
        set(rows) {
            throw SQLFeatureNotSupportedException()
        }


    val type: Int
        get() {
            throw SQLFeatureNotSupportedException()
        }


    val concurrency: Int
        get() {
            throw SQLFeatureNotSupportedException()
        }


    fun rowUpdated(): Boolean {
        throw SQLFeatureNotSupportedException()
    }


    fun rowInserted(): Boolean {
        throw SQLFeatureNotSupportedException()
    }


    fun rowDeleted(): Boolean {
        throw SQLFeatureNotSupportedException()
    }


    fun updateNull(columnIndex: Int) {
        throw SQLFeatureNotSupportedException()
    }


    fun updateBoolean(columnIndex: Int, x: Boolean) {
        throw SQLFeatureNotSupportedException()
    }


    fun updateByte(columnIndex: Int, x: Byte) {
        throw SQLFeatureNotSupportedException()
    }


    fun updateShort(columnIndex: Int, x: Short) {
        throw SQLFeatureNotSupportedException()
    }


    fun updateInt(columnIndex: Int, x: Int) {
        throw SQLFeatureNotSupportedException()
    }


    fun updateLong(columnIndex: Int, x: Long) {
        throw SQLFeatureNotSupportedException()
    }


    fun updateFloat(columnIndex: Int, x: Float) {
        throw SQLFeatureNotSupportedException()
    }


    fun updateDouble(columnIndex: Int, x: Double) {
        throw SQLFeatureNotSupportedException()
    }


    fun updateBigDecimal(columnIndex: Int, x: BigDecimal?) {
        throw SQLFeatureNotSupportedException()
    }


    fun updateString(columnIndex: Int, x: String?) {
        throw SQLFeatureNotSupportedException()
    }


    fun updateBytes(columnIndex: Int, x: ByteArray?) {
        throw SQLFeatureNotSupportedException()
    }


    fun updateDate(columnIndex: Int, x: Date?) {
        throw SQLFeatureNotSupportedException()
    }


    fun updateTime(columnIndex: Int, x: Time?) {
        throw SQLFeatureNotSupportedException()
    }


    fun updateTimestamp(columnIndex: Int, x: Timestamp?) {
        throw SQLFeatureNotSupportedException()
    }


    fun updateAsciiStream(columnIndex: Int, x: InputStream?, length: Int) {
        throw SQLFeatureNotSupportedException()
    }


    fun updateBinaryStream(columnIndex: Int, x: InputStream?, length: Int) {
        throw SQLFeatureNotSupportedException()
    }


    fun updateCharacterStream(columnIndex: Int, x: Reader?, length: Int) {
        throw SQLFeatureNotSupportedException()
    }


    fun updateObject(columnIndex: Int, x: Any?, scaleOrLength: Int) {
        throw SQLFeatureNotSupportedException()
    }


    fun updateObject(columnIndex: Int, x: Any?) {
        throw SQLFeatureNotSupportedException()
    }


    fun updateNull(columnLabel: String?) {
        throw SQLFeatureNotSupportedException()
    }


    fun updateBoolean(columnLabel: String?, x: Boolean) {
        throw SQLFeatureNotSupportedException()
    }


    fun updateByte(columnLabel: String?, x: Byte) {
        throw SQLFeatureNotSupportedException()
    }


    fun updateShort(columnLabel: String?, x: Short) {
        throw SQLFeatureNotSupportedException()
    }


    fun updateInt(columnLabel: String?, x: Int) {
        throw SQLFeatureNotSupportedException()
    }


    fun updateLong(columnLabel: String?, x: Long) {
        throw SQLFeatureNotSupportedException()
    }


    fun updateFloat(columnLabel: String?, x: Float) {
        throw SQLFeatureNotSupportedException()
    }


    fun updateDouble(columnLabel: String?, x: Double) {
        throw SQLFeatureNotSupportedException()
    }


    fun updateBigDecimal(columnLabel: String?, x: BigDecimal?) {
        throw SQLFeatureNotSupportedException()
    }


    fun updateString(columnLabel: String?, x: String?) {
        throw SQLFeatureNotSupportedException()
    }


    fun updateBytes(columnLabel: String?, x: ByteArray?) {
        throw SQLFeatureNotSupportedException()
    }


    fun updateDate(columnLabel: String?, x: Date?) {
        throw SQLFeatureNotSupportedException()
    }


    fun updateTime(columnLabel: String?, x: Time?) {
        throw SQLFeatureNotSupportedException()
    }


    fun updateTimestamp(columnLabel: String?, x: Timestamp?) {
        throw SQLFeatureNotSupportedException()
    }


    fun updateAsciiStream(columnLabel: String?, x: InputStream?, length: Int) {
        throw SQLFeatureNotSupportedException()
    }


    fun updateBinaryStream(columnLabel: String?, x: InputStream?, length: Int) {
        throw SQLFeatureNotSupportedException()
    }


    fun updateCharacterStream(columnLabel: String?, reader: Reader?, length: Int) {
        throw SQLFeatureNotSupportedException()
    }


    fun updateObject(columnLabel: String?, x: Any?, scaleOrLength: Int) {
        throw SQLFeatureNotSupportedException()
    }


    fun updateObject(columnLabel: String?, x: Any?) {
        throw SQLFeatureNotSupportedException()
    }


    fun insertRow() {
        throw SQLFeatureNotSupportedException()
    }


    fun updateRow() {
        throw SQLFeatureNotSupportedException()
    }


    fun deleteRow() {
        throw SQLFeatureNotSupportedException()
    }


    fun refreshRow() {
        throw SQLFeatureNotSupportedException()
    }


    fun cancelRowUpdates() {
        throw SQLFeatureNotSupportedException()
    }


    fun moveToInsertRow() {
        throw SQLFeatureNotSupportedException()
    }


    fun moveToCurrentRow() {
        throw SQLFeatureNotSupportedException()
    }


    open val statement: ClickHouseStatement?
        get() {
            throw SQLFeatureNotSupportedException()
        }


    fun getObject(columnIndex: Int, map: Map<String?, Class<*>?>?): Any {
        throw SQLFeatureNotSupportedException()
    }


    fun getRef(columnIndex: Int): Ref {
        throw SQLFeatureNotSupportedException()
    }


    fun getBlob(columnIndex: Int): Blob {
        throw SQLFeatureNotSupportedException()
    }


    fun getClob(columnIndex: Int): Clob {
        throw SQLFeatureNotSupportedException()
    }


    open fun getArray(columnIndex: Int): Array? {
        throw SQLFeatureNotSupportedException()
    }


    fun getObject(columnLabel: String?, map: Map<String?, Class<*>?>?): Any {
        throw SQLFeatureNotSupportedException()
    }


    fun getRef(columnLabel: String?): Ref {
        throw SQLFeatureNotSupportedException()
    }


    fun getBlob(columnLabel: String?): Blob {
        throw SQLFeatureNotSupportedException()
    }


    fun getClob(columnLabel: String?): Clob {
        throw SQLFeatureNotSupportedException()
    }


    open fun getArray(columnLabel: String?): Array? {
        throw SQLFeatureNotSupportedException()
    }


    open fun getDate(columnIndex: Int, cal: Calendar?): Date? {
        throw SQLFeatureNotSupportedException()
    }


    fun getDate(columnLabel: String?, cal: Calendar?): Date {
        throw SQLFeatureNotSupportedException()
    }


    fun getTime(columnIndex: Int, cal: Calendar?): Time {
        throw SQLFeatureNotSupportedException()
    }


    fun getTime(columnLabel: String?, cal: Calendar?): Time {
        throw SQLFeatureNotSupportedException()
    }


    fun getTimestamp(columnIndex: Int, cal: Calendar?): Timestamp {
        throw SQLFeatureNotSupportedException()
    }


    fun getTimestamp(columnLabel: String?, cal: Calendar?): Timestamp {
        throw SQLFeatureNotSupportedException()
    }


    open fun getURL(columnIndex: Int): URL? {
        throw SQLFeatureNotSupportedException()
    }


    open fun getURL(columnLabel: String?): URL? {
        throw SQLFeatureNotSupportedException()
    }


    fun updateRef(columnIndex: Int, x: Ref?) {
        throw SQLFeatureNotSupportedException()
    }


    fun updateRef(columnLabel: String?, x: Ref?) {
        throw SQLFeatureNotSupportedException()
    }


    fun updateBlob(columnIndex: Int, x: Blob?) {
        throw SQLFeatureNotSupportedException()
    }


    fun updateBlob(columnLabel: String?, x: Blob?) {
        throw SQLFeatureNotSupportedException()
    }


    fun updateClob(columnIndex: Int, x: Clob?) {
        throw SQLFeatureNotSupportedException()
    }


    fun updateClob(columnLabel: String?, x: Clob?) {
        throw SQLFeatureNotSupportedException()
    }


    fun updateArray(columnIndex: Int, x: Array?) {
        throw SQLFeatureNotSupportedException()
    }


    fun updateArray(columnLabel: String?, x: Array?) {
        throw SQLFeatureNotSupportedException()
    }


    fun getRowId(columnIndex: Int): RowId {
        throw SQLFeatureNotSupportedException()
    }


    fun getRowId(columnLabel: String?): RowId {
        throw SQLFeatureNotSupportedException()
    }


    fun updateRowId(columnIndex: Int, x: RowId?) {
        throw SQLFeatureNotSupportedException()
    }


    fun updateRowId(columnLabel: String?, x: RowId?) {
        throw SQLFeatureNotSupportedException()
    }


    val holdability: Int
        get() {
            throw SQLFeatureNotSupportedException()
        }


    open val isClosed: Boolean
        get() {
            throw SQLFeatureNotSupportedException()
        }


    fun updateNString(columnIndex: Int, nString: String?) {
        throw SQLFeatureNotSupportedException()
    }


    fun updateNString(columnLabel: String?, nString: String?) {
        throw SQLFeatureNotSupportedException()
    }


    fun updateNClob(columnIndex: Int, nClob: NClob?) {
        throw SQLFeatureNotSupportedException()
    }


    fun updateNClob(columnLabel: String?, nClob: NClob?) {
        throw SQLFeatureNotSupportedException()
    }


    fun getNClob(columnIndex: Int): NClob {
        throw SQLFeatureNotSupportedException()
    }


    fun getNClob(columnLabel: String?): NClob {
        throw SQLFeatureNotSupportedException()
    }


    fun getSQLXML(columnIndex: Int): SQLXML {
        throw SQLFeatureNotSupportedException()
    }


    fun getSQLXML(columnLabel: String?): SQLXML {
        throw SQLFeatureNotSupportedException()
    }


    fun updateSQLXML(columnIndex: Int, xmlObject: SQLXML?) {
        throw SQLFeatureNotSupportedException()
    }


    fun updateSQLXML(columnLabel: String?, xmlObject: SQLXML?) {
        throw SQLFeatureNotSupportedException()
    }


    fun getNString(columnIndex: Int): String {
        throw SQLFeatureNotSupportedException()
    }


    fun getNString(columnLabel: String?): String {
        throw SQLFeatureNotSupportedException()
    }


    fun getNCharacterStream(columnIndex: Int): Reader {
        throw SQLFeatureNotSupportedException()
    }


    fun getNCharacterStream(columnLabel: String?): Reader {
        throw SQLFeatureNotSupportedException()
    }


    fun updateNCharacterStream(columnIndex: Int, x: Reader?, length: Long) {
        throw SQLFeatureNotSupportedException()
    }


    fun updateNCharacterStream(columnLabel: String?, reader: Reader?, length: Long) {
        throw SQLFeatureNotSupportedException()
    }


    fun updateAsciiStream(columnIndex: Int, x: InputStream?, length: Long) {
        throw SQLFeatureNotSupportedException()
    }


    fun updateBinaryStream(columnIndex: Int, x: InputStream?, length: Long) {
        throw SQLFeatureNotSupportedException()
    }


    fun updateCharacterStream(columnIndex: Int, x: Reader?, length: Long) {
        throw SQLFeatureNotSupportedException()
    }


    fun updateAsciiStream(columnLabel: String?, x: InputStream?, length: Long) {
        throw SQLFeatureNotSupportedException()
    }


    fun updateBinaryStream(columnLabel: String?, x: InputStream?, length: Long) {
        throw SQLFeatureNotSupportedException()
    }


    fun updateCharacterStream(columnLabel: String?, reader: Reader?, length: Long) {
        throw SQLFeatureNotSupportedException()
    }


    fun updateBlob(columnIndex: Int, inputStream: InputStream?, length: Long) {
        throw SQLFeatureNotSupportedException()
    }


    fun updateBlob(columnLabel: String?, inputStream: InputStream?, length: Long) {
        throw SQLFeatureNotSupportedException()
    }


    fun updateClob(columnIndex: Int, reader: Reader?, length: Long) {
        throw SQLFeatureNotSupportedException()
    }


    fun updateClob(columnLabel: String?, reader: Reader?, length: Long) {
        throw SQLFeatureNotSupportedException()
    }


    fun updateNClob(columnIndex: Int, reader: Reader?, length: Long) {
        throw SQLFeatureNotSupportedException()
    }


    fun updateNClob(columnLabel: String?, reader: Reader?, length: Long) {
        throw SQLFeatureNotSupportedException()
    }


    fun updateNCharacterStream(columnIndex: Int, x: Reader?) {
        throw SQLFeatureNotSupportedException()
    }


    fun updateNCharacterStream(columnLabel: String?, reader: Reader?) {
        throw SQLFeatureNotSupportedException()
    }


    fun updateAsciiStream(columnIndex: Int, x: InputStream?) {
        throw SQLFeatureNotSupportedException()
    }


    fun updateBinaryStream(columnIndex: Int, x: InputStream?) {
        throw SQLFeatureNotSupportedException()
    }


    fun updateCharacterStream(columnIndex: Int, x: Reader?) {
        throw SQLFeatureNotSupportedException()
    }


    fun updateAsciiStream(columnLabel: String?, x: InputStream?) {
        throw SQLFeatureNotSupportedException()
    }


    fun updateBinaryStream(columnLabel: String?, x: InputStream?) {
        throw SQLFeatureNotSupportedException()
    }


    fun updateCharacterStream(columnLabel: String?, reader: Reader?) {
        throw SQLFeatureNotSupportedException()
    }


    fun updateBlob(columnIndex: Int, inputStream: InputStream?) {
        throw SQLFeatureNotSupportedException()
    }


    fun updateBlob(columnLabel: String?, inputStream: InputStream?) {
        throw SQLFeatureNotSupportedException()
    }


    fun updateClob(columnIndex: Int, reader: Reader?) {
        throw SQLFeatureNotSupportedException()
    }


    fun updateClob(columnLabel: String?, reader: Reader?) {
        throw SQLFeatureNotSupportedException()
    }


    fun updateNClob(columnIndex: Int, reader: Reader?) {
        throw SQLFeatureNotSupportedException()
    }


    fun updateNClob(columnLabel: String?, reader: Reader?) {
        throw SQLFeatureNotSupportedException()
    }


    fun <T> getObject(columnIndex: Int, type: Class<T>?): T {
        throw SQLFeatureNotSupportedException()
    }


    fun <T> getObject(columnLabel: String?, type: Class<T>?): T {
        throw SQLFeatureNotSupportedException()
    }


    fun <T> unwrap(iface: Class<T>?): T {
        throw SQLFeatureNotSupportedException()
    }


    fun isWrapperFor(iface: Class<*>?): Boolean {
        throw SQLFeatureNotSupportedException()
    }
}
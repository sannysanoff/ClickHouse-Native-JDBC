package com.github.housepower.jdbc.wrapper

import java.sql.*

abstract class SQLDatabaseMetadata : DatabaseMetaData {
    override fun getDriverMajorVersion(): Int {
        return 0
    }

    override fun getDriverMinorVersion(): Int {
        return 0
    }

    
    override fun allProceduresAreCallable(): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun allTablesAreSelectable(): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun getURL(): String {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun getUserName(): String {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun isReadOnly(): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun nullsAreSortedHigh(): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun nullsAreSortedLow(): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun nullsAreSortedAtStart(): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun nullsAreSortedAtEnd(): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun getDatabaseProductName(): String {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun getDatabaseProductVersion(): String {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun getDriverName(): String {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun getDriverVersion(): String {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun usesLocalFiles(): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun usesLocalFilePerTable(): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun supportsMixedCaseIdentifiers(): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun storesUpperCaseIdentifiers(): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun storesLowerCaseIdentifiers(): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun storesMixedCaseIdentifiers(): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun supportsMixedCaseQuotedIdentifiers(): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun storesUpperCaseQuotedIdentifiers(): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun storesLowerCaseQuotedIdentifiers(): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun storesMixedCaseQuotedIdentifiers(): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun getIdentifierQuoteString(): String {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun getSQLKeywords(): String {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun getNumericFunctions(): String {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun getStringFunctions(): String {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun getSystemFunctions(): String {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun getTimeDateFunctions(): String {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun getSearchStringEscape(): String {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun getExtraNameCharacters(): String {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun supportsAlterTableWithAddColumn(): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun supportsAlterTableWithDropColumn(): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun supportsColumnAliasing(): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun nullPlusNonNullIsNull(): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun supportsConvert(): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun supportsConvert(fromType: Int, toType: Int): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun supportsTableCorrelationNames(): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun supportsDifferentTableCorrelationNames(): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun supportsExpressionsInOrderBy(): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun supportsOrderByUnrelated(): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun supportsGroupBy(): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun supportsGroupByUnrelated(): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun supportsGroupByBeyondSelect(): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun supportsLikeEscapeClause(): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun supportsMultipleResultSets(): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun supportsMultipleTransactions(): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun supportsNonNullableColumns(): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun supportsMinimumSQLGrammar(): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun supportsCoreSQLGrammar(): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun supportsExtendedSQLGrammar(): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun supportsANSI92EntryLevelSQL(): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun supportsANSI92IntermediateSQL(): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun supportsANSI92FullSQL(): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun supportsIntegrityEnhancementFacility(): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun supportsOuterJoins(): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun supportsFullOuterJoins(): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun supportsLimitedOuterJoins(): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun getSchemaTerm(): String {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun getProcedureTerm(): String {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun getCatalogTerm(): String {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun isCatalogAtStart(): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun getCatalogSeparator(): String {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun supportsSchemasInDataManipulation(): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun supportsSchemasInProcedureCalls(): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun supportsSchemasInTableDefinitions(): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun supportsSchemasInIndexDefinitions(): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun supportsSchemasInPrivilegeDefinitions(): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun supportsCatalogsInDataManipulation(): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun supportsCatalogsInProcedureCalls(): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun supportsCatalogsInTableDefinitions(): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun supportsCatalogsInIndexDefinitions(): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun supportsCatalogsInPrivilegeDefinitions(): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun supportsPositionedDelete(): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun supportsPositionedUpdate(): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun supportsSelectForUpdate(): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun supportsStoredProcedures(): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun supportsSubqueriesInComparisons(): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun supportsSubqueriesInExists(): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun supportsSubqueriesInIns(): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun supportsSubqueriesInQuantifieds(): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun supportsCorrelatedSubqueries(): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun supportsUnion(): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun supportsUnionAll(): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun supportsOpenCursorsAcrossCommit(): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun supportsOpenCursorsAcrossRollback(): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun supportsOpenStatementsAcrossCommit(): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun supportsOpenStatementsAcrossRollback(): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun getMaxBinaryLiteralLength(): Int {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun getMaxCharLiteralLength(): Int {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun getMaxColumnNameLength(): Int {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun getMaxColumnsInGroupBy(): Int {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun getMaxColumnsInIndex(): Int {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun getMaxColumnsInOrderBy(): Int {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun getMaxColumnsInSelect(): Int {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun getMaxColumnsInTable(): Int {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun getMaxConnections(): Int {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun getMaxCursorNameLength(): Int {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun getMaxIndexLength(): Int {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun getMaxSchemaNameLength(): Int {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun getMaxProcedureNameLength(): Int {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun getMaxCatalogNameLength(): Int {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun getMaxRowSize(): Int {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun doesMaxRowSizeIncludeBlobs(): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun getMaxStatementLength(): Int {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun getMaxStatements(): Int {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun getMaxTableNameLength(): Int {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun getMaxTablesInSelect(): Int {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun getMaxUserNameLength(): Int {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun getDefaultTransactionIsolation(): Int {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun supportsTransactions(): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun supportsTransactionIsolationLevel(level: Int): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun supportsDataDefinitionAndDataManipulationTransactions(): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun supportsDataManipulationTransactionsOnly(): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun dataDefinitionCausesTransactionCommit(): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun dataDefinitionIgnoredInTransactions(): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun getProcedures(catalog: String, schemaPattern: String, procedureNamePattern: String): ResultSet {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun getProcedureColumns(catalog: String, schemaPattern: String, procedureNamePattern: String,
                                     columnNamePattern: String): ResultSet {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun getTables(catalog: String, schemaPattern: String, tableNamePattern: String, types: Array<String>): ResultSet {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun getSchemas(): ResultSet {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun getCatalogs(): ResultSet {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun getTableTypes(): ResultSet {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun getColumns(catalog: String, schemaPattern: String, tableNamePattern: String, columnNamePattern: String): ResultSet {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun getColumnPrivileges(catalog: String, schema: String, table: String, columnNamePattern: String): ResultSet {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun getTablePrivileges(catalog: String, schemaPattern: String, tableNamePattern: String): ResultSet {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun getBestRowIdentifier(catalog: String, schema: String, table: String, scope: Int, nullable: Boolean): ResultSet {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun getVersionColumns(catalog: String, schema: String, table: String): ResultSet {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun getPrimaryKeys(catalog: String, schema: String, table: String): ResultSet {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun getImportedKeys(catalog: String, schema: String, table: String): ResultSet {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun getExportedKeys(catalog: String, schema: String, table: String): ResultSet {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun getCrossReference(parentCatalog: String, parentSchema: String, parentTable: String,
                                   foreignCatalog: String, foreignSchema: String, foreignTable: String): ResultSet {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun getTypeInfo(): ResultSet {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun getIndexInfo(catalog: String, schema: String, table: String, unique: Boolean, approximate: Boolean): ResultSet {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun supportsResultSetType(type: Int): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun supportsResultSetConcurrency(type: Int, concurrency: Int): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun ownUpdatesAreVisible(type: Int): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun ownDeletesAreVisible(type: Int): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun ownInsertsAreVisible(type: Int): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun othersUpdatesAreVisible(type: Int): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun othersDeletesAreVisible(type: Int): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun othersInsertsAreVisible(type: Int): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun updatesAreDetected(type: Int): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun deletesAreDetected(type: Int): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun insertsAreDetected(type: Int): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun supportsBatchUpdates(): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun getUDTs(catalog: String, schemaPattern: String, typeNamePattern: String, types: IntArray): ResultSet {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun getConnection(): Connection {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun supportsSavepoints(): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun supportsNamedParameters(): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun supportsMultipleOpenResults(): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun supportsGetGeneratedKeys(): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun getSuperTypes(catalog: String, schemaPattern: String, typeNamePattern: String): ResultSet {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun getSuperTables(catalog: String, schemaPattern: String, tableNamePattern: String): ResultSet {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun getAttributes(catalog: String, schemaPattern: String, typeNamePattern: String,
                               attributeNamePattern: String): ResultSet {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun supportsResultSetHoldability(holdability: Int): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun getResultSetHoldability(): Int {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun getDatabaseMajorVersion(): Int {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun getDatabaseMinorVersion(): Int {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun getJDBCMajorVersion(): Int {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun getJDBCMinorVersion(): Int {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun getSQLStateType(): Int {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun locatorsUpdateCopy(): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun supportsStatementPooling(): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun getRowIdLifetime(): RowIdLifetime {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun getSchemas(catalog: String, schemaPattern: String): ResultSet {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun supportsStoredFunctionsUsingCallSyntax(): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun autoCommitFailureClosesAllResultSets(): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun getClientInfoProperties(): ResultSet {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun getFunctions(catalog: String, schemaPattern: String, functionNamePattern: String): ResultSet {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun getFunctionColumns(catalog: String, schemaPattern: String, functionNamePattern: String,
                                    columnNamePattern: String): ResultSet {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun getPseudoColumns(catalog: String, schemaPattern: String, tableNamePattern: String,
                                  columnNamePattern: String): ResultSet {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun generatedKeyAlwaysReturned(): Boolean {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun <T> unwrap(iface: Class<T>): T {
        throw SQLFeatureNotSupportedException()
    }

    
    override fun isWrapperFor(iface: Class<*>?): Boolean {
        throw SQLFeatureNotSupportedException()
    }
}
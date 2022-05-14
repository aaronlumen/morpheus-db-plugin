package com.lumen.edge.plugins.DBPlugin

import com.morpheusdata.core.Plugin
import com.morpheusdata.core.PluginProvider
import com.morpheusdata.core.*
import com.morpheusdata.model.*
import com.morpheusdata.core.network
import com.morpheusdata.core.cloud
import com.morpheusdata.model.Permission
import com.morpheusdata.model.OptionType
import com.morpheusdata.model.projection

/**
 * AaronLumen's Custom DB Relay Reports Plugin
 */
class DBPlugin extends Plugin {
    /** public interface MorpheusContext
    public List <KeyPairs> findOrGenerateKeyPair(Account tenantId)
    {
        return MorpheusCloudService.getCloud();{ }

    }


    public interface ReportProvider {
            extends UIExtensionProvider {
        }       HTMLResponse renderTemplate(ReportResult reportResult, java.util.Map<java.lang.String, java.util.List<ReportResultRow>> reportRowsBySection)

    }
**/

    public interface MorpheusReportService {
        getReadOnlyDatabaseConnection()
    }

    public List<OptionType> getSettings() {
        return DBPlugin.settings; }

    @Override
    String getCode() {
        return 'DBPlugin'
    }


    @Override
    void initialize() {
        AARONLUMENdbReportProvider AARONLUMENdbReportProvider = new AARONLUMENdbReportProvider(this, morpheus)
        this.pluginProviders.put(AARONLUMENdbReportProvider.code, AARONLUMENdbReportProvider)
        def optionSourceProvider = new optionSourceProvider(this, morpheus)
        this.pluginProviders.put(optionSourceProvider.code, optionSourceProvider)
        this.setName("Lumen Edge Orchestrator DB Relay")
        this.setDescription("Lumen Database Relay Plugin")
        String pluginSettings = morpheus.getSettings(DBPlugin.plugin).blockingGet()
        def pluginDeserialized = new JsonSlurper().parseText(pluginSettings)
    }

    @Override
    void onDestroy() {
    }


    @Override
    public List<Permission> getPermissions() {
        Permission permission = new Permission('Lumen DBRelay Report', 'AARONLUMEN-DB-Relay', [Permission.AccessType.full])
        return [permission]

    }

    void process(ReportResult reportResult) {
        morpheus.report.updateReportResultStatus(reportResult,ReportResult.Status.generating).blockingGet();
        Long displayOrder = 0
        List<GroovyRowResult> results = []
        Connection dbConnection

        try {
            dbConnection = morpheus.report.getReadOnlyDatabaseConnection().blockingGet()
            if(reportResult.configMap?.phrase) {
                String phraseMatch = "${reportResult.configMap?.phrase}%"
                results = new Sql(dbConnection).rows("SELECT id,name,status from instance WHERE name LIKE ${phraseMatch} order by name asc;")
            } else {
                results = new Sql(dbConnection).rows("SELECT id,name,status from instance order by name asc;")
            }
        } finally {
            morpheus.report.releaseDatabaseConnection(dbConnection)
        }
        log.info("Results: ${results}")
        Observable<GroovyRowResult> observable = Observable.fromIterable(results) as Observable<GroovyRowResult>
        observable.map{ resultRow ->
            log.info("Mapping resultRow ${resultRow}")
            Map<String,Object> data = [name: resultRow.name, id: resultRow.id, status: resultRow.status]
            ReportResultRow resultRowRecord = new ReportResultRow(section: ReportResultRow.SECTION_MAIN, displayOrder: displayOrder++, dataMap: data)
            log.info("resultRowRecord: ${resultRowRecord.dump()}")
            return resultRowRecord
        }.buffer(50).doOnComplete {
            morpheus.report.updateReportResultStatus(reportResult,ReportResult.Status.ready).blockingGet();
        }.doOnError { Throwable t ->
            morpheus.report.updateReportResultStatus(reportResult,ReportResult.Status.failed).blockingGet();
        }.subscribe {resultRows ->
            morpheus.report.appendResultRows(reportResult,resultRows).blockingGet()
        }
    }
}
/**
import com.morpheusdata.core.Plugin


 * Example Custom Reports Plugin

class ReportsPlugin extends Plugin {

    @Override
    String getCode() {
        return 'morpheus-reports-plugin'
    }

    @Override
    void initialize() {
        CustomReportProvider customReportProvider = new CustomReportProvider(this, morpheus)
        this.pluginProviders.put(customReportProvider.code, customReportProvider)
        this.setName("Custom Reports")

    }

    @Override
    void onDestroy() {
    }
}
 **/

package com.lumen.edge.plugins.DBPlugin


import com.morpheusdata.core.Plugin
import com.morpheusdata.model.Permission
import com.morpheusdata.model.OptionType

/**
 * AaronLumen's Custom DB Relay Reports Plugin
 */
class DBPlugin extends Plugin {

    @Override
    String getCode() {
        return 'DBPlugin'
    }

    @Override
    void initialize() {
        AARONLUMENdbReportProvider AARONLUMENdbReportProvider = new AARONLUMENdbReportProvider(this, morpheus)
        this.pluginProviders.put(AARONLUMENdbReportProvider.code, AARONLUMENdbReportProvider)
        def OptionSourceProvider = new OptionSourceProvider(this, morpheus)
        this.PluginProviders.put(OptionSourceProvider.code, OptionSourceProvider)
        this.setName("Lumen Edge Orchestrator DB Relay")
        this.setDescription("Lumen Database Relay Plugin by aaronlumen & Edge CORE©")
    }

    @Override
    void onDestroy() {
    }


    @Override
    public List<Permission> getPermissions() {
        Permission permission = new Permission('Lumen DBRelay Report', 'aaronlumenDBRelay', [Permission.AccessType.full])
        return [permission]

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

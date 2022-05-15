package com.morpheusdata.ui

import com.morpheusdata.core.Plugin
import com.morpheusdata.model.Permission

/**
 * Lumen Custom UI Plugin
 */
class LumenPlugin extends Plugin {

	@Override
	void initialize() {
		this.setName("Lumen Global JS/CSS")
		CustomJsProvider customJsProvider = new CustomJsProvider(this, this.morpheus)
		this.pluginProviders.put(customJsProvider.code, customJsProvider)
		
	}
	String getCode(){
		return "LumenPluginUI"
	}
	@Override
	void onDestroy() {
	}
}

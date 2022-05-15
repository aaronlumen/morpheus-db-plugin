package com.morpheusdata.ui

import com.morpheusdata.core.AbstractGlobalUIComponentProvider
import com.morpheusdata.core.AbstractInstanceTabProvider
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.core.web.MorpheusWebRequestService
import com.morpheusdata.model.UIScope
import com.morpheusdata.model.Account
import com.morpheusdata.model.ContentSecurityPolicy
import com.morpheusdata.model.Instance
import com.morpheusdata.model.User
import com.morpheusdata.views.HTMLResponse
import com.morpheusdata.views.ViewModel

class CustomJsProvider extends AbstractGlobalUIComponentProvider {
	String defaultToken = "Error: morpheusContext returned null for requestService"
	Plugin plugin
	MorpheusContext morpheusContext

	CustomJsProvider(Plugin plugin, MorpheusContext context) {
		this.plugin = plugin
		this.morpheusContext = context
	}

	@Override
	MorpheusContext getMorpheus() {
		morpheusContext
	}

	@Override
	Plugin getPlugin() {
		plugin
	}

	@Override
	String getCode() {
		return 'global-js-provider'
	}

	@Override
	String getName() {
		return 'Global JS Provider'
	}

	@Override
	HTMLResponse renderTemplate(User user, Account account) {
		ViewModel<String> model = new ViewModel<String>()
		MorpheusWebRequestService requestService = morpheusContext.getWebRequest()
		model.object = requestService?.getNonceToken() ?: defaultToken

		getRenderer().renderTemplate("hbs/globalJs", model)	}

	@Override
	Boolean show(User user, Account account) {
		return true
	}

	@Override
	ContentSecurityPolicy getContentSecurityPolicy() {
		ContentSecurityPolicy csp = new ContentSecurityPolicy()
		csp.scriptSrc = " 'unsafe-eval' *.force.com *.salesforceliveagent.com *.salesforce.com"
		csp.frameSrc = '*.force.com'
		csp.styleSrc = '*.force.com'
		csp.connectSrc = '*.force.com'
		csp
	}

}

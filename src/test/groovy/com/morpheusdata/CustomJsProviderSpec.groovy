package com.morpheusdata

import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.model.Account
import com.morpheusdata.model.User
import com.morpheusdata.ui.CustomJsProvider
import com.morpheusdata.views.HTMLResponse
import spock.lang.Specification
import spock.lang.Subject

class CustomJsProviderSpec extends Specification {
    Plugin plugin = Mock(Plugin)
    MorpheusContext context = Mock(MorpheusContext)
    @Subject
    CustomJsProvider provider = new CustomJsProvider(plugin, context)

    void "renderTemplate with instance"() {
        given:
        def user = new User()
        def account = new Account()

        when:
        HTMLResponse res = provider.renderTemplate(user, account)

        then:
        1 * plugin.name >> 'My Plugin'
        1 * plugin.classLoader >> this.class.classLoader
        res.html.compareTo("""<div id="#lumenChatBug"></div>
<style>
    .chatbug-class {
        font-weight: bold;
    }
</style>
<script>
    console.log('test')
</script>
""")
    }
}

import com.morpheus.core.Plugin;

class MyPlugin extends Plugin {

    @Override
    void initialize() {
        this.setName("Lumen Morpheus DB-Relay Plugin [by @aaronlumen]");
        CustomTabProvider tabProvider = new CustomTabProvider(this, morpheus);
        this.registerProvider(tabProvider);
    }
}
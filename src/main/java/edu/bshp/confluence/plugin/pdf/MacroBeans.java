package edu.bshp.confluence.plugin.pdf;

import com.atlassian.sal.api.message.I18nResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import static com.atlassian.plugins.osgi.javaconfig.OsgiServices.importOsgiService;

@Configuration
public class MacroBeans {
    @Bean
    public I18nResolver i18nResolver() {
        return importOsgiService(I18nResolver.class);
    }
}

package edu.bshp.confluence.plugin.pdf;

import com.atlassian.confluence.content.render.xhtml.ConversionContext;
import com.atlassian.confluence.macro.Macro;
import com.atlassian.confluence.renderer.radeox.macros.MacroUtils;
import com.atlassian.confluence.util.velocity.VelocityUtils;
import java.util.Map;

public class HideInPdfExport implements Macro {
    @Override
    public String execute(Map<String, String> params, String body, ConversionContext conversionContext) {
        Map<String, Object> context = MacroUtils.defaultVelocityContext();
        context.put("body", body);
        return VelocityUtils.getRenderedTemplate("/velocity/hiddenInPdfExport.vm", context);
    }

    @Override
    public BodyType getBodyType() {
        return BodyType.RICH_TEXT;
    }

    @Override
    public OutputType getOutputType() {
        return OutputType.BLOCK;
    }
}

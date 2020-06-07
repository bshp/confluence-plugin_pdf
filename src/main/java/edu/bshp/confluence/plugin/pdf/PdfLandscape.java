package edu.bshp.confluence.plugin.pdf;

import com.atlassian.confluence.content.render.xhtml.ConversionContext;
import com.atlassian.confluence.content.render.xhtml.storage.macro.MacroId;
import com.atlassian.confluence.macro.Macro;
import com.atlassian.confluence.renderer.radeox.macros.MacroUtils;
import com.atlassian.confluence.util.velocity.VelocityUtils;
import com.atlassian.confluence.xhtml.api.MacroDefinition;
import com.atlassian.fugue.Option;
import java.util.Map;
import java.util.NoSuchElementException;

public class PdfLandscape implements Macro {
    @Override
    public String execute(Map<String, String> params, String body, ConversionContext conversionContext) {
        String pageBodyStorageFormat = conversionContext.getEntity().getBodyContent().getBody();
        boolean landscapeFirstDomElememt = checkFirstDomElement(pageBodyStorageFormat, conversionContext);

        Map<String, Object> context = MacroUtils.defaultVelocityContext();
        context.put("body", body);
        context.put("landscapeFirstDomElement", landscapeFirstDomElememt);
        return VelocityUtils.getRenderedTemplate("/velocity/pdfLandscape.vm", context);
    }

    private boolean checkFirstDomElement(String pageBody, ConversionContext conversionContext) {
        //FIXME this is broken. p auto-cursor-target is always first DOM element
        String firstNode = pageBody.substring(pageBody.indexOf('<'), pageBody.indexOf('>') + 1);
        if (!firstNode.contains("ac:name")) { //macro-check
            return false;
        }

        if (!retrieveAttribute("ac:name", firstNode).equals("pdf_landscape")) { //landscapemacro-check
            return false;
        }

        MacroDefinition macroDefinition = (MacroDefinition) conversionContext.getProperty("macroDefinition");
        Option<MacroId> option = macroDefinition.getMacroId();
        try {
            if (option.isDefined()) {
                String macroId = option.get().getId();
                if (macroId.equals(retrieveAttribute("ac:macro-id", pageBody))) { //id-check
                    return true;
                }
            }
        } catch (NoSuchElementException e) {
            // in macro edit mode always throws exception, "try" is important to avoid exception in console
        }
        return false;
    }

    private String retrieveAttribute(String attribute, String tag) {
        if (tag.contains(attribute)) {
            int indexStartQuote = tag.indexOf(attribute) + attribute.length() + 2;
            return tag.substring(indexStartQuote, tag.indexOf('"', indexStartQuote + 1));
        } else {
            return "";
        }
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

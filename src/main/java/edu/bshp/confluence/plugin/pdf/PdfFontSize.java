package edu.bshp.confluence.plugin.pdf;

import com.atlassian.confluence.content.render.xhtml.ConversionContext;
import com.atlassian.confluence.macro.Macro;
import com.atlassian.confluence.macro.MacroExecutionException;
import com.atlassian.confluence.renderer.radeox.macros.MacroUtils;
import com.atlassian.confluence.util.velocity.VelocityUtils;
import com.atlassian.sal.api.message.I18nResolver;
import java.util.Map;
import java.util.UUID;

public class PdfFontSize implements Macro {
    private final I18nResolver i18n;

    public PdfFontSize(I18nResolver i18n) {
        this.i18n = i18n;
    }

    @Override
    public String execute(Map<String, String> params, String body, ConversionContext conversionContext) throws MacroExecutionException {
        int fontSize = getFontSize(params.get("fontsize"));
        body = changeFontSizeBody(body);

        Map<String, Object> context = MacroUtils.defaultVelocityContext();
        context.put("body", body);
        context.put("fontSize", fontSize);
        context.put("bodyId", UUID.randomUUID().toString());
        return VelocityUtils.getRenderedTemplate("/edu.bshp.confluence.plugin.pdf/velocity/pdfFontSize.vm", context);
    }

    private String changeFontSizeBody(String body) {
        //Transformation of bodyContent, as the text inside the paragraph needs to be explicitly styled to receive the macros font-style, or the style will be omitted while exporting to pdf
        int lastIndex = 0;
        while (body.indexOf("<p", lastIndex) >= 0) {
            int indexParagraph = body.indexOf("<p", lastIndex) + 2;
            body = body.substring(0, indexParagraph) + " style=\"font-size: 1.0em;\"" + body.substring(indexParagraph);
            lastIndex = indexParagraph;
        }
        return body;
    }

    private int getFontSize(String fontSizeString) throws MacroExecutionException {
        try {
            return Integer.parseInt(fontSizeString);
        } catch (NumberFormatException numberFormatException) {
            throw new MacroExecutionException(i18n.getText("edu.bshp.confluence.plugin.pdf.pdf_fontsize.error.nointeger"));
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

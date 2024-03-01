
package com.qhrtech.emr.restapi.services.impl;

import com.qhrtech.emr.restapi.services.RtfConversionService;
import com.qhrtech.emr.restapi.services.exceptions.RtfConversionException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.rtf.RTFEditorKit;
import org.springframework.stereotype.Component;

/**
 * Implementation of {@link RtfConversionService} that converts RTF formatted data.
 *
 * @author David.Huang
 */
@Component
public class DefaultRtfConversionService implements RtfConversionService {

  /**
   * The syntax of Header in the contents of an RTF File.
   *
   * Reference: Rich Text Format (RTF) Specification Version 1.9.1 by Microsoft Corporation 2008.
   */
  private static final String RTF_HEADER = "{\\rtf";

  /**
   * Retrieve the plain text from a styled document and omit trike through text.
   *
   * @param doc Styled document
   *
   * @return Plain text.
   *
   * @throws BadLocationException if the portion is not a valid part of the document.
   */
  private static String retrieveText(StyledDocument doc) throws BadLocationException {

    int offset = 0;
    int length = doc.getLength();

    StringBuilder result = new StringBuilder();

    String text = doc.getText(offset, length);
    for (int i = 0; i < text.length(); i++) {
      Element e = doc.getCharacterElement(i);

      // Omit strike through text
      Object attr = e.getAttributes().getAttribute("strike");
      if (attr != null) {
        continue;
      }

      result.append(text.charAt(i));
    }

    return result.toString();
  }

  /**
   * @inheritDoc
   *
   *             This implementation will remove rtf content annotated with strikethrough from the
   *             resulting string.
   */
  @Override
  public String getPainText(String rtfText) throws RtfConversionException {
    String originalText = rtfText;

    while (rtfText.contains("\\\\")) {
      rtfText = rtfText.replace("\\\\", "\\");
    }

    if (!isRtfFormat(rtfText)) {
      return originalText;
    }

    DefaultStyledDocument rtfDocument = new DefaultStyledDocument();

    try (StringReader reader = new StringReader(rtfText)) {
      RTFEditorKit rtfParser = new RTFEditorKit();
      rtfParser.read(reader, rtfDocument, 0);
      return retrieveText(rtfDocument);
    } catch (IOException | BadLocationException e) {
      throw new RtfConversionException("Could not convert Rich Text content.", e);
    }
  }

  /**
   * @inheritDoc
   */
  @Override
  public byte[] getStyledContentBytes(String content) throws RtfConversionException {
    try {
      if (isRtfFormat(content)) {
        return content.getBytes();
      }

      RTFEditorKit editor = new RTFEditorKit();
      Document doc = editor.createDefaultDocument();

      SimpleAttributeSet attrs = new SimpleAttributeSet();
      StyleConstants.setFontFamily(attrs, "Verdana");
      StyleConstants.setFontSize(attrs, 11);
      doc.insertString(0, content, attrs);

      ByteArrayOutputStream out = new ByteArrayOutputStream();
      editor.write(out, doc, 0, doc.getLength());
      out.close();

      return out.toByteArray();
    } catch (IOException | BadLocationException e) {
      throw new RtfConversionException("Could not convert Rich Text content.", e);
    }
  }

  /**
   * Checks if the content provided is RTF formatted.
   *
   * @param content Content to be checked
   *
   * @return <code>true</code> if the content is RTF formatted, or <code>false</code> if not.
   */
  private boolean isRtfFormat(String content) {
    return content.startsWith(RTF_HEADER);
  }
}

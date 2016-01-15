package com.lsntsolutions.gtmApp.helper.impl.pdf;

import com.lowagie.text.Font;

import java.awt.*;

/**
 * Created by a983060 on 15/12/2015.
 */
public final class PdfConstants {

    //Se usa en pdf de Input, Output y Supplying
    public static float[] columnWidths = {1.3f, 7f, 1.5f, 1.2f, 2f, 0.6f};

    // Fuentes
    public static Font fontHeader = new Font(Font.TIMES_ROMAN, 11f, Font.NORMAL, Color.BLACK);
    public static Font fontDetails = new Font(Font.TIMES_ROMAN, 10f, Font.NORMAL, Color.BLACK);
}

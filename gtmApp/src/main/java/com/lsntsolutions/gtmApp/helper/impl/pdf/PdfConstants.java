package com.lsntsolutions.gtmApp.helper.impl.pdf;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;

/**
 * Created by a983060 on 15/12/2015.
 */
public final class PdfConstants {

    //Se usa en pdf de Input, Output y Supplying
    public static float[] columnWidths = {1.3f, 7f, 1.5f, 1.2f, 2f, 0.6f};

    // Fuentes
    public static Font fontHeader = new Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 11f, com.itextpdf.text.Font.NORMAL, BaseColor.BLACK);
    public static Font fontDetails = new Font(Font.FontFamily.TIMES_ROMAN, 10f, Font.NORMAL, BaseColor.BLACK);
}

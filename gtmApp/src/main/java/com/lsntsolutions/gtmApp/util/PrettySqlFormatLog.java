package com.lsntsolutions.gtmApp.util;

import com.p6spy.engine.common.P6Util;
import org.hibernate.engine.jdbc.internal.BasicFormatterImpl;
import org.hibernate.engine.jdbc.internal.Formatter;
import com.p6spy.engine.spy.appender.MessageFormattingStrategy;

public class PrettySqlFormatLog implements MessageFormattingStrategy {

    private final Formatter formatter = new BasicFormatterImpl();

    @Override
    public String formatMessage(int connectionId, String now, long elapsed, String category, String prepared, String sql) {
        return P6Util.singleLine(sql);
    }

}
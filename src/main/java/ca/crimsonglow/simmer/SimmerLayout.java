package ca.crimsonglow.simmer;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.apache.log4j.Layout;
import org.apache.log4j.spi.LoggingEvent;

public class SimmerLayout extends Layout
{
	private static final String _DATE_FORMAT = "yyyy-MM-dd HH:mm:ssZ";
	private static final long _MILLISECONDS_PER_SECOND = 1000;

	private final Calendar _cal;
	private final Format _df;
	private final Object _tslock = new Object();
	private long _ts;

	public SimmerLayout()
	{
		super();
		_df = new SimpleDateFormat(_DATE_FORMAT);
		_cal = Calendar.getInstance();
		_ts = 0;
	}

	@Override
	public String format(LoggingEvent event)
	{
		String level = event.getLevel().toString();
		Date date = _cal.getTime();

		long now = System.currentTimeMillis();
		long ts = now - (now % _MILLISECONDS_PER_SECOND);

		// synchronize access to cached Calendar instance
		// update date once every second
		synchronized(_tslock) {
			if (ts != _ts) {
				_ts = ts;
				_cal.setTimeInMillis(now);
				date = _cal.getTime();
			}
		}

		// 200908181726 kjiwa inspired by amazon.platform.logging.AmznLayout
		// yyyy-MM-dd HH:mm:ssZ [level] logger: message
		StringBuffer sb = new StringBuffer();
		sb.append(_df.format(date)).append(" ");
		sb.append("[").append(level).append("] ");
		sb.append(event.getLoggerName()).append(": ");
		sb.append(event.getRenderedMessage());
		sb.append(LINE_SEP);

		return sb.toString();
	}

	@Override
	public boolean ignoresThrowable()
	{
		return true;
	}

	@Override
	public void activateOptions()
	{
	}
}

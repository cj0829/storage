package org.csr.common.storage.support;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public final class TimerUtil {
	private static TimerUtil instance = null;

	private static boolean isCanceled = false;

	private Timer timer = null;

	private TimerUtil() {
		this.timer = new Timer();
	}

	private void reloadTimer() {
		if (!isCanceled) {
			this.timer.cancel();
			this.timer = new Timer();
		}
	}

	public static synchronized TimerUtil getInstance() {
		if (instance == null) {
			instance = new TimerUtil();
		}
		return instance;
	}

	public void cancel() {
		isCanceled = true;
		this.timer.cancel();
	}

	public void schedule(TimerTask task, Date firstTime, long period) {
		if (!isCanceled)
			try {
				this.timer.schedule(task, firstTime, period);
			} catch (IllegalStateException ex) {
				reloadTimer();
			}
	}

	public void schedule(TimerTask task, Date time) {
		if (!isCanceled)
			try {
				this.timer.schedule(task, time);
			} catch (IllegalStateException ex) {
				reloadTimer();
			}
	}

	public void schedule(TimerTask task, long delay) {
		if (!isCanceled)
			try {
				this.timer.schedule(task, delay);
			} catch (IllegalStateException ex) {
				reloadTimer();
			}
	}

	public void schedule(TimerTask task, long delay, long period) {
		if (!isCanceled)
			try {
				this.timer.schedule(task, delay, period);
			} catch (IllegalStateException ex) {
				reloadTimer();
			}
	}

	public void scheduleAtFixedRate(TimerTask task, Date firstTime, long period) {
		if (!isCanceled)
			try {
				this.timer.schedule(task, firstTime, period);
			} catch (IllegalStateException ex) {
				reloadTimer();
			}
	}

	public void scheduleAtFixedRate(TimerTask task, long delay, long period) {
		if (!isCanceled)
			try {
				this.timer.schedule(task, delay, period);
			} catch (IllegalStateException ex) {
				reloadTimer();
			}
	}
}

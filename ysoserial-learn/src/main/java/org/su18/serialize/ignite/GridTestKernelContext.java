package org.su18.serialize.ignite;

import org.apache.ignite.IgniteCheckedException;
import org.apache.ignite.IgniteException;
import org.apache.ignite.IgniteLogger;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.internal.*;
import org.apache.ignite.internal.processors.metric.GridMetricManager;
import org.apache.ignite.internal.processors.plugin.IgnitePluginProcessor;
import org.apache.ignite.internal.processors.resource.GridResourceProcessor;
import org.apache.ignite.internal.processors.subscription.GridInternalSubscriptionProcessor;
import org.apache.ignite.internal.util.typedef.internal.S;
import org.apache.ignite.internal.util.typedef.internal.U;
import org.apache.ignite.plugin.PluginProvider;
import org.apache.ignite.spi.metric.noop.NoopMetricExporterSpi;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.ExecutorService;

/**
 * @author su18
 */
public class GridTestKernelContext extends GridKernalContextImpl {

	/**
	 * @param log Logger to use in context config.
	 */
	public GridTestKernelContext(IgniteLogger log) {
		this(log, new IgniteConfiguration());

		try {
			add(new IgnitePluginProcessor(this, config(), Collections.<PluginProvider>emptyList()));
		} catch (IgniteCheckedException e) {
			throw new IllegalStateException("Must not fail for empty plugins list.", e);
		}
	}

	/**
	 * @param log Logger to use in context config.
	 * @param cfg Configuration to use in Test
	 */
	public GridTestKernelContext(IgniteLogger log, IgniteConfiguration cfg) {
		super(new GridLoggerProxy(log, null, null, null),
				new IgniteKernal(null),
				cfg,
				new GridKernalGatewayImpl(null),
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				cfg.getPluginProviders() != null && cfg.getPluginProviders().length > 0 ?
						Arrays.asList(cfg.getPluginProviders()) : U.allPluginProviders(),
				null,
				null,
				null,
				new LongJVMPauseDetector(log)
		);

		setFieldValue(grid(), "cfg", config());
		setFieldValue(grid(), "ctx", this);

		config().setGridLogger(log);

		if (cfg.getMetricExporterSpi() == null || cfg.getMetricExporterSpi().length == 0)
			cfg.setMetricExporterSpi(new NoopMetricExporterSpi());

		add(new GridMetricManager(this));
		add(new GridResourceProcessor(this));
		add(new GridInternalSubscriptionProcessor(this));
	}

	public GridTestKernelContext() {
	}

	/**
	 * Starts everything added (in the added order).
	 *
	 * @throws IgniteCheckedException If failed
	 */
	public void start() throws IgniteCheckedException {
		for (GridComponent comp : this)
			comp.start();
	}

	/**
	 * Stops everything added.
	 *
	 * @param cancel Cancel parameter.
	 * @throws IgniteCheckedException If failed.
	 */
	public void stop(boolean cancel) throws IgniteCheckedException {
		List<GridComponent> comps = components();

		for (ListIterator<GridComponent> it = comps.listIterator(comps.size()); it.hasPrevious(); ) {
			GridComponent comp = it.previous();

			comp.stop(cancel);
		}
	}

	/**
	 * Sets system executor service.
	 *
	 * @param sysExecSvc Executor service
	 */
	public void setSystemExecutorService(ExecutorService sysExecSvc) {
		this.sysExecSvc = sysExecSvc;
	}

	/**
	 * Sets executor service.
	 *
	 * @param execSvc Executor service
	 */
	public void setExecutorService(ExecutorService execSvc) {
		this.execSvc = execSvc;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return S.toString(GridTestKernelContext.class, this, super.toString());
	}


	public static void setFieldValue(Object obj, String fieldName, Object val) throws IgniteException {
		assert obj != null;
		assert fieldName != null;

		try {
			Class<?> cls = obj instanceof Class ? (Class) obj : obj.getClass();

			Field field = cls.getDeclaredField(fieldName);

			boolean isFinal = (field.getModifiers() & Modifier.FINAL) != 0;

			boolean isStatic = (field.getModifiers() & Modifier.STATIC) != 0;

			if (isFinal && isStatic)
				throw new IgniteException("Modification of static final field through reflection.");

			boolean accessible = field.isAccessible();

			if (!accessible)
				field.setAccessible(true);

			field.set(obj, val);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			throw new IgniteException("Failed to set object field [obj=" + obj + ", field=" + fieldName + ']', e);
		}
	}
}

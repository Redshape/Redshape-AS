package com.redshape.ui.utils;

import com.redshape.ui.application.events.IEventQueue;
import com.redshape.ui.application.notifications.INotificationsManager;
import com.redshape.ui.application.status.IStatusBar;
import com.redshape.ui.data.bindings.render.IViewRenderer;
import com.redshape.ui.data.bindings.render.IViewRendererBuilder;
import com.redshape.ui.data.providers.IProvidersFactory;
import com.redshape.ui.data.state.IUIStateManager;
import com.redshape.ui.data.stores.IStoresManager;
import com.redshape.ui.views.IViewsManager;
import com.redshape.ui.windows.IWindowsManager;
import com.redshape.utils.clonners.IObjectsCloner;
import org.springframework.context.ApplicationContext;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

/**
 * Created by IntelliJ IDEA.
 * User: nikelin
 * Date: 03.01.11
 * Time: 21:25
 * To change this template use File | Settings | File Templates.
 */
public final class UIRegistry {
    private static Map<UIConstants.Attribute, Object> values = new HashMap<UIConstants.Attribute, Object>();
    private static JFrame context;
    private static MenuBar menu;
    private static IStatusBar progressBar;
	private static Settings settings;
    private static Timer timer = new Timer();
    private static IEventQueue eventsQueue;
	private static INotificationsManager notificationsManager;


    public static IEventQueue getEventQueue() {
        return getContext().getBean(IEventQueue.class);
    }

	public static void setSettings( Settings settings ) {
		UIRegistry.settings = settings;
	}

    public static void restartTimer() {
        timer.cancel();
        timer = new Timer();
    }
    
    public static Timer getTimer() {
        return timer;
    }
    
	public static Settings getSettings() {
		if ( UIRegistry.settings == null ) {
			UIRegistry.settings = new Settings();
		}

		return UIRegistry.settings;
	}

    public static IStatusBar getStatusBar() {
        return progressBar;
    }

    public static void setStatusBar(IStatusBar progressBar) {
        UIRegistry.progressBar = progressBar;
    }

    @SuppressWarnings("unchecked")
	public static <V> V set( UIConstants.Attribute id, Object value ) {
        values.put(id, value);
        return (V) get(id);
    }

    @SuppressWarnings("unchecked")
	public static <V> V get( UIConstants.Attribute id ) {
        return (V) values.get(id);
    }

    public static MenuBar getMenu() {
    	return UIRegistry.menu;
    }
    
    public static void setMenu( MenuBar menu ) {
    	UIRegistry.menu = menu;
    }
    
	public static void setRootContext( JFrame context ) {
        UIRegistry.context = context;
    }

    @SuppressWarnings("unchecked")
	public static <V extends JFrame> V getRootContext() {
        return (V) context;
    }
    
    public static IObjectsCloner getObjectsClonner() {
    	return getContext().getBean( IObjectsCloner.class );
    }
    
    @SuppressWarnings("unchecked")
	public static <V extends IViewRenderer<?, ?>> IViewRendererBuilder<V> getViewRendererFacade() {
    	return (IViewRendererBuilder<V>) getContext().getBean( IViewRendererBuilder.class );
    }
    
    public static IProvidersFactory getProvidersFactory() {
    	return getContext().getBean( IProvidersFactory.class );
    }
    
    public static IStoresManager getStoresManager() {
    	return getContext().getBean( IStoresManager.class );
    }
    
    public static IViewsManager getViewsManager() {
    	return getContext().getBean( IViewsManager.class );
    }

	public static INotificationsManager getNotificationsManager() {
		if ( notificationsManager != null ) {
			return notificationsManager;
		}

		return notificationsManager;
	}

	public static IUIStateManager getStateManager() {
		return getContext().getBean( IUIStateManager.class );
	}
    
    @SuppressWarnings("unchecked")
	public static <V extends IWindowsManager<?>> V getWindowsManager() {
    	return (V) getContext().getBean( IWindowsManager.class );
    }
    
    public static ApplicationContext getContext() {
    	return get( UIConstants.System.APP_CONTEXT);
    }

}
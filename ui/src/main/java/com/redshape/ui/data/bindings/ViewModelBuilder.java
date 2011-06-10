package com.redshape.ui.data.bindings;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import com.redshape.bindings.BeanInfo;
import com.redshape.bindings.BindingException;
import com.redshape.bindings.IBeanInfo;
import com.redshape.bindings.types.CollectionType;
import com.redshape.bindings.types.IBindable;
import com.redshape.ui.application.UIException;
import com.redshape.ui.data.bindings.views.ChoiceModel;
import com.redshape.ui.data.bindings.views.CollectionModel;
import com.redshape.ui.data.bindings.views.ComposedModel;
import com.redshape.ui.data.bindings.views.DefferedModel;
import com.redshape.ui.data.bindings.views.IComposedModel;
import com.redshape.ui.data.bindings.views.IDefferedModel;
import com.redshape.ui.data.bindings.views.IViewModel;
import com.redshape.ui.data.bindings.views.PropertyModel;

public class ViewModelBuilder implements IViewModelBuilder {
	private Map<Class<?>, IComposedModel> processed = 
						new HashMap<Class<?>, IComposedModel>();

	private Collection<Class<?>> processing = new HashSet<Class<?>>();
	
	@Override
	public IComposedModel createUI( Class<?> type ) throws UIException {
		return this.createUI( type, null );
	}
	
	@Override
	public synchronized IComposedModel createUI( Class<?> type, String name ) throws UIException {
		return this.createUI( type, null, name, true );
	}
	
	@Override
	public synchronized IComposedModel createUI( Class<?> type, boolean processDeffered ) throws UIException {
		return this.createUI( type, null, null, processDeffered );
	}
	
	@Override
	public synchronized IComposedModel createUI( Class<?> type, String id, String name, boolean processDeffered ) throws UIException {
		try {
			IBeanInfo beanInfo = this.createBeanInfo( type );
			
			if ( this.isProcessed(type) ) {
				return this.processed.get( type );
			}
			
			if ( this.isProcessing(type) ) {
				return this.createDefferedView(name, type);
			}
			
			this.processing.add(type);
			
			IComposedModel rootModel = new ComposedModel(beanInfo);
			rootModel.setId( id );
			rootModel.setTitle(name);
			
			Collection<IBindable> bindables = beanInfo.getBindables();
			for ( IBindable bindable : bindables ) {
				if ( !processDeffered || !bindable.isComposite() ) {
					rootModel.addChild( this.processBindable(bindable) );
				} else {
					rootModel.addChild( this.createUI( bindable.getType(), bindable.getId(), bindable.getName(), processDeffered ) );
				}
			}
			
			this.processed.put( type, rootModel );
			this.processing.remove( type );
			
			return rootModel;
		} catch ( BindingException e ) {
			throw new UIException( e.getMessage(), e );
		}
	}
	
	@Override
	public boolean isProcessed( Class<?> type ) {
		return this.processed.containsKey(type);
	}
	
	protected boolean isProcessing( Class<?> type ) {
		return this.processing.contains( type );
	}
	
	protected IDefferedModel createDefferedView( Class<?> type ) {
		return this.createDefferedView( null, type );
	}
	
	protected IDefferedModel createDefferedView( String name, Class<?> type ) {
		return this.createDefferedView( null, name, type );
	}
	
	protected IDefferedModel createDefferedView( String id, String name, Class<?> type ) {
		DefferedModel model = new DefferedModel( this.createBeanInfo(type) );
		model.setId( id );
		model.setTitle( name );
		return model;
	}
	
	protected IViewModel<?> processBindable( IBindable bindable ) throws UIException {
		if ( bindable.isCollection() ) {
			return this.processCollection( bindable );
		} else if ( bindable.isComposite() ) {
			return this.createDefferedView( bindable.getId(), bindable.getName(), bindable.getType() );
		} else {
			return this.processProperty(bindable);
		}
	}
	
	protected IViewModel<?> processCollection( IBindable bindable ) throws UIException {
		try {
			CollectionType collectionType = bindable.asCollectionObject().getCollectionType();
			if ( collectionType == null ) {
				collectionType = CollectionType.CHOICE;
			}

			switch ( collectionType ) {
			case CHOICE:
				return new ChoiceModel(bindable);
			case LIST:
				return new CollectionModel( bindable );
			default:
				throw new UIException("Unsupported collection type!");
			}
		} catch ( BindingException e  ) {
			throw new UIException("Binding exception", e);
		}
	}
	
	protected IViewModel<?> processProperty( IBindable property ) {
		return new PropertyModel( property );
	}
	
	protected IBeanInfo createBeanInfo( Class<?> type ) {
		return new BeanInfo(type);
	}

}

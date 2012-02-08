package com.redshape.ui.data.bindings.render.properties;

import com.redshape.bindings.types.IBindable;
import com.redshape.ui.application.UIException;
import com.redshape.ui.data.bindings.properties.IPropertyUI;
import com.redshape.utils.EnumUtils;
import com.redshape.utils.IEnum;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

public class IEnumUI extends JComboBox implements IPropertyUI<IEnum<?>, JComboBox> {
	private static final long serialVersionUID = 5479414921873691099L;

    private IBindable descriptor;

	public IEnumUI( IBindable bindable ) {
		this.descriptor = bindable;

        this.init();
	}

    @Override
    public JComboBox asComponent() {
        return this;
    }

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void init() {
		for ( IEnum<?> item : EnumUtils.allOf((Class<? extends IEnum>) this.descriptor.getType()) ) {
			this.addItem(item);
		}
		
		this.setRenderer(new RenderGuy());
	}

    @Override
    public void setValue(IEnum<?> value) throws UIException {
        this.setSelectedItem( value );
    }

    @Override
    public IEnum<?> getValue() throws UIException {
        return (IEnum<?>) this.getSelectedItem();
    }

    protected static class RenderGuy implements ListCellRenderer, Serializable {
		private static final long serialVersionUID = -5743380825040772169L;

		@Override
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			return new JLabel( String.valueOf( ( (IEnum<?>) value).name() ) );
		}
	}

}
package com.redshape.ui.data.bindings.render.properties;

import javax.swing.*;

import com.redshape.bindings.types.IBindable;
import com.redshape.ui.application.UIException;
import com.redshape.ui.data.bindings.properties.IPropertyUI;

public class StringUI extends JTextField implements IPropertyUI<String> {
	private static final long serialVersionUID = -870134475160003776L;

    @SuppressWarnings("unused")
	private IBindable descriptor;

	public StringUI( IBindable bindable ) {
		this.descriptor = bindable;
	}

    @Override
    public JComponent asComponent() {
        return this;
    }

    @Override
    public void setValue( String value) throws UIException {
        this.setText( value );
    }

    @Override
    public String getValue() throws UIException {
        return this.getText();
    }
}

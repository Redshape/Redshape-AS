package com.redshape.servlet.form.render.impl.fields;

import com.redshape.i18n.impl.StandardI18NFacade;
import com.redshape.servlet.form.RenderMode;
import com.redshape.servlet.form.fields.SelectField;

import java.util.Map;

public class SelectFieldRenderer extends AbstractFormFieldRenderer<SelectField<?>> {

	@Override
	public String render(SelectField<?> field, RenderMode mode ) {
		StringBuilder builder = new StringBuilder();
		builder.append("<select ");

		builder.append("name=\"").append( field.getCanonicalName() ).append("\" ");


		if ( field.getId() != null ) {
			builder.append("id=\"").append( field.getId() ).append("\" ");
		}

		this.applyErrorStateIfNeeds(field);
		this.buildAttributes(builder, field);

		builder.append(">");

		Map<String, ?> options = field.getOptions();
		for ( String key : options.keySet() ) {
			builder.append("<option value=\"").append( options.get(key) ).append("\" ");

			if ( field.getValue() != null && field.getValue().toString().equals( options.get( key ).toString() ) ) {
				builder.append(" selected=\"selected\" ");
			}

			builder.append(">");
			builder.append( StandardI18NFacade._( key ) )
					.append("</option> ");
		}

		builder.append("</select>");

		return this.applyDecorators( builder, field, mode );
	}

}

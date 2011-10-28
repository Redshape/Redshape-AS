package com.redshape.servlet.form.render.impl.fields;

import com.redshape.i18n.impl.StandardI18NFacade;
import com.redshape.servlet.form.RenderMode;
import com.redshape.servlet.form.decorators.IDecorator;
import com.redshape.servlet.form.fields.CheckboxGroupField;
import com.redshape.validators.result.IValidationResult;

import java.util.Map;

/**
 * @package com.redshape.servlet.form.render.impl.fields
 * @user cyril
 * @date 6/27/11 10:42 PM
 */
public class CheckboxGroupFieldRenderer extends AbstractFormFieldRenderer<CheckboxGroupField<?>> {

    public CheckboxGroupFieldRenderer() {
        super();
    }

    protected void renderItem( StringBuilder builder, CheckboxGroupField<?> field, String name, Object value ) {
        builder.append("<span class=\"label\">").append( StandardI18NFacade._(name) ).append("</span>")
		    .append("<input type=\"checkbox\" value=\"")
		    .append(value)
		    .append("\" ");

		if ( field.getValue() != null && field.getValues().contains(value) ) {
			builder.append(" checked=\"checked\" ");
		}

		Map<String, Object> params = field.getAttributes();
		if ( !field.getValidationResults().isEmpty() ) {
			for ( IValidationResult result : field.getValidationResults() ) {
				if ( !result.isValid() ) {
					String classValue = (String) params.get("class");
					if ( classValue == null ) {
						params.put("class", "error");
					} else {
						params.put("class", params.get("class") + " error" );
					}
					break;
				}
			}
		}

		this.buildAttributes( builder, field );

		String canonicalName = field.getCanonicalName();
		if ( !canonicalName.endsWith("[]") ) {
			canonicalName = canonicalName.concat("[]");
		}

		builder.append("name=\"").append( canonicalName ).append("\" ");

        builder.append("/><br/>");
    }

    @Override
    public String render(CheckboxGroupField<?> item, RenderMode mode) {
        StringBuilder builder = new StringBuilder();
		builder.append("<fieldset>");
        Map<String, Object> options = (Map<String, Object>) item.getOptions();
        for ( String option : options.keySet() ) {
            this.renderItem( builder, item, option, options.get(option) );
        }

		if ( options.isEmpty() ) {
			builder.append("<strong>Any options available</strong>");
		}

		builder.append("</fieldset>");

        String data = builder.toString();
        for ( IDecorator decorator : item.getDecorators() ) {
            data = decorator.decorate( item, data );
        }

        return data;
    }

}

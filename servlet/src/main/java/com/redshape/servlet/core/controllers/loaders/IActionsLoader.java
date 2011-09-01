package com.redshape.servlet.core.controllers.loaders;

import com.redshape.servlet.core.controllers.IAction;

import java.util.Set;

/**
 * @author Cyril A. Karpenko <self@nikelin.ru>
 * @package com.redshape.servlet.core.controllers
 * @date 8/13/11 12:30 PM
 */
public interface IActionsLoader {

	public Set<Class<? extends IAction>> load();

}
/*
 * Copyright (c) 2011 Nat Pryce, Timo Meinen.
 *
 * This file is part of Team Piazza.
 *
 * Team Piazza is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * Team Piazza is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.natpryce.piazza;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import static com.natpryce.piazza.BuildMonitorController.BUILD_TYPE_ID;
import static com.natpryce.piazza.BuildMonitorController.PROJECT_ID;
import static java.util.Collections.emptyList;
import jetbrains.buildServer.web.openapi.PageExtension;

public class PiazzaLinkPageExtension implements PageExtension {

	private final Piazza piazza;

	public PiazzaLinkPageExtension (Piazza piazza) {
		this.piazza = piazza;
	}

	public String getIncludeUrl () {
		return piazza.resourcePath("piazza-link.jsp");
	}

	public List<String> getCssPaths () {
		return emptyList();
	}

	public List<String> getJsPaths () {
		return emptyList();
	}

	public String getPluginName () {
		return Piazza.PLUGIN_NAME;
	}

	public boolean isAvailable (HttpServletRequest request) {
		return isBuildTypeView(request)
				|| isProjectView(request);
	}

	public void fillModel (Map<String, Object> model, HttpServletRequest request) {
		String queryParameter = queryParameter(request);
		model.put("piazzaHref", request.getContextPath() + Piazza.PATH + "?" + queryParameter + "=" + request.getParameter(queryParameter));
	}

	private String queryParameter (HttpServletRequest request) {
		if (isBuildTypeView(request)) {
			return BUILD_TYPE_ID;
		} else if (isProjectView(request)) {
			return PROJECT_ID;
		} else {
			throw new IllegalStateException("cannot create link for page at " + request.getRequestURI());
		}
	}

	private boolean isProjectView (HttpServletRequest request) {
		return request.getRequestURI().endsWith("/project.html");
	}

	private boolean isBuildTypeView (HttpServletRequest request) {
		return request.getRequestURI().endsWith("/viewType.html");
	}

}

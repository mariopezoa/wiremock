/*
 * Copyright (C) 2011 Thomas Akehurst
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.tomakehurst.wiremock.http;

import com.github.tomakehurst.wiremock.replacing.PlaceholderHandler;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static com.github.tomakehurst.wiremock.common.LocalNotifier.notifier;

public abstract class AbstractRequestHandler implements RequestHandler, RequestEventSource {

	protected List<RequestListener> listeners = newArrayList();
	protected final ResponseRenderer responseRenderer;
	
	public AbstractRequestHandler(ResponseRenderer responseRenderer) {
		this.responseRenderer = responseRenderer;
	}

	@Override
	public void addRequestListener(RequestListener requestListener) {
		listeners.add(requestListener);
	}

	@Override
	public Response handle(Request request) {
		ResponseDefinition responseDefinition = handleRequest(request);
		responseDefinition.setOriginalRequest(request);
		Response response = responseRenderer.render(responseDefinition);
		notifier().info("MPJ: "+ response.getBodyAsString());
		if(response.wasConfigured()) {
			PlaceholderHandler replacer = new PlaceholderHandler(response);
			response = replacer.handle();
		}
		for (RequestListener listener: listeners) {
			listener.requestReceived(request, response);
		}
		
		return response;
	}
	
	protected abstract ResponseDefinition handleRequest(Request request);
}

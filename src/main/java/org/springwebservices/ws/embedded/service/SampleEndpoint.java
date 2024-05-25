/*
 * Copyright 2007-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springwebservices.ws.embedded.service;

import java.util.List;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.springwebservices.product.schema.beans.GetProductRequest;
import org.springwebservices.product.schema.beans.Product;
import org.springwebservices.product.schema.beans.ProductResponse;

import com.elitecore.core.commons.util.Logger;

import repositiory.ProductRepository;


@Endpoint
public class SampleEndpoint {

	public final static String NAMESPACE = "http://www.springwebservices.org/product/schema/beans";
	public final static String GET_PERSONS_REQUEST = "get-product-request";
	
	//sample Repositiory for test data
	ProductRepository productRepo=new ProductRepository();

	/**
	 * 
	 * @param code
	 * @return
	 */
	@PayloadRoot(localPart = GET_PERSONS_REQUEST, namespace = NAMESPACE)
	public @ResponsePayload ProductResponse getProducts(@RequestPayload GetProductRequest code) {
		ProductResponse productResponse = new ProductResponse();
		Logger.logInfo("SOAP DEMO", "RequestData::"+code.getName());
		Product product=productRepo.findproduct(code.getName());
		productResponse.getProduct().add(product);
		List<Product> proResponse=productResponse.getProduct();
		for (Product temp : proResponse) {
			Logger.logInfo("SOAP DEMO", "ResponseData ::"+"Code:"+temp.getCode()+" Price:"+temp.getPrice()+" Description:"+temp.getDescription());
		}
		return productResponse;
		
	}

}

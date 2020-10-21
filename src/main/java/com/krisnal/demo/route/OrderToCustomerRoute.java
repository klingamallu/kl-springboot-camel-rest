package com.krisnal.demo.route;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jsonvalidator.JsonValidationException;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.processor.validation.SchemaValidationException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.krisnal.demo.beans.Customer;
import com.krisnal.demo.beans.ErrorResponse;
import com.krisnal.demo.beans.Order;
import com.krisnal.demo.beans.OrderResponse;
import com.krisnal.demo.beans.OrderToCustomer;
import com.networknt.schema.ValidationMessage;


@Component
public class OrderToCustomerRoute extends RouteBuilder{

	
	@Override
	public void configure() throws Exception {
		
		onException(Exception.class)
		 .handled(true)
		 .bean(this, "setErrorDetails")
		 .marshal().json(JsonLibrary.Jackson, ErrorResponse.class)
		 .log("response sent to client :: ${body}");
		
		restConfiguration()
	      .component("servlet").bindingMode(RestBindingMode.off);	     

	    rest().post("/orders")
	          .consumes(MediaType.APPLICATION_JSON_VALUE)
	          .produces(MediaType.APPLICATION_JSON_VALUE)
	          .to("direct:processOrder");
	
	    from("direct:processOrder").routeId("processOrder")	    
	      .log(LoggingLevel.INFO, "Received request :: ${body} \\n ${headers}")
	      .convertBodyTo(String.class)
	      .to("json-validator:order_schema.json")
	      .unmarshal().json(JsonLibrary.Jackson,Order.class)
	      .transform().method(OrderToCustomer.class, "convertToCustomer")
	      .marshal().jacksonxml(Customer.class)
	      .to("validator:customer.xsd")
	      .log("Transformed request as xml -> ${body}")
	      .bean("orderService", "createOrder")
	      .marshal().json(JsonLibrary.Jackson,OrderResponse.class)
	      .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(201));          
	
	}
	
	public ErrorResponse setErrorDetails(Exchange exchange, Exception exception) {
			System.out.println("exception" + exception);
			ErrorResponse resp = new ErrorResponse();
			if(exception instanceof JsonValidationException) {
				resp.setStatus(400);
				resp.setMessage("JSON validation occured");
			}
			else if(exception instanceof SchemaValidationException) {
				resp.setStatus(400);
				resp.setMessage("XML Schema validation occured");
			}
			else {
				resp.setStatus(503);
				resp.setMessage("Service error  occured");				
			}
			exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, 400);
			return resp;
	}
}

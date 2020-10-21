package com.krisnal.demo;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.builder.ExchangeBuilder;
import org.apache.camel.builder.NotifyBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.RouteDefinition;
import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.request.WebRequest;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SampleCamelApplicationTest {
	
	
	@Autowired
    private CamelContext camelContext;
	
	@EndpointInject(uri = "mock:response")
	private MockEndpoint resultEndpoint;
	
	 @EndpointInject(uri = "direct:processOrder")
	 private ProducerTemplate producer;

	@Test
	public void processOrderTest() throws Exception {
		
		String sampleInputRequest = camelContext.getTypeConverter().convertTo(String.class,
				new File("src/test/resources/sample_input.json"));
		String sampleOutputResponse = camelContext.getTypeConverter().convertTo(String.class,
				new File("src/test/resources/validResponse.xml"));
		
		RouteDefinition route = camelContext.getRouteDefinition("processOrder");
        route.adviceWith(camelContext, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {            	           	
            	interceptSendToEndpoint("validator:*").skipSendToOriginalEndpoint()
            	.to("mock:response");
                            	
            }
        });
		
       camelContext.start();		
	   producer.sendBody(sampleInputRequest);
	   NotifyBuilder notifyBuilder = new NotifyBuilder(camelContext).create();
	   notifyBuilder.matches(5, TimeUnit.SECONDS);
	   resultEndpoint.expectedMessageCount(1);
	   Exchange mockExchange = resultEndpoint.getExchanges().get(0);
	   String transformedMessage = camelContext.getTypeConverter().convertTo(String.class, mockExchange.getIn().getBody());
	   assertXMLEquals(transformedMessage, sampleOutputResponse);
	   resultEndpoint.assertIsSatisfied();		
	    	   
	}
	
	@Test
	public void processOrderTestFailure() throws Exception {
		
		String sampleInputRequest = camelContext.getTypeConverter().convertTo(String.class,
				new File("src/test/resources/invalid_input.json"));
		String sampleOutputResponse = camelContext.getTypeConverter().convertTo(String.class,
				new File("src/test/resources/error_response.json"));
				
       camelContext.start();
       
       Exchange exchange = ExchangeBuilder.anExchange(camelContext).withBody(sampleInputRequest).build();
	   Exchange out = producer.send(exchange);		
	   String transformedMessage = camelContext.getTypeConverter().convertTo(String.class, out.getIn().getBody());
	   
	   ObjectMapper mapper = new ObjectMapper();
	   assertEquals(mapper.readTree(transformedMessage), mapper.readTree(sampleOutputResponse));
		
	    	   
	}
	
  
	
	public static void assertXMLEquals(String expectedXML, String actualXML) throws Exception {
		XMLUnit.setIgnoreWhitespace(true);
		XMLUnit.setIgnoreAttributeOrder(true);

		DetailedDiff diff = new DetailedDiff(XMLUnit.compareXML(expectedXML, actualXML));

		List<?> allDifferences = diff.getAllDifferences();
		Assert.assertEquals("Differences found: " + diff.toString(), 0, allDifferences.size());
	}

}

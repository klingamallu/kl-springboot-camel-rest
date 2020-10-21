package com.krisnal.demo;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.camel.CamelContext;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.krisnal.demo.beans.Order;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class DemoIITest{
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CamelContext camelContext;

    @LocalServerPort
     int randomServerPort;

    @Test
    public void testAddOrderSuccess() throws URISyntaxException
   {
       final String baseUrl = "http://localhost:"+randomServerPort+"/camel/orders";
       URI uri = new URI(baseUrl);
       Order order = new Order();
       order.setOrderId(111);
       order.setCustomerId(123);
       order.setOrderAmount(500.00);
       order.setCurrency("GBP");
       order.setTotalItems(5);

       HttpHeaders headers = new HttpHeaders();
       headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Order> request = new HttpEntity<>(order, headers);

       ResponseEntity<String> result = this.restTemplate.postForEntity(uri, request, String.class);

        //Verify request succeed
       Assert.assertEquals(201, result.getStatusCodeValue());
       Assert.assertTrue(result.getBody().contains("Success"));
   }

    
    @Test
    public void testAddOrderFailure() throws URISyntaxException
   {
       final String baseUrl = "http://localhost:"+randomServerPort+"/camel/orders";
       URI uri = new URI(baseUrl);
       Order order = new Order();
       order.setOrderId(111);
       order.setCustomerId(123);
       order.setOrderAmount(500.00);
       order.setTotalItems(10);

       HttpHeaders headers = new HttpHeaders();
       headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Order> request = new HttpEntity<>(order, headers);

       ResponseEntity<String> result = this.restTemplate.postForEntity(uri, request, String.class);
       System.out.println(result);

        //Verify request succeed
       Assert.assertEquals(400, result.getStatusCodeValue());
       Assert.assertTrue(result.getBody().contains("JSON validation occured"));
   }

}
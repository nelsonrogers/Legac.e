/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testament.config.restemplate;

import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

/**
 * Pour corriger le bug 'cannot retry due to server authentication, in streaming mode;'
 * sur twitter.updateStatus()
 * customize rest template with an interceptor
 * @see https://stackoverflow.com/questions/16748969/java-net-httpretryexception-cannot-retry-due-to-server-authentication-in-strea
 * @see https://www.baeldung.com/spring-rest-template-builder
 */

// le bug peut survenir lorsque l'on veut poster un tweet ou envoyer un Direct Message par exemple

@Slf4j
 public class CustomRestTemplateCustomizer implements RestTemplateCustomizer {
    @Override
    public void customize(RestTemplate restTemplate) {
        log.info("Customizing Rest Template");
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setOutputStreaming(false);   
        restTemplate.getInterceptors().add(new CustomClientHttpRequestInterceptor());
        restTemplate.setRequestFactory(requestFactory);
    }
}

package org.justhro.sample.service.api;

import org.justhro.sample.service.api.exception.WrongNameException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        contextId = "sampleRemote",
        name = "sample-service"
)
public interface SampleRemote {

    @GetMapping("/samples/hello/{name}")
    ResponseEntity<String> sayHello(@PathVariable("name") String name) throws WrongNameException;
}

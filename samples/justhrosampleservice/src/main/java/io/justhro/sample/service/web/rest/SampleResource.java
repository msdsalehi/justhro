package io.justhro.sample.service.web.rest;

import io.justhro.sample.service.api.SampleRemote;
import io.justhro.sample.service.api.exception.WrongNameException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/samples/hello")
public class SampleResource implements SampleRemote {

    @Override
    @GetMapping("/{name}")
    public ResponseEntity<String> sayHello(@PathVariable("name") String name) {
        if (name == null || name.isEmpty() || name.equalsIgnoreCase("ali")){
            WrongNameException wrongNameException = new WrongNameException("messsage", null);
            wrongNameException.addCause("cause 1");
            wrongNameException.addCause("cause 2");
            throw wrongNameException;
        }
        return ResponseEntity.ok("Hello " + name);
    }
}

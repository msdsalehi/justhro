package io.justhro.sample.client.web.rest;

import io.justhro.sample.service.api.SampleRemote;
import io.justhro.sample.service.api.exception.WrongNameException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RunSampleController {

    private final static Logger LOGGER = LoggerFactory.getLogger(RunSampleController.class);
    private final SampleRemote sampleRemote;

    public RunSampleController(SampleRemote sampleRemote) {
        this.sampleRemote = sampleRemote;
    }

    @GetMapping("/{name}")
    public ResponseEntity<String> sayHello(@PathVariable("name") String name) throws WrongNameException {
        try {
            return sampleRemote.sayHello(name);
        } catch (WrongNameException e) {
            LOGGER.error(e.getMessage(), e);
            throw e;
        } catch (RuntimeException e) {
            LOGGER.error(e.getMessage(), e);
            throw e;
        }
    }
}

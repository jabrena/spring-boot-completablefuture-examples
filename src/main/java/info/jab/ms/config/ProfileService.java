package info.jab.ms.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class ProfileService {

    @Autowired
    private Environment environment;

    public List<String> getActiveProfiles() {
        return Arrays.asList(environment.getActiveProfiles());
    }
}

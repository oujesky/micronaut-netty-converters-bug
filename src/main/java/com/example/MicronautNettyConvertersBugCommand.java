package com.example;

import io.micronaut.configuration.picocli.PicocliRunner;
import io.micronaut.core.convert.ConversionContext;
import io.micronaut.core.convert.TypeConverter;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import picocli.CommandLine.Command;

import java.util.Optional;

@Command(name = "micronaut-netty-converters-bug", description = "...",
        mixinStandardHelpOptions = true)
public class MicronautNettyConvertersBugCommand implements Runnable {

    public static void main(String[] args) throws Exception {
        PicocliRunner.run(MicronautNettyConvertersBugCommand.class, args);
    }

    @Inject private TestClient testClient;

    public void run() {
        var response = testClient.getResponse();
        System.out.println(response);
    }

    public record TestResponse(String response) {}

    @Client
    public interface TestClient {
        @Get("https://micronaut.io/")
        TestResponse getResponse();
    }

    @Singleton
    public static class TestTypeConverter implements TypeConverter<String, TestResponse> {

        @Override
        public Optional<TestResponse> convert(String object, Class<TestResponse> targetType, ConversionContext context) {
            return Optional.of(new TestResponse(object.substring(0, 100)));
        }
    }

}

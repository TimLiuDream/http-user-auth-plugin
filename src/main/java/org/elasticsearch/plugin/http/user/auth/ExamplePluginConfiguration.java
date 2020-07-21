package org.elasticsearch.plugin.http.user.auth;

import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.env.Environment;

import org.elasticsearch.ElasticsearchParseException;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.common.xcontent.yaml.YamlXContent;
import org.elasticsearch.env.Environment;

import java.io.IOException;
import java.nio.file.Path;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.Files.newBufferedReader;
import static org.elasticsearch.common.io.Streams.copyToString;

public class ExamplePluginConfiguration {
    private String test = "not set in config";
    private boolean enable = false;
    private String userName = "";
    private String userPassword = "";

    @Inject
    public ExamplePluginConfiguration(Environment env) throws IOException {
        // The directory part of the location matches the artifactId of this plugin
        Path configFile = env.pluginsFile().resolve("http-user-auth-plugin-1.0/config.yaml");
        String contents = copyToString(newBufferedReader(configFile, UTF_8));
        XContentParser parser = YamlXContent.yamlXContent.createParser(contents);

        String currentFieldName = null;
        XContentParser.Token token = parser.nextToken();
        assert token == XContentParser.Token.START_OBJECT;
        while ((token = parser.nextToken()) != XContentParser.Token.END_OBJECT) {
            if (token == XContentParser.Token.FIELD_NAME) {
                currentFieldName = parser.currentName();
            } else if (token.isValue()) {
                if ("test".equals(currentFieldName)) {
                    test = parser.text();
                } else if ("http.user.auth.enable".equals(currentFieldName)){
                    enable = parser.booleanValue();
                } else if ("http.user.auth.name".equals(currentFieldName)){
                    userName = parser.text();
                } else if ("http.user.auth.password".equals(currentFieldName)){
                    userPassword = parser.text();
                } else{
                    throw new ElasticsearchParseException("Unrecognized config key: {}", currentFieldName);
                }
            } else {
                throw new ElasticsearchParseException("Unrecognized config key: {}", currentFieldName);
            }
        }
    }

    public String getTestConfig() {
        return test;
    }

    public String getUserName(){
        return userName;
    }

    public boolean getEnable(){
        return enable;
    }

    public String getUserPassword(){
        return userPassword;
    }
}
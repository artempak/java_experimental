package com.navasanta.internals.dvla;

import org.springframework.http.client.SimpleClientHttpRequestFactory;

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * Created by: artemp
 * Date: 16 Oct, 2017
 */
public class DvlaHttpRequestFactory extends SimpleClientHttpRequestFactory {
  @Override
  protected void prepareConnection(HttpURLConnection connection, String httpMethod) throws IOException {
    connection.setRequestProperty("x-api-key", "secret_key_");
    connection.setConnectTimeout(10000);
    connection.setReadTimeout(10000);
    super.prepareConnection(connection, httpMethod);
  }
}
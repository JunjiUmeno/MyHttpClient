import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.ProxyAuthenticationStrategy;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;

public class MyHttpClient {
    public static void main(String[] args) {
        proxy();
    }

    static String proxyHost = "192.168.33.10";
    static int proxyPort = 3128;
    static String username = "um";
    static String password = "um";


    private static CloseableHttpClient proxyClient() {
        UsernamePasswordCredentials creds = new UsernamePasswordCredentials(username, password);
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(new AuthScope(proxyHost, proxyPort), creds);
        HttpClientBuilder clientBuilder = HttpClientBuilder.create();

        clientBuilder.useSystemProperties();
        clientBuilder.setProxy(new HttpHost(proxyHost, proxyPort));
        clientBuilder.setDefaultCredentialsProvider(credsProvider);
        clientBuilder.setProxyAuthenticationStrategy(new ProxyAuthenticationStrategy());

        return clientBuilder.build();
    }


    private static void proxy() {
        String to = "https://google.com";
        CloseableHttpClient client = proxyClient();
        HttpGet get = new HttpGet();
        get.setURI(URI.create(to));
        try {
            CloseableHttpResponse response = client.execute(get);
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            response.getEntity().writeTo(output);
            byte[] b = output.toByteArray();
            System.out.println(new String(b));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

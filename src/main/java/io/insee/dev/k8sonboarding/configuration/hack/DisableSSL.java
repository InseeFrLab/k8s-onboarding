package io.insee.dev.k8sonboarding.configuration.hack;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

@Configuration
@ConfigurationProperties(prefix="io.insee.dev.k8sonboarding.hack")
public class DisableSSL {

        private boolean disableSSL = false;

    
        @PostConstruct
        public void disableSSL() {
            System.out.println(disableSSL);
            if (disableSSL) {
                System.out.println("Disabling SSL verification ...");
                try {
                    DisableSSL.turnOffSslChecking();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (KeyManagementException e) {
                    e.printStackTrace();
                }
            }
        }
    
        private static final TrustManager[] UNQUESTIONING_TRUST_MANAGER = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                    }

                    public java.security.cert.X509Certificate[] getAcceptedIssuers(){
                        return null;
                    }
                }
        };

        public  static void turnOffSslChecking() throws NoSuchAlgorithmException, KeyManagementException {
            // Install the all-trusting trust manager
            final SSLContext sc = SSLContext.getInstance("SSL");
            sc.init( null, UNQUESTIONING_TRUST_MANAGER, null );
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        }

        public static void turnOnSslChecking() throws KeyManagementException, NoSuchAlgorithmException {
            // Return it to the initial state (discovered by reflection, now hardcoded)
            SSLContext.getInstance("SSL").init( null, null, null );
        }

    public boolean isDisableSSL() {
        return disableSSL;
    }

    public void setDisableSSL(boolean disableSSL) {
        this.disableSSL = disableSSL;
    }
}

/**
 *   © Copyright IBM Corporation 2016.
 *   This is licensed under the following license.
 *   The Eclipse Public 1.0 License (http://www.eclipse.org/legal/epl-v10.html)
 *   U.S. Government Users Restricted Rights:  Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
 */

package com.urbancode.air.plugin.rcq

import java.security.cert.CertificateException
import java.security.cert.X509Certificate

import org.apache.http.auth.AuthScope
import org.apache.http.auth.UsernamePasswordCredentials
import org.apache.http.client.config.RequestConfig
//import org.apache.http.client.config.RequestConfig.Builder
import org.apache.http.client.CredentialsProvider
import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.client.methods.HttpPut
import org.apache.http.conn.ssl.AllowAllHostnameVerifier
import org.apache.http.conn.ssl.SSLContextBuilder
import org.apache.http.conn.ssl.TrustStrategy
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.BasicCredentialsProvider
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils;

public class ClearQuestHelper {

    URL cqURL
    String username
    String password
    boolean acceptAllCertificates

    public ClearQuestHelper(String hostname, String repo, String db, String dbid,
         String recordId, String username, String password, boolean acceptAllCertificates) {
        if (!(hostname.startsWith("http://") || hostname.startsWith("https://"))) {
            hostname = "https://" + hostname
        }
        if (hostname.endsWith("/")) {
            hostname = hostname.substring(0, hostname.length()-1)
        }
        String tempURL = hostname
        tempURL += "/cqweb/oslc/repo/" + repo
        tempURL += "/db/" + db
        tempURL += "/record/" + dbid + "-" + recordId

        println "URL: " + tempURL
        try {
            cqURL = new URL(tempURL)
        } catch (MalformedURLException ex) {
            throw new MalformedURLException("[Error] Invalid ClearQuest URL formed.")
        }
        this.username = username
        this.password = password
        this.acceptAllCertificates = acceptAllCertificates
    }

    public String getURL() {
        return cqURL.toString()
    }

    private CloseableHttpClient buildCloseableHttpClient() {
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(
                AuthScope.ANY,
                new UsernamePasswordCredentials(username, password));
        HttpClientBuilder httpclientbuilder = HttpClients.custom()
            .setDefaultCredentialsProvider(credsProvider)
        if (acceptAllCertificates) {
            httpclientbuilder.setHostnameVerifier(new AllowAllHostnameVerifier())
            httpclientbuilder.setSslcontext(new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy()
            {
                public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException
                {
                    return true;
                }
            }).build())
        }
        CloseableHttpClient httpclient = httpclientbuilder.build()

        return httpclient
    }

    public void addRelatedChangeRequest(String title, String link) {
        HttpPut httpput = new HttpPut(getURL() + "?oslc.properties=oslc_cm:relatedChangeRequest")
        RequestConfig clientConfig = RequestConfig.custom().setAuthenticationEnabled(true).build()
        httpput.setConfig(clientConfig)
        httpput.setHeader("OSLC-Core-Version","2.0")
        httpput.setHeader("Content-Type","application/rdf+xml")
        httpput.setHeader("Accept","accept/json")
        //httpput.setHeader("If-Match","")

        String body = """
            <rdf:RDF
                 xmlns:dcterms="http://purl.org/dc/terms/"
                 xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
                 xmlns:oslc_cm="http://open-services.net/ns/cm#">

                <oslc_cm:ChangeRequest rdf:about="${getURL()}">
                <oslc_cm:relatedChangeRequest rdf:ID="link1" rdf:resource="${link.replaceAll("&", "&amp;")}" />
                </oslc_cm:ChangeRequest>

                <rdf:Description rdf:about="#link1">
                    <dcterms:title>${title}</dcterms:title>
               </rdf:Description>
            </rdf:RDF>
        """

        println ""
        println "Body: "
        println body
        println""

        StringEntity strEntity = new StringEntity(body)
        httpput.setEntity(strEntity)

        CloseableHttpResponse response
        try {
            response = buildCloseableHttpClient().execute(httpput)
        } catch (javax.net.ssl.SSLHandshakeException ex) {
            println "[Error] Unable to accept the certificate."
            println "[Possible Solution] Select the 'Accept All Certificates' check box."
            throw ex
        }
        try {
            int code = response.getStatusLine().getStatusCode()
            if (code < 200 || code >= 300) {
                throw new Exception("[Error] HTTP Call failed.")
            }
        } catch (Exception ex) {
            println "[Error] Unable to set the Related Change Request"
            println "Status Line: " + response.getStatusLine()
            println "Entity: " + EntityUtils.toString(response.getEntity())
            throw ex
        } finally {
            response.close()
        }
    }
}

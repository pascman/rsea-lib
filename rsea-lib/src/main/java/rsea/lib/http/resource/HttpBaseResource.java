package rsea.lib.http.resource;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import rsea.lib.http.RSHttp;
import rsea.lib.http.exception.ParsingException;
import rsea.lib.http.exception.RequestException;
import rsea.lib.util.etc.KeyValue;

/**
 *
 * @author Redsea81
 *	통신관련 Res 공통
 */

public abstract class HttpBaseResource {

    public int errorCode = ResourceCode.SUCCESS;;
    public String errorMsg = null;
    protected boolean isMultiPart = false;
    protected static final String CRLF = "\r\n";
    private String reqUrl = null;
    private String reqMethod = "GET";
    public Object tag;
    protected List<KeyValue> params = new ArrayList<KeyValue>();
    protected JSONObject responseData;
    private HashMap<String,String> reqHeaders = new HashMap<String,String>();
    public ParamFactory paramFactory = null;


    public interface ParamFactory{
        public void interceptParam(HttpBaseResource beforeRes, HttpBaseResource interceptRes);
    }
    private Context context;

    public HttpBaseResource(){
    }
    public abstract JSONObject body();
    public URLConnection makeRequestRes() throws RequestException {
        URLConnection retVal;
        try{
            retVal = makeRequest();
        }catch (Exception e) {
            RequestException ex = new RequestException(e);
            throw ex;
        }
        return retVal;
    }

    protected abstract String charSet();
    protected abstract void parsor(InputStream response) throws Exception;
    public abstract void headerParsor(HttpURLConnection conn) throws Exception;
    public abstract void setParameter(String... param);

    public HashMap<String, String> getReqHeaders(){
        return reqHeaders;
    }

    public HttpBaseResource setParameter(ParamFactory factory){
        paramFactory = factory;
        return this;
    }
    public void setParameter(ArrayList<KeyValue> params){
        this.params.clear();
        this.params.addAll(params);
    }

    public void parsorRes(InputStream response) throws ParsingException {
        try{
            parsor(response);
        }catch (Exception e) {
            ParsingException ex = new ParsingException(e);
            throw ex;
        }
    }

    public String B64decode(String B64Data){
        String encodecredentials = new String(Base64.decode(B64Data, 0));
        return encodecredentials;
    }

    public List<KeyValue> getParams(){
        return params;
    }

    public void setTag(Object tag){
        this.tag = tag;
    }
    public Object getTag(){
        return tag;
    }

    public String generateParamter(){
        StringBuffer buffer = new StringBuffer();
        for(int i=0;i< params.size();i++){
            if(i==0){
                buffer.append(params.get(i).toString());
            }else{
                buffer.append("&"+ params.get(i).toString());
            }
        }
        return buffer.toString();
    }

    public  void trustAllHosts() {
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[]{};
            }

            @Override
            public void checkClientTrusted(
                    java.security.cert.X509Certificate[] chain,
                    String authType)
                    throws java.security.cert.CertificateException {

            }

            @Override
            public void checkServerTrusted(
                    java.security.cert.X509Certificate[] chain,
                    String authType)
                    throws java.security.cert.CertificateException {
                // TODO Auto-generated method stub

            }
        }};
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection
                    .setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getMethod(){
        ResourceInfo resourcInfo = getClass().getAnnotation(ResourceInfo.class);
        if(resourcInfo == null || resourcInfo.method() == null) return reqMethod;
        return resourcInfo.method();
    }

    public String getReqUrl(){
        ResourceInfo resourcInfo = getClass().getAnnotation(ResourceInfo.class);
        if(resourcInfo == null || resourcInfo.method() == null) return reqUrl;
        return resourcInfo.url();
    }


    protected URLConnection makeRequest() throws Exception {
        if (params == null)
            throw new RequestException("no Request Param");
        String boundary = null;
        String requestUrl = getReqUrl();
        if(getMethod().equals("GET")){
            requestUrl = requestUrl + "?" + generateParamter();
        }
        URL url;
        if(requestUrl.startsWith("http://") || requestUrl.startsWith("https://")){
            url = new URL(requestUrl);
        }else{
            url = new URL(RSHttp.gSetting.getDefaultUrl() + ":" + RSHttp.gSetting.getPort() + requestUrl);
        }
        HttpURLConnection conn;
        if(url.toString().startsWith("https")){
            trustAllHosts();
            HttpsURLConnection httpsConn = (HttpsURLConnection) url.openConnection();
            httpsConn.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String s, SSLSession sslSession) {
                    return true;
                }
            });
            conn = httpsConn;
        }else{
            conn = (HttpURLConnection) url.openConnection();
        }
        HashMap<String, String> header = getReqHeaders();
        for (String key : header.keySet()) {
            conn.setRequestProperty(key, header.get(key));
        }
        conn.setRequestMethod(getMethod());


        if(RSHttp.gSetting.isDebug()){
            for(KeyValue pair : params){
                Log.d("ResourceData", "makeRequest " + pair.getKey() + " : " + pair.getValue());
            }
        }
        conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
        if(conn.getRequestMethod().equals("GET")){

        }else{
            if(isMultiPart) {
                boundary = "===" + System.currentTimeMillis() + "===";
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setDoOutput(true);
                conn.setDoInput(true);
                OutputStream os = conn.getOutputStream();
                writeMultiPartData(boundary, conn.getOutputStream());
                os.flush();
            }else{
                OutputStream os = conn.getOutputStream();
                os.write(generateParamter().getBytes(charSet()));
                os.flush();
            }


        }
        return conn;
    }

    private void writeTextBody(String delimiter,OutputStream out,String key, String value) throws IOException {
        StringBuilder body = new StringBuilder();
        body.append(delimiter)
                .append("Content-Disposition: form-data; name=\"").append(key).append("\"").append(CRLF)
                .append("Content-Type: text/plain; charset=" + charSet()).append(CRLF)
                .append(CRLF)
                .append(value)
                .append(CRLF);
        out.write(body.toString().getBytes(charSet()));
        out.flush();
    }

    public void writeFilePart(String delimiter,OutputStream out,String key, String value)
            throws IOException {
        File file = new File(value);
        StringBuilder body = new StringBuilder();
        body.append(delimiter)
                .append("Content-Disposition: form-data; name=\"").append(key).append("\"; filename=\"").append(file.getName()).append("\"").append(CRLF)
                .append("Content-Type: " + URLConnection.guessContentTypeFromName(file.getName())).append(CRLF)
                .append("Content-Transfer-Encoding: binary").append(CRLF)
                .append(CRLF);
        out.write(body.toString().getBytes(charSet()));
        out.flush();
        FileInputStream inputStream = new FileInputStream(file);
        byte[] buffer = new byte[4096];
        int bytesRead = -1;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead);
        }
        out.flush();
        inputStream.close();
        out.write(CRLF.getBytes(charSet()));
        out.flush();
    }


    private void writeMultiPartData(String boundary,OutputStream out) throws IOException {
        String delimiter = "--" + boundary + CRLF;
        for(KeyValue param : params){
            if(param.getKey().startsWith("$")){
                writeFilePart(delimiter,out,param.getKey().replaceAll("\\$",""),param.getValue());
            }else{
                writeTextBody(delimiter,out,param.getKey(),param.getValue());
            }
        }
        out.write(("--" + boundary + "--").getBytes(charSet()));
        out.write(CRLF.getBytes(charSet()));
    }

    public HttpBaseResource addParameter(String... param) {
        return this;
    }
    public static HttpBaseResource instance(Class cls,Context context) {
        try {

            Constructor constructor = cls.getConstructor(Context.class);
            return (HttpBaseResource) constructor.newInstance(context);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public HttpBaseResource ap(String key, String value) {
        if(value != null){
            params.add(new KeyValue(key,value));
        }
        return this;
    }
    public HttpBaseResource ap(String key, Integer value) {
        if(value != null){
            params.add(new KeyValue(key,value.toString()));
        }
        params.add(new KeyValue(key,value.toString()));
        return this;
    }
    public HttpBaseResource ap(String key, Double value) {
        if(value != null){
            params.add(new KeyValue(key,value.toString()));
        }
        return this;
    }


    public void setContext(Context context) {
        this.context = context;
    }
    public Context getContext() {
        return context;
    }



    public HttpBaseResource useMultipart(){
        isMultiPart = true;
        return this;
    }

    public HttpBaseResource url(String url){
        this.reqUrl = url;
        return this;
    }
    public HttpBaseResource method(String method){
        this.reqMethod = method;
        return this;
    }

    public HttpBaseResource tag(Object tag){
        this.tag = tag;
        return this;
    }

}

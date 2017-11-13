package mad9132.doo.utils;

import android.support.annotation.NonNull;
import android.util.Base64;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Helper class for working with a remote server
 *
 * @author David Gassner
 */
public class HttpHelper {

    // TODO #3 - two overloaded versions of method downloadUrl
    /**
     * Returns text from a URL on a web server (no authentication)
     *
     * @param address
     * @return
     * @throws IOException
     */
    public static String downloadUrl(String address) throws IOException {
        return downloadUrl(address, "", "");
    }

    // TODO #4 - this version requires URL address, user, and password.
    /**
     * Returns text from a URL on a web server (with basic authentication)
     *
     * @param address
     * @param user
     * @param password
     * @return
     * @throws IOException
     */
    public static String downloadUrl(String address, @NonNull  String user, @NonNull  String password)
            throws IOException {

        InputStream is = null;

        // TODO #5 - build basic authentication string of credentials (user + password)
        // only build provided user and password are not empty
        // Format: Basic<space>user:password
        // Encoding: Base64
        StringBuilder loginBuilder = null;
        if ( (user.isEmpty() == false) && (password.isEmpty() == false) ) {
            byte[] loginBytes = (user + ":" + password).getBytes();
            loginBuilder = new StringBuilder()
                    .append("Basic ")
                    .append(Base64.encodeToString(loginBytes, Base64.DEFAULT));
        }

        try {
            URL url = new URL(address);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // TODO #6 - add Authorization as a request property to the connection object
            // only add provided login credentials exist
            // Key: Authorization (String)
            // Value: encoded login credentials (String)
            if (loginBuilder != null) {
                conn.addRequestProperty("Authorization", loginBuilder.toString());
            }
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();

            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                throw new IOException("Got response code " + responseCode);
            }
            is = conn.getInputStream();
            return readStream(is);
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    /**
     * Reads an InputStream and converts it to a String.
     *
     * @param stream
     * @return
     * @throws IOException
     */
    private static String readStream(InputStream stream) throws IOException {

        byte[] buffer = new byte[1024];
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        BufferedOutputStream out = null;
        try {
            int length = 0;
            out = new BufferedOutputStream(byteArray);
            while ((length = stream.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            out.flush();
            return byteArray.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
}
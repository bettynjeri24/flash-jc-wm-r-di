package io.eclectics.cargilldigital.smsservice;

import static kotlin.text.Charsets.UTF_8;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;


/**
 * This class class generates the HashKey
 */
public class AppSignatureHelper extends ContextWrapper {
    public static final String TAG = AppSignatureHelper.class.getSimpleName();

    private static final String HASH_TYPE = "SHA-256";
    public static final int NUM_HASHED_BYTES = 9;
    public static final int NUM_BASE64_CHAR = 11;

    public AppSignatureHelper(Context context) {
        super(context);
    }

    /**
     * Get all the app signatures for the current package
     *
     * @return
     */
    public ArrayList<String> getAppSignatures() {
        ArrayList<String> appCodes = new ArrayList<>();

        try {
            // Get all package signatures for the current package
            String packageName = getPackageName();
            PackageManager packageManager = getPackageManager();
            Signature[] signatures = packageManager.getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES).signatures;

            // For each signature create a compatible hash
            for (Signature signature : signatures) {
                String hash = hash(packageName, signature.toCharsString());
                if (hash != null) {
                    appCodes.add(String.format("%s", hash));
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
           // Timber.tag(TAG).e(e, "Unable to find package to obtain hash.");
            Log.e(""+e, "Unable to find package to obtain hash.");
        }
        return appCodes;
    }

    private static String hash(String packageName, String signature) {
        String appInfo = packageName + " " + signature;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(HASH_TYPE);
            messageDigest.update(appInfo.getBytes(UTF_8));
            byte[] hashSignature = messageDigest.digest();

            // truncated into NUM_HASHED_BYTES
            hashSignature = Arrays.copyOfRange(hashSignature, 0, NUM_HASHED_BYTES);
            // encode into Base64
            String base64Hash = Base64.encodeToString(hashSignature, Base64.NO_PADDING | Base64.NO_WRAP);
            base64Hash = base64Hash.substring(0, NUM_BASE64_CHAR);

            Log.e("pkg: %s -- hash: %s", packageName +"\t"+base64Hash);
            return base64Hash;
        } catch (NoSuchAlgorithmException e) {
           // Timber.tag(TAG + "sms_sample_test").v(e, "hash:NoSuchAlgorithm");
            Log.e("smserror","NoSuchAlgorithm");
        }
        return null;
    }
}
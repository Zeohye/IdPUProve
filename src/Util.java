import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by Nils Henning on 11/25/2014.
 */
public class Util {


    public static String ComputeXi(int q, int ei, String ai){
        String xi ="";
        if(ei == 0x01)
            if(ai == null)
                xi="0";
            else
                xi= "";//Hash(q);
        else if(ei==0x00) {
            if(0 <= Integer.parseInt(ai) && Integer.parseInt(ai) <= q)
                xi = ai+"";
        }else
            xi="Error";

        return xi;
    }

    public static String Hash (Object value){
        byte[] hash=getByteArrayToHash(value);

        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            hash = digest.digest(hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return bytesToHex(hash);
    }

    private static byte[] getByteArrayToHash(Object value){
        byte[] hash={};
        if(value instanceof Byte)
            hash = Hash_byte((Byte)value);
        else if(value instanceof String)
            hash = Hash_octectstring((String)value);
        else if(value == null)
            hash = Hash_null();
        else if (value instanceof ArrayList)
            hash = Hash_list((ArrayList<Object>)value);

        return hash;
    }


    private static byte[] Hash_byte(byte value){
        byte[] hash={value};
        return hash;
    }
    private static byte[] Hash_null(){
        return octectStringToByteArray("00000000");
    }
    private static byte[] Hash_octectstring(String value){
        int length = (value.length()/2)-1; //each oct is 2 chars, and we ignore the first 0x
        String lengthOct = Integer.toOctalString(length);
        if(lengthOct.length() <8)
            for(int i = lengthOct.length();i<8;i++)
                lengthOct = "0"+lengthOct;
        return octectStringToByteArray(lengthOct+value.substring(2));
    }
    private static byte[] Hash_list(ArrayList<Object> list){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );

        String lengthList = list.size()+"";
        if(lengthList.length() <8)
            for(int i = lengthList.length();i<8;i++)
                lengthList = "0"+lengthList;
        try {
            outputStream.write(octectStringToByteArray(lengthList));
            for(int i=0; i<list.size();i++)
                outputStream.write( getByteArrayToHash(list.get(i)) );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputStream.toByteArray( );
    }



    final protected static char[] hexArray = "0123456789abcdef".toCharArray();
    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    private static byte[] octectStringToByteArray(String s){
        byte bs[] = new byte[s.length() / 2];
        for (int i=0; i<s.length(); i+=2)
            bs[i/2] = (byte)Integer.parseInt(s.substring(i, i+2),16);
        return bs;
    }
}

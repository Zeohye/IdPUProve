import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Objects;
import java.nio.ByteBuffer;
import java.math.BigInteger;

/**
 * Created by Nils Henning on 11/25/2014.
 */
public class Util {

    public static String GetRandom() {
        return GetRandom(false);
    }

    public static String GetRandom(boolean isMult) {
        return "42";
        // TODO: fix
    }

    public static String ModPow(String base, String exp) {
        return Util.BigIntToHex(Util.HexToBigInt(base).modPow(Util.HexToBigInt(exp),Util.HexToBigInt(Parameters.p)));
    }

    public static String Mult(String f1, String f2) {
        return Util.BigIntToHex(Util.HexToBigInt(f1).multiply(Util.HexToBigInt(f2)).mod(Util.HexToBigInt(Parameters.p)));
    }

    public static String Addq(String f1, String f2) {
        return Util.BigIntToHex(Util.HexToBigInt(f1).add(Util.HexToBigInt(f2)).mod(Util.HexToBigInt(Parameters.q)));
    }

    public static String ComputeXi(int q, int ei, String ai){
        String xi ="";
        if(ei == 0x01)
            if(ai == null)
                xi="0";
            else
                xi= "";//Hash(q);
        else if(ei==0x00) {
            if(0 <= Integer.parseInt(ai) && Integer.parseInt(ai) < q)
                xi = ai;
        }else
            xi="Error";

        return xi;
    }

    public static String ComputeXt(String UIDp, String g0, ArrayList<Integer> es, String TI) {
        ArrayList<Object> hashList = new ArrayList<Object>();
        hashList.add(UIDp); hashList.add(Parameters.p); hashList.add(Parameters.q);
        hashList.add(Parameters.g);
        ArrayList<String> gs = new ArrayList<String>();
        // TODO: what to do??
        //String P = Hash(UIDp + Parameters.p + Parameters.q + Parameters.g + es);
        return "";
    }

    public static String HashToGroup(Object value) {
        BigInteger hash = new BigInteger(byteHash(value));
        BigInteger q = HexToBigInt(Parameters.q);
        return BigIntToHex(hash.mod(q));
    }

    public static String Hash (Object value){
        return bytesToHex(byteHash(value));
    }

    public static String BigIntToHex(BigInteger value) {
        return bytesToHex(value.toByteArray());
    }

    public static BigInteger HexToBigInt(String value) {
        return new BigInteger(value, 16);
    }

    private static byte[] byteHash(Object value) {
        byte[] hash=getByteArrayToHash(value);

        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            hash = digest.digest(hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hash;
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
        if(value.substring(0,2).equals("0x"))
            value = value.substring(2);
            int length = (value.length()/2);
        String lengthOct = Integer.toOctalString(length);
        if((lengthOct.length()%8) !=0)
            for(int i = lengthOct.length()%8;i<8;i++)
                lengthOct = "0"+lengthOct;
        return octectStringToByteArray(lengthOct+value);
    }
    private static byte[] Hash_list(ArrayList<Object> list){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );

        String lengthList = list.size()+"";
        if(lengthList.length() <8)
            for(int i = lengthList.length();i<8;i++)
                lengthList = "0"+lengthList;
        try {
            byte[] tmp = octectStringToByteArray(lengthList);
            outputStream.write(tmp);
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
        if(s.length()%2!=0)
            s="0"+s;
        byte bs[] = new byte[s.length() / 2];
        for (int i=0; i<s.length(); i+=2)
            bs[i / 2] = (byte) Integer.parseInt(s.substring(i, i+2),16);
        return bs;
    }
}

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.math.BigInteger;
import java.util.Objects;
import java.util.Random;

/**
 * Created by Nils Henning on 11/25/2014.
 */
public class Util {

    public static BigInteger GetRandom() {
        return GetRandom(false);
    }

    public static BigInteger GetRandom(boolean isMult) {
        BigInteger r;
        Random rnd = new Random();
        do {
            r = new BigInteger(256,rnd);
        }while (r.compareTo(Parameters.q)>=0 || (isMult && r.compareTo(new BigInteger("0"))==0));
        return r;
    }

    public static BigInteger ComputeXi(BigInteger q, int ei, BigInteger ai){
        BigInteger xi = new BigInteger("0");
        if(ei == 0x01)
            if(ai == null)
                xi=new BigInteger("0");
            else
                xi= HashToGroup(Hash(ai));
        else if(ei==0x00) {
            if(new BigInteger("0").compareTo(ai) < 1 && ai.compareTo(q) == -1)
                xi = ai;
        }else {
            System.out.println("xi has invalid value during ComputeXi");
            System.exit(-1);
        }
        return xi;
    }

    public static BigInteger ComputeXt(BigInteger UIDp, BigInteger g0, ArrayList<Integer> es, BigInteger TI) {
        ArrayList<Object> hashList = new ArrayList<Object>();
        hashList.add(UIDp); hashList.add(descGroup());
        hashList.add(gList(g0));
        hashList.add(es);

        BigInteger P = Hash(hashList);
        ArrayList<Object>  hashList2 = new ArrayList<Object>();
        hashList2.add(new Byte((byte)0x01));
        hashList2.add(P);
        hashList2.add(TI);
        return HashToGroup(Hash(hashList2));
    }

    public static BigInteger computeGamma(BigInteger xt, ArrayList<BigInteger> xis,BigInteger g0) {
        BigInteger y = g0;
        for(int i=0; i < Parameters.gxList.size();i++)
            y = y.multiply(Parameters.gxList.get(i).modPow(xis.get(i), Parameters.p));
        y = y.multiply(Parameters.gt.modPow(xt,Parameters.p));
        return y;
    }

    public static BigInteger ComputeIDToken(UProveToken t) {
        ArrayList<Object> hashList = new ArrayList<Object>();
        hashList.add(t.h);
        hashList.add(t.sigmaz);
        hashList.add(t.sigmac);
        hashList.add(t.sigmar);
        return Util.Hash(hashList);
    }

    public static BigInteger generateScopeElement(ArrayList<Object> descG,String s) {
        return ComputeVerifiableyRandomElement(descG,s,0);
    }

    private static BigInteger ComputeVerifiableyRandomElement(ArrayList<Object> descG, String s,int index) {
        BigInteger e = (Parameters.p.subtract(BigInteger.ONE)).divide(Parameters.q);
        byte count = 0;
        BigInteger g = new BigInteger("0");
        BigInteger TWO = new BigInteger("2");
        while (g.compareTo(TWO) == -1 ){
            if(count >= 255){
                System.out.println("Error count larger than allowed in ComputeVerifiableyRandomElement");
                System.exit(-1);
            }
            count++;
            BigInteger w = Hashraw(binaryConcat(s,index,count));
            g = w.modPow(e,Parameters.p);
        }
        return g;
    }

    private static String binaryConcat(String s,int index,int count) {
        int context = Integer.parseInt(s,2);
        int hex = Integer.parseInt("6767656E",16);
        int b = Integer.bitCount(hex);
        int ret = context << b;
        ret |= hex;
        b = Integer.bitCount(index);
        ret = ret << b;
        ret |=index;
        b= Integer.bitCount(count);
        ret = ret << b;
        ret |= count;
        return Integer.toString(ret);
    }


    private static ArrayList<Object> gList(BigInteger g0) {
        ArrayList<Object> ret = new ArrayList<Object>();
        ret.add(g0);
        ret.add(Parameters.g1);
        ret.add(Parameters.g2);
        ret.add(Parameters.g3);
        ret.add(Parameters.g4);
        ret.add(Parameters.g5);
        return ret;
    }


    public static ArrayList<Object> descGroup(){
        ArrayList<Object> ret = new ArrayList<Object>();
        ret.add(Parameters.p);
        ret.add(Parameters.q);
        ret.add(Parameters.g);
        return ret;
    }

    public static BigInteger HashToGroup(BigInteger value) {
        return value.mod(Parameters.q);
    }

    public static BigInteger Hash (Object value){
        return HexToBigInt(bytesToHex(byteHash(value)));
    }
    private static BigInteger Hashraw(String value) {
        byte[] hash = octectStringToByteArray(value);
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            hash = digest.digest(hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return new BigInteger(hash);
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
        else if (value instanceof BigInteger)
            hash = Hash_octectstring(BigIntToHex((BigInteger)value));


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

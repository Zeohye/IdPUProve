import java.lang.reflect.Array;
import java.math.BigInteger;
import java.util.ArrayList;

/**
 * Created by Nils Henning on 11/25/2014.
 */
public class Main {

    public static void main(String[] args) {
/*
        System.out.println("hash_byte (0x01) match: "+ Util.Hash((byte) 0x01).equals("4bf5122f344554c53bde2ebb8cd2b7e3d1600ad631c385a5d7cce23c7785459a"));

        System.out.println("hash_octectstring (0x0102030405) match: "+ Util.Hash("0x0102030405").equals("16df7d2d0c3882334fe0457d298a7b2413e1e5b7a880f0b5ec79eeeae7f58dd8"));

        System.out.println("hash_null (null) match: " + Util.Hash(null).equals("df3f619804a92fdb4057192dc43dd748ea778adc52bc498ce80524c014b81119"));

        ArrayList<Object> list = new ArrayList<Object>();
        list.add((byte)0x01);
        list.add("0x0102030405");
        list.add(null);
        System.out.println("hash_list [0x01, 0x0102030405, null] match: " + Util.Hash(list).equals("dfd6a31f867566ffeb6c657af1dafb564c3de74485058426633d4b6c8bad6732"));
        list.clear();

        System.out.println("hash_group (1.3.6.1.4.1.311.75.1.1.1) = \n7b36c8a3cf1552077e1cacb365888d25c9dc54f3faed7aff9b11859aa8e4ba06");
        list.add(Parameters.p);
        list.add(Parameters.q);
        list.add(Parameters.g);
        System.out.println(Util.Hash(list));
*/
        Issuer i = new Issuer(new BigInteger("55b802e016b8b24b14a0ace9a28cf9398142d4299a46306035ac6ef4d7b903fe",16),new BigInteger("56312e31205265766973696f6e20335465737420566563746f727320233130",16));
        Prover prover = new Prover("Nils","26","male","nilhenn@cs.au.dk","Rønde",i);
        ArrayList<Integer> es = new ArrayList<Integer>();
        es.add(1);es.add(1);es.add(1);es.add(0);es.add(0);

        UProveToken t = prover.GetToken(es,new BigInteger("546f6b656e20696e666f726d6174696f6e206669656c642076616c7565",16),new BigInteger("50726f76657220696e666f726d6174696f6e206669656c642076616c7565",16));
        ArrayList<Integer> D = new ArrayList<Integer>();
        ArrayList<Integer> U = new ArrayList<Integer>();
        U.add(0);U.add(1);U.add(2);U.add(3);U.add(4);
        ArrayList<Integer> C = new ArrayList<Integer>();
        C.add(0);C.add(1);C.add(2);
        int p = 1;
        String s = "011011";
        String m ="56657269666965725549442b72616e646f6d2064617461";
        PresentationProof pp = prover.generatePresentaionProve(es, D, U, C, p, s, m, t);
        System.out.println("verify result "+ Verifier.verify(i.getUIDp(),es,D,U,C,t,p,s,m,pp,i));
    }

}

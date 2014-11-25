import java.util.ArrayList;

/**
 * Created by Nils Henning on 11/25/2014.
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("hash_byte (0x01)");
        System.out.println("match: "+ Util.Hash((byte) 0x01).equals("4bf5122f344554c53bde2ebb8cd2b7e3d1600ad631c385a5d7cce23c7785459a"));

        System.out.println("hash_octectstring (0x0102030405)");
        System.out.println("match: "+ Util.Hash("0x0102030405").equals("16df7d2d0c3882334fe0457d298a7b2413e1e5b7a880f0b5ec79eeeae7f58dd8"));

        System.out.println("hash_null (null)");
        System.out.println("match: " + Util.Hash(null).equals("df3f619804a92fdb4057192dc43dd748ea778adc52bc498ce80524c014b81119"));

        System.out.println("hash_list [0x01, 0x0102030405, null]");
        ArrayList<Object> list = new ArrayList<Object>();
        list.add((byte)0x01);
        list.add("0x0102030405");
        list.add(null);
        System.out.println("match: " + Util.Hash(list).equals("dfd6a31f867566ffeb6c657af1dafb564c3de74485058426633d4b6c8bad6732"));
        list.clear();

        //System.out.println("hash_group (1.3.6.1.4.1.311.75.1.1.1) = \n7b36c8a3cf1552077e1cacb365888d25c9dc54f3faed7aff9b11859aa8e4ba06");





    }

}

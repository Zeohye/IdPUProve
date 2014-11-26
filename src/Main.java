import java.util.ArrayList;

/**
 * Created by Nils Henning on 11/25/2014.
 */
public class Main {

    public static void main(String[] args) {

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
        list.add(Parameters.g);
        list.add(Parameters.g1);
        list.add(Parameters.g2);
        list.add(Parameters.g3);
        list.add(Parameters.g4);
        list.add(Parameters.g5);
        list.add(Parameters.g6);
        list.add(Parameters.g7);
        list.add(Parameters.g8);
        list.add(Parameters.g9);
        list.add(Parameters.g10);
        list.add(Parameters.g11);
        list.add(Parameters.g12);
        list.add(Parameters.g13);
        list.add(Parameters.g14);
        list.add(Parameters.g15);
        list.add(Parameters.g16);
        list.add(Parameters.g17);
        list.add(Parameters.g18);
        list.add(Parameters.g19);
        list.add(Parameters.g20);
        list.add(Parameters.g21);
        list.add(Parameters.g22);
        list.add(Parameters.g23);
        list.add(Parameters.g24);
        list.add(Parameters.g25);
        list.add(Parameters.g26);
        list.add(Parameters.g27);
        list.add(Parameters.g28);
        list.add(Parameters.g29);
        list.add(Parameters.g30);
        list.add(Parameters.g31);
        list.add(Parameters.g32);
        list.add(Parameters.g33);
        list.add(Parameters.g34);
        list.add(Parameters.g35);
        list.add(Parameters.g36);
        list.add(Parameters.g37);
        list.add(Parameters.g38);
        list.add(Parameters.g39);
        list.add(Parameters.g40);
        list.add(Parameters.g41);
        list.add(Parameters.g42);
        list.add(Parameters.g43);
        list.add(Parameters.g44);
        list.add(Parameters.g45);
        list.add(Parameters.g46);
        list.add(Parameters.g47);
        list.add(Parameters.g48);
        list.add(Parameters.g49);
        list.add(Parameters.g50);
        System.out.println(Util.Hash(list));





    }

}

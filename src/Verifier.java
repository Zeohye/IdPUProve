import java.math.BigInteger;
import java.util.ArrayList;

/**
 * Created by Nils Henning on 11/25/2014.
 */
public class Verifier {

    public static Boolean verify(BigInteger UIDp,ArrayList<Integer> es,ArrayList<Integer>D,ArrayList<Integer>U,ArrayList<Integer>C,UProveToken t, int p, String s,String m,PresentationProof pp,Issuer issuer){
        if(!verifyTokenSignature(UIDp,es,t,issuer.g0))
            return false;

        BigInteger xt = Util.ComputeXt(UIDp,issuer.g0,es,t.TI);

        ArrayList<BigInteger> xInD = new ArrayList<BigInteger>();
        for(int i=0; i<D.size();i++)
            xInD.add(Util.ComputeXi(Parameters.q,es.get(D.get(i)),pp.attributesInD.get(i)));
        BigInteger UIDt = Util.ComputeIDToken(t);

        ArrayList<Object> hashList = new ArrayList<Object>();
        hashList.add(UIDt);
        hashList.add(pp.a);
        hashList.add(D);
        hashList.add(xInD);
        hashList.add(C);
        hashList.add(pp.cInC);
        hashList.add(pp.aInC);
        hashList.add(p);
        hashList.add(pp.ap);
        hashList.add(pp.Ps);
        hashList.add(m);
        BigInteger cp = Util.Hash(hashList);
        BigInteger c = Util.HashToGroup(Util.Hash(cp));



        BigInteger a = issuer.g0.multiply(Parameters.gt.modPow(xt,Parameters.p));
        for(int i=0; i<D.size();i++)
            a = a.multiply(Parameters.gxList.get(D.get(i)).modPow(xInD.get(i),Parameters.p));
        a = a.modPow(c.negate(),Parameters.p).multiply(t.h.modPow(pp.r0,Parameters.p));
        for(int i=0; i<U.size();i++)
            a = a.multiply(Parameters.gxList.get(U.get(i)).modPow(pp.rInU.get(i),Parameters.p));
        System.out.println("value of a pre hash " +a);
        a = Util.Hash(a);
        System.out.println("value of a    "+a);
        System.out.println("value of pp.a "+pp.a);
        if(!a.equals(pp.a))
            System.out.println("wrong secret key value, maybe");//return false;
        BigInteger gs;
        if(pp.ap != null && pp.Ps != null) {
            gs = Util.generateScopeElement(Util.descGroup(), s);
            BigInteger ap = Util.Hash(pp.Ps.modPow(c,Parameters.p).multiply(gs.modPow(pp.rInU.get(p),Parameters.p))); // TODO: pp.rInU.get(p)  Might be wrong
            if(!pp.ap.equals(ap)) return false;
        }
        for(int i=0; i<C.size();i++){
            BigInteger ai = Util.Hash(pp.cInC.get(C.get(i)).modPow(c,Parameters.p).multiply(Parameters.g.modPow(pp.rInU.get(C.get(i)),Parameters.p)).multiply(Parameters.g1.modPow(pp.rInC.get(C.get(i)),Parameters.p)));
            if(!ai.equals(pp.aInC.get(C.get(i))))return false;
        }

        return true;
    }

    public static Boolean verifyTokenSignature(BigInteger UIDp,ArrayList<Integer> es,UProveToken t,BigInteger g0){
        if(t.h.equals(BigInteger.ONE))return false;
        ArrayList<Object> hashList = new ArrayList<Object>();
        hashList.add(t.h);
        hashList.add(t.PI);
        hashList.add(t.sigmaz);
        hashList.add(Parameters.g.modPow(t.sigmar,Parameters.p).multiply(g0.modPow(t.sigmac.negate(),Parameters.p)));
        hashList.add(t.h.modPow(t.sigmar,Parameters.p).multiply(t.sigmaz.modPow(t.sigmac.negate(),Parameters.p)));
        BigInteger sigmac = Util.HashToGroup(Util.Hash(hashList));
        if(!t.sigmac.equals(sigmac)){
            System.out.println("test");return false;}

        return true;
    }


}

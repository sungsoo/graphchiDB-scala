package edu.cmu.graphchi.preprocessing;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 *
 */
public class TestVertexIdTranslate {

    @Test
    public void testTranslation() {
        for(int verticesInShard=10; verticesInShard < 1000000; verticesInShard += 100000) {
            for(int numShards=(10000000 / verticesInShard + 1); numShards < 100; numShards += 13) {
                VertexIdTranslate trans = new VertexIdTranslate(verticesInShard, numShards);
                for(int j=0; j < 10000000; j+=(1+j)) {
                     long vprime = trans.forward(j);
                     long back = trans.backward(vprime);
                     assertEquals(j, back);
                }


                for(int e=0; e<100; e++) {
                    assertEquals(e, trans.backward(trans.forward(e)));
                }

            }
        }
    }

    @Test
    public void testConstructFromString() {
        VertexIdTranslate tr = new VertexIdTranslate(99999, 44);
        String str = tr.stringRepresentation();

        VertexIdTranslate trReconstr = VertexIdTranslate.fromString(str);

        assertEquals(tr.getNumShards(), trReconstr.getNumShards());
        assertEquals(tr.getVertexIntervalLength(), trReconstr.getVertexIntervalLength());

        for(int j=0; j < 10000000; j+=(1+j)) {
            long v1 = tr.forward(j);
            long v2 = trReconstr.forward(j);
            assertEquals(v1, v2);
        }
    }

    @Test
    public void testEncoding() {
        for(long i = 0; i < 5000000000L; i += 1000000L) {
            for(long link=0; link<268435455L; link+=1000005L) {
                long vPacket = VertexIdTranslate.encodeVertexPacket(i, link);
                assertEquals(i, VertexIdTranslate.getVertexId(vPacket));
                assertEquals(link, VertexIdTranslate.getAux(vPacket));
            }
        }

        long x = VertexIdTranslate.encodeVertexPacket(1214077, 1<<30 -1);
        assertEquals(1214077, VertexIdTranslate.getVertexId(x));
        assertEquals(1<<30 - 1, VertexIdTranslate.getAux(x));
    }
}
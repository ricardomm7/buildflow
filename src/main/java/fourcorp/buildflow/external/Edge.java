package fourcorp.buildflow.external;

import java.util.Objects;

/**
 * @param <V> Vertex value type
 * @author DEI-ESINF
 */
public class Edge<V> {
    final private V vOrig;        // vertex origin
    final private V vDest;        // vertex destination


    public Edge(V vOrig, V vDest) {
        if ((vOrig == null) || (vDest == null)) throw new RuntimeException("Edge vertices cannot be null!");
        this.vOrig = vOrig;
        this.vDest = vDest;
    }

    public V getVOrig() {
        return vOrig;
    }

    public V getVDest() {
        return vDest;
    }


    @Override
    public String toString() {
        return String.format("%s -> %s\nWeight: %s", vOrig, vDest);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        @SuppressWarnings("unchecked") Edge<V> edge = (Edge<V>) o;
        return  vOrig.equals(edge.vOrig) &&
                vDest.equals(edge.vDest);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vOrig, vDest);
    }
}

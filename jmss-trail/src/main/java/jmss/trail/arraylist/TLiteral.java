package jmss.trail.arraylist;

import jmss.annotations.NotNull;

class TLiteral {
    final Integer lit;
    final boolean decision; //unit or decision literal

    TLiteral(@NotNull Integer lit, boolean decision) {
        this.lit = lit;
        this.decision = decision;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TLiteral literal = (TLiteral) o;

        return lit.equals(literal.lit);

    }

    @Override
    public int hashCode() {
        return lit.hashCode();
    }

//    @Override
//    public String toString() {
//        return lit + "";
//    }
}
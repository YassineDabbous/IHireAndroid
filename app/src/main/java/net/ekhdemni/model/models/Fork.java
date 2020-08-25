package net.ekhdemni.model.models;

public class Fork extends Commun {

    public String justForEqualMethod;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Fork)) return false;
        if (!super.equals(o)) return false;

        Fork fork = (Fork) o;

        return justForEqualMethod != null ? justForEqualMethod.equals(fork.justForEqualMethod) : fork.justForEqualMethod == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (justForEqualMethod != null ? justForEqualMethod.hashCode() : 0);
        return result;
    }


}
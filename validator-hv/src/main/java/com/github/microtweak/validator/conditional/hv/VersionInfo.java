package com.github.microtweak.validator.conditional.hv;

import lombok.Getter;
import lombok.ToString;
import org.hibernate.validator.HibernateValidator;

import java.util.Objects;
import java.util.Optional;

import static java.lang.Integer.parseInt;

@Getter
@ToString
public final class VersionInfo implements Comparable<VersionInfo> {

    private static VersionInfo current;

    private final Integer major;
    private final Integer minor;
    private final Integer patch;
    private final String suffix;

    private VersionInfo(String text) {
        final String[] parts = text.split("[.-]");

        this.major = parseInt( parts[0] );
        this.minor = parts.length >= 2 ? parseInt( parts[1] ) : null;
        this.patch = parts.length >= 3 ? parseInt( parts[2] ) : null;
        this.suffix = parts.length >= 4 ? parts[3] : null;
    }

    public static VersionInfo of(String text) {
        return new VersionInfo(text);
    }

    public static VersionInfo getCurrent() {
        if (current == null) {
            current = Optional.ofNullable( HibernateValidator.class.getPackage() )
                    .filter(Objects::nonNull)
                    .filter(pkg -> "hibernate-validator".equals(pkg.getImplementationTitle()))
                    .map(pkg -> new VersionInfo( pkg.getImplementationVersion() ) )
                    .orElseThrow(() -> new IllegalStateException("Could not determine Hibernate Validator version"));
        }
        return current;
    }

    @Override
    public int compareTo(VersionInfo other) {
        if (getMajor() != other.getMajor()) {
            return Integer.compare(getMajor(), other.getMajor());
        }

        if (getMinor() != other.getMinor()) {
            return Integer.compare(getMinor(), other.getMinor());
        }

        return Integer.compare(getPatch(), other.getPatch());
    }

    public boolean lt(VersionInfo other) {
        return compareTo(other) == -1;
    }

    public boolean lt(String version) {
        return lt( of(version) );
    }

    public boolean le(VersionInfo other) {
        return compareTo(other) <= 0;
    }

    public boolean le(String version) {
        return le( of(version) );
    }

    public boolean eq(VersionInfo other) {
        return compareTo(other) == 0;
    }

    public boolean eq(String version) {
        return eq( of(version) );
    }

    public boolean ge(VersionInfo other) {
        return compareTo(other) >= 0;
    }

    public boolean ge(String version) {
        return ge( of(version) );
    }

    public boolean gt(VersionInfo other) {
        return compareTo(other) == 1;
    }

    public boolean gt(String version) {
        return gt( of(version) );
    }

}

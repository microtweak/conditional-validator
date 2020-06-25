package com.github.microtweak.validator.conditional.hv;

import org.junit.jupiter.api.Test;

import static com.github.microtweak.validator.conditional.hv.VersionInfo.of;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class VersionInfoTest {

    @Test
    public void onlyMajorRelease() {
        assertAll(
            () -> assertTrue( of("2.0.0").lt("3.0.0") ),
            () -> assertTrue( of("2.0.0").le("3.0.0") ),
            () -> assertTrue( of("2.0.0").le("2.0.0") ),
            () -> assertTrue( of("2.0.0").eq("2.0.0") ),
            () -> assertTrue( of("2.0.0").ge("2.0.0") ),
            () -> assertTrue( of("2.0.0").ge("1.0.0") ),
            () -> assertTrue( of("2.0.0").gt("1.0.0") )
        );
    }

    @Test
    public void onlyMinorRelease() {
        assertAll(
            () -> assertTrue( of("0.2.0").lt("0.3.0") ),
            () -> assertTrue( of("0.2.0").le("0.3.0") ),
            () -> assertTrue( of("0.2.0").le("0.2.0") ),
            () -> assertTrue( of("0.2.0").eq("0.2.0") ),
            () -> assertTrue( of("0.2.0").ge("0.2.0") ),
            () -> assertTrue( of("0.2.0").ge("0.1.0") ),
            () -> assertTrue( of("0.2.0").gt("0.1.0") )
        );
    }

    @Test
    public void onlyPatchRelease() {
        assertAll(
            () -> assertTrue( of("0.0.2").lt("0.0.3") ),
            () -> assertTrue( of("0.0.2").le("0.0.3") ),
            () -> assertTrue( of("0.0.2").le("0.0.2") ),
            () -> assertTrue( of("0.0.2").eq("0.0.2") ),
            () -> assertTrue( of("0.0.2").ge("0.0.2") ),
            () -> assertTrue( of("0.0.2").ge("0.0.1") ),
            () -> assertTrue( of("0.0.2").gt("0.0.1") )
        );
    }

    @Test
    public void allReleases() {
        assertAll(
            () -> assertTrue( of("6.0.1.Final").lt("6.0.20.Final") ),
            () -> assertTrue( of("6.0.1.Final").lt("6.1.0.Final") )
        );

        assertAll(
            () -> assertTrue( of("6.0.0.Final").le("6.0.0.Beta1") ),
            () -> assertTrue( of("6.0.0.Final").le("6.1.0.Beta1") )
        );

        assertAll(
            () -> assertTrue( of("6.0.17.Final").eq("6.0.17.Final") ),
            () -> assertTrue( of("6.0.0.Final").eq("6.0.0.Beta") )
        );

        assertAll(
            () -> assertTrue( of("6.0.0.Beta1").ge("6.0.0.Final") ),
            () -> assertTrue( of("6.1.0.Beta1").ge("6.0.0.Final") )
        );

        assertAll(
            () -> assertTrue( of("7.0.0.Final").gt("6.1.5.Final") ),
            () -> assertTrue( of("6.1.5.Final").gt("6.0.17.Final") )
        );
    }

}

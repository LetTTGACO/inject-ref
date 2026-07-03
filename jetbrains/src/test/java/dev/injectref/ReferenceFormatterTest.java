package dev.injectref;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ReferenceFormatterTest {
    @Test
    public void addsAtPrefixToRelativePath() {
        assertEquals("@src/components/Button.tsx", ReferenceFormatter.formatRelativePath("src/components/Button.tsx"));
    }

    @Test
    public void normalizesWindowsSeparators() {
        assertEquals("@src/components/Button.tsx", ReferenceFormatter.formatRelativePath("src\\components\\Button.tsx"));
    }

    @Test
    public void returnsNullForEmptyRelativePath() {
        assertNull(ReferenceFormatter.formatRelativePath(""));
    }

    @Test
    public void formatsPathInsideProjectBasePath() {
        assertEquals(
            "@src/components/Button.tsx",
            ReferenceFormatter.formatAbsolutePath("/repo/project", "/repo/project/src/components/Button.tsx")
        );
    }

    @Test
    public void returnsNullForPathOutsideProjectBasePath() {
        assertNull(ReferenceFormatter.formatAbsolutePath("/repo/project", "/repo/other/Button.tsx"));
    }
}

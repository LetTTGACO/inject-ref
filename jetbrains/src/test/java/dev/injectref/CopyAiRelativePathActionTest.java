package dev.injectref;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class CopyAiRelativePathActionTest {
    @Test
    public void rejectsMultipleSelectedFiles() {
        String first = new String("first");
        String second = new String("second");

        assertNull(CopyAiRelativePathAction.resolveValue(List.of(), new String[] { first, second }, null, null));
    }

    @Test
    public void usesSingleSelectedFileFromArray() {
        String selected = new String("selected");
        String fallback = new String("fallback");

        assertSame(selected, CopyAiRelativePathAction.resolveValue(List.of(), new String[] { selected }, "ignored", fallback));
    }

    @Test
    public void fallsBackToSelectedFileWhenArrayIsMissing() {
        String selected = new String("selected");

        assertSame(selected, CopyAiRelativePathAction.resolveValue(List.of(), null, selected, "editor"));
    }

    @Test
    public void fallsBackToEditorFileWhenSelectionArrayIsEmpty() {
        String editor = new String("editor");

        assertSame(editor, CopyAiRelativePathAction.resolveValue(List.of(), new String[0], null, editor));
    }

    @Test
    public void usesSingleCopyReferenceElementBeforeVirtualFileData() {
        String selected = new String("copy-reference-selected");
        String fallback = new String("fallback");

        assertSame(selected, CopyAiRelativePathAction.resolveValue(List.of(selected), new String[] { fallback }, "ignored", "editor"));
    }

    @Test
    public void rejectsMultipleCopyReferenceElements() {
        String first = new String("first");
        String second = new String("second");
        String editor = new String("editor");

        assertNull(CopyAiRelativePathAction.resolveValue(List.of(first, second), null, null, editor));
    }

    @Test
    public void fallsBackWhenCopyReferenceElementsAreMissing() {
        String selected = new String("selected");

        assertSame(selected, CopyAiRelativePathAction.resolveValue(null, new String[] { selected }, null, "editor"));
    }

    @Test
    public void allowsDirectoryTargets() {
        assertFalse(CopyAiRelativePathAction.shouldRejectTarget(true, true));
    }

    @Test
    public void rejectsMissingTargets() {
        assertTrue(CopyAiRelativePathAction.shouldRejectTarget(false, false));
    }
}
